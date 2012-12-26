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
public class ApplicationUser implements Serializable {

  public static final int FLAG_VISIBLE = 0;
  public static final int FLAG_ADMIN = 1;
  public static final int FLAG_WRITE = 2;

  public interface Observer {
    void onUpdate(ApplicationUser appu);
  }

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;

  /**
   * The key of the application.
   */
  private int appId;
  private Application app;

  /**
   * The key of the user.
   */
  private int appUserId;

  /**
   * The flags of the application.
   */
  private int flags;

  /**
   * The top of the application widget.
   */
  private int top;

  /**
   * The left position of the application widget.
   */
  private int left;

  /**
   * The width of the application widget.
   *
   */
  private int width;

  /**
   * The height of the application widget
   *
   */
  private int height;

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
   * The date of the first time this object was persisted.
   */
  private Date subscribedAt = new Date();

  /**
   * An observer to receive callbacks whenever this {@link ApplicationUser} is updated.
   */
  private transient Observer observer;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param appId
   * @param appUserId
   */
  public ApplicationUser(int appId, int appUserId) {
    assert GWT.isClient();
    this.appId = appId;
    this.appUserId = appUserId;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param appId
   * @param appUserId
   * @param flags
   * @param top
   * @param left
   * @param width
   * @param height
   * @param userId
   * @param lastUpdatedAt
   * @param subscribedAt
   */
  public ApplicationUser(int id, int appId, int appUserId, int flags, 
      int top, int left, int width, int height, int userId, Date lastUpdatedAt,
      Date subscribedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.appId = appId;
    this.appUserId = appUserId;
    this.flags = flags;
    this.top = top;
    this.left = left;
    this.width = width;
    this.height = height;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
    this.subscribedAt = subscribedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private ApplicationUser() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public int getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(int appUserId) {
    this.appUserId = appUserId;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public int getTop() {
    return top;
  }

  public void setTop(int top) {
    this.top = top;
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
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

  public Date getSubscribedAt() {
    return subscribedAt;
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
   * Invoked when the application template has been saved to the server.
   *
   * @param lastUpdatedAt
   *          the time that the server reported for the save
   * @return <code>this</code>, for chaining purposes
   */
  ApplicationUser update(Date lastUpdatedAt) {
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
  ApplicationUser update(ApplicationUser appt) {
    if (!appt.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = appt.id;
      appId = appt.appId;
      appUserId = appt.appUserId;
      flags = appt.flags;
      top = appt.top;
      left = appt.left;
      width = appt.width;
      height = appt.height;
      userId = appt.userId;
      lastUpdatedAt = appt.lastUpdatedAt;
      subscribedAt = appt.subscribedAt;
      if (observer != null) observer.onUpdate(this);
    }
    return this;
  }

  ApplicationUser update(int id, Date lastUpdatedAt) {
    this.id = id;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }

  public boolean isVisible() {
    return ClientUtils.getFlag(FLAG_VISIBLE, this.flags);
  }

  public boolean isAdmin() {
    return ClientUtils.getFlag(FLAG_ADMIN, this.flags);
  }

  public boolean isWrite() {
    return ClientUtils.getFlag(FLAG_WRITE, this.flags);
  }
}
