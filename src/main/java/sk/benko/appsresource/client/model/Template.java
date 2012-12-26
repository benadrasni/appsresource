package sk.benko.appsresource.client.model;

import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a template.
 *
 */
@SuppressWarnings("serial")
public class Template extends DesignItem {

  /**
   * The object type to which the template belongs.
   */
  private int otId;
  private ObjectType ot;

  /**
   * The leaf object attribute.
   */
  private int oaId;
  private ObjectAttribute oa;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param code
   * @param name
   */
  public Template(String name) {
    assert GWT.isClient();
    this.name = name;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param key
   * @param code
   * @param name
   * @param desc
   * @param otId
   * @param oaId
   * @param userId
   * @param lastUpdatedAt
   */
  public Template(int id, String code, String name, String desc, int otId, 
      ObjectType ot, int oaId, ObjectAttribute oa, int userId, 
      Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.otId = otId;
    this.ot = ot;
    this.oaId = oaId;
    this.oa = oa;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private Template() {
  }
  
  public int getOtId() {
    return otId;
  }

  public void setOtId(int otId) {
    this.otId = otId;
  }

  public ObjectType getOt() {
    return ot;
  }

  public void setOt(ObjectType ot) {
    this.ot = ot;
  }

  public int getOaId() {
    return oaId;
  }

  public void setOaId(int oaId) {
    this.oaId = oaId;
  }

  public ObjectAttribute getOa() {
    return oa;
  }

  public void setOa(ObjectAttribute oa) {
    this.oa = oa;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * template has been modified.
   *
   * @param template
   *          a template containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  Template update(Template template) {
    if (!template.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = template.id;
      code = template.code;
      name = template.name;
      desc = template.desc;
      otId = template.otId;
      ot = template.ot;
      oaId = template.oaId;
      oa = template.oa;
      userId = template.userId;
      lastUpdatedAt = template.lastUpdatedAt;
    }
    return this;
  }
}
