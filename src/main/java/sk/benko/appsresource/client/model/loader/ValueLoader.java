package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;

import java.util.List;
import java.util.Map;

/**
 * Controls all aspects of loading the set of {@link AObject}s.
 */
public class ValueLoader extends RetryTimer implements AsyncCallback<Map<Integer, Map<Integer, List<AValue>>>> {
  private final ApplicationModel model;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   */
  public ValueLoader(ApplicationModel model) {
    this.model = model;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    getModel().getService().getValues(getModel().getObject(), this);
  }

  @Override
  public void onFailure(Throwable caught) {
    getModel().onServerFailed(caught instanceof ApplicationService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(Map<Integer, Map<Integer, List<AValue>>> result) {
    getModel().onServerSucceeded();
    getModel().getStatusObserver().onTaskFinished();
    getModel().notifyValuesLoaded(getModel().getObject(), result);
  }

  /**
   * @return the model
   */
  public ApplicationModel getModel() {
    return model;
  }

  @Override
  protected void retry() {
    start();
  }
}
