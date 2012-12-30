package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;

import java.util.ArrayList;

/**
 * Controls all aspects of loading the set of {@link ObjectRelation}s. This class
 * takes care of performing (and possibly retrying) a query for the initial set
 * of ObjectTypes and then continues polling the server for updates.
 */
public class ObjectRelationLoader extends RetryTimer implements AsyncCallback<ArrayList<ObjectRelation>> {

  private final DesignerModel model;
  private final int otId;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param otId  the object type's id
   */
  public ObjectRelationLoader(DesignerModel model, int otId) {
    this.model = model;
    this.otId = otId;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    ((DbServiceAsync) model.getService()).getObjectRelations(otId, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof DbService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(ArrayList<ObjectRelation> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyObjectRelationsLoaded(otId, result);
  }

  @Override
  protected void retry() {
    start();
  }
}
