package sk.benko.appsresource.client.designer.layout;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import sk.benko.appsresource.client.model.DesignerModel;

/**
 * Parent of all tables which displays design items.
 *
 */
public abstract class TableView extends FlowPanel {

  private DesignerView designerView;
  private FlexTable header;

  /**
   * @param designerView the top level view
   */
  public TableView(final DesignerView designerView) {
    this.designerView = designerView;

    setStyleName("content-table");
    filter();
  }

  /**
   * Initialize column header.
   */
  public abstract void initializeHeader();

  /**
   * Empty filter
   */
  public void filter() {}
  
  // getters and setters
  
  /**
   * @return the designerView
   */
  public DesignerView getDesignerView() {
    return designerView;
  }

  /**
   * @return the model
   */
  public DesignerModel getModel() {
    return designerView.getDesignerModel();
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
