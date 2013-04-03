package sk.benko.appsresource.client.designer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabel;
import sk.benko.appsresource.client.model.DesignItem;
import sk.benko.appsresource.client.model.DesignerModel;

/**
 * A generic widget to display designer dialog.
 */
public abstract class DesignerDialog extends CommonDialog {

  protected static String TBWIDTH_SMALL = "50px";
  protected static String TBWIDTH = "200px";
  private DesignerView designerView;
  private DesignItem item;
  private String headerText;
  private Tree menu;
  private NavigationLabel menu1;
  private Label lblHeader;
  private Label lblCode;
  private Label lblCodeValue;
  private Label lblName;
  private TextBox tbName;
  private Label lblDesc;
  private TextBox tbDesc;

  /**
   * @param designerView the top level view
   */
  public DesignerDialog(final DesignerView designerView) {
    this.designerView = designerView;

    lblHeader = new Label();
    getHeader().add(lblHeader);
    lblCode = new Label(Main.constants.templateAttributeCode());
    lblCodeValue = new Label();
    lblCodeValue.setStyleName(ClientUtils.CSS_DIALOG_LABEL_DISABLED);
    lblName = new Label(Main.constants.templateAttributeName());
    tbName = new TextBox();
    tbName.setWidth(TBWIDTH);
    lblDesc = new Label(Main.constants.templateAttributeDesc());
    tbDesc = new TextBox();
    tbDesc.setWidth(TBWIDTH);

    menu = new Tree((Tree.Resources) GWT.create(TreeResource.class), false);
    menu1 = new NavigationLabel(designerView, new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyDialogNavigationItemClicked(event.getRelativeElement());
        getBodyRight().clear();
        getBodyRight().add(getItemWidget());
      }
    });
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " "
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);

    getBodyLeft().add(menu);


  }

  protected abstract FlexTable getItemWidget();

  /**
   * @return the designer view
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

  // getters and setters

  /**
   * @return the menu
   */
  public Tree getMenu() {
    return menu;
  }


  /**
   * @return the first menu item
   */
  public NavigationLabel getMenu1() {
    return menu1;
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
    if (item == null) {
      getBOk().getElement().setInnerText(Main.constants.create());
      lblCodeValue.setText(Main.constants.toBeComputed());
      lblHeader.setText(Main.constants.newItem() + " " + headerText);
      reset();
    } else {
      getBOk().getElement().setInnerText(Main.constants.save());
      lblCodeValue.setText(item.getCode());
      lblHeader.setText(headerText);
      load(item);
    }
    getBodyRight().clear();
    getBodyRight().add(getItemWidget());
  }

  /**
   * @param headerText the text to set
   */
  public void setHeaderText(String headerText) {
    this.headerText = headerText;
    menu1.setText(headerText);
  }

  /**
   * @return Value for property 'lblCode'.
   */
  public Label getLblCode() {
    return lblCode;
  }

  /**
   * @return Value for property 'lblCodeValue'.
   */
  public Label getLblCodeValue() {
    return lblCodeValue;
  }

  /**
   * @return Value for property 'lblName'.
   */
  public Label getLblName() {
    return lblName;
  }

  /**
   * Getter for property 'tbName'.
   *
   * @return Value for property 'tbName'.
   */
  public TextBox getTbName() {
    return tbName;
  }

  /**
   * @return Value for property 'lblDesc'.
   */
  public Label getLblDesc() {
    return lblDesc;
  }

  /**
   * Getter for property 'tbDesc'.
   *
   * @return Value for property 'tbDesc'.
   */
  public TextBox getTbDesc() {
    return tbDesc;
  }

  protected void fill(DesignItem item) {
    item.setName(tbName.getText().trim());
    item.setDesc(tbDesc.getText().trim().length() > 0 ? getTbDesc().getText().trim() : null);
  }

  protected void load(DesignItem item) {
    tbName.setText(item.getName() == null ? "" : item.getName());
    tbDesc.setText(item.getDesc() == null ? "" : item.getDesc());

    getMenu().removeItems();
    getMenu().addItem(menu1);
  }

  protected void reset() {
    tbName.setText("");
    tbDesc.setText("");

    getMenu().removeItems();
    getMenu().addItem(menu1);
  }
}
