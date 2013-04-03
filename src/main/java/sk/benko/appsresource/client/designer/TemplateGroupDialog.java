package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.ui.widget.IntegerTextBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class TemplateGroupDialog extends DesignerDialog implements Model.ApplicationObserver {
  private final static int DEFAULT_RANK = 0;
  private final static int DEFAULT_SUBRANK = 0;
  private final static int DEFAULT_LABELTOP = 10;
  private final static int DEFAULT_LABELLEFT = 10;
  private final static int DEFAULT_LABELWIDTH = 50;
  private final static String DEFAULT_LABELWIDTHUNIT = "px";
  private final static String DEFAULT_LABELALIGN = "left";
  private CheckBox cbLabel;
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
  private DropDownBox ddbTemplate;
  private FlexTable widgetTemplateGroup;

  /**
   * @param designerView the top level view
   */
  public TemplateGroupDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.templateGroup());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getTemplateGroup() == null)
              setItem(new TemplateGroup(getTbName().getText(), ddbTemplate.getSelection().getId()));
            fill(getTemplateGroup());
            designerView.getDesignerModel().createOrUpdateTemplateGroup(getTemplateGroup());
            close();
          }
        }, ClickEvent.getType());

    ddbTemplate = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    cbLabel = new CheckBox(Main.constants.templateGroupLabel());

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
  }

  @Override
  public void onApplicationTemplatesLoaded(List<ApplicationTemplate> appts) {
    fillTemplates(appts);

    getModel().getStatusObserver().onTaskFinished();
  }

  /**
   * @return the templateGroup
   */
  public TemplateGroup getTemplateGroup() {
    return (TemplateGroup) getItem();
  }

  /**
   * Getter for property 'widgetTemplateGroup'.
   *
   * @return Value for property 'widgetTemplateGroup'.
   */
  @Override
  protected FlexTable getItemWidget() {
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
      widgetTemplateGroup.setWidget(3, 1, ddbTemplate);
      widgetTemplateGroup.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateGroup.setWidget(4, 1, cbLabel);

      widgetTemplateGroup.setWidget(5, 0, lblRank);
      widgetTemplateGroup.setWidget(5, 1, tbRank);

      widgetTemplateGroup.setWidget(6, 0, lblSubrank);
      widgetTemplateGroup.setWidget(6, 1, tbSubrank);

      widgetTemplateGroup.setWidget(7, 0, lblLabelTop);
      widgetTemplateGroup.setWidget(7, 1, tbLabelTop);

      widgetTemplateGroup.setWidget(8, 0, lblLabelLeft);
      widgetTemplateGroup.setWidget(8, 1, tbLabelLeft);

      widgetTemplateGroup.setWidget(9, 0, lblLabelWidth);
      widgetTemplateGroup.setWidget(9, 1, tbLabelWidth);

      Label lblLabelWidthUnit = new Label(Main.constants.templateGroupLabelWidthUnit());
      widgetTemplateGroup.setWidget(10, 0, lblLabelWidthUnit);
      widgetTemplateGroup.setWidget(10, 1, tbLabelWidthUnit);

      Label lblLabelAlign = new Label(Main.constants.templateGroupLabelAlign());
      widgetTemplateGroup.setWidget(11, 0, lblLabelAlign);
      widgetTemplateGroup.setWidget(11, 1, tbLabelAlign);
    }

    return widgetTemplateGroup;
  }

  /**
   * Fill {@link TemplateGroup} with values from UI. Parent method must be called first.
   *
   * @param templateGroup the template group which should be filled with UI values
   */
  private void fill(TemplateGroup templateGroup) {
    super.fill(templateGroup);

    templateGroup.setTId(ddbTemplate.getSelection().getId());
    templateGroup.setT((Template) ddbTemplate.getSelection().getUserObject());

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

  /**
   * Load {@link TemplateGroup} to UI. Parent method must be called first.
   *
   * @param item the template group which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    assert (item != null);
    super.load(item);

    TemplateGroup templateGroup = (TemplateGroup) item;
    ddbTemplate.setSelection(new DropDownObjectImpl(templateGroup.getTId(),
        templateGroup.getT().getName(), templateGroup.getT()));
    cbLabel.setValue(ClientUtils.getFlag(TemplateGroup.FLAG_SHOW_LABEL, getTemplateGroup().getFlags()));
    tbRank.setText("" + templateGroup.getRank());
    tbSubrank.setText("" + templateGroup.getSubRank());
    tbLabelTop.setText("" + templateGroup.getLabelTop());
    tbLabelLeft.setText("" + templateGroup.getLabelLeft());
    tbLabelWidth.setText("" + templateGroup.getLabelWidth());
    tbLabelWidthUnit.setText(templateGroup.getLabelWidthUnit());
    tbLabelAlign.setText(templateGroup.getLabelAlign());

    if (getModel().getAppTemplateByApp(getModel().getApplication().getId()) == null) {
      ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(),
          getModel().getApplication());
      atl.start();
    } else {
      fillTemplates(getModel().getAppTemplateByApp(getModel().getApplication().getId()));
    }
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();

    ddbTemplate.setSelection(new DropDownObjectImpl(0, Main.constants.chooseTemplate()));
    cbLabel.setValue(false);
    tbRank.setText("" + DEFAULT_RANK);
    tbSubrank.setText("" + DEFAULT_SUBRANK);
    tbLabelTop.setText("" + DEFAULT_LABELTOP);
    tbLabelLeft.setText("" + DEFAULT_LABELLEFT);
    tbLabelWidth.setText("" + DEFAULT_LABELWIDTH);
    tbLabelWidthUnit.setText(DEFAULT_LABELWIDTHUNIT);
    tbLabelAlign.setText(DEFAULT_LABELALIGN);

    getModel().getStatusObserver().onTaskFinished();
  }

  // private methods

  private void fillTemplates(Collection<ApplicationTemplate> templates) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ApplicationTemplate appt : templates) {
      items.add(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
    }
    ddbTemplate.setItems(items);
  }
}
