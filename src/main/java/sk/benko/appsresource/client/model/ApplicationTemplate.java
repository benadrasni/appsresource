package sk.benko.appsresource.client.model;

import java.io.Serializable;
import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing an application.
 *
 */
@SuppressWarnings("serial")
public class ApplicationTemplate implements Serializable {

  public static final int FLAG_PUBLICDATA = 0;

  public interface Observer {
    void onUpdate(ApplicationTemplate appt);
  }

  /**
   * The key of the application.
   */
  private int appId;
  private Application app;

  /**
   * The key of the template.
   */
  private int tId;
  private Template t;

  /**
   * The flags of the application.
   */
  private int flags;

  /**
   * The key of the parent menu template.
   */
  private int parentMenuId;

  /**
   * The rank of the template within menu.
   */
  private int rank;

  /**
   * The key of the Author to whom this objectType belongs.
   */
  private int userId;

  /**
   * The time of the most recent update. This value is always supplied by the
   * server.
   */
  private Date lastUpdatedAt;

  /**
   * An observer to receive callbacks whenever this {@link ApplicationTemplate} is updated.
   */
  private transient Observer observer;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param appId
   * @param tId
   */
  public ApplicationTemplate(int appId, int tId) {
    assert GWT.isClient();
    this.appId = appId;
    this.tId = tId;
  }

  /**
   * A constructor to be used on client-side only.
   *
   * @param appId
   * @param tId
   * @param flags
   * @param parentMenuId
   * @param rank
   */
  public ApplicationTemplate(int appId, int tId, int flags, int parentMenuId,
      int rank) {
    assert GWT.isClient();
    this.appId = appId;
    this.tId = tId;
    this.flags = flags;
    this.parentMenuId = parentMenuId;
    this.rank = rank;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param appId
   * @param tId
   * @param flags
   * @param parentMenuId
   * @param rank
   * @param userId
   * @param lastUpdatedAt
   */
  public ApplicationTemplate(int appId, Application app, int tId, Template t,
      int flags, int parentMenuId, int rank, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.appId = appId;
    this.app = app;
    this.tId = tId;
    this.t = t;
    this.flags = flags;
    this.parentMenuId = parentMenuId;
    this.rank = rank;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private ApplicationTemplate() {
  }

  public int getAppId() {
    return appId;
  }

  public void setAppId(int appId) {
    this.appId = appId;
  }

  public Application getApp() {
    return app;
  }

  public void setApp(Application app) {
    this.app = app;
  }

  public int getTId() {
    return tId;
  }

  public void setTId(int tId) {
    this.tId = tId;
  }

  public Template getT() {
    return t;
  }

  public void setT(Template t) {
    this.t = t;
  }

  public int getParentMenuId() {
    return parentMenuId;
  }

  public void setParentMenuId(int parentMenuId) {
    this.parentMenuId = parentMenuId;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Date getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  /**
   * Gets the observer that is receiving notification when 
   * the application template is modified.
   *
   * @return
   */
  public Observer getObserver() {
    return observer;
  }

  /**
   * Sets the observer that will receive notification when this application 
   * template is modified.
   *
   * @param observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  /**
   * Initializes transient data structures in the object. This will be called
   * directly by the controlling model when the application template 
   * is first received.
   *
   * @param model
   *          the model that owns this {@link ApplicationTemplate}
   */
  void initialize(DesignerModel model) {
  }

  /**
   * Invoked when the application template has been saved to the server.
   *
   * @param lastUpdatedAt
   *          the time that the server reported for the save
   * @return <code>this</code>, for chaining purposes
   */
  ApplicationTemplate update(Date lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * application template has been modified.
   *
   * @param appt
   *          a note containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  ApplicationTemplate update(ApplicationTemplate appt) {
    if (!appt.getLastUpdatedAt().equals(lastUpdatedAt)) {
      appId = appt.appId;
      tId = appt.tId;
      flags = appt.flags;
      parentMenuId = appt.parentMenuId;
      rank = appt.rank;
      userId = appt.userId;
      lastUpdatedAt = appt.lastUpdatedAt;
      if (observer != null) observer.onUpdate(this);
    }
    return this;
  }

  ApplicationTemplate update(int appId, int tId, Date lastUpdatedAt) {
    this.appId = appId;
    this.tId = tId;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
  
  public boolean isPublicData() {
    return ClientUtils.getFlag(FLAG_PUBLICDATA, this.flags);
  }

}
