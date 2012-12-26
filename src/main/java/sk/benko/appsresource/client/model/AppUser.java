package sk.benko.appsresource.client.model;

import java.io.Serializable;

import sk.benko.appsresource.client.ClientUtils;

/**
 * A client-side data object representing an author.
 *
 */
@SuppressWarnings("serial")
public class AppUser implements Serializable {
   public static int FLAG_DESIGNER = 0;

  /**
   * Returns a shorter name for an author. For names of the form a@company.com,
   * this returns "a". For names of the form First Last, this returns "First".
   *
   * @return
   */
  public static String getShortName(String name) {
    final int atIndex = name.indexOf('@');
    if (atIndex > 0) {
      return name.substring(0, atIndex);
    }

    final int spIndex = name.indexOf(' ');
    if (spIndex > 0) {
      return name.substring(0, spIndex);
    }

    return name;
  }

  /**
   * The user's id.
   */
  private int id;

  /**
   * The user's email address.
   */
  private String email;

  /**
   * A nickname for the user. If the user has no nick name this will be set to
   * the user's email address, as well.
   */
  private String name;

  /**
   * The user's flags.
   */
  private int flags;

  /**
   * @param email
   *          the authors email
   * @param name
   *          a nick name for the user
   */
  public AppUser(int id, String email, String name, int flags) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.flags = flags;
  }

  /**
   * Need for RPC serialization.
   */
  @SuppressWarnings("unused")
  private AppUser() {
  }

  public int getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public int getFlags() {
    return flags;
  }

  /**
   * Convenience method that calls through to
   * {@link AppUser#getShortName(String)}.
   *
   * @return
   */
  public String getShortName() {
    return getShortName(name);
  }
  
  public boolean isDesigner() {
    return ClientUtils.getFlag(FLAG_DESIGNER, this.flags);
  }

}
