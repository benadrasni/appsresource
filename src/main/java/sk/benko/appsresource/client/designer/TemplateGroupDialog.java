package sk.benko.appsresource.client.designer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.ApplicationTemplateLoader;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.ui.widget.IntegerTextBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 *
 */
public class TemplateGroupDialog extends DesignerDialog implements 
    Model.ApplicationObserver {
  private final static int DEFAULT_RANK = 0;
  private final static int DEFAULT_SUBRANK = 0;
  private final static int DEFAULT_LABELTOP = 10;
  private final static int DEFAULT_LABELLEFT = 10;
  private final static int DEFAULT_LABELWIDTH = 50;
  private final static String DEFAULT_LABELWIDTHUNIT = "px";
  private final static String DEFAULT_LABELALIGN = "left";

  final CheckBox cbLabel = new CheckBox();
  private DropDownBox ddbTemplate;
  
  final Label lblRank = new Label(Main.constants.templateGroupRank());
  final Label lblSubrank = new Label(Main.constants.templateGroupSubrank());
  final Label lblLabelTop = new Label(Main.constants.templateGroupLabelTop());
  final Label lblLabelLeft = new Label(Main.constants.templateGroupLabelLeft());
  final Label lblLabelWidth = new Label(Main.constants.templateGroupLabelWidth());

  final IntegerTextBox tbRank = new IntegerTextBox(lblRank, getBOk());
  final IntegerTextBox tbSubrank = new IntegerTextBox(lblSubrank, getBOk());
  final IntegerTextBox tbLabelTop = new IntegerTextBox(lblLabelTop, getBOk());
  final IntegerTextBox tbLabelLeft = new IntegerTextBox(lblLabelLeft, getBOk());
  final IntegerTextBox tbLabelWidth = new IntegerTextBox(lblLabelWidth, getBOk());
  final TextBox tbLabelWidthUnit = new TextBox();
  final TextBox tbLabelAlign = new TextBox();

  private FlexTable widgetTemplateGroup;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateGroupDialog(final DesignerModel model, TemplateGroup tg) {
    super(model, tg);
    getModel().addDataObserver(this);

    getHeader().add(new Label((getTemplateGroup() == null ? Main.constants.newItem() + " " 
        : "") + Main.constants.templateGroup()));

    NavigationLabelView menu1 = new NavigationLabelView(
        getModel(), Main.constants.templateGroup(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        getModel().notifyDialogNavigationItemClicked(event.getRelativeElement());
        getBodyRight().clear();
        getBodyRight().add(getWidgetTemplateGroup());
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);
    getBodyLeft().add(menu1);
    
    getBodyRight().add(getWidgetTemplateGroup());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getTemplateGroup() == null)
                setItem(new TemplateGroup(getTbName().getText(),
                    getDdbTemplate().getSelection().getId()));
              fill(getTemplateGroup());
              model.createOrUpdateTemplateGroup(getTemplateGroup());
              close();
            }
        }, ClickEvent.getType());
    getBOk().getElement().setInnerText(getTemplateGroup() == null ? 
        Main.constants.create() : Main.constants.save());
  }
  
  @Override
  public void close() {
    getModel().removeDataObserver(this);
    hide();
  }

  @Override
  public void onApplicationTemplatesLoaded(List<ApplicationTemplate> appts) {
    fillTemplates(appts);
  }
  
  /**
   * Getter for property 'widgetTemplateGroup'.
   * 
   * @return Value for property 'widgetTemplateGroup'.
   */
  protected FlexTable getWidgetTemplateGroup() {
    if (widgetTemplateGroup == null) {
      widgetTemplateGroup = new FlexTable();

      Label lblCode = new Label(Main.constants.templateGroupCode());
      widgetTemplateGroup.setWidget(0, 0, lblCode);
      widgetTemplateGroup.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.templateGroupName());
      widgetTemplateGroup.setWidget(1, 0, lblName);
      widgetTemplateGroup.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.objectTypeDesc());
      widgetTemplateGroup.setWidget(2, 0, lblDesc);
      widgetTemplateGroup.setWidget(2, 1, getTbDesc());
      
      Label lblT = new Label(Main.constants.templateGroupT());
      widgetTemplateGroup.setWidget(3, 0, lblT);
      widgetTemplateGroup.setWidget(3, 1, getDdbTemplate());
      widgetTemplateGroup.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      cbLabel.setText(Main.constants.templateGroupLabel());
      if (getTemplateGroup() != null) cbLabel.setValue(ClientUtils
          .getFlag(TemplateGroup.FLAG_SHOW_LABEL, getTemplateGroup().getFlags()));
      widgetTemplateGroup.setWidget(4, 1, cbLabel);

      widgetTemplateGroup.setWidget(5, 0, lblRank);
      if (getTemplateGroup() != null) tbRank.setText(""+getTemplateGroup().getRank());
      else tbRank.setText(""+DEFAULT_RANK);
      widgetTemplateGroup.setWidget(5, 1, tbRank);

      widgetTemplateGroup.setWidget(6, 0, lblSubrank);
      if (getTemplateGroup() != null) tbSubrank.setText(""+getTemplateGroup().getSubRank());
      else tbSubrank.setText(""+DEFAULT_SUBRANK);
      widgetTemplateGroup.setWidget(6, 1, tbSubrank);

      widgetTemplateGroup.setWidget(7, 0, lblLabelTop);
      if (getTemplateGroup() != null) tbLabelTop.setText(""+getTemplateGroup().getLabelTop());
      else tbLabelTop.setText(""+DEFAULT_LABELTOP);
      widgetTemplateGroup.setWidget(7, 1, tbLabelTop);

      widgetTemplateGroup.setWidget(8, 0, lblLabelLeft);
      if (getTemplateGroup() != null) tbLabelLeft.setText(""+getTemplateGroup().getLabelLeft());
      else tbLabelLeft.setText(""+DEFAULT_LABELLEFT);
      widgetTemplateGroup.setWidget(8, 1, tbLabelLeft);

      widgetTemplateGroup.setWidget(9, 0, lblLabelWidth);
      if (getTemplateGroup() != null) tbLabelWidth.setText(""+getTemplateGroup().getLabelWidth());
      else tbLabelWidth.setText(""+DEFAULT_LABELWIDTH);
      widgetTemplateGroup.setWidget(9, 1, tbLabelWidth);

      Label lblLabelWidthUnit = new Label(Main.constants.templateGroupLabelWidthUnit());
      widgetTemplateGroup.setWidget(10, 0, lblLabelWidthUnit);
      if (getTemplateGroup() != null) tbLabelWidthUnit.setText(getTemplateGroup().getLabelWidthUnit());
      else tbLabelWidthUnit.setText(DEFAULT_LABELWIDTHUNIT);
      widgetTemplateGroup.setWidget(10, 1, tbLabelWidthUnit);

      Label lblLabelAlign = new Label(Main.constants.templateGroupLabelAlign());
      widgetTemplateGroup.setWidget(11, 0, lblLabelAlign);
      if (getTemplateGroup() != null) tbLabelAlign.setText(getTemplateGroup().getLabelAlign());
      else tbLabelAlign.setText(DEFAULT_LABELALIGN);
      widgetTemplateGroup.setWidget(11, 1, tbLabelAlign);      
    }
    
    return widgetTemplateGroup;
  }

  /**
   * Getter for property 'ddbTemplate'.
   * 
   * @return Value for property 'ddbTemplate'.
   */
  protected DropDownBox getDdbTemplate() {
    if (ddbTemplate == null) {
      ddbTemplate = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER);
      if (getTemplateGroup() != null && getModel().getTemplate().getOa() != null)
        ddbTemplate.setSelection(new DropDownObjectImpl(getTemplateGroup().getTId(), 
            getTemplateGroup().getT().getName(), getTemplateGroup().getT()));
      else
        ddbTemplate.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseTemplate()));
      
      if (getModel().getAppTemplateByApp(getModel().getApplication().getId()) == null) {
        ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), 
            getModel().getApplication());
        atl.start();    
      } else
        fillTemplates(getModel().getAppTemplateByApp(getModel().getApplication().getId()));
      
    }
    return ddbTemplate;
  }

  /**
   * @return the templateGroup
   */
  public TemplateGroup getTemplateGroup() {
    return (TemplateGroup)getItem();
  }

  // private methods
  private void fill(TemplateGroup templateGroup) {
    super.fill(templateGroup);

    templateGroup.setTId(getDdbTemplate().getSelection().getId());
    templateGroup.setT((Template)getDdbTemplate().getSelection().getUserObject());
    
    // flags
    int flags = 0;
    if (cbLabel.getValue())
      flags = ClientUtils.setFlag(TemplateGroup.FLAG_SHOW_LABEL, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateGroup.FLAG_SHOW_LABEL, flags);
    templateGroup.setFlags(flags);

    // rank
    templateGroup.setRank(Integer.parseInt(tbRank.getText()));
    templateGroup.setSubRank(Integer.parseInt(tbSubrank.getText()));

    // label
    if (ClientUtils.getFlag(TemplateGroup.FLAG_SHOW_LABEL, flags)) {
      templateGroup.setLabelTop(Integer.parseInt(tbLabelTop.getText()));
      templateGroup.setLabelLeft(Integer.parseInt(tbLabelLeft.getText()));
      templateGroup.setLabelWidth(Integer.parseInt(tbLabelWidth.getText()));
      templateGroup.setLabelWidthUnit(tbLabelWidthUnit.getText());
      templateGroup.setLabelAlign(tbLabelAlign.getText());
    }
    
  }
  
  private void fillTemplates (Collection<ApplicationTemplate> templates) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (ApplicationTemplate appt : templates) {
      items.add(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
    }
    getDdbTemplate().setItems(items);
  }
}
