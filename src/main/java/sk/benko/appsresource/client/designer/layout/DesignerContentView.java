package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.designer.Sections;
import sk.benko.appsresource.client.layout.TableDispatcher;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class DesignerContentView extends FlowPanel implements ChangeHandler {

  private DesignerModel model;
  private DesignerToolbarView toolbar;
  private TableDispatcher table;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public DesignerContentView(DesignerModel model) {
    getElement().setId(CSSConstants.CSS_CONTENT);
    setModel(model);
    this.addDomHandler(this, ChangeEvent.getType());
  }
  
  public void initialize(Sections section) {
    clear();
    getToolbar().initialize(section);
    add(getToolbar());
    getTable().initialize(section);
    add(getTable());
  }

  @Override
  public void onChange(ChangeEvent event) {
    getTable().filter(getToolbar());
  }

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
   * Getter for property 'toolbar'.
   * 
   * @return Value for property 'toolbar'.
   */
  protected DesignerToolbarView getToolbar() {
    if (toolbar == null) {
      toolbar = new DesignerToolbarView(getModel());
    }
    return toolbar;
  }

  /**
   * Getter for property 'table'.
   * 
   * @return Value for property 'table'.
   */
  protected TableDispatcher getTable() {
    if (table == null) {
      table = new TableDispatcher(getModel());
    }
    return table;
  }

}
