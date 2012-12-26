package sk.benko.appsresource.client.model.loader;

import sk.benko.appsresource.client.TreeItemData;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.RetryTimer;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.result.GetTreeLevelResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Controls all aspects of loading the set of {@link AObject}s. 
 *
 */
public class TreeLevelLoader extends RetryTimer 
    implements AsyncCallback<GetTreeLevelResult> {

  private final ApplicationModel model;
  private final int langId;
  private final int tId;
  private final TreeItem ti;
  private final String key;
  private final TemplateAttribute ta;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model     the model to which this loader is bound
   * @param langId    language's id
   * @param tId       template's id
   * @param ti        actual tree item
   * @param key       the key
   * @param ta        template attribute
   */
  public TreeLevelLoader(ApplicationModel model, int langId, int tId, 
      TreeItem ti, String key, TemplateAttribute ta) {
    this.model = model;
    this.langId = langId;
    this.tId = tId;
    this.ti = ti;
    this.key = key;
    this.ta = ta;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTreeLevel(langId, tId, 
        ((TreeItemData)ti.getUserObject()).getPath(), ta, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
    
    retryLater();
  }

  @Override
  public void onSuccess(GetTreeLevelResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTreeLevelLoaded(ti, key, ta, result.getTreeLevel());
  }

  @Override
  protected void retry() {
    start();
  }
}
