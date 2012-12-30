package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.Template;

import java.util.Map;

/**
 * Controls all aspects of loading the set of related object counts.
 */
public class RelatedObjectCountLoader implements AsyncCallback<Map<Template, Integer>> {

  private final ApplicationModel model;
  private final int rel;
  private final Template t;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param rel   relation's id
   * @param t     the template
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
  public void onSuccess(Map<Template, Integer> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyRelatedObjectCountsLoaded(rel, result);
  }

  /**
   * @return the model
   */
  public ApplicationModel getModel() {
    return model;
  }
}
