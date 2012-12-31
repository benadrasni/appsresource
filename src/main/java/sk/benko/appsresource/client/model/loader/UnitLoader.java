package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls all aspects of loading the set of {@link Unit}s. This class
 * takes care of performing (and possibly retrying) a query for the initial set
 * of Units.
 */
public class UnitLoader extends RetryTimer implements AsyncCallback<List<Unit>> {

  private final DesignerModel model;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   */
  public UnitLoader(DesignerModel model) {
    this.model = model;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    ((DbServiceAsync) model.getService()).getUnits(this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof DbService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(List<Unit> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyUnitsLoaded(result);
  }

  @Override
  protected void retry() {
    start();
  }
}
