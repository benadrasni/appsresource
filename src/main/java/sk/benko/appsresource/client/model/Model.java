package sk.benko.appsresource.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sk.benko.appsresource.client.model.ApplicationModel.ChooseObjectObserver;

import com.google.gwt.dom.client.Element;

/**
 * Encapsulates the entire application data controller for the application. The
 * model controls all RPC to the server and is responsible for keeping client
 * side copies of data synchronized with the server.
 *
 */
public abstract class Model {

  /**
   * An observer interface to deliver all data change events.
   */
  public interface DataObserver {
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link Application}.
   */
  public interface ApplicationObserver extends DataObserver {

    /**
     * Called when a list of {@link ApplicationTemplate}s is loaded.
     *
     * @param appts
     *          the list of application templates
     */
    void onApplicationTemplatesLoaded(ArrayList<ApplicationTemplate> appts);
  }
  
  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateTree}.
   */
  public interface TemplateTreeObserver extends DataObserver {

    /**
     * Called when a list of {@link TemplateTree}s is loaded.
     *
     * @param tts
     *          the list of template trees
     */
    void onTemplateTreesLoaded(ArrayList<TemplateTree> tts);
  }
  
  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateTreeItem}.
   */
  public interface TemplateTreeItemObserver extends DataObserver {

    /**
     * Called when a list of {@link TemplateTreeItem}s is loaded.
     *
     * @param tt
     *          the template tree
     * @param ttis
     *          the list of template tree items
     */
    void onTemplateTreeItemsLoaded(TemplateTree tt, ArrayList<TemplateTreeItem> ttis);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateList}.
   */
  public interface TemplateListObserver extends DataObserver {

    /**
     * Called when a list of {@link TemplateList}s is loaded.
     *
     * @param tls
     *          the list of template lists
     */
    void onTemplateListsLoaded(String ID, int tId, List<TemplateList> tls);
  }  

  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateTreeItem}.
   */
  public interface TemplateListItemObserver extends DataObserver {

    /**
     * Called when a list of {@link TemplateListItem}s is loaded.
     *
     * @param tl
     *          the template list
     * @param tlis
     *          the list of template list items
     */
    void onTemplateListItemsLoaded(TemplateList tl, List<TemplateListItem> tlis);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateAttribute}.
   */
  public interface TemplateAttributeObserver {

    /**
     * Called when a list of {@link TemplateAttribute}s is loaded.
     *
     * @param t
     *          the template
     * @param tas
     *          the list of template attributes
     * @param tr
     *          the template relation
     */
    void onTemplateAttributesLoaded(Template t, 
        ArrayList<TemplateAttribute> tas, TemplateRelation tr);
  }
  
  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateRelation}.
   */
  public interface TemplateRelationObserver {

    /**
     * Called when a list of {@link TemplateRelation}s is loaded.
     *
     * @param t
     * @param trs
     *          the list of template relations
     */
    void onTemplateRelationsLoaded(Template t, ArrayList<TemplateRelation> trs);
  }
  
  /**
   * An observer interface to handle view changes.
   */
  public interface ViewObserver {

    /**
     * Called when menu item is clicked.
     *
     */
    void onMenuItemClicked();

    /**
     * Called when navigation item is clicked.
     *
     */
    void onNavigationItemClicked(Element element);

    /**
     * Called when dialog navigation item is clicked.
     *
     */
    void onDialogNavigationItemClicked(Element element);
  }

  /**
   * A simple callback for reporting success to the caller asynchronously. This
   * is used for call sites where the caller needs to know the result of an RPC.
   */
  public static interface SuccessCallback {
    void onResponse(boolean success);
  }

  /**
   * Encapsulates a linked list node that is used by {@link TaskQueue} to keep
   * an ordered list of pending {@link Task}s.
   */
  private static class Node {
    private final Task task;

    private Node next;

    Node(Task task) {
      this.task = task;
    }

    void execute(TaskQueue queue) {
      task.execute(queue);
    }
  }

  /**
   * Encapsulates a task for writing data to the server. The tasks are managed
   * by the {@link TaskQueue} and are auto-retried on failure.
   */
  protected abstract static class Task {
    private TaskQueue queue;

    abstract void execute();

    void execute(TaskQueue queue) {
      this.queue = queue;
      execute();
    }

    TaskQueue getQueue() {
      return queue;
    }
  }

  /**
   * Provides a mechanism to perform write tasks sequentially and retry tasks
   * that fail.
   */
  class TaskQueue extends RetryTimer {

    private Node head, tail;

    public void post(Task task) {
      final Node node = new Node(task);
      if (isIdle()) {
        head = tail = node;
        executeHead();
      } else {
        enqueueTail(node);
      }
    }

    private void enqueueTail(Node node) {
      assert head != null && tail != null;
      assert node != null;
      tail = tail.next = node;
    }

    private void executeHead() {
      head.execute(this);
    }

    private void executeNext() {
      head = head.next;
      if (head != null) {
        executeHead();
      } else {
        tail = null;
      }
    }

    private boolean isIdle() {
      return head == null;
    }

    void taskFailed(Task task, boolean fatal) {
      assert task == head.task;

      onServerFailed(fatal);

      //retryLater();
    }

    void taskSucceeded(Task task) {
      assert task == head.task;
      onServerSucceeded();

      resetRetryCount();

      executeNext();
    }

    @Override
    protected void retry() {
      executeHead();
    }
  }

  //native static void forceApplicationReload();

  /**
   * The list of the observers monitoring the model for data related events.
   */
  protected final List<DataObserver> dataObservers = new ArrayList<DataObserver>();

  /**
   * {@link TemplateTree} Observer
   */
  protected final List<TemplateTreeObserver> ttObservers = 
      new ArrayList<TemplateTreeObserver>();
  
  /**
   * {@link TemplateAttribute} Observer
   */
  protected final List<TemplateAttributeObserver> taObservers = 
      new ArrayList<TemplateAttributeObserver>();
  
  /**
   * {@link TemplateRelation} Observer
   */
  protected final List<TemplateRelationObserver> trObservers = 
      new ArrayList<TemplateRelationObserver>();

  
  /**
   * The list of the observers monitoring the model for data related events.
   */
  private final List<ViewObserver> viewObservers = new ArrayList<ViewObserver>();

  /**
   * The observer that is receiving status events.
   */
  protected final StatusObserver statusObserver;

  /**
   * A task queue to manage all writes to the server.
   */
  protected final TaskQueue taskQueue = new TaskQueue();

  /**
   * Indicates whether the RPC end point is currently responding.
   */
  private boolean offline;
  
  /**
   * Actual template.
   */
  private ApplicationTemplate appt;

  /**
   * Cache for templates associated with actual application
   * indexed by template id
   */
  private HashMap<Integer, ApplicationTemplate> t_appts;  

  
  /** 
   * Cache for application's templates indexed by application id 
   */
  private HashMap<Integer, ArrayList<ApplicationTemplate>> a_appts;
  
  /**
   * Cache for loaded trees indexed by template id.
   */
  private HashMap<Integer, ArrayList<TemplateTree>> trees =
      new HashMap<Integer, ArrayList<TemplateTree>>();

  /**
   * Cache for loaded tree items indexed by tree id and template id.
   */
  private HashMap<Integer, ArrayList<TemplateTreeItem>> treeItems =
      new HashMap<Integer, ArrayList<TemplateTreeItem>>();

  /**
   * Cache for loaded lists indexed by template id.
   */
  private HashMap<Integer, ArrayList<TemplateList>> lists =
      new HashMap<Integer, ArrayList<TemplateList>>();

  /**
   * Cache for loaded list items indexed by list id and template id.
   */
  private HashMap<Integer, ArrayList<TemplateListItem>> listItems =
      new HashMap<Integer, ArrayList<TemplateListItem>>();

  /**
   * Cache for loaded attributes indexed by template id.
   */
  private HashMap<Integer, ArrayList<TemplateAttribute>> t_attrs;  
  
  
  /**
   * Cache for loaded relations indexed by template id.
   */
  private HashMap<Integer, ArrayList<TemplateRelation>> t_rels;
  
  /**
   * Constructor
   * @param statusObserver
   */
  
  public Model(StatusObserver statusObserver) {
    this.statusObserver = statusObserver;
  }

  public abstract ServiceAsync getService();

  /**
   * @return the appt
   */
  public ApplicationTemplate getAppt() {
    return appt;
  }

  /**
   * @param appt the appt to set
   */
  public void setAppt(ApplicationTemplate appt) {
    this.appt = appt;
  }

  /**
   * Gets application templates.
   *
   * @return
   */
  public HashMap<Integer, ArrayList<ApplicationTemplate>> getAppTemplatesByApp() {
    if (a_appts == null)
      a_appts = new HashMap<Integer, ArrayList<ApplicationTemplate>>();
    return a_appts;
  }

  public ArrayList<ApplicationTemplate> getAppTemplateByApp(int appId) {
    return getAppTemplatesByApp().get(appId);
  }  
  
  public HashMap<Integer, ApplicationTemplate> getAppTemplatesByTemplate() {
    if (t_appts == null)
      t_appts = new HashMap<Integer, ApplicationTemplate>();
    return t_appts;
  }

  public ApplicationTemplate getAppTemplateByTemplate(int tId) {
    return getAppTemplatesByTemplate().get(tId);
  }   
  
  public HashMap<Integer, ArrayList<TemplateTree>> getTrees() {
    return trees;
  }
  
  public HashMap<Integer, ArrayList<TemplateTreeItem>> getTreeItems() {
    return treeItems;
  }

  public HashMap<Integer, ArrayList<TemplateList>> getLists() {
    return lists;
  }
  
  public HashMap<Integer, ArrayList<TemplateListItem>> getListItems() {
    return listItems;
  }

  /**
   * Getter for property 't_attrs'.
   * 
   * @return Value for property 't_attrs'.
   */
  public HashMap<Integer, ArrayList<TemplateAttribute>> getAttrsByTemplate() {
    if (t_attrs == null) {
      t_attrs = new HashMap<Integer, ArrayList<TemplateAttribute>>();
    }
    return t_attrs;
  }  
  
  /**
   * Getter for property 't_rels'.
   * 
   * @return Value for property 't_rels'.
   */
  public HashMap<Integer, ArrayList<TemplateRelation>> getRelsByTemplate() {
    if (t_rels == null) {
      t_rels = new HashMap<Integer, ArrayList<TemplateRelation>>();
    }
    return t_rels;
  }
  
  /**
   * Subscribes a {@link DataObserver} to receive data related events from this
   * {@link Model}.
   *
   * @param observer
   */
  public void addDataObserver(DataObserver observer) {
    dataObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link DataObserver from this {@link Model}.
   *
   * @param observer
   */
  public void removeDataObserver(DataObserver observer) {
    dataObservers.remove(observer);
  }

  /**
   * Subscribes a {@link TemplateTreeObserver} to receive data related 
   * events from this {@link Model}.
   *
   * @param observer
   */
  public void addTemplateTreeObserver(TemplateTreeObserver observer) {
    ttObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateTreeObserver from this {@link Model}.
   *
   * @param observer
   */
  public void removeTemplateTreeObserver(TemplateTreeObserver observer) {
    ttObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link TemplateAttributeObserver} to receive data related 
   * events from this {@link ApplicationModel}.
   *
   * @param observer
   */
  public void addTemplateAttributeObserver(TemplateAttributeObserver observer) {
    taObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateAttributeObserver from this 
   * {@link ApplicationModel}.
   *
   * @param observer
   */
  public void removeTemplateAttributeObserver(TemplateAttributeObserver observer) {
    taObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link TemplateRelationObserver} to receive data related 
   * events from this {@link ApplicationModel}.
   *
   * @param observer
   */
  public void addTemplateRelationObserver(TemplateRelationObserver observer) {
    trObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateRelationObserver from this 
   * {@link ApplicationModel}.
   *
   * @param observer
   */
  public void removeTemplateRelationObserver(TemplateRelationObserver observer) {
    trObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link ViewObserver} to receive view related events from this
   * {@link Model}.
   *
   * @param observer
   */
  public void addViewObserver(ViewObserver observer) {
    viewObservers.add(observer);
  }

  public StatusObserver getStatusObserver() {
    return statusObserver;
  }
  
  // view events
  public void notifyMenuItemClicked() {
    for (ViewObserver viewObserver : viewObservers) {
      viewObserver.onMenuItemClicked();
    }
  }

  public void notifyNavigationItemClicked(Element element) {
    for (ViewObserver viewObserver : viewObservers) {
      viewObserver.onNavigationItemClicked(element);
    }
  }

  public void notifyDialogNavigationItemClicked(Element element) {
    for (ViewObserver viewObserver : viewObservers) {
      viewObserver.onDialogNavigationItemClicked(element);
    }
  }

  /**
   * Invoked by tasks and loaders when RPC invocations begin to fail.
   */
  public void onServerFailed(boolean fatal) {
    if (fatal) {
      //forceApplicationReload();
      return;
    }

    if (!offline) {
      statusObserver.onServerWentAway();
      offline = true;
    }
  }

  /**
   * Invoked by tasks and loaders when RPC invocations succeed.
   */
  public void onServerSucceeded() {
    if (offline) {
      statusObserver.onServerCameBack();
      offline = false;
    }
  }
  
  // application events
  public void notifyApplicationTemplatesLoaded(Application app, 
      ArrayList<ApplicationTemplate> appts) {
    getAppTemplatesByApp().put(app.getId(),appts);

    for (ApplicationTemplate appt : appts) {
      getAppTemplatesByTemplate().put(appt.getTId(), appt);
    }

    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ApplicationObserver)
        ((ApplicationObserver) dataObserver).onApplicationTemplatesLoaded(appts);
    }
  }    
  
  public void notifyTemplateTreesLoaded(int tId, ArrayList<TemplateTree> tts) {
    getTrees().put(tId, tts);
    
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof TemplateTreeObserver)
        ((TemplateTreeObserver)dataObserver).onTemplateTreesLoaded(tts);
    }
  }
  
  public void notifyTemplateTreeItemsLoaded(TemplateTree tt, ArrayList<TemplateTreeItem> ttis) {
    getTreeItems().put(tt.getTId(), ttis);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof TemplateTreeItemObserver)
        ((TemplateTreeItemObserver)dataObserver).onTemplateTreeItemsLoaded(tt, ttis);
    }
  }
  
  public void notifyChooseTemplateTreesLoaded(int tId, ArrayList<TemplateTree> tts) {
    getTrees().put(tId, tts);

    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ChooseObjectObserver)
        ((ChooseObjectObserver) dataObserver).onChooseTemplateTreesLoaded(tts);
    }
  }
  
  public void notifyChooseTemplateTreeItemsLoaded(TemplateTree tt, ArrayList<TemplateTreeItem> ttis) {
    getTreeItems().put(tt.getTId(), ttis);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ChooseObjectObserver)
        ((ChooseObjectObserver)dataObserver).onChooseTemplateTreeItemsLoaded(tt, ttis);
    }
  }
  
  public void notifyTemplateListsLoaded(String ID, int tId, ArrayList<TemplateList> tls) {
    getLists().put(tId, tls);
    List<DataObserver> clones = new ArrayList<DataObserver>(dataObservers);
    for (DataObserver dataObserver : clones) { 
      if (dataObserver instanceof TemplateListObserver)
        ((TemplateListObserver)dataObserver).onTemplateListsLoaded(ID, tId, tls);
    }
  }
  
  public void notifyTemplateListItemsLoaded(TemplateList tl, ArrayList<TemplateListItem> tlis) {
    getListItems().put(tl.getId(), tlis);
    List<DataObserver> clones = new ArrayList<DataObserver>(dataObservers);
    for (DataObserver dataObserver : clones) { 
      if (dataObserver instanceof TemplateListItemObserver) 
        ((TemplateListItemObserver)dataObserver).onTemplateListItemsLoaded(tl, tlis);
    }
  }
  
  public void notifyTemplateAttributesLoaded(Template t, 
      ArrayList<TemplateAttribute> tas, TemplateRelation tr) {
    getAttrsByTemplate().put(t.getId(), tas);
    for (TemplateAttributeObserver taObserver : taObservers)
      taObserver.onTemplateAttributesLoaded(t, tas, tr);
  }
  
  public void notifyTemplateRelationsLoaded(Template t, 
      ArrayList<TemplateRelation> trs) {
    getRelsByTemplate().put(t.getId(), trs);
    for (TemplateRelationObserver trObserver : trObservers)
      trObserver.onTemplateRelationsLoaded(t, trs);
  }
}
