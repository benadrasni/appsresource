package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.AppUserService.UserInfoResult;

/**
 * An observer interface used to get callbacks during the initial user login.
 */
public interface LoadObserver {

  /**
   * Invoked when the user loads successfully.
   *
   * @param result
   */
  void onModelLoaded(UserInfoResult result);

  /**
   * Invoked when the model fails to load.
   */
  void onModelLoadFailed();
}

