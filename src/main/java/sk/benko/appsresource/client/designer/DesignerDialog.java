package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignItem;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A generic widget to display designer dialog.
 *
 */
public abstract class DesignerDialog extends CommonDialog {

  protected static String TBWIDTH_SMALL = "50px";
  protected static String TBWIDTH = "200px";

  private DesignerModel model;
  private DesignItem item;

  private Label lblCodeValue;
  private TextBox tbName;
  private TextBox tbDesc;
  
  /**
   * 
   */
  public DesignerDialog(DesignerModel model, DesignItem item) {
    setModel(model);
    setItem(item);
    getBOk().getElement().setInnerText(getItem() == null ? 
        Main.constants.create() : Main.constants.save());
  }
  
  protected void fill(DesignItem item) {
    item.setName(getTbName().getText().trim());
    item.setDesc(getTbDesc().getText().trim().length() > 0 ? 
        getTbDesc().getText().trim() : null);
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
   * @return the item
   */
  public DesignItem getItem() {
    return item;
  }

  /**
   * @param item the item to set
   */
  public void setItem(DesignItem item) {
    this.item = item;
  }
  
  /**
   * Getter for property 'lblCode'.
   * 
   * @return Value for property 'lblCode'.
   */
  public Label getLblCodeValue() {
    if (lblCodeValue == null) {
      if (getItem() != null) lblCodeValue = new Label(getItem().getCode());
      else lblCodeValue = new Label(Main.constants.toBeComputed());
      lblCodeValue.setStyleName(ClientUtils.CSS_DIALOG_LABEL_DISABLED);
    }  
    return lblCodeValue;
  }  
  
  /**
   * Getter for property 'tbName'.
   * 
   * @return Value for property 'tbName'.
   */
  public TextBox getTbName() {
    if (tbName == null) {
      tbName = new TextBox();
      tbName.setWidth(TBWIDTH);

      if (getItem() != null) 
        getTbName().setText(getItem().getName());
    }  
    return tbName;
  }
  
  /**
   * Getter for property 'tbDesc'.
   * 
   * @return Value for property 'tbDesc'.
   */
  public TextBox getTbDesc() {
    if (tbDesc == null) {
      tbDesc = new TextBox();
      tbDesc.setWidth(TBWIDTH);
      if (getItem() != null) 
        tbDesc.setText(getItem().getDesc());
    }  
    return tbDesc;
  }

}
