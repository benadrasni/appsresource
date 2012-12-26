package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.AppUserService.GetApplicationsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link Application}s. This class 
 * takes care of performing (and possibly retrying) a query for the initial set 
 * of Applications and then continues polling the server for updates.
 *
 */
class ApplicationLoader extends RetryTimer implements
    AsyncCallback<AppUserService.GetApplicationsResult> {

  private final UserModel model;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   */
  public ApplicationLoader(UserModel model) {
    this.model = model;
  }

  public void start() {
    ((AppUserServiceAsync)model.getService()).getApplications(this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof DbService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(GetApplicationsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyApplicationsLoaded(result.getApplications());
  }

  @Override
  protected void retry() {
    start();
  }
}
