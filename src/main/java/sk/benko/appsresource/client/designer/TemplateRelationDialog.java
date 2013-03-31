package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;
import sk.benko.appsresource.client.ui.widget.IntegerTextBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class TemplateRelationDialog extends DesignerDialog implements DesignerModel.ObjectRelationObserver,
    Model.ApplicationObserver {

  private int DEFAULT_RANK = 0;
  private int DEFAULT_SUBRANK = 0;
  private DropDownBox ddbTemplate1;
  private DropDownBox ddbTemplate2;
  private DropDownBox ddbOr;
  private CheckBox cbVisible;
  private Label lblRank;
  private Label lblSubrank;
  private IntegerTextBox tbRank;
  private IntegerTextBox tbSubrank;
  private FlexTable widgetTemplateRelation;

  /**
   * @param designerView the top level view
   */
  public TemplateRelationDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.templateRelation());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getTemplateRelation() == null)
              setItem(new TemplateRelation(getTbName().getText(),
                  ddbTemplate1.getSelection().getId(),
                  ddbTemplate2.getSelection().getId(),
                  ddbOr.getSelection().getId()));
            fill(getTemplateRelation());
            designerView.getDesignerModel().createOrUpdateTemplateRelation(getTemplateRelation());
            TemplateRelationDialog.this.hide();
          }
        }, ClickEvent.getType());

    ddbTemplate1 = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        loadRelations();
      }

    });
    ddbTemplate2 = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        loadRelations();
      }

    });
    ddbOr = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    cbVisible = new CheckBox(Main.constants.templateRelationVisible());
    lblRank = new Label(Main.constants.templateRelationRank());
    tbRank = new IntegerTextBox(lblRank, getBOk());
    lblSubrank = new Label(Main.constants.templateRelationSubrank());
    tbSubrank = new IntegerTextBox(lblSubrank, getBOk());

    assert (getModel().getApplication() != null);
    List<ApplicationTemplate> appts = getModel().getAppTemplateByApp(getModel().getApplication().getId());
    assert (appts != null);
    onApplicationTemplatesLoaded(appts);

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
  }

  @Override
  public void onObjectRelationCreated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationUpdated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationsLoaded(int otId, Collection<ObjectRelation> objectRelations) {
    fillObjectRelations(objectRelations);
  }

  @Override
  public void onApplicationTemplatesLoaded(List<ApplicationTemplate> appts) {
    fillTemplates(ddbTemplate1, appts);
    fillTemplates(ddbTemplate2, appts);

    loadRelations();
  }

  /**
   * @return the templateRelation
   */
  public TemplateRelation getTemplateRelation() {
    return (TemplateRelation) getItem();
  }

  /**
   * Getter for property 'widgetTemplateRelation'.
   *
   * @return Value for property 'widgetTemplateRelation'.
   */
  @Override
  public FlexTable getItemWidget() {
    if (widgetTemplateRelation == null) {
      widgetTemplateRelation = new FlexTable();
      Label lblCode = new Label(Main.constants.templateRelationCode());
      widgetTemplateRelation.setWidget(0, 0, lblCode);
      widgetTemplateRelation.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.templateRelationName());
      widgetTemplateRelation.setWidget(1, 0, lblName);
      widgetTemplateRelation.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.objectTypeDesc());
      widgetTemplateRelation.setWidget(2, 0, lblDesc);
      widgetTemplateRelation.setWidget(2, 1, getTbDesc());

      Label lblT1 = new Label(Main.constants.templateRelationT1());
      widgetTemplateRelation.setWidget(3, 0, lblT1);
      widgetTemplateRelation.setWidget(3, 1, ddbTemplate1);
      widgetTemplateRelation.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblT2 = new Label(Main.constants.templateRelationT2());
      widgetTemplateRelation.setWidget(4, 0, lblT2);
      widgetTemplateRelation.setWidget(4, 1, ddbTemplate2);
      widgetTemplateRelation.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblOr = new Label(Main.constants.templateRelationOr());
      widgetTemplateRelation.setWidget(5, 0, lblOr);
      widgetTemplateRelation.setWidget(5, 1, ddbOr);
      widgetTemplateRelation.getFlexCellFormatter().addStyleName(5, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateRelation.setWidget(6, 1, cbVisible);

      widgetTemplateRelation.setWidget(7, 0, lblRank);
      widgetTemplateRelation.setWidget(7, 1, tbRank);

      widgetTemplateRelation.setWidget(8, 0, lblSubrank);
      widgetTemplateRelation.setWidget(8, 1, tbSubrank);
    }

    return widgetTemplateRelation;
  }

  /**
   * Fill {@link TemplateRelation} with values from UI. Parent method must be called first.
   *
   * @param templateRelation a template relation which should be filled with UI values
   */
  protected void fill(TemplateRelation templateRelation) {
    super.fill(templateRelation);

    templateRelation.setT1Id(ddbTemplate1.getSelection().getId());
    templateRelation.setT1((Template) ddbTemplate1.getSelection().getUserObject());
    templateRelation.setT2Id(ddbTemplate2.getSelection().getId());
    templateRelation.setT2((Template) ddbTemplate2.getSelection().getUserObject());
    templateRelation.setOrId(ddbOr.getSelection().getId());
    templateRelation.setOr((ObjectRelation) ddbOr.getSelection().getUserObject());

    // flags
    int flags = 0;
    if (cbVisible.getValue())
      flags = ClientUtils.setFlag(TemplateRelation.FLAG_VISIBLE, flags);
    else
      flags = ClientUtils.unsetFlag(TemplateRelation.FLAG_VISIBLE, flags);
    templateRelation.setFlags(flags);

    // rank
    templateRelation.setRank(Integer.parseInt(tbRank.getText()));
    templateRelation.setSubrank(Integer.parseInt(tbSubrank.getText()));
  }

  /**
   * Load {@link TemplateRelation} to UI. Parent method must be called first.
   *
   * @param item the template relation which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    super.load(item);

    TemplateRelation templateRelation = (TemplateRelation) item;
    if (templateRelation != null) {
      if (templateRelation.getT1() != null) {
        ddbTemplate1.setSelection(new DropDownObjectImpl(templateRelation.getT1Id(),
            templateRelation.getT1().getName(), templateRelation.getT1()));
      }
      if (templateRelation.getT2() != null) {
        ddbTemplate2.setSelection(new DropDownObjectImpl(templateRelation.getT2Id(),
            templateRelation.getT2().getName(), templateRelation.getT2()));
      }
      if (templateRelation.getOr() != null)
        ddbOr.setSelection(new DropDownObjectImpl(templateRelation.getOrId(),
            templateRelation.getOr().getName(), templateRelation.getOr()));

      cbVisible.setValue(ClientUtils.getFlag(TemplateRelation.FLAG_VISIBLE, templateRelation.getFlags()));
      tbRank.setText("" + templateRelation.getRank());
      tbSubrank.setText("" + templateRelation.getSubrank());

      loadRelations();
    }
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();
    ddbTemplate1.setSelection(new DropDownObjectImpl(0, Main.constants.chooseTemplate()));
    ddbTemplate2.setSelection(new DropDownObjectImpl(0, Main.constants.chooseTemplate()));
    ddbOr.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    cbVisible.setValue(true);
    tbRank.setText("" + DEFAULT_RANK);
    tbSubrank.setText("" + DEFAULT_SUBRANK);
  }

  // private methods

  private void fillTemplates(DropDownBox ddb, Collection<ApplicationTemplate> templates) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ApplicationTemplate appt : templates) {
      items.add(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
    }
    ddb.setItems(items);
  }

  private void fillObjectRelations(Collection<ObjectRelation> objectRelations) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    ddbOr.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    for (ObjectRelation or : objectRelations) {
      if (or.getOt1Id() == ((Template) ddbTemplate1.getSelection().getUserObject()).getOtId()) {
        items.add(new DropDownObjectImpl(or.getId(), or.getName(), or));
        if (getTemplateRelation() != null && getTemplateRelation().getOrId() == or.getId())
          ddbOr.setSelection(items.get(items.size() - 1));
      }
    }
    ddbOr.setItems(items);
  }

  private void loadRelations() {
    if (ddbTemplate1.getSelection().getId() > 0 && ddbTemplate2.getSelection().getId() > 0) {
      Collection<ObjectRelation> ors = getModel().getObjectRelations()
          .get(((Template) ddbTemplate2.getSelection().getUserObject()).getOtId());
      if (ors == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(),
            ((Template) ddbTemplate2.getSelection().getUserObject()).getOtId());
        orl.start();
      } else
        fillObjectRelations(ors);
    }
  }
}
