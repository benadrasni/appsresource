package sk.benko.appsresource.client.model;

import java.io.Serializable;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a language.
 *
 */
@SuppressWarnings("serial")
public class Language implements Serializable {

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;

  /**
   * The code of the language.
   */
  private String code;

  /**
   * The name of the language.
   */
  private String name;

  /**
   * A constructor to be used on server-side only.
   *
   * @param key
   * @param code
   * @param name
   */
  public Language(int id, String code, String name) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private Language() {
  }

  public int getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
