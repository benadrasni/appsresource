package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AppUserService.GetApplicationUsersResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link ApplicationUser}s. 
 *
 */
public class ApplicationUserLoader extends RetryTimer implements
    AsyncCallback<AppUserService.GetApplicationUsersResult> {
  private final UserModel model;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   */
  public ApplicationUserLoader(UserModel model) {
    this.model = model;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    ((AppUserServiceAsync)model.getService()).getUserApplications(model.getCurrentAuthor().getId(), this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof DbService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(GetApplicationUsersResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyApplicationUsersLoaded(result.getApplicationUsers());
  }

  @Override
  protected void retry() {
    start();
  }
}
