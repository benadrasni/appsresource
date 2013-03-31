package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.designer.Sections;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class DesignerContentView extends FlowPanel implements ChangeHandler {

  private DesignerToolbarView toolbar;
  private TableDispatcher table;
  
  /**
   * @param designerView the top level view
   */
  public DesignerContentView(final DesignerView designerView) {
    getElement().setId(CSSConstants.CSS_CONTENT);
    toolbar = new DesignerToolbarView(designerView);
    table = new TableDispatcher(designerView);
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
   * Getter for property 'toolbar'.
   * 
   * @return Value for property 'toolbar'.
   */
  protected DesignerToolbarView getToolbar() {
    return toolbar;
  }

  /**
   * Getter for property 'table'.
   * 
   * @return Value for property 'table'.
   */
  protected TableDispatcher getTable() {
    return table;
  }

}
