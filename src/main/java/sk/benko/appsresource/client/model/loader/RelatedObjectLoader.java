package sk.benko.appsresource.client.model.loader;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.result.GetSearchObjectsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link AObject}s. 
 *
 */
public class RelatedObjectLoader implements AsyncCallback<GetSearchObjectsResult> {

  private final String ID;
  private final ApplicationModel model;
  private final int langId;
  private final int objId;
  private final int rel;
  private final int tlId;
  private final int from;
  private final int perPage;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public RelatedObjectLoader(String ID, ApplicationModel model, int langId, 
      int objId, int rel, int tlId, int from, int perPage) {
    this.ID = ID;
    this.model = model;
    this.langId = langId;
    this.objId = objId;
    this.rel = rel;
    this.tlId = tlId;
    this.from = from;
    this.perPage = perPage;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getRelatedObjects(langId, objId, rel, tlId, from, perPage, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetSearchObjectsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyRelatedObjectsLoaded(ID, rel, result.getObjectValues());
  }
}
