package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.TemplateAttribute;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display object type in table row.
 *
 */
public class TemplateAttributeRowView extends FlexTable implements DesignerModel.TemplateAttributeUpdateObserver {

  private DesignerView designerView;
  private TemplateAttribute templateAttribute;
  
  /**
   * @param templateAttribute   the template attribute
   * @param style               css style
   */
  public TemplateAttributeRowView(final TemplateAttribute templateAttribute, String style) {
    setStyleName(style);
    this.templateAttribute = templateAttribute;
    getElement().setId(""+ this.templateAttribute.getId());
  }

  /**
   * @param designerView        the top level view
   * @param templateAttribute   the template attribute
   * @param style               css style
   */
  public TemplateAttributeRowView(final DesignerView designerView, final TemplateAttribute templateAttribute,
                                  final String style) {
    this(templateAttribute, style);
    this.designerView = designerView;
    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {

        designerView.getTemplateAttributeDialog().setItem(templateAttribute);
        designerView.getTemplateAttributeDialog().show();
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  public void generateWidgetTree() {
    Label lblName = new Label(getTemplateAttribute().getName() + 
        " (" + getTemplateAttribute().getCode() + ")");
    setWidget(0, 0, lblName);
  }

  public void generateWidgetFull() {
    Label lblCode = new Label(getTemplateAttribute().getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(getTemplateAttribute().getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(getTemplateAttribute().getDesc());
    setWidget(0, 2, lblDesc);
    Label lblTg = new Label(getTemplateAttribute().getTg().getName() 
        + " (" + getTemplateAttribute().getTg().getT().getName() + ")");
    setWidget(0, 3, lblTg);
    Label lblOa = new Label(getTemplateAttribute().getOa() != null ? 
        getTemplateAttribute().getOa().getName() : "");
    setWidget(0, 4, lblOa);
    Label lblStyle;
    switch (getTemplateAttribute().getStyle()) {
      case TemplateAttribute.STYLE_LABEL:
        lblStyle = new Label(Main.constants.templateAttributeStyle1());
        break;
      case TemplateAttribute.STYLE_TEXTBOX:
        lblStyle = new Label(Main.constants.templateAttributeStyle2());
        break;
      case TemplateAttribute.STYLE_DATEPICKER:
        lblStyle = new Label(Main.constants.templateAttributeStyle3());
        break;
      case TemplateAttribute.STYLE_DYNAMICCOMBO:
        lblStyle = new Label(Main.constants.templateAttributeStyle4());
        break;
      case TemplateAttribute.STYLE_TABLE:
        lblStyle = new Label(Main.constants.templateAttributeStyle5());
        break;
      case TemplateAttribute.STYLE_IMAGE:
        lblStyle = new Label(Main.constants.templateAttributeStyle6());
        break;
      case TemplateAttribute.STYLE_TEXTAREA:
        lblStyle = new Label(Main.constants.templateAttributeStyle7());
        break;
      case TemplateAttribute.STYLE_LINK:
        lblStyle = new Label(Main.constants.templateAttributeStyle8());
        break;
      default:
        lblStyle = new Label("");
        break;
    }
    setWidget(0, 5, lblStyle);
    Label lblDefault = new Label(getTemplateAttribute().getDef());
    setWidget(0, 6, lblDefault);
    Label lblLength = new Label(""+getTemplateAttribute().getLength());
    setWidget(0, 7, lblLength);
  }

  public void generateWidgetNormal() {
    Label lblCode = new Label(getTemplateAttribute().getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(getTemplateAttribute().getName());
    setWidget(0, 1, lblName);
    Label lblTg = new Label(getTemplateAttribute().getTg().getName());
    setWidget(0, 2, lblTg);
    Label lblOa = new Label(getTemplateAttribute().getOa().getName());
    setWidget(0, 3, lblOa);
    Label lblStyle;
    switch (getTemplateAttribute().getStyle()) {
    case TemplateAttribute.STYLE_LABEL:
      lblStyle = new Label(Main.constants.templateAttributeStyle1());
      break;
    case TemplateAttribute.STYLE_TEXTBOX:
      lblStyle = new Label(Main.constants.templateAttributeStyle2());
      break;
    case TemplateAttribute.STYLE_DATEPICKER:
      lblStyle = new Label(Main.constants.templateAttributeStyle3());
      break;

    default:
      lblStyle = new Label("");
      break;
    }
    setWidget(0, 4, lblStyle);
  }

  @Override
  public void onTemplateAttributeCreated(TemplateAttribute templateAttribute) {
  }

  @Override
  public void onTemplateAttributeUpdated(TemplateAttribute templateAttribute) {
    if (this.templateAttribute.getId() == templateAttribute.getId())
      generateWidgetFull();    
  }

  @Override
  public void onLoad() {
    if (designerView != null) {
      designerView.getDesignerModel().addTemplateAttributeUpdateObserver(this);
    }
  }

  @Override
  public void onUnload() {
    if (designerView != null) {
      designerView.getDesignerModel().removeTemplateAttributeUpdateObserver(this);
    }
  }

  // getters and setters
  
  /**
   * @return the templateAttribute
   */
  public TemplateAttribute getTemplateAttribute() {
    return templateAttribute;
  }
}
