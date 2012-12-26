package sk.benko.appsresource.client.model;

import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a template attribute.
 *
 */
@SuppressWarnings("serial")
public class TemplateAttribute extends DesignItem {
  public static String FUCTION_YEAR = "year(%s)";
  public static String FUCTION_SUBSTRING = "substring(%s";
  
  public static final int FLAG_MANDATORY = 0;
  public static final int FLAG_SHOW_LABEL = 1;
  public static final int FLAG_SHOW_VALUE = 2;
  public static final int FLAG_SHOW_UNIT = 3;
  public static final int FLAG_DESCENDANT = 4;
  public static final int FLAG_DERIVED = 5;

  public static final int STYLE_LABEL = 1;
  public static final int STYLE_TEXTBOX = 2;
  public static final int STYLE_DATEPICKER = 3;
  public static final int STYLE_DYNAMICCOMBO = 4;
  public static final int STYLE_TABLE = 5;
  public static final int STYLE_IMAGE = 6;
  public static final int STYLE_TEXTAREA = 7;
  public static final int STYLE_LINK = 8;

  public static final int TABLETYPE_TABLE = 0;
  public static final int TABLETYPE_GRAPH = 1;
  public static final int TABLETYPE_TABLEGRAPH = 2;

  public static final int TABLEORIENTATION_HORIZONTAL = 0;
  public static final int TABLEORIENTATION_VERTICAL = 1;

  public static final int TABLEGRAPHTYPE_LINE = 0;
  public static final int TABLEGRAPHTYPE_BAR = 1;
  public static final int TABLEGRAPHTYPE_AREA = 2;

  /**
   * The template group to which the template attribute belongs.
   */
  private int tgId;
  private TemplateGroup tg;

  /**
   * The object attribute which the template attribute represents.
   */
  private int oaId;
  private ObjectAttribute oa;

  /**
   * The flags of the template attribute.
   * 1st bit ... attribute label's visibility
   * 2nd bit ... attribute's visibility
   * 3rd bit ... attribute unit's visibility
   */
  private int flags;

  /**
   * The style of the template attribute.
   */
  private int style;

  /**
   * The tabindex of the template attribute.
   */
  private int tabIndex;

  /**
   * The default value of the template attribute.
   */
  private String def;

  /**
   * The length of the template attribute.
   */
  private int length;

  /**
   * The position's top of the template attribute's label.
   */
  private int labelTop;

  /**
   * The position's left of the template attribute's label.
   */
  private int labelLeft;

  /**
   * The width of the template attribute's label.
   */
  private int labelWidth;

  /**
   * The unit of the width of the template attribute's label.
   */
  private String labelWidthUnit;

  /**
   * The align of the template attribute's label.
   */
  private String labelAlign;

  /**
   * The position's top of the template group's label.
   */
  private int top;

  /**
   * The position's left of the template attribute.
   */
  private int left;

  /**
   * The width of the template attribute.
   */
  private int width;

  /**
   * The unit of the width of the template attribute.
   */
  private String widthUnit;

  /**
   * The align of the template attribute.
   */
  private String align;

  /**
   * The position's top of the template attribute's unit.
   */
  private int unitTop;

  /**
   * The position's left of the template attribute's unit.
   */
  private int unitLeft;

  /**
   * The width of the template attribute's unit.
   */
  private int unitWidth;

  /**
   * The unit of the width of the template attribute's unit.
   */
  private String unitWidthUnit;

  /**
   * The align of the template attribute's unit.
   */
  private String unitAlign;
  
  /**
   * Shared values are used for specific styles (like dynamic combos, tables, ...) 
   */
  private int shared1;
  private int shared2;
  private int shared3;
  private int shared4;
  private int shared5;
  
  /**
   * A basic constructor to be used on client-side only.
   *
   * @param name
   * @param tgId
   */
  public TemplateAttribute(String name, int tgId) {
    assert GWT.isClient();
    this.name = name;
    this.tgId = tgId;
  }
  
  /**
   * A constructor to be used on server-side only.
   *
   * @param key
   * @param code
   * @param name
   * @param desc
   * @param tgId
   * @param oaId
   * @param flags
   * @param style
   * @param default
   * @param length
   * @param labelTop
   * @param labelLeft
   * @param labelWidth
   * @param labelWidthUnit
   * @param labelAlign
   * @param top
   * @param left
   * @param width
   * @param widthUnit
   * @param align
   * @param unitTop
   * @param unitLeft
   * @param unitWidth
   * @param unitWidthUnit
   * @param unitAlign
   * @param shared1
   * @param shared2
   * @param shared3
   * @param shared4
   * @param shared5
   * @param userId
   * @param lastUpdatedAt
   */
  public TemplateAttribute(int id, String code, String name, String desc, 
      int tgId, TemplateGroup tg, int oaId, ObjectAttribute oa, int flags, 
      int style, int tabIndex, String def, int length, int labelTop, 
      int labelLeft, int labelWidth, String labelWidthUnit, String labelAlign, 
      int top, int left, int width, String widthUnit, String align, int unitTop, 
      int unitLeft, int unitWidth, String unitWidthUnit, String unitAlign, 
      int shared1, int shared2, int shared3, int shared4, int shared5, 
      int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.tgId = tgId;
    this.tg = tg;
    this.oaId = oaId;
    this.oa = oa;
    this.flags = flags;
    this.style = style;
    this.tabIndex = tabIndex;
    this.def = def;
    this.length = length;
    this.labelTop = labelTop;
    this.labelLeft = labelLeft;
    this.labelWidth = labelWidth;
    this.labelWidthUnit = labelWidthUnit;
    this.labelAlign = labelAlign;
    this.top = top;
    this.left = left;
    this.width = width;
    this.widthUnit = widthUnit;
    this.align = align;
    this.unitTop = unitTop;
    this.unitLeft = unitLeft;
    this.unitWidth = unitWidth;
    this.unitWidthUnit = unitWidthUnit;
    this.unitAlign = unitAlign;
    this.shared1 = shared1;
    this.shared2 = shared2;
    this.shared3 = shared3;
    this.shared4 = shared4;
    this.shared5 = shared5;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TemplateAttribute() {
  }
  
  public int getTgId() {
    return tgId;
  }

  public void setTgId(int tgId) {
    this.tgId = tgId;
  }

  public TemplateGroup getTg() {
    return tg;
  }

  public void setTg(TemplateGroup tg) {
    this.tg = tg;
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

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public int getStyle() {
    return style;
  }

  public void setStyle(int style) {
    this.style = style;
  }
  
  public int getTabIndex() {
    return tabIndex;
  }

  public void setTabIndex(int tabIndex) {
    this.tabIndex = tabIndex;
  }

  public String getDef() {
    return def;
  }

  public void setDef(String def) {
    this.def = def;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getLabelTop() {
    return labelTop;
  }

  public void setLabelTop(int labelTop) {
    this.labelTop = labelTop;
  }

  public int getLabelLeft() {
    return labelLeft;
  }

  public void setLabelLeft(int labelLeft) {
    this.labelLeft = labelLeft;
  }

  public int getLabelWidth() {
    return labelWidth;
  }

  public void setLabelWidth(int labelWidth) {
    this.labelWidth = labelWidth;
  }

  public String getLabelWidthUnit() {
    return labelWidthUnit;
  }

  public void setLabelWidthUnit(String labelWidthUnit) {
    this.labelWidthUnit = labelWidthUnit;
  }

  public String getLabelAlign() {
    return labelAlign;
  }

  public void setLabelAlign(String labelAlign) {
    this.labelAlign = labelAlign;
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

  public String getWidthUnit() {
    return widthUnit;
  }

  public void setWidthUnit(String widthUnit) {
    this.widthUnit = widthUnit;
  }

  public String getAlign() {
    return align;
  }

  public void setAlign(String align) {
    this.align = align;
  }

  public int getUnitTop() {
    return unitTop;
  }

  public void setUnitTop(int unitTop) {
    this.unitTop = unitTop;
  }

  public int getUnitLeft() {
    return unitLeft;
  }

  public void setUnitLeft(int unitLeft) {
    this.unitLeft = unitLeft;
  }

  public int getUnitWidth() {
    return unitWidth;
  }

  public void setUnitWidth(int unitWidth) {
    this.unitWidth = unitWidth;
  }

  public String getUnitWidthUnit() {
    return unitWidthUnit;
  }

  public void setUnitWidthUnit(String unitWidthUnit) {
    this.unitWidthUnit = unitWidthUnit;
  }

  public String getUnitAlign() {
    return unitAlign;
  }

  public void setUnitAlign(String unitAlign) {
    this.unitAlign = unitAlign;
  }

  public int getShared1() {
    return shared1;
  }

  public void setShared1(int shared1) {
    this.shared1 = shared1;
  }

  public int getShared2() {
    return shared2;
  }

  public void setShared2(int shared2) {
    this.shared2 = shared2;
  }

  public int getShared3() {
    return shared3;
  }

  public void setShared3(int shared3) {
    this.shared3 = shared3;
  }

  public int getShared4() {
    return shared4;
  }

  public void setShared4(int shared4) {
    this.shared4 = shared4;
  }

  public int getShared5() {
    return shared5;
  }

  public void setShared5(int shared5) {
    this.shared5 = shared5;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * template group has been modified.
   *
   * @param templateAttribute
   *          a template attribute containing up-to-date information 
   *          about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  TemplateAttribute update(TemplateAttribute templateAttribute) {
    if (!templateAttribute.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = templateAttribute.id;
      code = templateAttribute.code;
      name = templateAttribute.name;
      desc = templateAttribute.desc;
      tgId = templateAttribute.tgId;
      tg = templateAttribute.tg;
      oaId = templateAttribute.oaId;
      oa = templateAttribute.oa;
      style = templateAttribute.style;
      length = templateAttribute.length;
      def = templateAttribute.def;
      labelTop = templateAttribute.labelTop;
      labelLeft = templateAttribute.labelLeft;
      labelWidth = templateAttribute.labelWidth;
      labelWidthUnit = templateAttribute.labelWidthUnit;
      labelAlign = templateAttribute.labelAlign;
      top = templateAttribute.top;
      left = templateAttribute.left;
      width = templateAttribute.width;
      widthUnit = templateAttribute.widthUnit;
      align = templateAttribute.align;
      unitTop = templateAttribute.unitTop;
      unitLeft = templateAttribute.unitLeft;
      unitWidth = templateAttribute.unitWidth;
      unitWidthUnit = templateAttribute.unitWidthUnit;
      unitAlign = templateAttribute.unitAlign;
      shared1 = templateAttribute.shared1;
      shared2 = templateAttribute.shared2;
      shared3 = templateAttribute.shared3;
      shared4 = templateAttribute.shared4;
      shared5 = templateAttribute.shared5;
      userId = templateAttribute.userId;
      lastUpdatedAt = templateAttribute.lastUpdatedAt;
    }
    return this;
  }

  TemplateAttribute update(int id, String code, TemplateGroup tg, 
      ObjectAttribute oa, Date lastUpdatedAt) {
    this.id = id;
    this.code = code;
    this.tg = tg;
    this.oa = oa;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
  
  public boolean isMandatory() {
    return ClientUtils.getFlag(FLAG_MANDATORY, this.flags);
  }

  public boolean isLabelVisible() {
    return ClientUtils.getFlag(FLAG_SHOW_LABEL, this.flags);
  }

  public boolean isValueVisible() {
    return ClientUtils.getFlag(FLAG_SHOW_VALUE, this.flags);
  }

  public boolean isUnitVisible() {
    return ClientUtils.getFlag(FLAG_SHOW_UNIT, this.flags);
  }
  
  public boolean isDescendant() {
    return ClientUtils.getFlag(FLAG_DESCENDANT, this.flags);
  }

  public boolean isDerived() {
    return ClientUtils.getFlag(FLAG_DERIVED, this.flags);
  }

}
