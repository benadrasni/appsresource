package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.TemplateGroup;

import java.util.Collection;

/**
 * A widget to display object type in table row.
 */
public class TemplateGroupRowView extends FlexTable implements
    DesignerModel.TemplateGroupObserver {

  private DesignerView designerView;
  private TemplateGroup templateGroup;

  /**
   * @param designerView  the top level view
   * @param templateGroup the template group
   */
  public TemplateGroupRowView(final DesignerView designerView, final TemplateGroup templateGroup) {
    this.designerView = designerView;
    this.templateGroup = templateGroup;

    setStyleName("content-row");

    generateWidget(templateGroup);

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {

        designerView.getTemplateGroupDialog().setItem(templateGroup);
        designerView.getTemplateGroupDialog().show();
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
  public void onTemplateGroupCreated(TemplateGroup templateGroup) {
  }

  @Override
  public void onTemplateGroupUpdated(TemplateGroup templateGroup) {
    if (this.templateGroup.getId() == templateGroup.getId())
      generateWidget(templateGroup);
  }

  @Override
  public void onTemplateGroupsLoaded(Collection<TemplateGroup> templateGroups) {
  }

  @Override
  public void onLoad() {
    designerView.getDesignerModel().addTemplateGroupObserver(this);
  }

  @Override
  public void onUnload() {
    designerView.getDesignerModel().removeTemplateGroupObserver(this);
  }

  private void generateWidget(TemplateGroup tg) {
    Label lblCode = new Label(tg.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(tg.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(tg.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblT = new Label(tg.getT().getName());
    setWidget(0, 3, lblT);
    Label lblRank = new Label("" + tg.getRank());
    setWidget(0, 4, lblRank);
    Label lblSubrank = new Label("" + tg.getSubRank());
    setWidget(0, 5, lblSubrank);
  }
}
