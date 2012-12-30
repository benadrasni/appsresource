package sk.benko.appsresource.client.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a template tree.
 *
 */
@SuppressWarnings("serial")
public class TemplateList implements Serializable {

  public static final int FLAG_DEFAULT = 0;
  
  public interface Observer {
    void onUpdate(TemplateList templateList);
  }

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;

  /**
   * The code of the template tree.
   */
  private String code;

  /**
   * The name of the template tree.
   */
  private String name;

  /**
   * The description of the template tree.
   */
  private String desc;

  /**
   * The template to which the template tree belongs.
   */
  private int tId;
  private Template t;

  /**
   * The flags of the template tree.
   * 1st bit ... default
   */
  private int flags;

  /**
   * The rank of the template tree within a template.
   */
  private int rank;

  /**
   * The key of the Author to whom this template tree belongs.
   */
  private int userId;

  /**
   * The time of the most recent update. This value is always supplied by the
   * server.
   */
  private Date lastUpdatedAt;

  /**
   * An observer to receive callbacks whenever this {@link TemplateList} is updated.
   */
  private transient Observer observer;

  /**
   * A constructor to be used on client-side only.
   *
   * @param name
   * @param tId
   */
  public TemplateList(String name, int tId) {
    assert GWT.isClient();
    this.name = name;
    this.tId = tId;
  }
  
  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param code
   * @param name
   * @param desc
   * @param tId
   * @param flags
   * @param userId
   * @param lastUpdatedAt
   */
  public TemplateList(int id, String code, String name, String desc, int tId, 
      int flags, int rank, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.tId = tId;
    this.flags = flags;
    this.rank = rank;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TemplateList() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
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

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
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
   * the objecType is modified.
   *
   * @return
   */
  public Observer getObserver() {
    return observer;
  }

  /**
   * Sets the observer that will receive notification when this objectType is
   * modified.
   *
   * @param observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  /**
   * Initializes transient data structures in the object. This will be called
   * directly by the controlling model when the objectType is first received.
   *
   * @param model
   *          the model that owns this {@link TemplateList}
   */
  void initialize(DesignerModel model) {
  }

  /**
   * Invoked when the TemplateGroup has been saved to the server.
   *
   * @param lastUpdatedAt
   *          the time that the server reported for the save
   * @return <code>this</code>, for chaining purposes
   */
  TemplateList update(Date lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * template tree has been modified.
   *
   * @param templateList
   *          a template containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  TemplateList update(TemplateList templateList) {
    if (!templateList.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = templateList.id;
      code = templateList.code;
      name = templateList.name;
      desc = templateList.desc;
      tId = templateList.tId;
      flags = templateList.flags;
      rank = templateList.rank;
      userId = templateList.userId;
      lastUpdatedAt = templateList.lastUpdatedAt;
      if (observer != null) observer.onUpdate(this);
    }
    return this;
  }

  TemplateList update(int id, String code, Date lastUpdatedAt) {
    this.id = id;
    this.code = code;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
  
  public boolean isDefault() {
    return ClientUtils.getFlag(FLAG_DEFAULT, this.flags);
  }

}
