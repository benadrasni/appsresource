package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.TemplateRelation;

/**
 * A widget to display object type in table row.
 */
public class TemplateRelationRowView extends FlexTable implements
    DesignerModel.TemplateRelationUpdateObserver {

  private DesignerView designerView;
  private TemplateRelation templateRelation;

  /**
   * @param designerView     the top level view
   * @param templateRelation the template relation
   */
  public TemplateRelationRowView(final DesignerView designerView, final TemplateRelation templateRelation) {
    this.designerView = designerView;
    this.templateRelation = templateRelation;

    setStyleName("content-row");

    generateWidget(templateRelation);

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getTemplateRelationDialog().setItem(templateRelation);
        designerView.getTemplateRelationDialog().show();
      }
    });

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }
    }, MouseDownEvent.getType());
  }

  @Override
  public void onTemplateRelationCreated(TemplateRelation templateRelation) {
  }

  @Override
  public void onTemplateRelationUpdated(TemplateRelation templateRelation) {
    if (this.templateRelation.getId() == templateRelation.getId())
      generateWidget(templateRelation);
  }

  @Override
  public void onLoad() {
    designerView.getDesignerModel().addTemplateRelationUpdateObserver(this);
  }

  @Override
  public void onUnload() {
    designerView.getDesignerModel().removeTemplateRelationUpdateObserver(this);
  }

  private void generateWidget(TemplateRelation tr) {
    Label lblCode = new Label(tr.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(tr.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(tr.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblT1 = new Label(tr.getT1().getName());
    setWidget(0, 3, lblT1);
    Label lblOr = new Label(tr.getOr().getName());
    setWidget(0, 4, lblOr);
    Label lblT2 = new Label(tr.getT2().getName());
    setWidget(0, 5, lblT2);
    Label lblRank = new Label("" + tr.getRank());
    setWidget(0, 6, lblRank);
    Label lblSubrank = new Label("" + tr.getSubrank());
    setWidget(0, 7, lblSubrank);
  }
}
