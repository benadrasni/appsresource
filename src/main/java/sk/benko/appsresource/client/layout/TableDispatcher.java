package sk.benko.appsresource.client.layout;

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
import sk.benko.appsresource.client.designer.layout.DesignerToolbarView;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * A widget to display .
 *
 */
public class TableDispatcher extends ScrollPanel {

  private DesignerModel model;
  private HashMap<Sections, TableView> views;
  private TableView actualView;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TableDispatcher(final DesignerModel model) {
    getElement().setId(CSSConstants.CSS_CONTENT_TEMPLATE);
    setModel(model);
    
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
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(DesignerModel model) {
    this.model = model;
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
        setActualView(new ApplicationTableView(getModel()));
        break;
      case TEMPLATE:
        setActualView(new TemplateTableView(getModel()));
        break;
      case TEMPLATEGROUP:
        setActualView(new TemplateGroupTableView(getModel()));
        break;
      case TEMPLATEATTRIBUTE:
        setActualView(new TemplateAttributeTableView(getModel()));
        break;
      case TEMPLATERELATION:
        setActualView(new TemplateRelationTableView(getModel()));
        break;
      case OBJECTTYPE:
        setActualView(new ObjectTypeTableView(getModel()));
        break;
      case OBJECTATTRIBUTE:
        setActualView(new ObjectAttributeTableView(getModel()));
        break;
      case OBJECTRELATION:
        setActualView(new ObjectRelationTableView(getModel()));
        break;
      case VALUETYPE:
        setActualView(new ValueTypeTableView(getModel()));
        break;
      case UNIT:
        setActualView(new UnitTableView(getModel()));
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
