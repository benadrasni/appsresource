package sk.benko.appsresource.client.designer;

import java.util.ArrayList;
import java.util.Collection;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;
import sk.benko.appsresource.client.ui.widget.IntegerTextBox;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 *
 */
public class TemplateRelationDialog extends DesignerDialog implements
    DesignerModel.ObjectRelationObserver {
  
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
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateRelationDialog(final DesignerModel model, TemplateRelation tr) {
    super(model, tr);
    getModel().addObjectRelationObserver(this);

    getHeader().add(new Label((getTemplateRelation() == null ? Main.constants.newItem() + " " 
        : "") + Main.constants.templateRelation()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.templateRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);

    getBodyLeft().add(menu1);
    getBodyRight().add(getWidgetTemplateRelation());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getTemplateRelation() == null)
                setItem(new TemplateRelation(getTbName().getText(),
                    getDdbTemplate1().getSelection().getId(),
                    getDdbTemplate2().getSelection().getId(),
                    getDdbOr().getSelection().getId()));
              fill(getTemplateRelation());
              model.createOrUpdateTemplateRelation(getTemplateRelation());
              TemplateRelationDialog.this.hide();
            }
        }, ClickEvent.getType());
  }

  @Override
  public void close() {
    getModel().removeObjectRelationObserver(this);
    hide();
  }

  @Override
  public void onObjectRelationCreated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationUpdated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationsLoaded(int otId,
      Collection<ObjectRelation> objectRelations) {
    fillObjectRelations(objectRelations);
  }
  
  /**
   * @return the templateRelation
   */
  public TemplateRelation getTemplateRelation() {
    return (TemplateRelation)getItem();
  }
  
  /**
   * Getter for property 'ddbTemplate1'.
   * 
   * @return Value for property 'ddbTemplate1'.
   */
  protected DropDownBox getDdbTemplate1() {
    if (ddbTemplate1 == null) {
      ddbTemplate1 = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          loadRelations();
        }
  
      });
      if (getTemplateRelation() != null && getTemplateRelation().getT1() != null)   
        ddbTemplate1.setSelection(new DropDownObjectImpl(getTemplateRelation().getT1Id(), 
            getTemplateRelation().getT1().getName(), getTemplateRelation().getT1()));
      else
        ddbTemplate1.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseTemplate()));
      
      assert(getModel().getApplication() != null);
      assert(getModel().getAppTemplateByApp(getModel().getApplication().getId()) != null);
      fillTemplates(ddbTemplate1, getModel().getAppTemplateByApp(getModel().getApplication().getId()));
    }
    return ddbTemplate1;
  }    
  
  /**
   * Getter for property 'ddbTemplate2'.
   * 
   * @return Value for property 'ddbTemplate2'.
   */
  protected DropDownBox getDdbTemplate2() {
    if (ddbTemplate2 == null) {
      ddbTemplate2 = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          loadRelations();
        }
  
      });
      if (getTemplateRelation() != null && getTemplateRelation().getT2() != null) {  
        ddbTemplate2.setSelection(new DropDownObjectImpl(getTemplateRelation().getT2Id(), 
            getTemplateRelation().getT2().getName(), getTemplateRelation().getT2()));
        loadRelations();
      } else
        ddbTemplate2.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseTemplate()));
      
      assert(getModel().getApplication() != null);
      assert(getModel().getAppTemplateByApp(getModel().getApplication().getId()) != null);
      fillTemplates(ddbTemplate2, getModel().getAppTemplateByApp(getModel().getApplication().getId()));
    }
    return ddbTemplate2;
  }   
  
  /**
   * Getter for property 'ddbOr'.
   * 
   * @return Value for property 'ddbOr'.
   */
  protected DropDownBox getDdbOr() {
    if (ddbOr == null) {
      ddbOr = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      if (getTemplateRelation() != null && getTemplateRelation().getOr() != null)  
        ddbOr.setSelection(new DropDownObjectImpl(getTemplateRelation().getOrId(), 
            getTemplateRelation().getOr().getName(), getTemplateRelation().getOr()));
      else
        ddbOr.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectRelation()));
    }
      
    return ddbOr;
  }
  
  /**
   * Getter for property 'cbVisible'.
   * 
   * @return Value for property 'cbVisible'.
   */
  protected CheckBox getCbVisible() {
    if (cbVisible == null) {
      cbVisible = new CheckBox(Main.constants.templateRelationVisible());
      if (getTemplateRelation() != null) cbVisible.setValue(ClientUtils
          .getFlag(TemplateRelation.FLAG_VISIBLE, getTemplateRelation().getFlags()));
      else
        cbVisible.setValue(true);
    }
      
    return cbVisible;
  }
  
  /**
   * Getter for property 'lblRank'.
   * 
   * @return Value for property 'lblRank'.
   */
  protected Label getLblRank() {
    if (lblRank == null) {
      lblRank = new Label(Main.constants.templateRelationRank());
    }
    return lblRank;
  }
  
  /**
   * Getter for property 'tbRank'.
   * 
   * @return Value for property 'tbRank'.
   */
  protected IntegerTextBox getTbRank() {
    if (tbRank == null) {
      tbRank = new IntegerTextBox(getLblRank(), getBOk());
      if (getTemplateRelation() != null) 
        tbRank.setText(""+getTemplateRelation().getRank());
      else 
        tbRank.setText(""+DEFAULT_RANK);
    }
    return tbRank;
  }
  
  /**
   * Getter for property 'lblSubrank'.
   * 
   * @return Value for property 'lblSubrank'.
   */
  protected Label getLblSubrank() {
    if (lblSubrank == null) {
      lblSubrank = new Label(Main.constants.templateRelationSubrank());
    }
    return lblSubrank;
  }

  /**
   * Getter for property 'tbSubrank'.
   * 
   * @return Value for property 'tbSubrank'.
   */
  protected IntegerTextBox getTbSubrank() {
    if (tbSubrank == null) {
      tbSubrank = new IntegerTextBox(getLblSubrank(), getBOk());
      if (getTemplateRelation() != null) 
        tbSubrank.setText(""+getTemplateRelation().getSubrank());
      else 
        tbSubrank.setText(""+DEFAULT_SUBRANK);
    }
    return tbSubrank;
  }

  /**
   * Getter for property 'widgetTemplateRelation'.
   * 
   * @return Value for property 'widgetTemplateRelation'.
   */
  public FlexTable getWidgetTemplateRelation() {
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
      widgetTemplateRelation.setWidget(3, 1, getDdbTemplate1());
      widgetTemplateRelation.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblT2 = new Label(Main.constants.templateRelationT2());
      widgetTemplateRelation.setWidget(4, 0, lblT2);
      widgetTemplateRelation.setWidget(4, 1, getDdbTemplate2());
      widgetTemplateRelation.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblOr = new Label(Main.constants.templateRelationOr());
      widgetTemplateRelation.setWidget(5, 0, lblOr);
      widgetTemplateRelation.setWidget(5, 1, getDdbOr());
      widgetTemplateRelation.getFlexCellFormatter().addStyleName(5, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetTemplateRelation.setWidget(6, 1, getCbVisible());

      widgetTemplateRelation.setWidget(7, 0, getLblRank());
      widgetTemplateRelation.setWidget(7, 1, getTbRank());

      widgetTemplateRelation.setWidget(8, 0, getLblSubrank());
      widgetTemplateRelation.setWidget(8, 1, getTbSubrank());
    }
    
    return widgetTemplateRelation;
  }
  
  // private methods
  
  private void fill(TemplateRelation templateRelation) {
    super.fill(templateRelation);

    templateRelation.setT1Id(getDdbTemplate1().getSelection().getId());
    templateRelation.setT1((Template)getDdbTemplate1().getSelection().getUserObject());
    templateRelation.setT2Id(getDdbTemplate2().getSelection().getId());
    templateRelation.setT2((Template)getDdbTemplate2().getSelection().getUserObject());
    templateRelation.setOrId(getDdbOr().getSelection().getId());
    templateRelation.setOr((ObjectRelation)getDdbOr().getSelection().getUserObject());
    
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
  
  private void fillTemplates(DropDownBox ddb, Collection<ApplicationTemplate> templates) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (ApplicationTemplate appt : templates) {
      items.add(new DropDownObjectImpl(appt.getTId(), appt.getT().getName(), appt.getT()));
    }
    ddb.setItems(items);
  }
  
  private void fillObjectRelations(Collection<ObjectRelation> objectRelations) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    getDdbOr().setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    for (ObjectRelation or : objectRelations) {
      if (or.getOt1Id() == ((Template)getDdbTemplate1().getSelection().getUserObject()).getOtId()) {
        items.add(new DropDownObjectImpl(or.getId(), or.getName(), or));
        if (getTemplateRelation() != null && getTemplateRelation().getOrId() == or.getId())
          getDdbOr().setSelection(items.get(items.size()-1));
      }
    }
    getDdbOr().setItems(items);
  }
  
  private void loadRelations() {
    if (getDdbTemplate1().getSelection().getId() > 0 
        && getDdbTemplate2().getSelection().getId() > 0) {
      Collection<ObjectRelation> ors = getModel().getObjectRelations()
          .get(((Template)getDdbTemplate2().getSelection().getUserObject()).getOtId());
      if (ors == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(), 
            ((Template)getDdbTemplate2().getSelection().getUserObject()).getOtId());
        orl.start();    
      } else
        fillObjectRelations(ors);
    }
  }
}
