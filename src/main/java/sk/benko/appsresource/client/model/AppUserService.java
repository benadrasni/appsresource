package sk.benko.appsresource.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * The RPC api available to the client. The asynchronous version that is used
 * directly by the client is {@link AppUserServiceAsync}.
 */
@RemoteServiceRelativePath("userservice")
public interface AppUserService extends Service {

  /**
   * Get all applications for the currently logged user.
   *
   * @return
   * @throws AccessDeniedException
   */
  GetApplicationsResult getApplications() throws AccessDeniedException;

  /**
   * Get all languages.
   *
   * @return
   * @throws AccessDeniedException
   */
  GetLanguagesResult getLanguages() throws AccessDeniedException;

  /**
   * Returns the information needed to load the application template.
   *
   * @return a result object
   * @throws AccessDeniedException
   */
  UserInfoResult getUserInfo() throws AccessDeniedException;

  /**
   * Get all users for the application.
   *
   * @param appId
   * @return
   * @throws AccessDeniedException
   */
  GetApplicationUsersResult getApplicationUsers(int appId)
      throws AccessDeniedException;

  /**
   * Get all applications for the currently logged in user.
   *
   * @param userId the user to query
   * @return
   * @throws AccessDeniedException
   */
  GetApplicationUsersResult getUserApplications(int userId)
      throws AccessDeniedException;

  /**
   * Create a new {@link ApplicationUser}.
   *
   * @param applicationUser the applicationUser
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateApplicationUserResult createOrUpdateApplicationUser(
      ApplicationUser applicationUser, AppUser author);

  /**
   * Encapsulates a response for {@link AppUserService#getUserInfo()}.
   */
  @SuppressWarnings("serial")
  static class UserInfoResult implements Serializable {
    private AppUser author;
    private String logoutUrl;

    /**
     * Constructs a new response. This constructor can only be invoked on the
     * server.
     *
     * @param author    the current author
     * @param logoutUrl a url that can be used to log the current user out
     */
    public UserInfoResult(AppUser author, String logoutUrl) {
      assert !GWT.isClient();
      this.author = author;
      this.logoutUrl = logoutUrl;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private UserInfoResult() {
    }

    /**
     * Returns the current author.
     *
     * @return
     */
    public AppUser getAuthor() {
      return author;
    }

    /**
     * Returns a url that can be used to log the author out.
     *
     * @return
     */
    public String getLogoutUrl() {
      return logoutUrl;
    }
  }

  /**
   * Encapsulates a response from
   * {@link AppUserService#createOrUpdateApplicationUser(ApplicationUser, AppUser)}
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateApplicationUserResult implements Serializable {
    private int appuId;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param appuId     the key that was assigned to the new {@link ApplicationUser}
     * @param updateTime the time assigned to {@link ApplicationUser#getLastUpdatedAt()}
     */
    public CreateOrUpdateApplicationUserResult(int appuId, Date updateTime) {
      assert !GWT.isClient();
      this.appuId = appuId;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateApplicationUserResult() {
    }

    /**
     * Returns part of the key that was assigned to the new {@link ApplicationUser}.
     *
     * @return
     */
    public int getId() {
      return appuId;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link ApplicationUser#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from {@link sk.benko.appsresource.client.model.AppUserService#getLanguages()}
   */
  @SuppressWarnings("serial")
  static class GetLanguagesResult implements Serializable {
    private ArrayList<Language> languages;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param languages the list of languages to return
     */
    public GetLanguagesResult(ArrayList<Language> languages) {
      assert !GWT.isClient();
      this.languages = languages;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private GetLanguagesResult() {
    }

    /**
     * Returns the applications that were returned by the server. This can be
     * zero-length, but will not be null.
     *
     * @return
     */
    public ArrayList<Language> getLanguages() {
      return languages;
    }
  }

  /**
   * Encapsulates a response from {@link AppUserService#getApplications()}.
   */
  @SuppressWarnings("serial")
  static class GetApplicationsResult implements Serializable {
    private ArrayList<Application> applications;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param applications the list of applications to return
     */
    public GetApplicationsResult(ArrayList<Application> applications) {
      assert !GWT.isClient();
      this.applications = applications;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private GetApplicationsResult() {
    }

    /**
     * Returns the applications that were returned by the server. This can be
     * zero-length, but will not be null.
     *
     * @return
     */
    public ArrayList<Application> getApplications() {
      return applications;
    }
  }

  /**
   * Encapsulates a response from {@link AppUserService#getApplicationUsers(int)}
   */
  @SuppressWarnings("serial")
  static class GetApplicationUsersResult implements Serializable {
    private ArrayList<ApplicationUser> appus;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param appus the list of users's applications to return
     */
    public GetApplicationUsersResult(ArrayList<ApplicationUser> appus) {
      assert !GWT.isClient();
      this.appus = appus;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private GetApplicationUsersResult() {
    }

    /**
     * Returns the notes that were returned by the server. This can be
     * zero-length, but will not be null.
     *
     * @return
     */
    public ArrayList<ApplicationUser> getApplicationUsers() {
      return appus;
    }

  }
}
