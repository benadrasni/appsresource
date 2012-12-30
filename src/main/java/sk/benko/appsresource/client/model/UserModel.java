package sk.benko.appsresource.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AppUserService.CreateOrUpdateApplicationUserResult;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Encapsulates the entire application data controller for the application. The
 * model controls all RPC to the server and is responsible for keeping client
 * side copies of data synchronized with the server.
 *
 */
public class UserModel extends Model {

  /**
   * An observer interface to deliver all change events placed on 
   * {@link Language}.
   */
  public interface LanguageObserver extends DataObserver {
    /**
     * Called when a list of {@link Language}s is loaded.
     *
     * @param languages
     *          the list of applications
     */
    void onLanguagesLoaded(ArrayList<Language> languages);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link Application}.
   */
  public interface ApplicationObserver extends DataObserver {
    /**
     * Called when a list of {@link Application}s is loaded.
     *
     * @param applications
     *          the list of applications
     */
    void onApplicationsLoaded(Collection<Application> applications);
  }


  /**
   * An observer interface to deliver all change events placed on 
   * {@link ApplicationUser}.
   */
  public interface ApplicationUserObserver extends DataObserver {
    /**
     * Called when a new {@link ApplicationUser} is created.
     *
     * @param applicationUser
     *          the applicationUser that was created
     */
    void onApplicationUserCreated(ApplicationUser applicationUser);

    /**
     * Called when a new {@link ApplicationUser} is updated.
     *
     * @param applicationUser
     *          the applicationUser that was updated
     */
    void onApplicationUserUpdated(ApplicationUser applicationUser);

    /**
     * Called when a list of {@link ApplicationUser}s is loaded.
     *
     * @param applicationUsers
     *          the list of applicationUsers
     */
    void onApplicationUsersLoaded(ArrayList<ApplicationUser> applicationUsers);
  }

  /**
   * A task that manages the call to the server to create or update 
   * an object type.
   */
  private class CreateOrUpdateApplicationUserTask extends Task implements
      AsyncCallback<CreateOrUpdateApplicationUserResult> {
    private final ApplicationUser applicationUser;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateApplicationUserTask(ApplicationUser applicationUser, AppUser author) {
      this.create = applicationUser.getId() == 0;
      this.author = author;
      this.applicationUser = applicationUser;
    }

    @Override
    public void execute() {
      Main.status.showTaskStatus(Main.constants.saving());
      api.createOrUpdateApplicationUser(applicationUser, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof AppUserService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateApplicationUserResult result) {
      applicationUser.update(result.getId(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      getStatusObserver().onTaskFinished();
      if (create) notifyApplicationUserCreated(applicationUser);
      else notifyApplicationUserUpdated(applicationUser);
    }
  }

  /**
   * An rpc proxy for making calls to the server.
   */
  final AppUserServiceAsync api;
  
  /**
   * The currently logged in author.
   */
  private final AppUser appUser;

  /**
   * A url that can be used to log the current user out.
   */
  private final String logoutUrl;

  /** Languages */
  protected HashMap<Integer, Language> languages;

  /** Applications */
  private HashMap<Integer, Application> applications;

  /** Application users */
  protected HashMap<Integer, ApplicationUser> applicationUsers;

  /**
   * Manages the initial loading of applications and polls repeatedly for 
   * changes.
   */
  final LanguageLoader languageLoader;
  final ApplicationLoader applicationLoader;

  public UserModel(AppUser appUser, String logoutUrl, AppUserServiceAsync api, StatusObserver statusObserver) {
    super(statusObserver);
    this.appUser = appUser;
    this.logoutUrl = logoutUrl;
    this.api = api;
    
    languageLoader = new LanguageLoader(this);
    languageLoader.start();
    applicationLoader = new ApplicationLoader(this);
    applicationLoader.start();
  }
  
  @Override
  public ServiceAsync getService() {
    return api;
  }
  
  /**
   * Gets a url that can be used to log out the current user.
   *
   * @return
   */
  public String getLogoutUrl() {
    return logoutUrl;
  }

  /**
   * Gets the currently logged in author.
   *
   * @return
   */
  public AppUser getCurrentAuthor() {
    return appUser;
  }

  /**
   * Creates an {@link ApplicationUser} with the specified name and persists that 
   * change to the server.
   *
   * @param applicationUser
   */
  public void createOrUpdateApplicationUser(final ApplicationUser applicationUser) {
    taskQueue.post(new CreateOrUpdateApplicationUserTask(applicationUser, getCurrentAuthor()));
  }
  
  /**
   * Gets applications.
   *
   * @return
   */
  public HashMap<Integer, Language> getLanguages() {
    return languages;
  }

  public Language getLanguage(int langId) {
    return getLanguages().get(langId);
  }

  /**
   * @param applications the applications to set
   */
  public void setApplications(HashMap<Integer, Application> applications) {
    this.applications = applications;
  }

  /**
   * Gets applications.
   *
   * @return
   */
  public HashMap<Integer, Application> getApplications() {
    return applications;
  }

  public Application getApplication(int appId) {
    return applications.get(appId);
  }

  /**
   * Gets user's application.
   *
   * @return
   */
  public HashMap<Integer, ApplicationUser> getApplicationUsers() {
    return applicationUsers;
  }

  public ApplicationUser getApplicationUser(int appuId) {
    return applicationUsers.get(appuId);
  }
  
  // loading sequence

  void notifyLanguagesLoaded(ArrayList<Language> languages) {
    this.languages = new HashMap<Integer, Language>();
    for (Language language : languages) {
      getLanguages().put(language.getId(), language);
    }
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof LanguageObserver)
        ((LanguageObserver)dataObserver).onLanguagesLoaded(languages);
    }
  }

  void notifyApplicationsLoaded(ArrayList<Application> applications) {
    this.applications = new LinkedHashMap<Integer, Application>();
    for (Application application : applications) {
      this.applications.put(application.getId(), application);
    }
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ApplicationObserver)
        ((ApplicationObserver)dataObserver).onApplicationsLoaded(applications);
    }
  }

  void notifyApplicationUsersLoaded(ArrayList<ApplicationUser> appus) {
    this.applicationUsers = new HashMap<Integer, ApplicationUser>();
    for (ApplicationUser applicationUser : appus) {
      applicationUser.setApp(getApplication(applicationUser.getAppId()));
      getApplicationUsers().put(applicationUser.getId(), applicationUser);
    }
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ApplicationUserObserver)
        ((ApplicationUserObserver)dataObserver).onApplicationUsersLoaded(appus);
    }
  }

  // application user events

  void notifyApplicationUserCreated(ApplicationUser applicationUser) {
    getApplicationUsers().put(applicationUser.getId(), applicationUser);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ApplicationUserObserver)
        ((ApplicationUserObserver)dataObserver).onApplicationUserCreated(applicationUser);
    }
  }

  void notifyApplicationUserUpdated(ApplicationUser applicationUser) {
    getApplicationUsers().put(applicationUser.getId(), applicationUser);
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ApplicationUserObserver)
        ((ApplicationUserObserver)dataObserver).onApplicationUserUpdated(applicationUser);
    }
  }
}
