package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Template;

import java.util.Collection;

/**
 * A widget to display object type in table row.
 */
public class TemplateRowView extends FlexTable implements DesignerModel.TemplateObserver {

  private DesignerView designerView;
  private Template template;

  /**
   * @param template     the template
   * @param style        css style
   */
  public TemplateRowView(final Template template, final String style) {
    this.template = template;
    setStyleName(style);
  }

  /**
   * @param designerView the top level view
   * @param template     the template
   * @param style        css style
   */
  public TemplateRowView(final DesignerView designerView, final Template template, final String style) {
    this(template, style);
    this.designerView = designerView;
    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Main.status.showTaskStatus(Main.constants.loading());
        designerView.getTemplateDialog().setItem(template);
        designerView.getTemplateDialog().show();
      }
    });
    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }
    }, MouseDownEvent.getType());
  }

  public void generateWidgetTree() {
    setWidget(0, 0, new Label(template.getName()));
    getFlexCellFormatter().setWidth(0, 0, "150px");
    if (template.getOt() != null)
      setWidget(0, 1, new Label("(" + template.getOt().getName() + ")"));
  }

  public void generateWidgetFull() {
    Label lblCode = new Label(template.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(template.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(template.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblOt = new Label(template.getOt() != null ? template.getOt().getName() : "");
    setWidget(0, 3, lblOt);
    Label lblOa = new Label(template.getOa() != null ? template.getOa().getName() : "");
    setWidget(0, 4, lblOa);
  }

  @Override
  public void onTemplateCreated(Template template) {
  }

  @Override
  public void onTemplateUpdated(Template template) {
    if (this.template.getId() == template.getId())
      generateWidgetFull();
  }

  @Override
  public void onTemplatesLoaded(Collection<Template> templates) {
  }

  @Override
  public void onLoad() {
    if (designerView != null)
      designerView.getDesignerModel().addTemplateObserver(this);
  }

  @Override
  public void onUnload() {
    if (designerView != null)
      designerView.getDesignerModel().removeTemplateObserver(this);
  }

  // getters and setters

  /**
   * @return the template
   */
  public Template getTemplate() {
    return template;
  }
}
