package sk.benko.appsresource.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.loader.ValueLoader;
import sk.benko.appsresource.client.model.result.CountObjectsResult;
import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectResult;
import sk.benko.appsresource.client.model.result.UpdateValueResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Encapsulates the entire application data controller for the application. The
 * model controls all RPC to the server and is responsible for keeping client
 * side copies of data synchronized with the server.
 */
public class ApplicationModel extends Model {

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}.
   */
  public interface ObjectObserver {
    /**
     * Called when a new {@link AObject} is created.
     *
     * @param object      the object that was created
     */
    void onObjectCreated(AObject object, Map<Integer, Map<Integer, List<AValue>>> values);

    /**
     * Called when an {@link AObject} is deleted.
     *
     * @param object      the object that was deleted
     */
    void onObjectDeleted(AObject object);

  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}s.
   */
  public interface ObjectsObserver extends DataObserver {
    /**
     * Called when objects are imported.
     *
     * @param count the count of imported objects
     */
    void onObjectsImported(int count);

    /**
     * Called when objects removed.
     *
     * @param count the count of removed objects
     */
    void onObjectsRemoved(int count);

    /**
     * Called when a list of {@link AObject}s for specific {@link TreeItem} is loaded.
     *
     * @param ti      tree item
     * @param objects the list of objects
     */
    void onObjectsLoaded(TreeItem ti, List<AObject> objects);

    /**
     * Called when a list of items is loaded.
     *
     * @param items     the list of {@link TreeLevel}'s items which represent one level in tree
     */
    void onTreeLevelLoaded(TreeItem ti, TemplateAttribute ta, List<TreeLevel> items);
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}.
   */
  public interface SearchObjectCountObserver extends DataObserver {
    /**
     * Called when counts by {@link Template} for search string is loaded.
     *
     * @param searchString the string to search
     * @param counts       map for search results counts by template
     */
    void onSearchObjectCountsLoaded(String searchString, Map<Template, Integer> counts);
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}.
   */
  public interface SearchObjectObserver extends DataObserver {
    /**
     * Called when a result of the search is loaded.
     *
     * @param ID           identification for UI part which sent a request
     * @param searchString the string to search
     * @param tlId         the id of template list
     * @param objectValues result of the search
     */
    void onSearchObjectsLoaded(String ID, String searchString, int tlId, Map<AObject, List<AValue>> objectValues);
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}.
   */
  public interface RelatedObjectCountObserver extends DataObserver {
    /**
     * Called when counts by {@link Template} for relation is loaded.
     *
     * @param rel    the id of the relation
     * @param counts a map for search results counts by template
     */
    void onRelatedObjectCountsLoaded(int rel, Map<Template, Integer> counts);
  }

  final UserModel userModel;

  /**
   * An rpc proxy for making calls to the server.
   */
  final ApplicationServiceAsync api;

  final ApplicationUser appu;

  /**
   * The list of the observers monitoring the model for data related events.
   */

  /**
   * {@link AObject} Observer
   */
  private final List<ObjectObserver> objectObservers =
      new ArrayList<ObjectObserver>();

  /**
   * {@link AValue} Observer
   */
  private final List<ValueObserver> valueObservers =
      new ArrayList<ValueObserver>();

  /**
   * Subscribes a {@link ObjectObserver} to receive data related
   * events from this {@link ApplicationModel}.
   *
   * @param observer
   */
  public void addObjectObserver(ObjectObserver observer) {
    objectObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link ObjectObserver from this
   * {@link ApplicationModel}.
   *
   * @param observer
   */
  public void removeObjectObserver(ObjectObserver observer) {
    objectObservers.remove(observer);
  }

  /**
   * Subscribes a {@link ValueObserver} to receive data related
   * events from this {@link ApplicationModel}.
   *
   * @param observer
   */
  public void addValueObserver(ValueObserver observer) {
    valueObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link ValueObserver from this
   * {@link ApplicationModel}.
   *
   * @param observer
   */
  public void removeValueObserver(ValueObserver observer) {
    valueObservers.remove(observer);
  }

  /**
   * Unsubscribes all observers from this {@link ApplicationModel}.
   */
  public void removeObservers() {
    dataObservers.clear();
    ttObservers.clear();
    taObservers.clear();
    trObservers.clear();
    objectObservers.clear();
    valueObservers.clear();
  }

  /**
   * Cache for loaded tree levels indexed by key constructed from path.
   */
  private Map<String, List<TreeLevel>> treeLevels = new HashMap<String, List<TreeLevel>>();

  /**
   * Cache for loaded objects in last level of tree
   * indexed by key constructed from path.
   */
  private Map<String, List<AObject>> treeObjects = new HashMap<String, List<AObject>>();

  /**
   * Actual object.
   */
  private AObject object;

  /**
   * All object's values (and values of connected objects )
   * indexed by object attribute id and object id.
   */
  private Map<Integer, Map<Integer, List<AValue>>> allValues;

  /**
   * Connected objects to actual objects (via dynamic combo)
   * indexed by combination of relation id and object id.
   */
  private HashMap<String, AObject> rels;

  /**
   * Actual visible template relation
   */
  private TemplateRelation templateRelation;

  /**
   * Constructor
   *
   * @param umodel
   * @param appu
   */
  public ApplicationModel(UserModel umodel, ApplicationUser appu) {
    super(umodel.getStatusObserver());
    this.api = GWT.create(ApplicationService.class);
    this.appu = appu;
    this.userModel = umodel;
  }

  public ApplicationServiceAsync getService() {
    return api;
  }

  public ApplicationUser getAppu() {
    return appu;
  }

  public UserModel getUserModel() {
    return userModel;
  }

  public Map<String, List<TreeLevel>> getTreeLevels() {
    return treeLevels;
  }

  public Map<String, List<AObject>> getTreeObjects() {
    return treeObjects;
  }

  /**
   * @return an actual object
   */
  public AObject getObject() {
    return object;
  }

  /**
   * @param object the object to set
   */
  public void setObject(AObject object) {
    this.object = object;
  }

  /**
   * @return the allValues
   */
  public Map<Integer, Map<Integer, List<AValue>>> getAllValues() {
    return allValues;
  }

  /**
   * @return values of actual object
   */
  public Map<Integer, List<AValue>> getValues() {
    return getAllValues() != null ? getAllValues().get(getObject().getId())
        : null;
  }

  /**
   * @param allValues the values to set
   */
  public void setAllValues(Map<Integer, Map<Integer, List<AValue>>> allValues) {
    this.allValues = allValues;
  }

  /**
   * Getter for property 'rels'.
   *
   * @return Value for property 'rels'.
   */
  public HashMap<String, AObject> getRels() {
    if (rels == null)
      rels = new HashMap<String, AObject>();
    return rels;
  }

  /**
   * @return the templateRelation
   */
  public TemplateRelation getTemplateRelation() {
    return templateRelation;
  }

  /**
   * @param templateRelation the templateRelation to set
   */
  public void setTemplateRelation(TemplateRelation templateRelation) {
    this.templateRelation = templateRelation;
  }

  /**
   * Creates an {@link AObject} with values and persists that change to
   * the server.
   *
   * @param object
   * @param values
   */
  public void createObject(final AObject object, final List<AValue> values) {
    taskQueue.post(new CreateObjectTask(object, values, userModel.getCurrentAuthor()));
  }

  /**
   * Deletes an {@link AObject} and persists that change to the server.
   */
  public void deleteObject() {
    taskQueue.post(new DeleteObjectTask(userModel.getCurrentAuthor()));
  }

  /**
   * Creates, updates or deletes an {@link AValue} with persists
   * that change to the server.
   *
   * @param value
   */
  public void updateValue(final AValue value) {
    taskQueue.post(new UpdateValueTask(value, userModel.getCurrentAuthor()));
  }

  /**
   * Import {@link AObject}s.
   *
   * @param app
   * @param t
   * @param filename
   * @param map
   * @param keys
   * @param onlyUpdate
   */
  public void importObjects(final Application app, final Template t, final String filename,
                            final Map<Integer, TemplateAttribute> map,
                            final Map<Integer, TemplateAttribute> keys,
                            boolean onlyUpdate) {
    taskQueue.post(new ImportObjectsTask(app, t, filename, map, keys, onlyUpdate,
        userModel.getCurrentAuthor()));
  }

  /**
   * Remove duplicates - {@link AObject}.
   *
   * @param app
   * @param t
   * @param keys
   */
  public void removeDuplicates(final Application app, final Template t, final Map<Integer, TemplateAttribute> keys) {
    taskQueue.post(new RemoveDuplicatesTask(app, t, keys, userModel.getCurrentAuthor()));
  }

  public void notifySearchCountsLoaded(String searchString, HashMap<Template, Integer> counts) {
    ArrayList<SearchObjectCountObserver> socos = new ArrayList<SearchObjectCountObserver>();
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof SearchObjectCountObserver)
        socos.add((SearchObjectCountObserver) dataObserver);
    }
    for (SearchObjectCountObserver soco : socos) {
      soco.onSearchObjectCountsLoaded(searchString, counts);
    }
  }

  public void notifySearchObjectsLoaded(String ID, String searchString, int tlId, Map<AObject,
      List<AValue>> objectValues) {
    List<DataObserver> clones = new ArrayList<DataObserver>(dataObservers);
    for (DataObserver dataObserver : clones) {
      if (dataObserver instanceof SearchObjectObserver)
        ((SearchObjectObserver) dataObserver).onSearchObjectsLoaded(ID, searchString,
            tlId, objectValues);
    }
  }

  public void notifyRelatedObjectCountsLoaded(int rel, HashMap<Template, Integer> counts) {
    ArrayList<RelatedObjectCountObserver> rocos = new ArrayList<RelatedObjectCountObserver>();
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof RelatedObjectCountObserver)
        rocos.add((RelatedObjectCountObserver) dataObserver);
    }
    for (RelatedObjectCountObserver roco : rocos) {
      roco.onRelatedObjectCountsLoaded(rel, counts);
    }
  }

  public void notifyRelatedObjectsLoaded(String ID, int rel, Map<AObject, List<AValue>> objectValues) {
    List<DataObserver> clones = new ArrayList<DataObserver>(dataObservers);
    for (DataObserver dataObserver : clones) {
      if (dataObserver instanceof RelatedObjectObserver)
        ((RelatedObjectObserver) dataObserver).onRelatedObjectsLoaded(ID, rel, objectValues);
    }
  }

  // ObjectObserver

  void notifyObjectCreated(AObject object, Map<Integer,
      Map<Integer, List<AValue>>> values) {
    setObject(object);
    setAllValues(values);
    List<ObjectObserver> clones = new ArrayList<ObjectObserver>(objectObservers);
    for (ObjectObserver objectObserver : clones) {
      objectObserver.onObjectCreated(object, values);
    }
    notifyValuesLoaded(object, values);
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}.
   */
  public interface RelatedObjectObserver extends DataObserver {
    /**
     * Called when a list of {@link AObject}s is loaded.
     *
     * @param ID           identification for UI part which sent a request
     * @param rel          the id of the relation
     * @param objectValues result of the relation search
     */
    void onRelatedObjectsLoaded(String ID, int rel, Map<AObject, List<AValue>> objectValues);
  }

  void notifyObjectDeleted(AObject object) {
    setObject(null);
    setAllValues(null);
    List<ObjectObserver> clones = new ArrayList<ObjectObserver>(objectObservers);
    for (ObjectObserver objectObserver : clones) {
      objectObserver.onObjectDeleted(object);
    }
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AObject}.
   */
  public interface ChooseObjectObserver extends DataObserver {
    /**
     * Called when a list of {@link TemplateTree}s is loaded.
     *
     * @param tts the list of template trees
     */
    void onChooseTemplateTreesLoaded(ArrayList<TemplateTree> tts);

    /**
     * Called when a list of {@link TemplateTreeItem}s is loaded.
     *
     * @param tt   the template tree
     * @param ttis the list of template tree items
     */
    void onChooseTemplateTreeItemsLoaded(TemplateTree tt, ArrayList<TemplateTreeItem> ttis);

    /**
     * Called when a list of items is loaded.
     *
     * @param items the list of items which represent one level in tree
     */
    void onChooseTreeLevelLoaded(TreeItem ti, TemplateAttribute ta, List<TreeLevel> items);

    /**
     * Called when a list of {@link AObject}s for given tree item is loaded.
     *
     * @param ti      the tree item
     * @param objects objects belong to the tree item
     */
    void onChooseObjectsLoaded(TreeItem ti, List<AObject> objects);
  }  // ObjectsObserver

  public void notifyObjectsImported(int count) {
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ObjectsObserver)
        ((ObjectsObserver) dataObserver).onObjectsImported(count);
    }
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AValue}.
   */
  public interface ValueObserver {

    /**
     * Called when a list of {@link AValue}s is loaded.
     *
     * @param object      the object which holds loaded values
     * @param values      loaded values
     */
    void onValuesLoaded(AObject object, Map<Integer, Map<Integer, List<AValue>>> values);

    /**
     * Called when an {@link AValue} is updated.
     *
     * @param value     the value which was updated
     */
    void onValueUpdated(AValue value);
  }

  public void notifyObjectsRemoved(int count) {
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ObjectsObserver)
        ((ObjectsObserver) dataObserver).onObjectsRemoved(count);
    }
  }

  /**
   * An observer interface to deliver all change events placed on
   * {@link AValue}.
   */
  public interface FileObserver extends DataObserver {

    /**
     * Called when an import file is loaded.
     *
     * @param fieldName the name of the field
     * @param rows      loaded rows
     */
    void onFileLoaded(String fieldName, ArrayList<String> rows);
  }

  public void notifyTreeLevelLoaded(TreeItem ti, String key, TemplateAttribute ta, List<TreeLevel> items) {
    getTreeLevels().put(key, items);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ObjectsObserver)
        ((ObjectsObserver) dataObserver).onTreeLevelLoaded(ti, ta, items);
    }
  }

  /**
   * A task that manages the call to the server to create or update
   * an object.
   */
  private class CreateObjectTask extends Task implements
      AsyncCallback<CreateOrUpdateObjectResult> {
    private final AppUser author;
    private final AObject object;
    private final List<AValue> values;

    public CreateObjectTask(AObject object, List<AValue> values, AppUser author) {
      this.author = author;
      this.object = object;
      this.values = values;
    }

    @Override
    public void execute() {
      Main.status.showTaskStatus(Main.constants.saving());
      api.createObject(object, values, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof ApplicationService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateObjectResult result) {
      object.update(result.getId(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      getStatusObserver().onTaskFinished();
      notifyObjectCreated(object, result.getValues());
    }
  }

  public void notifyObjectsLoaded(TreeItem ti, String key, List<AObject> objects) {
    getTreeObjects().put(key, objects);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ObjectsObserver)
        ((ObjectsObserver) dataObserver).onObjectsLoaded(ti, objects);
    }
  }

  /**
   * A task that manages the call to the server to create or update
   * an object.
   */
  private class UpdateValueTask extends Task implements
      AsyncCallback<UpdateValueResult> {
    private final AValue value;
    private final AppUser author;

    public UpdateValueTask(AValue value, AppUser author) {
      this.value = value;
      this.author = author;
    }

    @Override
    public void execute() {
      Main.status.showTaskStatus(Main.constants.saving());
      api.updateValue(value, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof ApplicationService.AccessDeniedException);
    }

    public void onSuccess(UpdateValueResult result) {
      value.update(result.getId(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      getStatusObserver().onTaskFinished();
      notifyValueUpdated(result.getValue());
      if (value.getValueRef() > 0) {
        ValueLoader vl = new ValueLoader(ApplicationModel.this);
        vl.start();
      }
    }
  }  // ValueObserver

  public void notifyValuesLoaded(AObject object, Map<Integer, Map<Integer, List<AValue>>> values) {
    setAllValues(values);
    List<ValueObserver> clones = new ArrayList<ValueObserver>(valueObservers);
    for (ValueObserver valueObserver : clones) {
      valueObserver.onValuesLoaded(object, values);
    }
  }

  /**
   * A task that manages the call to the server to create or update
   * an object.
   */
  private class DeleteObjectTask extends Task implements
      AsyncCallback<String> {
    private final AppUser author;

    public DeleteObjectTask(AppUser author) {
      this.author = author;
    }

    @Override
    public void execute() {
      api.deleteObject(getObject(), author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof ApplicationService.AccessDeniedException);
    }

    public void onSuccess(String result) {
      getQueue().taskSucceeded(this);
      notifyObjectDeleted(object);
    }
  }

  public void notifyValueUpdated(AValue value) {
    List<ValueObserver> clones = new ArrayList<ValueObserver>(valueObservers);
    for (ValueObserver valueObserver : clones) {
      valueObserver.onValueUpdated(value);
    }
  }

  /**
   * A task that manages the call to the server to create or update
   * an object type.
   */
  private class ImportObjectsTask extends Task implements
      AsyncCallback<CountObjectsResult> {
    private final Application app;
    private final Template t;
    private final String filename;
    private final Map<Integer, TemplateAttribute> map;
    private final Map<Integer, TemplateAttribute> keys;
    private final boolean onlyUpdate;
    private final AppUser author;

    public ImportObjectsTask(Application app, Template t, String filename,
                             Map<Integer, TemplateAttribute> map,
                             Map<Integer, TemplateAttribute> keys,
                             boolean onlyUpdate,
                             AppUser author) {
      this.app = app;
      this.t = t;
      this.filename = filename;
      this.map = map;
      this.keys = keys;
      this.onlyUpdate = onlyUpdate;
      this.author = author;
    }

    @Override
    public void execute() {
      api.importObjects(app, t, filename, map, keys, onlyUpdate, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof ApplicationService.AccessDeniedException);
    }

    public void onSuccess(CountObjectsResult result) {
      getQueue().taskSucceeded(this);
      notifyObjectsImported(result.getCount());
    }
  }

  public void notifyChooseObjectsLoaded(TreeItem ti, String key, List<AObject> objects) {
    getTreeObjects().put(key, objects);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ChooseObjectObserver)
        ((ChooseObjectObserver) dataObserver).onChooseObjectsLoaded(ti, objects);
    }
  }

  /**
   * A task that manages the call to the server to create or update
   * an object type.
   */
  private class RemoveDuplicatesTask extends Task implements
      AsyncCallback<CountObjectsResult> {
    private final Application app;
    private final Template t;
    private final Map<Integer, TemplateAttribute> keys;
    private final AppUser author;

    public RemoveDuplicatesTask(Application app, Template t,
                                Map<Integer, TemplateAttribute> keys,
                                AppUser author) {
      this.app = app;
      this.t = t;
      this.keys = keys;
      this.author = author;
    }

    @Override
    public void execute() {
      api.removeDuplicates(app, t, keys, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof ApplicationService.AccessDeniedException);
    }

    public void onSuccess(CountObjectsResult result) {
      getQueue().taskSucceeded(this);
      notifyObjectsRemoved(result.getCount());
    }
  }

  public void notifyChooseTreeLevelLoaded(TreeItem ti, String key, TemplateAttribute ta, List<TreeLevel> items) {
    getTreeLevels().put(key, items);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ChooseObjectObserver)
        ((ChooseObjectObserver) dataObserver).onChooseTreeLevelLoaded(ti, ta, items);
    }
  }
}
