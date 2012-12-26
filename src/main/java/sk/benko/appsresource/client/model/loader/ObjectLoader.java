package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import sk.benko.appsresource.client.TreeItemData;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.result.GetObjectsResult;

/**
 * Controls all aspects of loading the set of {@link AObject}s.
 */
public class ObjectLoader extends RetryTimer implements AsyncCallback<GetObjectsResult> {

  private final ApplicationModel model;
  private final int langId;
  private final int tId;
  private final TreeItem ti;
  private final String key;
  private final TemplateAttribute ta;
  private final boolean isChooseTree;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param langId
   */
  public ObjectLoader(ApplicationModel model, int langId, int tId, TreeItem ti, String key, TemplateAttribute ta) {
    this(model, langId, tId, ti, key, ta, false);
  }

  public ObjectLoader(ApplicationModel model, int langId, int tId, TreeItem ti, String key, TemplateAttribute ta,
                      boolean isChooseTree) {
    this.model = model;
    this.langId = langId;
    this.tId = tId;
    this.ti = ti;
    this.key = key;
    this.ta = ta;
    this.isChooseTree = isChooseTree;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getObjects(langId, tId,
        ((TreeItemData) ti.getUserObject()).getPath(), ta, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(GetObjectsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    if (isChooseTree)
      model.notifyChooseObjectsLoaded(ti, key, result.getObjects());
    else
      model.notifyObjectsLoaded(ti, key, result.getObjects());
  }

  @Override
  protected void retry() {
    start();
  }
}
