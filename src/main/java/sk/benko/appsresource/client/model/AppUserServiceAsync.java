package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asynchronous interface for calls to {@link AppUserService}.
 *
 *
 */
public interface AppUserServiceAsync extends ServiceAsync {

  /**
   * @see AppUserService#getUserInfo()
   * @param callback
   */
  void getUserInfo(AsyncCallback<AppUserService.UserInfoResult> callback);

  /**
   * @see AppUserService#getLanguages()
   * @param callback
   */
  void getLanguages(AsyncCallback<AppUserService.GetLanguagesResult> callback);

  /**
   * @see AppUserService#getApplications()
   * @param callback
   */
  void getApplications(AsyncCallback<AppUserService.GetApplicationsResult> callback);
  
  /**
   * @see AppUserService#createOrUpdateApplicationUser(ApplicationUser, AppUser)
   * @param applicationUser
   * @param author
   * @param callback
   */
  void createOrUpdateApplicationUser(ApplicationUser applicationUser, AppUser author,
      AsyncCallback<AppUserService.CreateOrUpdateApplicationUserResult> callback);
  
  /**
   * @see AppUserService#getApplicationUsers(int userId)
   * @param appId
   * @param callback
   */
  void getApplicationUsers(int appId,
      AsyncCallback<AppUserService.GetApplicationUsersResult> callback);

  /**
   * @see AppUserService#getUserApplications(int userId)
   * @param userId
   * @param callback
   */
  void getUserApplications(int userId,
      AsyncCallback<AppUserService.GetApplicationUsersResult> callback);

}
