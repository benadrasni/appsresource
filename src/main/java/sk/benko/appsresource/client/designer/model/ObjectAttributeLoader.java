package sk.benko.appsresource.client.designer.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DbService;
import sk.benko.appsresource.client.model.DbServiceAsync;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.RetryTimer;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls all aspects of loading the set of {@link ObjectAttribute}s. This class 
 * takes care of performing (and possibly retrying) a query for the initial set 
 * of ObjectAtributes.
 *
 */
public class ObjectAttributeLoader extends RetryTimer implements AsyncCallback<List<ObjectAttribute>> {

  private final DesignerModel model;
  private final int otId;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model     the model to which this loader is bound
   * @param otId      the object type's id
   */
  public ObjectAttributeLoader(DesignerModel model, int otId) {
    this.model = model;
    this.otId = otId;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    ((DbServiceAsync)model.getService()).getObjectAttributes(otId, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof DbService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(List<ObjectAttribute> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyObjectAttributesLoaded(otId, result);
  }

  @Override
  protected void retry() {
    start();
  }
}
