package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.AppUserService.GetLanguagesResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link Language}s. This class 
 * takes care of performing (and possibly retrying) a query for the initial set 
 * of Applications and then continues polling the server for updates.
 *
 */
class LanguageLoader extends RetryTimer implements
    AsyncCallback<AppUserService.GetLanguagesResult> {

  private final UserModel model;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   */
  public LanguageLoader(UserModel model) {
    this.model = model;
  }

  public void start() {
    ((AppUserServiceAsync)model.getService()).getLanguages(this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof AppUserService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(GetLanguagesResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyLanguagesLoaded(result.getLanguages());
  }

  @Override
  protected void retry() {
    start();
  }
}
