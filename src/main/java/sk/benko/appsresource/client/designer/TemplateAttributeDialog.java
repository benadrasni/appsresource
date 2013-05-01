package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.designer.model.ObjectAttributeLoader;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabel;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.ui.widget.IntegerTextBox;
import sk.benko.appsresource.client.util.DateHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *  Dialog box for creating or editing template attribute. Application and template must be defined in model.
 */
public class TemplateAttributeDialog extends DesignerDialog implements DesignerModel.TemplateGroupObserver,
    DesignerModel.ObjectAttributeObserver {

  private static ArrayList<DropDownObject> styles;
  private static ArrayList<DropDownObject> tableTypes;
  private static ArrayList<DropDownObject> tableOrientations;
  private static ArrayList<DropDownObject> graphTypes;

  static {
    styles = new ArrayList<DropDownObject>();
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_LABEL, Main.constants.templateAttributeStyle1()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_TEXTBOX, Main.constants.templateAttributeStyle2()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_DATEPICKER, Main.constants.templateAttributeStyle3()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_DYNAMICCOMBO, Main.constants.templateAttributeStyle4()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_TABLE, Main.constants.templateAttributeStyle5()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_IMAGE, Main.constants.templateAttributeStyle6()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_TEXTAREA, Main.constants.templateAttributeStyle7()));
    styles.add(new DropDownObjectImpl(TemplateAttribute.STYLE_LINK, Main.constants.templateAttributeStyle8()));

    tableTypes = new ArrayList<DropDownObject>();
    tableTypes.add(new DropDownObjectImpl(TemplateAttribute.TABLETYPE_TABLE, Main.constants.templateAttributeTableGraphType1()));
    tableTypes.add(new DropDownObjectImpl(TemplateAttribute.TABLETYPE_GRAPH, Main.constants.templateAttributeTableGraphType2()));
    tableTypes.add(new DropDownObjectImpl(TemplateAttribute.TABLETYPE_TABLEGRAPH, Main.constants.templateAttributeTableGraphType3()));

    tableOrientations = new ArrayList<DropDownObject>();
    tableOrientations.add(new DropDownObjectImpl(TemplateAttribute.TABLEORIENTATION_HORIZONTAL, Main.constants.templateAttributeTableType1()));
    tableOrientations.add(new DropDownObjectImpl(TemplateAttribute.TABLEORIENTATION_VERTICAL, Main.constants.templateAttributeTableType1()));

    graphTypes = new ArrayList<DropDownObject>();
    graphTypes.add(new DropDownObjectImpl(TemplateAttribute.TABLEGRAPHTYPE_LINE, Main.constants.templateAttributeTableGraphType1()));
    graphTypes.add(new DropDownObjectImpl(TemplateAttribute.TABLEGRAPHTYPE_BAR, Main.constants.templateAttributeTableGraphType2()));
    graphTypes.add(new DropDownObjectImpl(TemplateAttribute.TABLEGRAPHTYPE_AREA, Main.constants.templateAttributeTableGraphType3()));
  }

  final ApplicationModel applicationModel;
  private int DEFAULT_LENGTH = 255;
  private int DEFAULT_LABEL_TOP = 10;
  private int DEFAULT_LABEL_LEFT = 10;
  private int DEFAULT_LABEL_WIDTH = 100;
  private int DEFAULT_TOP = 10;
  private int DEFAULT_LEFT = 100;
  private int DEFAULT_WIDTH = 200;
  private int DEFAULT_UNIT_TOP = 10;
  private int DEFAULT_UNIT_LEFT = 360;
  private int DEFAULT_UNIT_WIDTH = 30;
  private int DEFAULT_TAB_INDEX = 0;
  private int DEFAULT_START_YEAR = 1970;
  private ObjectTemplate objectTemplate;
  private DropDownBox ddbTemplateGroup;
  private DropDownBox ddbObjectAttribute;
  private CheckBox cbMandatory;
  private CheckBox cbDerived;
  private CheckBox cbDesc;
  private CheckBox cbLabel;
  private CheckBox cbValue;
  private CheckBox cbUnit;
  private DropDownBox ddbStyle;
  private Label lblTable;
  private DropDownBox ddbTable;
  // date
  private Label lblStartYear;
  private IntegerTextBox tbStartYear;
  private Label lblEndYear;
  private IntegerTextBox tbEndYear;
  // dynamic combo
  private Label lblDCT;
  private DropDownBox ddbDCT;
  private Label lblDCOA;
  private DropDownBox ddbDCOA;
  // table
  private Label lblTType;
  private DropDownBox ddbTType;
  private Label lblTOrientation;
  private DropDownBox ddbTOrientation;
  private Label lblTGraphType;
  private DropDownBox ddbTGraphType;
  private Label lblTGraphX;
  private DropDownBox ddbTGraphX;
  private Label lblTGraphY;
  private DropDownBox ddbTGraphY;
  // second column
  private Label lblTop;
  private IntegerTextBox tbLabelTop;
  private IntegerTextBox tbTop;
  private IntegerTextBox tbUnitTop;
  private Label lblLeft;
  private IntegerTextBox tbLabelLeft;
  private IntegerTextBox tbLeft;
  private IntegerTextBox tbUnitLeft;
  private Label lblWidth;
  private IntegerTextBox tbLabelWidth;
  private IntegerTextBox tbWidth;
  private IntegerTextBox tbUnitWidth;
  private WidthUnitListBox tbLabelWidthUnit;
  private WidthUnitListBox tbWidthUnit;
  private WidthUnitListBox tbUnitWidthUnit;
  private AlignListBox tbLabelAlign;
  private AlignListBox tbAlign;
  private AlignListBox tbUnitAlign;
  private Label lblTabIndex;
  private IntegerTextBox tbTabIndex;
  private TextBox tbDef;
  private Label lblLength;
  private IntegerTextBox tbLength;
  private FlexTable widgetTemplateAttribute;
  private NavigationLabel menu2;

  /**
   * @param designerView the top level view
   */
  public TemplateAttributeDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.templateAttribute());

    assert (getModel().getApplication() != null);
    assert (getModel().getTemplate() != null);
    if (getTemplateAttribute() != null)
      assert (getTemplateAttribute().getTg().getTId() == getModel().getTemplate().getId());

    applicationModel = new ApplicationModel(designerView.getDesignerModel().getUserModel(), null);

    menu2 = new NavigationLabel(
        designerView, Main.constants.templateView(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (getTemplateAttribute() != null) {
          designerView.getDesignerModel().notifyDialogNavigationItemClicked(event.getRelativeElement());
          widgetTemplateAttribute.setVisible(false);
          if (objectTemplate == null)
            getBodyRight().add(getObjectTemplate());
          else
            getObjectTemplate().setVisible(true);
        }
      }
    });

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getTemplateAttribute() == null)
              setItem(new TemplateAttribute(getTbName().getText(), ddbTemplateGroup.getSelection().getId()));
            fill(getTemplateAttribute());
            designerView.getDesignerModel().createOrUpdateTemplateAttribute(getTemplateAttribute());
            close();
          }
        }, ClickEvent.getType());

    ddbTemplateGroup = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbObjectAttribute = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        fillDCTemplates();
      }

    });
    cbMandatory = new CheckBox(Main.constants.templateAttributeMandatory());
    cbDerived = new CheckBox(Main.constants.templateAttributeDerived());
    cbDesc = new CheckBox(Main.constants.templateAttributeDescendant());
    cbLabel = new CheckBox(Main.constants.templateAttributeLabel());
    cbValue = new CheckBox(Main.constants.templateAttributeValue());
    cbUnit = new CheckBox(Main.constants.templateAttributeUnit());
    ddbStyle = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        setVisibility();
      }
    });
    ddbStyle.setItems(styles);

    lblTable = new Label(Main.constants.templateAttributeTable());
    ddbTable = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    lblDCT = new Label(Main.constants.templateAttributeDCT());
    ddbDCT = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER,
        new ChangeHandler() {
          @Override
          public void onChange(ChangeEvent event) {
            loadAttributes(((Template) ddbDCT.getSelection().getUserObject()).getOtId(), ddbDCOA,
                getTemplateAttribute() != null ? getTemplateAttribute().getShared2() : 0);
          }
        });
    lblDCOA = new Label(Main.constants.templateAttributeDCOA());
    ddbDCOA = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);

    lblTType = new Label(Main.constants.templateAttributeTableType());
    ddbTType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        setTableVisibility();
      }
    });
    ddbTType.setItems(tableTypes);
    lblTOrientation = new Label(Main.constants.templateAttributeTableOrientation());
    ddbTOrientation = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbTOrientation.setItems(tableOrientations);

    lblTGraphType = new Label(Main.constants.templateAttributeTableGraphType());
    ddbTGraphType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbTGraphType.setItems(graphTypes);
    lblTGraphX = new Label(Main.constants.templateAttributeTableGraphX());
    ddbTGraphX = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    lblTGraphY = new Label(Main.constants.templateAttributeTableGraphY());
    ddbTGraphY = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);

    lblTop = new Label(Main.constants.templateAttributeTop());
    tbLabelTop = new IntegerTextBox(lblTop, getBOk());
    tbLabelTop.setWidth(TBWIDTH_SMALL);
    tbTop = new IntegerTextBox(lblTop, getBOk());
    tbTop.setWidth(TBWIDTH_SMALL);
    tbUnitTop = new IntegerTextBox(lblTop, getBOk());
    tbUnitTop.setWidth(TBWIDTH_SMALL);
    lblLeft = new Label(Main.constants.templateAttributeLeft());
    tbLabelLeft = new IntegerTextBox(lblLeft, getBOk());
    tbLabelLeft.setWidth(TBWIDTH_SMALL);
    tbLeft = new IntegerTextBox(lblLeft, getBOk());
    tbLeft.setWidth(TBWIDTH_SMALL);
    tbUnitLeft = new IntegerTextBox(lblLeft, getBOk());
    tbUnitLeft.setWidth(TBWIDTH_SMALL);
    lblWidth = new Label(Main.constants.templateAttributeWidth());
    tbLabelWidth = new IntegerTextBox(lblWidth, getBOk());
    tbLabelWidth.setWidth(TBWIDTH_SMALL);
    tbWidth = new IntegerTextBox(lblWidth, getBOk());
    tbWidth.setWidth(TBWIDTH_SMALL);
    tbUnitWidth = new IntegerTextBox(lblWidth, getBOk());
    tbUnitWidth.setWidth(TBWIDTH_SMALL);
    tbLabelWidthUnit = new WidthUnitListBox();
    tbWidthUnit = new WidthUnitListBox();
    tbUnitWidthUnit = new WidthUnitListBox();
    tbLabelAlign = new AlignListBox();
    tbAlign = new AlignListBox();
    tbUnitAlign = new AlignListBox();

    tbDef = new TextBox();
    lblLength = new Label(Main.constants.templateAttributeLength());
    tbLength = new IntegerTextBox(lblLength, getBOk());
    lblTabIndex = new Label(Main.constants.templateAttributeTabIndex());
    tbTabIndex = new IntegerTextBox(lblTabIndex, getBOk());
    tbTabIndex.setWidth(TBWIDTH_SMALL);
    lblStartYear = new Label(Main.constants.templateAttributeStartYear());
    tbStartYear = new IntegerTextBox(lblStartYear, getBOk());
    tbStartYear.setWidth(TBWIDTH_SMALL);
    lblEndYear = new Label(Main.constants.templateAttributeEndYear());
    tbEndYear = new IntegerTextBox(lblEndYear, getBOk());
    tbEndYear.setWidth(TBWIDTH_SMALL);

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
  }

  @Override
  public void onObjectAttributeCreated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributeUpdated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributesLoaded(int otId, Collection<ObjectAttribute> objectAttributes) {
    // fill connected object attribute
    if (otId == getModel().getTemplate().getOtId()) {
      fillObjectAttributes(ddbObjectAttribute, objectAttributes,
          getTemplateAttribute() != null ? getTemplateAttribute().getOaId() : 0);
      fillDCTemplates();
    }

    // fill dynamic combo object attribute
    if (ddbDCT.getSelection() != null && ddbDCT.getSelection().getUserObject() != null
        && otId == ((Template) ddbDCT.getSelection().getUserObject()).getOtId())
      fillObjectAttributes(ddbDCOA, objectAttributes,
          getTemplateAttribute() != null ? getTemplateAttribute().getShared2() : 0);
  }

  @Override
  public void onTemplateGroupCreated(TemplateGroup templateGroup) {
  }

  @Override
  public void onTemplateGroupUpdated(TemplateGroup templateGroup) {
  }

  @Override
  public void onTemplateGroupsLoaded(Collection<TemplateGroup> templateGroups) {
    fillTemplateGroups(templateGroups);
  }

  /**
   * @return the templateAttribute
   */
  public TemplateAttribute getTemplateAttribute() {
    return (TemplateAttribute) getItem();
  }

  // getters and setters

  /**
   * Getter for property 'objectTemplate'.
   *
   * @return Value for property 'objectTemplate'.
   */
  public ObjectTemplate getObjectTemplate() {
    if (objectTemplate == null) {
      objectTemplate = new ObjectTemplate(applicationModel);
      objectTemplate.initialize(getTemplateAttribute().getTg().getT());
    }
    return objectTemplate;
  }

  @Override
  protected FlexTable getItemWidget() {
    if (widgetTemplateAttribute == null) {
      widgetTemplateAttribute = new FlexTable();

      // first column
      widgetTemplateAttribute.setWidget(0, 0, getLblCode());
      widgetTemplateAttribute.setWidget(0, 1, getLblCodeValue());

      widgetTemplateAttribute.setWidget(1, 0, getLblName());
      widgetTemplateAttribute.setWidget(1, 1, getTbName());

      widgetTemplateAttribute.setWidget(2, 0, getLblDesc());
      widgetTemplateAttribute.setWidget(2, 1, getTbDesc());

      Label lblTg = new Label(Main.constants.templateAttributeTg());
      widgetTemplateAttribute.setWidget(3, 0, lblTg);
      widgetTemplateAttribute.setWidget(3, 1, ddbTemplateGroup);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblOa = new Label(Main.constants.templateAttributeOa());
      widgetTemplateAttribute.setWidget(4, 0, lblOa);
      widgetTemplateAttribute.setWidget(4, 1, ddbObjectAttribute);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateAttribute.setWidget(5, 1, cbMandatory);
      widgetTemplateAttribute.setWidget(6, 1, cbDesc);
      widgetTemplateAttribute.setWidget(7, 1, cbDerived);

      Label lblStyle = new Label(Main.constants.templateAttributeStyle());
      widgetTemplateAttribute.setWidget(8, 0, lblStyle);
      widgetTemplateAttribute.setWidget(8, 1, ddbStyle);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(8, 1, ClientUtils.CSS_ALIGN_RIGHT);

      // space between columns
      widgetTemplateAttribute.getCellFormatter().setWidth(0, 2, TBWIDTH_SMALL);

      // second column
      widgetTemplateAttribute.setWidget(0, 4, cbLabel);
      widgetTemplateAttribute.setWidget(0, 5, cbValue);
      widgetTemplateAttribute.setWidget(0, 6, cbUnit);

      widgetTemplateAttribute.setWidget(1, 3, lblTop);
      widgetTemplateAttribute.setWidget(1, 4, tbLabelTop);
      widgetTemplateAttribute.setWidget(1, 5, tbTop);
      widgetTemplateAttribute.setWidget(1, 6, tbUnitTop);

      widgetTemplateAttribute.setWidget(2, 3, lblLeft);
      widgetTemplateAttribute.setWidget(2, 4, tbLabelLeft);
      widgetTemplateAttribute.setWidget(2, 5, tbLeft);
      widgetTemplateAttribute.setWidget(2, 6, tbUnitLeft);

      widgetTemplateAttribute.setWidget(3, 3, lblWidth);
      widgetTemplateAttribute.setWidget(3, 4, tbLabelWidth);
      widgetTemplateAttribute.setWidget(3, 5, tbWidth);
      widgetTemplateAttribute.setWidget(3, 6, tbUnitWidth);

      Label lblWidthUnit = new Label(Main.constants.templateAttributeWidthUnit());
      widgetTemplateAttribute.setWidget(4, 3, lblWidthUnit);
      widgetTemplateAttribute.setWidget(4, 4, tbLabelWidthUnit);
      widgetTemplateAttribute.setWidget(4, 5, tbWidthUnit);
      widgetTemplateAttribute.setWidget(4, 6, tbUnitWidthUnit);

      Label lblAlign = new Label(Main.constants.templateAttributeAlign());
      widgetTemplateAttribute.setWidget(5, 3, lblAlign);
      widgetTemplateAttribute.setWidget(5, 4, tbLabelAlign);
      widgetTemplateAttribute.setWidget(5, 5, tbAlign);
      widgetTemplateAttribute.setWidget(5, 6, tbUnitAlign);

      Label lblDef = new Label(Main.constants.templateAttributeDefault());
      widgetTemplateAttribute.setWidget(6, 3, lblDef);
      widgetTemplateAttribute.setWidget(6, 4, tbDef);
      widgetTemplateAttribute.getFlexCellFormatter().setColSpan(6, 4, 3);

      widgetTemplateAttribute.setWidget(7, 3, lblLength);
      tbLength.setWidth(TBWIDTH_SMALL);
      widgetTemplateAttribute.setWidget(7, 4, tbLength);

      widgetTemplateAttribute.setWidget(8, 3, lblTabIndex);
      widgetTemplateAttribute.setWidget(8, 4, tbTabIndex);
    }
    return widgetTemplateAttribute;
  }

  /**
   * Fill {@link TemplateAttribute} with values from UI. Parent method must be called first.
   *
   * @param templateAttribute the template attribute which should be filled with UI values
   */
  protected void fill(TemplateAttribute templateAttribute) {
    super.fill(templateAttribute);

    // template group
    int itg = ddbTemplateGroup.getSelection().getId();
    templateAttribute.setTgId(itg);
    templateAttribute.setTg((TemplateGroup) ddbTemplateGroup.getSelection().getUserObject());

    // object attribute
    int ioa = ddbObjectAttribute.getSelection().getId();
    templateAttribute.setOaId(ioa);
    templateAttribute.setOa((ObjectAttribute) ddbObjectAttribute.getSelection().getUserObject());

    // default
    String def = tbDef.getText().trim();
    templateAttribute.setDef(def.length() == 0 ? null : def);

    // length
    templateAttribute.setLength(new Integer(ClientUtils
        .parseInt(tbLength.getText().trim())));

    // tabindex
    templateAttribute.setTabIndex(Integer.parseInt(tbTabIndex.getText()));

    // flags
    int flags = 0;
    if (cbMandatory.getValue())
      flags = ClientUtils.setFlag(TemplateAttribute.FLAG_MANDATORY, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateAttribute.FLAG_MANDATORY, flags);
    if (cbLabel.getValue())
      flags = ClientUtils.setFlag(TemplateAttribute.FLAG_SHOW_LABEL, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateAttribute.FLAG_SHOW_LABEL, flags);
    if (cbValue.getValue())
      flags = ClientUtils.setFlag(TemplateAttribute.FLAG_SHOW_VALUE, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateAttribute.FLAG_SHOW_VALUE, flags);
    if (cbUnit.getValue())
      flags = ClientUtils.setFlag(TemplateAttribute.FLAG_SHOW_UNIT, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateAttribute.FLAG_SHOW_UNIT, flags);
    if (cbDesc.getValue())
      flags = ClientUtils.setFlag(TemplateAttribute.FLAG_DESCENDANT, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateAttribute.FLAG_DESCENDANT, flags);
    if (cbDerived.getValue())
      flags = ClientUtils.setFlag(TemplateAttribute.FLAG_DERIVED, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateAttribute.FLAG_DERIVED, flags);
    templateAttribute.setFlags(flags);

    // style
    templateAttribute.setStyle(ddbStyle.getSelection().getId());

    // label
    if (ClientUtils.getFlag(TemplateAttribute.FLAG_SHOW_LABEL, flags)) {
      templateAttribute.setLabelTop(Integer.parseInt(tbLabelTop.getText()));
      templateAttribute.setLabelLeft(Integer.parseInt(tbLabelLeft.getText()));
      templateAttribute.setLabelWidth(Integer.parseInt(tbLabelWidth.getText()));
      templateAttribute.setLabelWidthUnit(tbLabelWidthUnit
          .getItemText(tbLabelWidthUnit.getSelectedIndex()));
      templateAttribute.setLabelAlign(tbLabelAlign.getItemText(tbLabelAlign
          .getSelectedIndex()));
    } else {
      templateAttribute.setLabelTop(0);
      templateAttribute.setLabelLeft(0);
      templateAttribute.setLabelWidth(0);
      templateAttribute.setLabelWidthUnit(null);
      templateAttribute.setLabelAlign(null);
    }

    // value
    if (ClientUtils.getFlag(TemplateAttribute.FLAG_SHOW_VALUE, flags)) {
      templateAttribute.setTop(new Integer(ClientUtils
          .parseInt(tbTop.getText().trim())));
      templateAttribute.setLeft(new Integer(ClientUtils
          .parseInt(tbLeft.getText().trim())));
      templateAttribute.setWidth(new Integer(ClientUtils
          .parseInt(tbWidth.getText().trim())));
      templateAttribute.setWidthUnit(tbWidthUnit.getItemText(tbWidthUnit
          .getSelectedIndex()));
      templateAttribute
          .setAlign(tbAlign.getItemText(tbAlign.getSelectedIndex()));
    } else {
      templateAttribute.setTop(0);
      templateAttribute.setLeft(0);
      templateAttribute.setWidth(0);
      templateAttribute.setWidthUnit(null);
      templateAttribute.setAlign(null);
    }

    // unit
    if (ClientUtils.getFlag(TemplateAttribute.FLAG_SHOW_UNIT, flags)) {
      templateAttribute.setUnitTop(Integer.parseInt(tbUnitTop.getText()));
      templateAttribute.setUnitLeft(Integer.parseInt(tbUnitLeft.getText()));
      templateAttribute.setUnitWidth(Integer.parseInt(tbUnitWidth.getText()));
      templateAttribute.setUnitWidthUnit(tbUnitWidthUnit
          .getItemText(tbUnitWidthUnit.getSelectedIndex()));
      templateAttribute.setUnitAlign(tbUnitAlign.getItemText(tbUnitAlign
          .getSelectedIndex()));
    } else {
      templateAttribute.setUnitTop(0);
      templateAttribute.setUnitLeft(0);
      templateAttribute.setUnitWidth(0);
      templateAttribute.setUnitWidthUnit(null);
      templateAttribute.setUnitAlign(null);
    }

    // shared
    switch (templateAttribute.getStyle()) {
      case TemplateAttribute.STYLE_DATEPICKER:
        templateAttribute.setShared1(new Integer(ClientUtils.parseInt(tbStartYear.getText().trim())));
        templateAttribute.setShared2(new Integer(ClientUtils.parseInt(tbEndYear.getText().trim())));
        break;
      case TemplateAttribute.STYLE_DYNAMICCOMBO:
        templateAttribute.setShared1(ddbDCT.getSelection().getId());
        templateAttribute.setShared2(ddbDCOA.getSelection().getId());
        templateAttribute.setShared3(0);
        templateAttribute.setShared4(0);
        templateAttribute.setShared5(ddbTable.getSelection().getId());
        break;
      case TemplateAttribute.STYLE_TABLE:
        templateAttribute.setShared1(ddbTType.getSelection().getId());
        templateAttribute.setShared2(ddbTOrientation.getSelection().getId());
        if (ddbTType.getSelection().getId() == TemplateAttribute.TABLETYPE_GRAPH
            || ddbTType.getSelection().getId() == TemplateAttribute.TABLETYPE_TABLEGRAPH) {
          templateAttribute.setShared3(ddbTGraphType.getSelection().getId());
          templateAttribute.setShared4(ddbTGraphX.getSelection().getId());
          templateAttribute.setShared5(ddbTGraphY.getSelection().getId());
        } else {
          templateAttribute.setShared3(0);
          templateAttribute.setShared4(0);
          templateAttribute.setShared5(0);
        }
        break;
      default:
        templateAttribute.setShared1(0);
        templateAttribute.setShared2(0);
        templateAttribute.setShared3(0);
        templateAttribute.setShared4(0);
        templateAttribute.setShared5(ddbTable.getSelection().getId());
        break;
    }
  }

  /**
   * Load {@link Template} to UI. Parent method must be called first.
   *
   * @param item the template attribute which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    assert (item != null);
    super.load(item);

    getMenu().addItem(menu2);

    TemplateAttribute templateAttribute = (TemplateAttribute) item;
    ddbTemplateGroup.setSelection(new DropDownObjectImpl(templateAttribute.getTgId(),
        templateAttribute.getTg().getName(), templateAttribute.getTg()));
    loadAttributes(getModel().getTemplate().getOtId(), ddbObjectAttribute, templateAttribute.getOaId());
    cbMandatory.setValue(ClientUtils.getFlag(TemplateAttribute.FLAG_MANDATORY, templateAttribute.getFlags()));
    cbDerived.setValue(ClientUtils.getFlag(TemplateAttribute.FLAG_DERIVED, templateAttribute.getFlags()));
    cbDesc.setValue(ClientUtils.getFlag(TemplateAttribute.FLAG_DESCENDANT, templateAttribute.getFlags()));
    cbLabel.setValue(ClientUtils.getFlag(TemplateAttribute.FLAG_SHOW_LABEL, templateAttribute.getFlags()));
    cbValue.setValue(ClientUtils.getFlag(TemplateAttribute.FLAG_SHOW_VALUE, templateAttribute.getFlags()));
    cbUnit.setValue(ClientUtils.getFlag(TemplateAttribute.FLAG_SHOW_UNIT, templateAttribute.getFlags()));
    ddbStyle.setSelection(styles.get(templateAttribute.getStyle() - 1));
    ddbTType.setSelection(tableTypes.get(templateAttribute.getShared1()));
    ddbTOrientation.setSelection(tableOrientations.get(templateAttribute.getShared2()));
    ddbTGraphType.setSelection(graphTypes.get(templateAttribute.getShared3()));

    tbLabelTop.setText("" + templateAttribute.getLabelTop());
    tbTop.setText("" + templateAttribute.getTop());
    tbUnitTop.setText("" + templateAttribute.getUnitTop());
    tbLabelLeft.setText("" + templateAttribute.getLabelLeft());
    tbLeft.setText("" + templateAttribute.getLeft());
    tbUnitLeft.setText("" + templateAttribute.getUnitLeft());
    tbLabelWidth.setText("" + templateAttribute.getLabelWidth());
    tbWidth.setText("" + templateAttribute.getWidth());
    tbUnitWidth.setText("" + templateAttribute.getUnitWidth());
    tbLabelWidthUnit.setText(templateAttribute.getLabelWidthUnit());
    tbWidthUnit.setText(templateAttribute.getWidthUnit());
    tbUnitWidthUnit.setText(templateAttribute.getUnitWidthUnit());
    tbLabelAlign.setText(templateAttribute.getLabelAlign());
    tbAlign.setText(templateAttribute.getAlign());
    tbUnitAlign.setText(templateAttribute.getUnitAlign());

    tbTabIndex.setText("" + templateAttribute.getTabIndex());
    tbStartYear.setText("" + templateAttribute.getShared1());
    tbEndYear.setText("" + templateAttribute.getShared2());
    tbDef.setText(templateAttribute.getDef());
    tbLength.setText("" + templateAttribute.getLength());

    Collection<TemplateGroup> tgs = getModel().getTemplateGroups().get(getModel().getTemplate().getId());
    if (tgs == null) {
      TemplateGroupLoader tgl = new TemplateGroupLoader(getModel(), getModel().getTemplate());
      tgl.start();
    } else {
      fillTemplateGroups(tgs);
    }

    Collection<TemplateAttribute> tas = getModel().getAttrsByTemplate().get(getModel().getTemplate().getId());
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (TemplateAttribute ta : tas) {
      if (ta.getStyle() == TemplateAttribute.STYLE_TABLE) {
        items.add(new DropDownObjectImpl(ta.getId(), ta.getName(), ta));
        if (templateAttribute.getShared5() == ta.getId())
          ddbTable.setSelection(new DropDownObjectImpl(ta.getId(), ta.getName(), ta));
      }
    }
    ddbTable.setItems(items);

    fillDCTemplates();
    setVisibility();
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();

    ddbTemplateGroup.setSelection(new DropDownObjectImpl(0, Main.constants.chooseTemplateGroup()));
    ddbObjectAttribute.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectAttribute()));
    loadAttributes(getModel().getTemplate().getOtId(), ddbObjectAttribute, 0);
    cbMandatory.setValue(false);
    cbDerived.setValue(false);
    cbDesc.setValue(false);
    cbLabel.setValue(true);
    cbValue.setValue(true);
    cbUnit.setValue(false);
    ddbStyle.setSelection(styles.get(1));
    ddbTable.setSelection(new DropDownObjectImpl(0, Main.constants.chooseTable()));

    ddbTType.setSelection(tableTypes.get(0));
    ddbTOrientation.setSelection(tableOrientations.get(0));
    ddbTGraphType.setSelection(graphTypes.get(0));
    ddbTGraphX.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectAttribute()));
    ddbTGraphY.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectAttribute()));

    tbLabelTop.setText("" + DEFAULT_LABEL_TOP);
    tbTop.setText("" + DEFAULT_TOP);
    tbUnitTop.setText("" + DEFAULT_UNIT_TOP);
    tbLabelLeft.setText("" + DEFAULT_LABEL_LEFT);
    tbLeft.setText("" + DEFAULT_LEFT);
    tbUnitLeft.setText("" + DEFAULT_UNIT_LEFT);
    tbLabelWidth.setText("" + DEFAULT_LABEL_WIDTH);
    tbWidth.setText("" + DEFAULT_WIDTH);
    tbUnitWidth.setText("" + DEFAULT_UNIT_WIDTH);
    tbLabelWidthUnit.setText("");
    tbWidthUnit.setText("");
    tbUnitWidthUnit.setText("");
    tbLabelAlign.setText("");
    tbAlign.setText("");
    tbUnitAlign.setText("");

    tbTabIndex.setText("" + DEFAULT_TAB_INDEX);
    DateHelper date = new DateHelper(new Date());
    tbStartYear.setText("" + DEFAULT_START_YEAR);
    tbEndYear.setText("" + date.getYear());
    tbDef.setText("");
    tbLength.setText("" + DEFAULT_LENGTH);

    ddbDCOA.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectAttribute()));
  }

  // private methods

  private void fillTemplateGroups(Collection<TemplateGroup> templateGroups) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (TemplateGroup tg : templateGroups) {
      items.add(new DropDownObjectImpl(tg.getId(), tg.getName(), tg));
    }
    ddbTemplateGroup.setItems(items);
  }

  private void fillObjectAttributes(DropDownBox ddb, Collection<ObjectAttribute> objectAttributes, int selection) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ObjectAttribute oa : objectAttributes) {
      items.add(new DropDownObjectImpl(oa.getId(), oa.getName(), oa));
      if (selection == oa.getId())
        ddb.setSelection(items.get(items.size() - 1));
    }
    ddb.setItems(items);
  }

  private void fillDCTemplates() {
    ObjectAttribute oa = (ObjectAttribute) ddbObjectAttribute.getSelection().getUserObject();
    if (oa != null && oa.getVt().getType() == ValueType.VT_REF) {
      ddbStyle.setSelection(
          new DropDownObjectImpl(TemplateAttribute.STYLE_DYNAMICCOMBO, Main.constants.templateAttributeStyle4()));

      ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
      Collection<ApplicationTemplate> appts = getModel().getAppTemplateByApp(getModel().getApplication().getId());
      assert (appts != null);
      for (ApplicationTemplate appt : appts) {
        if (appt.getT().getOtId() == oa.getShared1()) {
          items.add(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
          if (getTemplateAttribute() != null && getTemplateAttribute().getShared1() == appt.getTId())
            ddbDCT.setSelection(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
        }
      }
      ddbDCT.setItems(items);

      loadAttributes(((Template) ddbDCT.getSelection().getUserObject()).getOtId(), ddbDCOA,
          getTemplateAttribute() != null ? getTemplateAttribute().getShared2() : 0);

      setVisibility();
    }
  }

  private void loadAttributes(int otId, DropDownBox ddb, int selection) {
    Collection<ObjectAttribute> oas = getModel().getObjectAttributes().get(otId);
    if (oas == null) {
      ObjectAttributeLoader oal = new ObjectAttributeLoader(getModel(), otId);
      oal.start();
    } else {
      fillObjectAttributes(ddb, oas, selection);
    }
  }

  private void setVisibility() {
    boolean dynacomboVisible = ddbStyle.getSelection().getId() == TemplateAttribute.STYLE_DYNAMICCOMBO;
    boolean tableVisible = ddbStyle.getSelection().getId() == TemplateAttribute.STYLE_TABLE;
    boolean isDatePicker = ddbStyle.getSelection().getId() == TemplateAttribute.STYLE_DATEPICKER;

    setUnitVisible();

    if (dynacomboVisible) {
      widgetTemplateAttribute.setWidget(9, 0, lblTable);
      widgetTemplateAttribute.setWidget(9, 1, ddbTable);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(9, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateAttribute.setWidget(10, 0, lblDCT);
      widgetTemplateAttribute.setWidget(10, 1, ddbDCT);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(10, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateAttribute.setWidget(11, 0, lblDCOA);
      widgetTemplateAttribute.setWidget(11, 1, ddbDCOA);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(11, 1, ClientUtils.CSS_ALIGN_RIGHT);
    } else if (tableVisible) {
//                widgetTemplateAttribute.setWidget(9, 0, lblTType);
//                widgetTemplateAttribute.setWidget(9, 1, ddbTType);
//
//                widgetTemplateAttribute.setWidget(10, 0, lblTOrientation);
//                widgetTemplateAttribute.setWidget(10, 1, ddbTOrientation);
//
//                NativeEvent nevent = Document.get().createChangeEvent();
//                DomEvent.fireNativeEvent(nevent, getLbTType());
    } else if (isDatePicker) {
      widgetTemplateAttribute.setWidget(9, 0, lblStartYear);
      widgetTemplateAttribute.setWidget(9, 1, tbStartYear);

      widgetTemplateAttribute.setWidget(10, 0, lblEndYear);
      widgetTemplateAttribute.setWidget(10, 1, tbEndYear);
    } else {
      widgetTemplateAttribute.setWidget(9, 0, lblTable);
      widgetTemplateAttribute.setWidget(9, 1, ddbTable);
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(9, 1, ClientUtils.CSS_ALIGN_RIGHT);


      if (widgetTemplateAttribute.getRowCount() > 10) {
        if (widgetTemplateAttribute.getWidget(10, 0) != null)
          widgetTemplateAttribute.getWidget(10, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(10) > 1
            && widgetTemplateAttribute.getWidget(10, 1) != null)
          widgetTemplateAttribute.getWidget(10, 1).removeFromParent();
      }
      if (widgetTemplateAttribute.getRowCount() > 11) {
        if (widgetTemplateAttribute.getWidget(11, 0) != null)
          widgetTemplateAttribute.getWidget(11, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(11) > 1
            && widgetTemplateAttribute.getWidget(11, 1) != null)
          widgetTemplateAttribute.getWidget(11, 1).removeFromParent();
      }
      if (widgetTemplateAttribute.getRowCount() > 12) {
        if (widgetTemplateAttribute.getWidget(12, 0) != null)
          widgetTemplateAttribute.getWidget(12, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(12) > 1
            && widgetTemplateAttribute.getWidget(12, 1) != null)
          widgetTemplateAttribute.getWidget(12, 1).removeFromParent();
      }
      if (widgetTemplateAttribute.getRowCount() > 13) {
        if (widgetTemplateAttribute.getWidget(13, 0) != null)
          widgetTemplateAttribute.getWidget(13, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(13) > 1
            && widgetTemplateAttribute.getWidget(13, 1) != null)
          widgetTemplateAttribute.getWidget(13, 1).removeFromParent();
      }
    }
  }

  private void setUnitVisible() {
    boolean unitVisible = ddbStyle.getSelection().getId() != TemplateAttribute.STYLE_DYNAMICCOMBO
        && ddbStyle.getSelection().getId() != TemplateAttribute.STYLE_DATEPICKER
        && ddbStyle.getSelection().getId() != TemplateAttribute.STYLE_TABLE;

    cbUnit.setVisible(unitVisible);
    tbUnitTop.setVisible(unitVisible);
    tbUnitLeft.setVisible(unitVisible);
    tbUnitWidth.setVisible(unitVisible);
    tbUnitWidthUnit.setVisible(unitVisible);
    tbUnitAlign.setVisible(unitVisible);
  }

  private void setTableVisibility() {
    boolean graphVisible = ddbTType.getSelection().getId() == TemplateAttribute.TABLETYPE_GRAPH ||
        ddbTType.getSelection().getId() == TemplateAttribute.TABLETYPE_TABLEGRAPH;

    if (graphVisible) {
      widgetTemplateAttribute.setWidget(10, 0, lblTGraphType);
      widgetTemplateAttribute.setWidget(10, 1, ddbTGraphType);

      widgetTemplateAttribute.setWidget(11, 0, lblTGraphX);
      widgetTemplateAttribute.setWidget(11, 1, ddbTGraphX);

      widgetTemplateAttribute.setWidget(12, 0, lblTGraphY);
      widgetTemplateAttribute.setWidget(12, 1, ddbTGraphY);
    } else {
      if (widgetTemplateAttribute.getRowCount() > 10) {
        if (widgetTemplateAttribute.getWidget(10, 0) != null)
          widgetTemplateAttribute.getWidget(10, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(10) > 1
            && widgetTemplateAttribute.getWidget(10, 1) != null)
          widgetTemplateAttribute.getWidget(10, 1).removeFromParent();
      }
      if (widgetTemplateAttribute.getRowCount() > 11) {
        if (widgetTemplateAttribute.getWidget(11, 0) != null)
          widgetTemplateAttribute.getWidget(11, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(11) > 1
            && widgetTemplateAttribute.getWidget(11, 1) != null)
          widgetTemplateAttribute.getWidget(11, 1).removeFromParent();
      }
      if (widgetTemplateAttribute.getRowCount() > 12) {
        if (widgetTemplateAttribute.getWidget(12, 0) != null)
          widgetTemplateAttribute.getWidget(12, 0).removeFromParent();
        if (widgetTemplateAttribute.getCellCount(12) > 1
            && widgetTemplateAttribute.getWidget(12, 1) != null)
          widgetTemplateAttribute.getWidget(12, 1).removeFromParent();
      }
    }
  }
}
