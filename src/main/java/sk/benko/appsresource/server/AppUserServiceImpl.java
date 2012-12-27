package sk.benko.appsresource.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.benko.appsresource.client.model.AppUser;
import sk.benko.appsresource.client.model.AppUserService;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.Language;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * The server-side RPC endpoint for {@link AppUserService}.
 *
 *
 */
@SuppressWarnings("serial")
public class AppUserServiceImpl extends ServiceImpl implements AppUserService {
  private static final Logger log = Logger.getLogger(AppUserServiceImpl.class.getName());

  /**
   * A reference to a cache service.
   */
  private final Cache cache = new Cache(MemcacheServiceFactory
      .getMemcacheService());

  /**
   * A reference to the data store.
   */
  private final StoreDB store = new StoreDB();
  
  /**
   * A convenient way to get the current user and throw an exception if the user
   * isn't logged in.
   *
   * @param userService
   *          the user service to use
   * @return the current user
   * @throws AccessDeniedException
   */
  private static User tryGetCurrentUser(UserService userService)
      throws AccessDeniedException {
    if (!userService.isUserLoggedIn()) {
      throw new AppUserService.AccessDeniedException();
    }
    return userService.getCurrentUser();
  }
  
  public UserInfoResult getUserInfo() throws AccessDeniedException {
    final UserService userService = UserServiceFactory.getUserService();
    final User user = tryGetCurrentUser(userService);
    final StoreDB.Api api = store.getApi();
    try {
      StoreDB.AppUser appUser = api.getUser(user.getEmail());
    
      final UserInfoResult result = new AppUserService.UserInfoResult(new AppUser(
          appUser.getId(), user.getEmail(), user.getNickname(), appUser.getFlags()), 
          userService.createLogoutURL(userService.createLoginURL("/")));
      api.commit();

      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }
  
  /*
   * Languages
   */

  private static ArrayList<Language> toClientLanguages(
      ArrayList<StoreDB.Language> languages) {
    final ArrayList<Language> clients = new ArrayList<Language>();
    for (StoreDB.Language n : languages) {
      clients.add(new Language(n.getId(), n.getCode(), n.getName()));
    }
    return clients;
  }

  public GetLanguagesResult getLanguages() throws AccessDeniedException {
    return new GetLanguagesResult(getLangs());
  }

  private ArrayList<Language> getLangs() {
    final StoreDB.Api api = store.getApi();
    try {
      return toClientLanguages(api.getLanguages());
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<Language>();  
  }  
  
  /*
   * Application
   */

  private static ArrayList<Application> toClientApplications(
      ArrayList<StoreDB.Application> applications) {
    final ArrayList<Application> clients = new ArrayList<Application>();
    for (StoreDB.Application n : applications) {
      clients.add(new Application(n.getId(), n.getCode(), n.getName(), 
          n.getDesc(), n.getCategory(), n.getFlags(), n.getUserId(), 
          n.getLastUpdatedAt()));
    }
    return clients;
  }

  public GetApplicationsResult getApplications() throws AccessDeniedException {
    return new GetApplicationsResult(getAs());
  }

  private ArrayList<Application> getAs() {
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<Application> fromCache = cache.getApplications();
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putApplications(
          toClientApplications(api.getApplications()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<Application>();  
  }  

  /*
   * Application User
   */
  
  private static ArrayList<ApplicationUser> toClientApplicationUsers(
      ArrayList<StoreDB.ApplicationUser> applicationUsers) {
    final ArrayList<ApplicationUser> clients = new ArrayList<ApplicationUser>();
    for (StoreDB.ApplicationUser n : applicationUsers) {
      clients.add(new ApplicationUser(n.getId(), n.getAppId(), n.getAppUserId(), 
          n.getFlags(), n.getTop(), n.getLeft(), n.getWidth(), n.getHeight(),
          n.getUserId(), n.getLastUpdatedAt(), n.getSubscribedAt()));
    }
    return clients;
  }

  public GetApplicationUsersResult getApplicationUsers(int appId)
      throws AccessDeniedException {
    return new AppUserService.GetApplicationUsersResult(getAppUsers(appId));
  }

  public GetApplicationUsersResult getUserApplications(int userId)
      throws AccessDeniedException {
    return new AppUserService.GetApplicationUsersResult(getUserApps(userId));
  }

  public CreateOrUpdateApplicationUserResult createOrUpdateApplicationUser(
      final ApplicationUser applicationUser, final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.ApplicationUser au = new StoreDB.ApplicationUser(applicationUser.getId(), 
          applicationUser.getAppId(), applicationUser.getAppUserId(), applicationUser.getFlags(), 
          applicationUser.getTop(), applicationUser.getLeft(), applicationUser.getWidth(),
          applicationUser.getHeight(), author.getId());
      api.saveApplicationUser(au);
      final CreateOrUpdateApplicationUserResult result = 
          new CreateOrUpdateApplicationUserResult(au.getId(), au.getLastUpdatedAt());
      api.commit();
      
      cache.deleteApplicationUsers(au.getAppUserId());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }
  
  private ArrayList<ApplicationUser> getAppUsers(int appId) {
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<ApplicationUser> fromCache = cache.getApplicationUsers(appId);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putApplicationUsers(
          appId, toClientApplicationUsers(api.getApplicationUsers(appId)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<ApplicationUser>();  
  }

  private ArrayList<ApplicationUser> getUserApps(int userId) {
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<ApplicationUser> fromCache = cache.getApplicationUsers(userId);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putApplicationUsers(
          userId, toClientApplicationUsers(api.getUserApplications(userId)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<ApplicationUser>();  
  }
}
