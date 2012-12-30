package sk.benko.appsresource.client.model;

import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a template group.
 *
 */
@SuppressWarnings("serial")
public class TemplateGroup extends DesignItem {

  public static final int FLAG_SHOW_LABEL = 0;

  /**
   * The template to which the template group belongs.
   */
  private int tId;
  private Template t;

  /**
   * The flags of the template group.
   * 1st bit ... group label visible
   */
  private int flags;

  /**
   * The rank of the template group.
   */
  private int rank;

  /**
   * The subRank of the template group within a tab.
   */
  private int subRank;

  /**
   * The position's top of the template group's label.
   */
  private int labelTop;

  /**
   * The position's left of the template group's label.
   */
  private int labelLeft;

  /**
   * The width of the template group's label.
   */
  private int labelWidth;

  /**
   * The unit of the width of the template group's label.
   */
  private String labelWidthUnit;

  /**
   * The align of the template group's label.
   */
  private String labelAlign;

  /**
   * A constructor to be used on client-side only.
   *
   * @param name
   * @param tId
   */
  public TemplateGroup(String name, int tId) {
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
   * @param rank
   * @param subRank
   * @param labelTop
   * @param labelLeft
   * @param labelWidth
   * @param labelWidthUnit
   * @param labelAlign
   * @param userId
   * @param lastUpdatedAt
   */
  public TemplateGroup(int id, String code, String name, String desc, int tId, 
      Template t, int flags, int rank, int subRank, int labelTop, int labelLeft, int labelWidth,
      String labelWidthUnit, String labelAlign, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.tId = tId;
    this.t = t;
    this.flags = flags;
    this.rank = rank;
    this.subRank = subRank;
    this.labelTop = labelTop;
    this.labelLeft = labelLeft;
    this.labelWidth = labelWidth;
    this.labelWidthUnit = labelWidthUnit;
    this.labelAlign = labelAlign;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }
  
  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TemplateGroup() {
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
   * @return the subRank
   */
  public int getSubRank() {
    return subRank;
  }

  /**
   * @param subRank the subRank to set
   */
  public void setSubRank(int subRank) {
    this.subRank = subRank;
  }

  /**
   * @return the labelTop
   */
  public int getLabelTop() {
    return labelTop;
  }

  /**
   * @param labelTop the labelTop to set
   */
  public void setLabelTop(int labelTop) {
    this.labelTop = labelTop;
  }

  /**
   * @return the labelLeft
   */
  public int getLabelLeft() {
    return labelLeft;
  }

  /**
   * @param labelLeft the labelLeft to set
   */
  public void setLabelLeft(int labelLeft) {
    this.labelLeft = labelLeft;
  }

  /**
   * @return the labelWidth
   */
  public int getLabelWidth() {
    return labelWidth;
  }

  /**
   * @param labelWidth the labelWidth to set
   */
  public void setLabelWidth(int labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * @return the labelWidthUnit
   */
  public String getLabelWidthUnit() {
    return labelWidthUnit;
  }

  /**
   * @param labelWidthUnit the labelWidthUnit to set
   */
  public void setLabelWidthUnit(String labelWidthUnit) {
    this.labelWidthUnit = labelWidthUnit;
  }

  public String getLabelAlign() {
    return labelAlign;
  }

  public void setLabelAlign(String labelAlign) {
    this.labelAlign = labelAlign;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * template group has been modified.
   *
   * @param templateGroup
   *          a template containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  TemplateGroup update(TemplateGroup templateGroup) {
    if (!templateGroup.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = templateGroup.id;
      code = templateGroup.code;
      name = templateGroup.name;
      desc = templateGroup.desc;
      tId = templateGroup.tId;
      t = templateGroup.t;
      rank = templateGroup.rank;
      subRank = templateGroup.subRank;
      labelTop = templateGroup.labelTop;
      labelLeft = templateGroup.labelLeft;
      labelWidth = templateGroup.labelWidth;
      labelWidthUnit = templateGroup.labelWidthUnit;
      labelAlign = templateGroup.labelAlign;
      userId = templateGroup.userId;
      lastUpdatedAt = templateGroup.lastUpdatedAt;
    }
    return this;
  }

  public boolean isLabelVisible() {
    return ClientUtils.getFlag(FLAG_SHOW_LABEL, this.flags);
  }
}
