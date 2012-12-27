package sk.benko.appsresource.client.model;

import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing an application.
 *
 */
@SuppressWarnings("serial")
public class Application extends DesignItem {

  public static final int FLAG_PUBLIC = 0;
  public static final int FLAG_RECOMMENDED = 1;

  /**
   * The category of the application.
   */
  private String category;

  /**
   * The flags of the application.
   */
  private int flags;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param name      the name of the application
   */
  public Application(String name) {
    assert GWT.isClient();
    this.name = name;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param id              application's id
   * @param code            application's code
   * @param name            application's name
   * @param desc            application's description
   * @param flags           application's flags
   * @param userId          who created application
   * @param lastUpdatedAt   when application was updated
   */
  public Application(int id, String code, String name, String desc, 
      String category, int flags, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.category = category;
    this.flags = flags;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }
  
  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private Application() {
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * application has been modified.
   *
   * @param application   an application containing up-to-date information about <code>this</code>
   * @return              <code>this</code>, for chaining purposes
   */
  Application update(Application application) {
    if (!application.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = application.id;
      code = application.code;
      name = application.name;
      desc = application.desc;
      category = application.category;
      flags = application.flags;
      userId = application.userId;
      lastUpdatedAt = application.lastUpdatedAt;
    }
    return this;
  }

  public boolean isPublic() {
    return ClientUtils.getFlag(FLAG_PUBLIC, this.flags);
  }

  public boolean isRecommended() {
    return ClientUtils.getFlag(FLAG_RECOMMENDED, this.flags);
  }
}
