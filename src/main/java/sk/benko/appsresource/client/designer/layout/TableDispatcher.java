package sk.benko.appsresource.client.designer.layout;

import java.util.HashMap;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.designer.ApplicationTableView;
import sk.benko.appsresource.client.designer.ObjectAttributeTableView;
import sk.benko.appsresource.client.designer.ObjectRelationTableView;
import sk.benko.appsresource.client.designer.ObjectTypeTableView;
import sk.benko.appsresource.client.designer.Sections;
import sk.benko.appsresource.client.designer.TemplateAttributeTableView;
import sk.benko.appsresource.client.designer.TemplateGroupTableView;
import sk.benko.appsresource.client.designer.TemplateRelationTableView;
import sk.benko.appsresource.client.designer.TemplateTableView;
import sk.benko.appsresource.client.designer.UnitTableView;
import sk.benko.appsresource.client.designer.ValueTypeTableView;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * A widget to display .
 *
 */
public class TableDispatcher extends ScrollPanel {

  private DesignerView designerView;
  private HashMap<Sections, TableView> views;
  private TableView actualView;
  
  /**
   * @param designerView the top level view
   */
  public TableDispatcher(final DesignerView designerView) {
    this.designerView = designerView;

    getElement().setId(CSSConstants.CSS_CONTENT_TEMPLATE);
    setStyleName("content");
    setHeight(Window.getClientHeight()-150+"px");
  }
  
  public void initialize(Sections section) {
    clear();
    add(getTableView(section));
  }
  
  public void filter(DesignerToolbarView toolbar) {
    getActualView().filter();
  }
  
  // getters and setters
  
  /**
   * @return the model
   */
  public DesignerModel getModel() {
    return designerView.getDesignerModel();
  }

  /**
   * Getter for property 'views'.
   * 
   * @return Value for property 'views'.
   */
  public HashMap<Sections, TableView> getViews() {
    if (views == null)
      views = new HashMap<Sections, TableView>();
    return views;
  }

  /**
   * @return the actualView
   */
  public TableView getActualView() {
    return actualView;
  }

  /**
   * @param actualView the actualView to set
   */
  public void setActualView(TableView actualView) {
    this.actualView = actualView;
  }

  // private methods
  
  /**
   * Get proper TableView by section
   * 
   * @return actualView
   */
  private TableView getTableView(Sections section) {
    setActualView(getViews().get(section));
    if (getActualView() == null) {
      switch (section) {
      case APPLICATION:
        setActualView(new ApplicationTableView(designerView));
        break;
      case TEMPLATE:
        setActualView(new TemplateTableView(designerView));
        break;
      case TEMPLATEGROUP:
        setActualView(new TemplateGroupTableView(designerView));
        break;
      case TEMPLATEATTRIBUTE:
        setActualView(new TemplateAttributeTableView(designerView));
        break;
      case TEMPLATERELATION:
        setActualView(new TemplateRelationTableView(designerView));
        break;
      case OBJECTTYPE:
        setActualView(new ObjectTypeTableView(designerView));
        break;
      case OBJECTATTRIBUTE:
        setActualView(new ObjectAttributeTableView(designerView));
        break;
      case OBJECTRELATION:
        setActualView(new ObjectRelationTableView(designerView));
        break;
      case VALUETYPE:
        setActualView(new ValueTypeTableView(designerView));
        break;
      case UNIT:
        setActualView(new UnitTableView(designerView));
        break;
      case NONE:
        break;

      default:
        break;
      }
      getViews().put(section, getActualView());
    }
    return getActualView();
  }
}
