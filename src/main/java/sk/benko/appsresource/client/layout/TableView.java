package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display .
 *
 */
public abstract class TableView extends FlowPanel {

  private DesignerModel model;
  private FlexTable header;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TableView(final DesignerModel model) {
    setModel(model);
    setStyleName("content-table");
    filter();
  }
  
  public abstract void initializeHeader();

  public void filter() {}
  
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
   * Getter for property 'header'.
   * 
   * @return Value for property 'header'.
   */
  protected FlexTable getHeader() {
    if (header == null) {
      header = new FlexTable();
      header.setStyleName("content");
      header.addStyleName("content-header");
      
      initializeHeader();
    }
    return header;
  }
}
