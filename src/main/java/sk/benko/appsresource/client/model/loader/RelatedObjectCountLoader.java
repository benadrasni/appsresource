package sk.benko.appsresource.client.model.loader;

import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.result.GetSearchCountsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of related object counts. 
 *
 */
public class RelatedObjectCountLoader implements AsyncCallback<GetSearchCountsResult> {

  private final ApplicationModel model;
  private final int rel;
  private final Template t;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public RelatedObjectCountLoader(ApplicationModel model, int rel, 
      Template t) {
    this.model = model;
    this.rel = rel;
    this.t = t;
  }

  public void start() {
    model.getService().getRelatedObjectCounts(getModel().getObject().getId(),
        rel, t, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetSearchCountsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyRelatedObjectCountsLoaded(rel, result.getCounts());
  }

  /**
   * @return the model
   */
  public ApplicationModel getModel() {
    return model;
  }
}
