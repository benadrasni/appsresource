package sk.benko.appsresource.client.designer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.designer.model.ObjectAttributeLoader;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateGroupLoader;
import sk.benko.appsresource.client.model.ValueType;
import sk.benko.appsresource.client.ui.widget.IntegerTextBox;
import sk.benko.appsresource.client.util.DateHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Tree.Resources;

/**
 *
 */
public class TemplateAttributeDialog extends DesignerDialog implements 
    DesignerModel.TemplateGroupObserver,
    DesignerModel.ObjectAttributeObserver {
  
  private int DEFAULT_LENGTH = 255;
  private int DEFAULT_LABELTOP = 10;
  private int DEFAULT_LABELLEFT = 10;
  private int DEFAULT_LABELWIDTH = 100;
  private int DEFAULT_TOP = 10;
  private int DEFAULT_LEFT = 100;
  private int DEFAULT_WIDTH = 200;
  private int DEFAULT_UNITTOP = 10;
  private int DEFAULT_UNITLEFT = 360;
  private int DEFAULT_UNITWIDTH = 30;
  private int DEFAULT_TABINDEX = 0;

  private int DEFAULT_STARTYEAR = 1970;
  
  final ApplicationModel amodel;
  private ObjectTemplate objectTemplate;

  private Label lblCode;
  private Label lblName;
  private Label lblDesc;
  private Label lblTg;
  private DropDownBox ddbTemplateGroup;
  private Label lblOa;
  private DropDownBox ddbObjectAttribute;
  
  private CheckBox cbMandatory;
  private CheckBox cbDerived;
  private CheckBox cbDesc;
  private CheckBox cbLabel;
  private CheckBox cbValue;
  private CheckBox cbUnit;

  private Label lblStyle;
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
  private ListBox lbTType;
  private Label lblTOrientation;
  private ListBox lbTOrientation;
  private Label lblTGraphType;
  private ListBox lbTGraphType;
  private Label lblTGraphX;
  private ListBox lbTGraphX;
  private Label lblTGraphY;
  private ListBox lbTGraphY;

  // second column
  private Label lblTop;
  private IntegerTextBox tbLabelTop;
  private IntegerTextBox tbTop;
  private IntegerTextBox tbUnitTop;

  private Label lblTabIndex;
  private IntegerTextBox tbTabIndex;
  
  final TextBox tbDef = new TextBox();
  final Label lblLength = new Label(Main.constants.templateAttributeLength());
  final Label lblLeft = new Label(Main.constants.templateAttributeLeft());
  final Label lblWidth = new Label(Main.constants.templateAttributeWidth());

  final IntegerTextBox tbLength = new IntegerTextBox(lblLength, getBOk());
  final IntegerTextBox tbLabelLeft = new IntegerTextBox(lblLeft, getBOk());
  final IntegerTextBox tbLeft = new IntegerTextBox(lblLeft, getBOk());
  final IntegerTextBox tbUnitLeft = new IntegerTextBox(lblLeft, getBOk());
  final IntegerTextBox tbLabelWidth = new IntegerTextBox(lblWidth, getBOk());
  final IntegerTextBox tbWidth = new IntegerTextBox(lblWidth, getBOk());
  final IntegerTextBox tbUnitWidth = new IntegerTextBox(lblWidth, getBOk());
  final WidthUnitListBox tbLabelWidthUnit = new WidthUnitListBox();
  final WidthUnitListBox tbWidthUnit = new WidthUnitListBox();
  final WidthUnitListBox tbUnitWidthUnit = new WidthUnitListBox();
  final AlignListBox tbLabelAlign = new AlignListBox();
  final AlignListBox tbAlign = new AlignListBox();
  final AlignListBox tbUnitAlign = new AlignListBox();
  
  Tree menu = new Tree((Resources) GWT.create(TreeResource.class), false);
  
  FlexTable widgetTemplateAttribute = new FlexTable();
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateAttributeDialog(final DesignerModel model, TemplateAttribute ta) {
    super(model, ta);
    
    assert(getModel().getApplication() != null);
    assert(getModel().getTemplate() != null);
    if (getTemplateAttribute() != null)
      assert(getTemplateAttribute().getTg().getTId() == getModel().getTemplate().getId());

    getModel().addTemplateGroupObserver(this);
    getModel().addObjectAttributeObserver(this);
    
    amodel = new ApplicationModel(model.getUserModel(), null);

    getHeader().add(new Label((getTemplateAttribute() == null ? Main.constants.newItem() + " " 
        : "") + Main.constants.templateAttribute()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.templateAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        model.notifyDialogNavigationItemClicked(event.getRelativeElement());
        widgetTemplateAttribute.setVisible(true);
        getObjectTemplate().setVisible(false);
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);
    menu.addItem(menu1);
    initializeTemplateAttribute();

    if (getTemplateAttribute() != null) {
      NavigationLabelView menu2 = new NavigationLabelView(
          model, Main.constants.templateView(), new ClickHandler() {
        public void onClick(ClickEvent event) {
          if (getTemplateAttribute() != null) {
            model.notifyDialogNavigationItemClicked(event.getRelativeElement());
            widgetTemplateAttribute.setVisible(false);
            if (objectTemplate == null)
              getBodyRight().add(getObjectTemplate());
            else
              getObjectTemplate().setVisible(true);
          }
        }
      });
      menu.addItem(menu2);
    }
    
    getBodyLeft().add(menu);
    getBodyRight().add(widgetTemplateAttribute);

    getBCancel().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            close();
          }
        }, ClickEvent.getType());
    getBCancel().getElement().setInnerText(Main.constants.cancel());
    
    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getTemplateAttribute() == null)
                setItem(new TemplateAttribute(getTbName().getText(),
                    getDdbTemplateGroup().getSelection().getId()));
              fill(getTemplateAttribute());
              model.createOrUpdateTemplateAttribute(getTemplateAttribute());
              close();
            }
        }, ClickEvent.getType());
    getBOk().getElement().setInnerText(getTemplateAttribute() == null ? 
        Main.constants.create() : Main.constants.save());
  }
  
  public void close() {
    getModel().removeTemplateGroupObserver(this);
    getModel().removeObjectAttributeObserver(this);
    hide();
  }
  
  @Override
  public void onObjectAttributeCreated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributeUpdated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributesLoaded(int otId,
      Collection<ObjectAttribute> objectAttributes) {
    // fill connected object attribute
    if (otId == getModel().getTemplate().getOtId()) {
      fillObjectAttributes(getDdbObjectAttribute(), objectAttributes, 
          getTemplateAttribute() != null ? getTemplateAttribute().getOaId() : 0);
      fillDCTemplates();
    }
    
    // fill dynamic combo object attribute
    if (getDdbDCT().getSelection() != null 
        && getDdbDCT().getSelection().getUserObject() != null 
        && otId == ((Template)getDdbDCT().getSelection().getUserObject()).getOtId())
      fillObjectAttributes(getDdbDCOA(), objectAttributes,
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
  
  private void initializeTemplateAttribute() {
    // first column
    widgetTemplateAttribute.setWidget(0, 0, getLblCode());
    widgetTemplateAttribute.setWidget(0, 1, getLblCodeValue());

    widgetTemplateAttribute.setWidget(1, 0, getLblName());
    widgetTemplateAttribute.setWidget(1, 1, getTbName());

    widgetTemplateAttribute.setWidget(2, 0, getLblDesc());
    widgetTemplateAttribute.setWidget(2, 1, getTbDesc());

    widgetTemplateAttribute.setWidget(3, 0, getLblTg());
    widgetTemplateAttribute.setWidget(3, 1, getDdbTemplateGroup());
    widgetTemplateAttribute.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

    widgetTemplateAttribute.setWidget(4, 0, getLblOa());
    widgetTemplateAttribute.setWidget(4, 1, getDdbObjectAttribute());
    widgetTemplateAttribute.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

    widgetTemplateAttribute.setWidget(5, 1, getCbMandatory());
    widgetTemplateAttribute.setWidget(6, 1, getCbDesc());
    widgetTemplateAttribute.setWidget(7, 1, getCbDerived());
    
    widgetTemplateAttribute.setWidget(8, 0, getLblStyle());
    widgetTemplateAttribute.setWidget(8, 1, getDdbStyle());
    widgetTemplateAttribute.getFlexCellFormatter().addStyleName(8, 1, ClientUtils.CSS_ALIGN_RIGHT);

    NativeEvent eventStyle = Document.get().createChangeEvent();
    DomEvent.fireNativeEvent(eventStyle, getDdbStyle());
    
    // space between columns
    widgetTemplateAttribute.getCellFormatter().setWidth(0, 2, TBWIDTH_SMALL);
    
    // second column
    widgetTemplateAttribute.setWidget(0, 4, getCbLabel());
    widgetTemplateAttribute.setWidget(0, 5, getCbValue());
    widgetTemplateAttribute.setWidget(0, 6, getCbUnit());

    widgetTemplateAttribute.setWidget(1, 3, getLblTop());
    widgetTemplateAttribute.setWidget(1, 4, getTbLabelTop());
    widgetTemplateAttribute.setWidget(1, 5, getTbTop());
    widgetTemplateAttribute.setWidget(1, 6, getTbUnitTop());

    widgetTemplateAttribute.setWidget(2, 3, lblLeft);
    if (getTemplateAttribute() != null) tbLabelLeft.setText(""+getTemplateAttribute().getLabelLeft());
    else tbLabelLeft.setText(""+DEFAULT_LABELLEFT);
    tbLabelLeft.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(2, 4, tbLabelLeft);
    if (getTemplateAttribute() != null) tbLeft.setText(""+getTemplateAttribute().getLeft());
    else tbLeft.setText(""+DEFAULT_LEFT);
    tbLeft.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(2, 5, tbLeft);
    if (getTemplateAttribute() != null) tbUnitLeft.setText(""+getTemplateAttribute().getUnitLeft());
    else tbUnitLeft.setText(""+DEFAULT_UNITLEFT);
    tbUnitLeft.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(2, 6, tbUnitLeft);

    widgetTemplateAttribute.setWidget(3, 3, lblWidth);
    if (getTemplateAttribute() != null) tbLabelWidth.setText(""+getTemplateAttribute().getLabelWidth());
    else tbLabelWidth.setText(""+DEFAULT_LABELWIDTH);
    tbLabelWidth.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(3, 4, tbLabelWidth);
    if (getTemplateAttribute() != null) tbWidth.setText(""+getTemplateAttribute().getWidth());
    else tbWidth.setText(""+DEFAULT_WIDTH);
    tbWidth.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(3, 5, tbWidth);
    if (getTemplateAttribute() != null) tbUnitWidth.setText(""+getTemplateAttribute().getUnitWidth());
    else tbUnitWidth.setText(""+DEFAULT_UNITWIDTH);
    tbUnitWidth.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(3, 6, tbUnitWidth);

    Label lblWidthUnit = new Label(Main.constants.templateAttributeWidthUnit());
    widgetTemplateAttribute.setWidget(4, 3, lblWidthUnit);
    if (getTemplateAttribute() != null) tbLabelWidthUnit.setText(""+getTemplateAttribute().getLabelWidthUnit());
    widgetTemplateAttribute.setWidget(4, 4, tbLabelWidthUnit);
    if (getTemplateAttribute() != null) tbWidthUnit.setText(""+getTemplateAttribute().getWidthUnit());
    widgetTemplateAttribute.setWidget(4, 5, tbWidthUnit);
    if (getTemplateAttribute() != null) tbUnitWidthUnit.setText(""+getTemplateAttribute().getUnitWidthUnit());
    widgetTemplateAttribute.setWidget(4, 6, tbUnitWidthUnit);

    Label lblAlign = new Label(Main.constants.templateAttributeAlign());
    widgetTemplateAttribute.setWidget(5, 3, lblAlign);
    if (getTemplateAttribute() != null) tbLabelAlign.setText(""+getTemplateAttribute().getLabelAlign());
    widgetTemplateAttribute.setWidget(5, 4, tbLabelAlign);
    if (getTemplateAttribute() != null) tbAlign.setText(""+getTemplateAttribute().getAlign());
    widgetTemplateAttribute.setWidget(5, 5, tbAlign);
    if (getTemplateAttribute() != null) tbUnitAlign.setText(""+getTemplateAttribute().getUnitAlign());
    widgetTemplateAttribute.setWidget(5, 6, tbUnitAlign);
    
    Label lblDef = new Label(Main.constants.templateAttributeDefault());
    widgetTemplateAttribute.setWidget(6, 3, lblDef);
    if (getTemplateAttribute() != null) tbDef.setText(getTemplateAttribute().getDef());
    widgetTemplateAttribute.setWidget(6, 4, tbDef);
    widgetTemplateAttribute.getFlexCellFormatter().setColSpan(6, 4, 3);

    widgetTemplateAttribute.setWidget(7, 3, lblLength);
    if (getTemplateAttribute() != null) tbLength.setText(""+getTemplateAttribute().getLength());
    else tbLength.setText(""+DEFAULT_LENGTH);
    tbLength.setWidth(TBWIDTH_SMALL);
    widgetTemplateAttribute.setWidget(7, 4, tbLength);
    
    widgetTemplateAttribute.setWidget(8, 3, getLblTabIndex());
    widgetTemplateAttribute.setWidget(8, 4, getTbTabIndex());

  }

  // getters and setters
  
  /**
   * @return the templateAttribute
   */
  public TemplateAttribute getTemplateAttribute() {
    return (TemplateAttribute)getItem();
  }

  /**
   * Getter for property 'lblCode'.
   * 
   * @return Value for property 'lblCode'.
   */
  protected Label getLblCode() {
    if (lblCode == null) {
      lblCode = new Label(Main.constants.templateAttributeCode());
    }
    return lblCode;
  }

  /**
   * Getter for property 'lblName'.
   * 
   * @return Value for property 'lblName'.
   */
  protected Label getLblName() {
    if (lblName == null) {
      lblName = new Label(Main.constants.templateAttributeName());
    }
    return lblName;
  }
  
  /**
   * Getter for property 'lblDesc'.
   * 
   * @return Value for property 'lblDesc'.
   */
  protected Label getLblDesc() {
    if (lblDesc == null) {
      lblDesc = new Label(Main.constants.templateAttributeDesc());
    }
    return lblDesc;
  }
  
  /**
   * Getter for property 'lblTg'.
   * 
   * @return Value for property 'lblTg'.
   */
  protected Label getLblTg() {
    if (lblTg == null) {
      lblTg = new Label(Main.constants.templateAttributeTg());
    }
    return lblTg;
  }
  
  /**
   * Getter for property 'ddbTemplateGroup'.
   * 
   * @return Value for property 'ddbTemplateGroup'.
   */
  protected DropDownBox getDdbTemplateGroup() {
    if (ddbTemplateGroup == null) {
      ddbTemplateGroup = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER);
      if (getTemplateAttribute() != null && getTemplateAttribute().getTg() != null)
        ddbTemplateGroup.setSelection(new DropDownObjectImpl(getTemplateAttribute().getTgId(), 
            getTemplateAttribute().getTg().getName(), getTemplateAttribute().getTg()));
      else
        ddbTemplateGroup.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseTemplateGroup()));
      
      Collection<TemplateGroup> tgs = getModel().getTemplateGroups()
          .get(getModel().getTemplate().getId());
      if (tgs == null) {
        TemplateGroupLoader tgl = new TemplateGroupLoader(getModel(), 
            getModel().getTemplate());
        tgl.start();    
      } else
        fillTemplateGroups(tgs);
      
    }
    return ddbTemplateGroup;
  }  
  
  /**
   * Getter for property 'lblOa'.
   * 
   * @return Value for property 'lblOa'.
   */
  protected Label getLblOa() {
    if (lblOa == null) {
      lblOa = new Label(Main.constants.templateAttributeOa());
    }
    return lblOa;
  }
  
  /**
   * Getter for property 'ddbObjectAttribute'.
   * 
   * @return Value for property 'ddbObjectAttribute'.
   */
  protected DropDownBox getDdbObjectAttribute() {
    if (ddbObjectAttribute == null) {
      ddbObjectAttribute = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
              fillDCTemplates();
            }
      
      });
      loadAttributes(getModel().getTemplate().getOtId(), getDdbObjectAttribute(),
          getTemplateAttribute() != null ? getTemplateAttribute().getOaId() : 0);

      fillDCTemplates();
    }
    return ddbObjectAttribute;
  }  
  
  /**
   * Getter for property 'cbMandatory'.
   * 
   * @return Value for property 'cbMandatory'.
   */
  protected CheckBox getCbMandatory() {
    if (cbMandatory == null) {
      cbMandatory = new CheckBox(Main.constants.templateAttributeMandatory());
      if (getTemplateAttribute() != null) cbMandatory.setValue(ClientUtils
          .getFlag(TemplateAttribute.FLAG_MANDATORY, 
              getTemplateAttribute().getFlags()));
    }
    return cbMandatory;
  }

  /**
   * Getter for property 'cbDerived'.
   * 
   * @return Value for property 'cbDerived'.
   */
  protected CheckBox getCbDerived() {
    if (cbDerived == null) {
      cbDerived = new CheckBox(Main.constants.templateAttributeDerived());
      if (getTemplateAttribute() != null) cbDerived.setValue(ClientUtils
          .getFlag(TemplateAttribute.FLAG_DERIVED, 
              getTemplateAttribute().getFlags()));
    }
    return cbDerived;
  }
  /**
   * Getter for property 'cbDesc'.
   * 
   * @return Value for property 'cbDesc'.
   */
  protected CheckBox getCbDesc() {
    if (cbDesc == null) {
      cbDesc = new CheckBox(Main.constants.templateAttributeDescendant());
      if (getTemplateAttribute() != null) cbDesc.setValue(ClientUtils
          .getFlag(TemplateAttribute.FLAG_DESCENDANT, 
              getTemplateAttribute().getFlags()));
    }
    return cbDesc;
  }

  /**
   * Getter for property 'cbLabel'.
   * 
   * @return Value for property 'cbLabel'.
   */
  protected CheckBox getCbLabel() {
    if (cbLabel == null) {
      cbLabel = new CheckBox(Main.constants.templateAttributeLabel());
      cbLabel.setValue(true);
      if (getTemplateAttribute() != null) cbLabel.setValue(ClientUtils
          .getFlag(TemplateAttribute.FLAG_SHOW_LABEL, 
              getTemplateAttribute().getFlags()));

    }
    return cbLabel;
  }

  /**
   * Getter for property 'cbValue'.
   * 
   * @return Value for property 'cbValue'.
   */
  protected CheckBox getCbValue() {
    if (cbValue == null) {
      cbValue = new CheckBox(Main.constants.templateAttributeValue());
      cbValue.setValue(true);
      if (getTemplateAttribute() != null) cbValue.setValue(ClientUtils
          .getFlag(TemplateAttribute.FLAG_SHOW_VALUE, 
              getTemplateAttribute().getFlags()));

    }
    return cbValue;
  }

  /**
   * Getter for property 'cbUnit'.
   * 
   * @return Value for property 'cbUnit'.
   */
  protected CheckBox getCbUnit() {
    if (cbUnit == null) {
      cbUnit = new CheckBox(Main.constants.templateAttributeUnit());
      if (getTemplateAttribute() != null) cbUnit.setValue(ClientUtils
          .getFlag(TemplateAttribute.FLAG_SHOW_UNIT, 
              getTemplateAttribute().getFlags()));

    }
    return cbUnit;
  }
  
  /**
   * Getter for property 'lblStyle'.
   * 
   * @return Value for property 'lblStyle'.
   */
  protected Label getLblStyle() {
    if (lblStyle == null) {
      lblStyle = new Label(Main.constants.templateAttributeStyle());
    }
    return lblStyle;
  }
  
  /**
   * Getter for property 'ddbStyle'.
   * 
   * @return Value for property 'ddbStyle'.
   */
  protected DropDownBox getDdbStyle() {
    if (ddbStyle == null) {
      ddbStyle = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {
            
            @Override
            public void onChange(ChangeEvent event) {
              setVisibility();
            }
          });
      
      ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
        
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_LABEL, 
          Main.constants.templateAttributeStyle1()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_TEXTBOX, 
          Main.constants.templateAttributeStyle2()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_DATEPICKER, 
          Main.constants.templateAttributeStyle3()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_DYNAMICCOMBO, 
          Main.constants.templateAttributeStyle4()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_TABLE, 
          Main.constants.templateAttributeStyle5()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_IMAGE, 
          Main.constants.templateAttributeStyle6()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_TEXTAREA, 
          Main.constants.templateAttributeStyle7()));
      items.add(new DropDownObjectImpl(TemplateAttribute.STYLE_LINK, 
          Main.constants.templateAttributeStyle8()));
      
      if (getTemplateAttribute() != null)
        ddbStyle.setSelection(items.get(getTemplateAttribute().getStyle()-1));
      else
        ddbStyle.setSelection(items.get(TemplateAttribute.STYLE_TEXTBOX-1));
      ddbStyle.setItems(items);
 
    }
    return ddbStyle;
  } 

  /**
   * Getter for property 'lblTable'.
   * 
   * @return Value for property 'lblTable'.
   */
  protected Label getLblTable() {
    if (lblTable == null) {
      lblTable = new Label(Main.constants.templateAttributeTable());
    }
    return lblTable;
  }

  /**
   * Getter for property 'ddbTable'.
   * 
   * @return Value for property 'ddbTable'.
   */
  protected DropDownBox getDdbTable() {
    if (ddbTable == null) {
      ddbTable = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER);

      ddbTable.setSelection(new DropDownObjectImpl(0, 
          Main.constants.chooseTable()));
      
      Collection<TemplateAttribute> tas = getModel().getAttrsByTemplate()
          .get(getModel().getTemplate().getId());
      assert(tas != null);
      ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
      for (TemplateAttribute ta : tas) {
        if (ta.getStyle() == TemplateAttribute.STYLE_TABLE) {
          items.add(new DropDownObjectImpl(ta.getId(), ta.getName(), ta));
          if (getTemplateAttribute() != null && getTemplateAttribute().getShared5() == ta.getId())
            ddbTable.setSelection(new DropDownObjectImpl(ta.getId(), 
                ta.getName(), ta));
            
        }
      }
      ddbTable.setItems(items);
      
    }
    return ddbTable;
  }  
  
  /**
   * Getter for property 'lblDCT'.
   * 
   * @return Value for property 'lblDCT'.
   */
  protected Label getLblDCT() {
    if (lblDCT == null) {
      lblDCT = new Label(Main.constants.templateAttributeDCT());
    }
    return lblDCT;
  }
  
  /**
   * Getter for property 'ddbDCT'.
   * 
   * @return Value for property 'ddbDCT'.
   */
  protected DropDownBox getDdbDCT() {
    if (ddbDCT == null) {
      ddbDCT = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
              loadAttributes(((Template)getDdbDCT().getSelection()
                  .getUserObject()).getOtId(), getDdbDCOA(),
                  getTemplateAttribute() != null ? getTemplateAttribute().getShared2() : 0);
            }
      });
    }
    return ddbDCT;
  }
  
  /**
   * Getter for property 'lblDCOA'.
   * 
   * @return Value for property 'lblDCOA'.
   */
  protected Label getLblDCOA() {
    if (lblDCOA == null) {
      lblDCOA = new Label(Main.constants.templateAttributeDCOA());
    }
    return lblDCOA;
  }
  
  /**
   * Getter for property 'ddbDCOA'.
   * 
   * @return Value for property 'ddbDCOA'.
   */
  protected DropDownBox getDdbDCOA() {
    if (ddbDCOA == null) {
      ddbDCOA = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER);
    }
    return ddbDCOA;
  }  
  
  /**
   * Getter for property 'lblTType'.
   * 
   * @return Value for property 'lblTType'.
   */
  protected Label getLblTType() {
    if (lblTType == null) {
      lblTType = new Label(Main.constants.templateAttributeTableType());
    }
    return lblTType;
  }

  /**
   * Getter for property 'lbTType'.
   * 
   * @return Value for property 'lbTType'.
   */
  protected ListBox getLbTType() {
    if (lbTType == null) {
      lbTType = new ListBox();
      lbTType.setWidth("100%");
      lbTType.addItem(Main.constants.templateAttributeTableType1(), 
          ""+TemplateAttribute.TABLETYPE_TABLE); 
      lbTType.addItem(Main.constants.templateAttributeTableType2(), 
          ""+TemplateAttribute.TABLETYPE_GRAPH); 
      lbTType.addItem(Main.constants.templateAttributeTableType3(), 
          ""+TemplateAttribute.TABLETYPE_TABLEGRAPH);
      
      if (getTemplateAttribute() != null) 
        lbTType.setSelectedIndex(getTemplateAttribute().getShared1());
      else 
        lbTType.setSelectedIndex(TemplateAttribute.TABLETYPE_TABLE);
      
      lbTType.addChangeHandler(new ChangeHandler() {
        
        @Override
        public void onChange(ChangeEvent event) {
          
          boolean graphVisible = lbTType.getValue(lbTType.getSelectedIndex())
              .equals(""+TemplateAttribute.TABLETYPE_GRAPH) ||
              lbTType.getValue(lbTType.getSelectedIndex())
              .equals(""+TemplateAttribute.TABLETYPE_TABLEGRAPH);
              
          if (graphVisible) {
            widgetTemplateAttribute.setWidget(10, 0, getLblTGraphType());
            widgetTemplateAttribute.setWidget(10, 1, getLbTGraphType());

            widgetTemplateAttribute.setWidget(11, 0, getLblTGraphX());
            widgetTemplateAttribute.setWidget(11, 1, getLbTGraphX());

            widgetTemplateAttribute.setWidget(12, 0, getLblTGraphY());
            widgetTemplateAttribute.setWidget(12, 1, getLbTGraphY());
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
      });

    }
    return lbTType;
  }
  
  /**
   * Getter for property 'lblTOrientation'.
   * 
   * @return Value for property 'lblTOrientation'.
   */
  protected Label getLblTOrientation() {
    if (lblTOrientation == null) {
      lblTOrientation = new Label(Main.constants.templateAttributeTableOrientation());
    }
    return lblTOrientation;
  }
  
  /**
   * Getter for property 'lbTOrientation'.
   * 
   * @return Value for property 'lbTOrientation'.
   */
  protected ListBox getLbTOrientation() {
    if (lbTOrientation == null) {
      lbTOrientation = new ListBox();
      lbTOrientation.setWidth("100%");
      lbTOrientation.addItem(Main.constants.templateAttributeTableOrientation1(), 
          ""+TemplateAttribute.TABLEORIENTATION_HORIZONTAL); 
      lbTOrientation.addItem(Main.constants.templateAttributeTableOrientation2(), 
          ""+TemplateAttribute.TABLEORIENTATION_VERTICAL); 
      
      if (getTemplateAttribute() != null) 
        lbTOrientation.setSelectedIndex(getTemplateAttribute().getShared2());
      else 
        lbTOrientation.setSelectedIndex(TemplateAttribute.TABLEORIENTATION_HORIZONTAL);

    }
    return lbTOrientation;
  }

  /**
   * Getter for property 'lblTGraphType'.
   * 
   * @return Value for property 'lblTGraphType'.
   */
  protected Label getLblTGraphType() {
    if (lblTGraphType == null) {
      lblTGraphType = new Label(Main.constants.templateAttributeTableGraphType());
    }
    return lblTGraphType;
  }
  
  /**
   * Getter for property 'lbTGraphType'.
   * 
   * @return Value for property 'lbTGraphType'.
   */
  protected ListBox getLbTGraphType() {
    if (lbTGraphType == null) {
      lbTGraphType = new ListBox();
      lbTGraphType.setWidth("100%");
      lbTGraphType.addItem(Main.constants.templateAttributeTableGraphType1(), 
          ""+TemplateAttribute.TABLEGRAPHTYPE_LINE); 
      lbTGraphType.addItem(Main.constants.templateAttributeTableGraphType2(), 
          ""+TemplateAttribute.TABLEGRAPHTYPE_BAR); 
      lbTGraphType.addItem(Main.constants.templateAttributeTableGraphType3(), 
          ""+TemplateAttribute.TABLEGRAPHTYPE_AREA); 
      
      if (getTemplateAttribute() != null) 
        lbTGraphType.setSelectedIndex(getTemplateAttribute().getShared3());
      else 
        lbTGraphType.setSelectedIndex(TemplateAttribute.TABLEGRAPHTYPE_LINE);

    }
    return lbTGraphType;
  }

  /**
   * Getter for property 'lblTGraphX'.
   * 
   * @return Value for property 'lblTGraphX'.
   */
  protected Label getLblTGraphX() {
    if (lblTGraphX == null) {
      lblTGraphX = new Label(Main.constants.templateAttributeTableGraphX());
    }
    return lblTGraphX;
  }
  
  /**
   * Getter for property 'lbTGraphX'.
   * 
   * @return Value for property 'lbTGraphX'.
   */
  protected ListBox getLbTGraphX() {
    if (lbTGraphX == null) {
      lbTGraphX = new ListBox();
      lbTGraphX.setWidth("100%");
      
      if (getTemplateAttribute() != null) 
        lbTGraphX.setSelectedIndex(getTemplateAttribute().getShared4());
      else 
        lbTGraphX.setSelectedIndex(0);

    }
    return lbTGraphX;
  }
  
  /**
   * Getter for property 'lblTGraphY'.
   * 
   * @return Value for property 'lblTGraphY'.
   */
  protected Label getLblTGraphY() {
    if (lblTGraphY == null) {
      lblTGraphY = new Label(Main.constants.templateAttributeTableGraphY());
    }
    return lblTGraphY;
  }
  
  /**
   * Getter for property 'lbTGraphY'.
   * 
   * @return Value for property 'lbTGraphY'.
   */
  protected ListBox getLbTGraphY() {
    if (lbTGraphY == null) {
      lbTGraphY = new ListBox();
      lbTGraphY.setWidth("100%");
      
      if (getTemplateAttribute() != null) 
        lbTGraphY.setSelectedIndex(getTemplateAttribute().getShared5());
      else 
        lbTGraphY.setSelectedIndex(0);

    }
    return lbTGraphY;
  }

  /**
   * Getter for property 'lblTop'.
   * 
   * @return Value for property 'lblTop'.
   */
  protected Label getLblTop() {
    if (lblTop == null) {
      lblTop = new Label(Main.constants.templateAttributeTop());
    }
    return lblTop;
  }
  
  /**
   * Getter for property 'tbLabelTop'.
   * 
   * @return Value for property 'tbLabelTop'.
   */
  protected IntegerTextBox getTbLabelTop() {
    if (tbLabelTop == null) {
      tbLabelTop = new IntegerTextBox(getLblTop(), getBOk());
      if (getTemplateAttribute() != null) 
        tbLabelTop.setText(""+getTemplateAttribute().getLabelTop());
      else tbLabelTop.setText(""+DEFAULT_LABELTOP);
      tbLabelTop.setWidth(TBWIDTH_SMALL);
    }
    return tbLabelTop;
  }

  /**
   * Getter for property 'tbTop'.
   * 
   * @return Value for property 'tbTop'.
   */
  protected IntegerTextBox getTbTop() {
    if (tbTop == null) {
      tbTop = new IntegerTextBox(getLblTop(), getBOk());
      if (getTemplateAttribute() != null) 
        tbTop.setText(""+getTemplateAttribute().getTop());
      else tbTop.setText(""+DEFAULT_TOP);
      tbTop.setWidth(TBWIDTH_SMALL);
    }
    return tbTop;
  }

  /**
   * Getter for property 'tbUnitTop'.
   * 
   * @return Value for property 'tbUnitTop'.
   */
  protected IntegerTextBox getTbUnitTop() {
    if (tbUnitTop == null) {
      tbUnitTop = new IntegerTextBox(getLblTop(), getBOk());
      if (getTemplateAttribute() != null) 
        tbUnitTop.setText(""+getTemplateAttribute().getUnitTop());
      else tbUnitTop.setText(""+DEFAULT_UNITTOP);
      tbUnitTop.setWidth(TBWIDTH_SMALL);
    }
    return tbUnitTop;
  }

  /**
   * Getter for property 'lblTabIndex'.
   * 
   * @return Value for property 'lblTabIndex'.
   */
  protected Label getLblTabIndex() {
    if (lblTabIndex == null) {
      lblTabIndex = new Label(Main.constants.templateAttributeTabIndex());
    }
    return lblTabIndex;
  }
  
  /**
   * Getter for property 'tbTabIndex'.
   * 
   * @return Value for property 'tbTabIndex'.
   */
  protected IntegerTextBox getTbTabIndex() {
    if (tbTabIndex == null) {
      tbTabIndex = new IntegerTextBox(getLblTop(), getBOk());
      if (getTemplateAttribute() != null) 
        tbTabIndex.setText(""+getTemplateAttribute().getTabIndex());
      else tbTabIndex.setText(""+DEFAULT_TABINDEX);
      tbTabIndex.setWidth(TBWIDTH_SMALL);
    }
    return tbTabIndex;
  }
  
  /**
   * Getter for property 'lblStartYear'.
   * 
   * @return Value for property 'lblStartYear'.
   */
  protected Label getLblStartYear() {
    if (lblStartYear == null) {
      lblStartYear = new Label(Main.constants.templateAttributeStartYear());
    }
    return lblStartYear;
  }
  
  /**
   * Getter for property 'tbStartYear'.
   * 
   * @return Value for property 'tbStartYear'.
   */
  protected IntegerTextBox getTbStartYear() {
    if (tbStartYear == null) {
      tbStartYear = new IntegerTextBox(getLblTop(), getBOk());
      if (getTemplateAttribute() != null) 
        tbStartYear.setText(""+getTemplateAttribute().getShared1());
      else tbStartYear.setText(""+DEFAULT_STARTYEAR);
      tbStartYear.setWidth(TBWIDTH_SMALL);
    }
    return tbStartYear;
  }

  /**
   * Getter for property 'lblEndYear'.
   * 
   * @return Value for property 'lblEndYear'.
   */
  protected Label getLblEndYear() {
    if (lblEndYear == null) {
      lblEndYear = new Label(Main.constants.templateAttributeEndYear());
    }
    return lblEndYear;
  }

  /**
   * Getter for property 'tbEndYear'.
   * 
   * @return Value for property 'tbEndYear'.
   */
  protected IntegerTextBox getTbEndYear() {
    if (tbEndYear == null) {
      tbEndYear = new IntegerTextBox(getLblTop(), getBOk());
      DateHelper date = new DateHelper(new Date());
      if (getTemplateAttribute() != null) 
        tbEndYear.setText(""+getTemplateAttribute().getShared2());
      else tbEndYear.setText(""+date.getYear());
      tbEndYear.setWidth(TBWIDTH_SMALL);
    }
    return tbEndYear;
  }

  /**
   * Getter for property 'objectTemplate'.
   * 
   * @return Value for property 'objectTemplate'.
   */
  public ObjectTemplate getObjectTemplate() {
    if (objectTemplate == null) {
      objectTemplate = new ObjectTemplate(amodel);
      objectTemplate.initialize(getTemplateAttribute().getTg().getT());
    }
    return objectTemplate;
  }
  
  // private methods

  private void fill(TemplateAttribute templateAttribute) {
    super.fill(templateAttribute);

    // template group
    int itg = getDdbTemplateGroup().getSelection().getId();
    templateAttribute.setTgId(itg);
    templateAttribute.setTg((TemplateGroup)getDdbTemplateGroup().getSelection().getUserObject());

    // object attribute
    int ioa = getDdbObjectAttribute().getSelection().getId();
    templateAttribute.setOaId(ioa);
    templateAttribute.setOa((ObjectAttribute)getDdbObjectAttribute().getSelection().getUserObject());

    // default
    String def = tbDef.getText().trim();
    templateAttribute.setDef(def.length() == 0 ? null : def);

    // length
    templateAttribute.setLength(new Integer(ClientUtils
        .parseInt(tbLength.getText().trim())));

    // tabindex
    templateAttribute.setTabIndex(Integer.parseInt(getTbTabIndex().getText()));

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
    templateAttribute.setStyle(getDdbStyle().getSelection().getId());

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
      templateAttribute.setShared1(new Integer(ClientUtils
          .parseInt(getTbStartYear().getText().trim())));
      templateAttribute.setShared2(new Integer(ClientUtils
          .parseInt(getTbEndYear().getText().trim())));
      break;
    case TemplateAttribute.STYLE_DYNAMICCOMBO:
      templateAttribute.setShared1(getDdbDCT().getSelection().getId());
      templateAttribute.setShared2(getDdbDCOA().getSelection().getId());
      templateAttribute.setShared3(0);
      templateAttribute.setShared4(0);
      templateAttribute.setShared5(getDdbTable().getSelection().getId());
      break;
    case TemplateAttribute.STYLE_TABLE:
      templateAttribute.setShared1(Integer.parseInt(getLbTType().getValue(
          getLbTType().getSelectedIndex())));
      templateAttribute.setShared2(Integer.parseInt(getLbTOrientation()
          .getValue(getLbTOrientation().getSelectedIndex())));
      if (getLbTType().getValue(getLbTType().getSelectedIndex()).equals(
          "" + TemplateAttribute.TABLETYPE_GRAPH)
          || getLbTType().getValue(getLbTType().getSelectedIndex()).equals(
              "" + TemplateAttribute.TABLETYPE_TABLEGRAPH)) {
        templateAttribute.setShared3(Integer.parseInt(getLbTGraphType()
            .getValue(getLbTGraphType().getSelectedIndex())));
        templateAttribute.setShared4(Integer.parseInt(getLbTGraphX().getValue(
            getLbTGraphX().getSelectedIndex())));
        templateAttribute.setShared5(Integer.parseInt(getLbTGraphY().getValue(
            getLbTGraphY().getSelectedIndex())));
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
      templateAttribute.setShared5(getDdbTable().getSelection().getId());
      break;
    }
  }
  
  private void fillTemplateGroups (Collection<TemplateGroup> templateGroups) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (TemplateGroup tg : templateGroups) {
      items.add(new DropDownObjectImpl(tg.getId(), tg.getName(), tg));
    }
    getDdbTemplateGroup().setItems(items);
  }
  
  private void fillObjectAttributes (DropDownBox ddb,
      Collection<ObjectAttribute> objectAttributes, int selection) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (ObjectAttribute oa : objectAttributes) {
      items.add(new DropDownObjectImpl(oa.getId(), oa.getName(), oa));
      if (selection == oa.getId())
        ddb.setSelection(items.get(items.size()-1));
    }
    ddb.setItems(items);
  }
  
  private void fillDCTemplates() {
    ObjectAttribute oa = (ObjectAttribute)getDdbObjectAttribute()
        .getSelection().getUserObject();
    if (oa != null && oa.getVt().getType() == ValueType.VT_REF) {
      getDdbStyle().setSelection(
          new DropDownObjectImpl(TemplateAttribute.STYLE_DYNAMICCOMBO, Main.constants.templateAttributeStyle4()));

      ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
      Collection<ApplicationTemplate> appts = getModel().getAppTemplateByApp(getModel().getApplication().getId());
      assert(appts != null);
      for (ApplicationTemplate appt : appts) {
        if (appt.getT().getOtId() == oa.getShared1()) {
          items.add(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
          if (getTemplateAttribute() != null && getTemplateAttribute().getShared1() == appt.getTId())
            getDdbDCT().setSelection(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
        }
      }
      getDdbDCT().setItems(items);
      
      loadAttributes(((Template)getDdbDCT().getSelection().getUserObject()).getOtId(), getDdbDCOA(),
          getTemplateAttribute() != null ? getTemplateAttribute().getShared2() : 0);

      setVisibility();
    }
  }
  
  private void loadAttributes(int otId, DropDownBox ddb, int selection) {
    ddb.setSelection(new DropDownObjectImpl(0, 
        Main.constants.chooseObjectAttribute()));
    Collection<ObjectAttribute> oas = getModel().getObjectAttributes().get(otId);
    if (oas == null) {
      ObjectAttributeLoader oal = new ObjectAttributeLoader(getModel(), otId);
      oal.start();    
    } else
      fillObjectAttributes(ddb, oas, selection);
  }

  private void setVisibility() {
    boolean dynacomboVisible = ddbStyle.getSelection().getId() ==
        TemplateAttribute.STYLE_DYNAMICCOMBO;
    boolean tableVisible = ddbStyle.getSelection().getId() ==
        TemplateAttribute.STYLE_TABLE;
    boolean isDatePicker = ddbStyle.getSelection().getId() ==
        TemplateAttribute.STYLE_DATEPICKER;

    setUnitVisible();

    if (dynacomboVisible) {
      widgetTemplateAttribute.setWidget(9, 0, getLblTable());
      widgetTemplateAttribute.setWidget(9, 1, getDdbTable());
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(9, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateAttribute.setWidget(10, 0, getLblDCT());
      widgetTemplateAttribute.setWidget(10, 1, getDdbDCT());
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(10, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateAttribute.setWidget(11, 0, getLblDCOA());
      widgetTemplateAttribute.setWidget(11, 1, getDdbDCOA());
      widgetTemplateAttribute.getFlexCellFormatter().addStyleName(11, 1, ClientUtils.CSS_ALIGN_RIGHT);
    } else if (tableVisible) {
//                widgetTemplateAttribute.setWidget(9, 0, getLblTType());
//                widgetTemplateAttribute.setWidget(9, 1, getLbTType());
//
//                widgetTemplateAttribute.setWidget(10, 0, getLblTOrientation());
//                widgetTemplateAttribute.setWidget(10, 1, getLbTOrientation());
//
//                NativeEvent nevent = Document.get().createChangeEvent();
//                DomEvent.fireNativeEvent(nevent, getLbTType());
    } else if (isDatePicker) {
      widgetTemplateAttribute.setWidget(9, 0, getLblStartYear());
      widgetTemplateAttribute.setWidget(9, 1, getTbStartYear());

      widgetTemplateAttribute.setWidget(10, 0, getLblEndYear());
      widgetTemplateAttribute.setWidget(10, 1, getTbEndYear());
    } else {
      widgetTemplateAttribute.setWidget(9, 0, getLblTable());
      widgetTemplateAttribute.setWidget(9, 1, getDdbTable());
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
    boolean unitVisible = getDdbStyle().getSelection().getId() !=
        TemplateAttribute.STYLE_DYNAMICCOMBO &&
        getDdbStyle().getSelection().getId() !=
            TemplateAttribute.STYLE_DATEPICKER &&
        getDdbStyle().getSelection().getId() !=
            TemplateAttribute.STYLE_TABLE;

    getCbUnit().setVisible(unitVisible);
    getTbUnitTop().setVisible(unitVisible);
    tbUnitLeft.setVisible(unitVisible);
    tbUnitWidth.setVisible(unitVisible);
    tbUnitWidthUnit.setVisible(unitVisible);
    tbUnitAlign.setVisible(unitVisible);
  }
}
