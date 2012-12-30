package sk.benko.appsresource.client.model;

import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a template relation.
 *
 */
@SuppressWarnings("serial")
public class TemplateRelation extends DesignItem {

  public static final int FLAG_VISIBLE = 0;

  /**
   * The template to which the template relation belongs.
   */
  private int t1Id;
  private Template t1;

  /**
   * The template to which the template is connected.
   */
  private int t2Id;
  private Template t2;

  /**
   * The object relation to which the template relation belongs.
   */
  private int orId;
  private ObjectRelation or;

  /**
   * The flags of the template relation.
   * 1st bit ... relation visible
   */
  private int flags;

  /**
   * The rank of the template relation.
   */
  private int rank;

  /**
   * The subrank of the template relation within a tab.
   */
  private int subrank;

  /**
   * A constructor to be used on client-side only.
   *
   * @param name
   * @param t1Id
   * @param t2Id
   * @param orId
   */
  public TemplateRelation(String name, int t1Id, int t2Id, int orId) {
    assert GWT.isClient();
    this.name = name;
    this.t1Id = t1Id;
    this.t2Id = t2Id;
    this.orId = orId;
  }
  
  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param code
   * @param name
   * @param desc
   * @param t1Id
   * @param t1
   * @param t2Id
   * @param t2
   * @param orId
   * @param or
   * @param flags
   * @param rank
   * @param subRank
   * @param userId
   * @param lastUpdatedAt
   */
  public TemplateRelation(int id, String code, String name, String desc, 
      int t1Id, Template t1, int t2Id, Template t2, int orId, ObjectRelation or, 
      int flags, int rank, int subRank, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.t1Id = t1Id;
    this.t1 = t1;
    this.t2Id = t2Id;
    this.t2 = t2;
    this.orId = orId;
    this.or = or;
    this.flags = flags;
    this.rank = rank;
    this.subrank = subRank;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TemplateRelation() {
  }
  
  public int getT1Id() {
    return t1Id;
  }

  public void setT1Id(int t1Id) {
    this.t1Id = t1Id;
  }

  public Template getT1() {
    return t1;
  }

  public void setT1(Template t1) {
    this.t1 = t1;
  }
  
  public int getT2Id() {
    return t2Id;
  }

  public void setT2Id(int t2Id) {
    this.t2Id = t2Id;
  }

  public Template getT2() {
    return t2;
  }

  public void setT2(Template t2) {
    this.t2 = t2;
  }

  public int getOrId() {
    return orId;
  }

  public void setOrId(int orId) {
    this.orId = orId;
  }

  public ObjectRelation getOr() {
    return or;
  }

  public void setOr(ObjectRelation or) {
    this.or = or;
  }

  /**
   * @return the flags
   */
  public int getFlags() {
    return flags;
  }

  /**
   * @param flags the flags to set
   */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /**
   * @return the rank
   */
  public int getRank() {
    return rank;
  }

  /**
   * @param rank the rank to set
   */
  public void setRank(int rank) {
    this.rank = rank;
  }

  /**
   * @return the subrank
   */
  public int getSubrank() {
    return subrank;
  }

  /**
   * @param subrank the subrank to set
   */
  public void setSubrank(int subrank) {
    this.subrank = subrank;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * template relation has been modified.
   *
   * @param templateRelation
   *          a template containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  TemplateRelation update(TemplateRelation templateRelation) {
    if (!templateRelation.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = templateRelation.id;
      code = templateRelation.code;
      name = templateRelation.name;
      desc = templateRelation.desc;
      t1Id = templateRelation.t1Id;
      t1 = templateRelation.t1;
      t2Id = templateRelation.t2Id;
      t2 = templateRelation.t2;
      orId = templateRelation.orId;
      or = templateRelation.or;
      rank = templateRelation.rank;
      subrank = templateRelation.subrank;
      userId = templateRelation.userId;
      lastUpdatedAt = templateRelation.lastUpdatedAt;
    }
    return this;
  }

  TemplateRelation update(int id, String code, ObjectRelation or, Template t1, 
      Template t2, Date lastUpdatedAt) {
    this.id = id;
    this.code = code;
    this.or = or;
    this.t1 = t1;
    this.t2 = t2;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
  
  public boolean isVisible() {
    return ClientUtils.getFlag(FLAG_VISIBLE, this.flags);
  }
}
