package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.TreeLevel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * A widget to display object in table row.
 *
 */
public class TreeLevelRowView extends TreeItem {
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TreeLevelRowView(final TreeLevel item) {

    //this.getElement().setId(""+item.getOaId());
    this.setStyleName("object-tree-row");
    FlexTable treeItemTable = new FlexTable();
    
    Label leaf = new Label();
    if (item.getValueString() != null)
      leaf.setText(item.getValueString());
    else if (item.getValueDate() != null)
      leaf.setText(""+item.getValueDate());
    else if (item.getValueDouble() != null)
      leaf.setText(""+item.getValueDouble());
    else {
      leaf.setText(Main.constants.noDefined());
      leaf.setStyleName(ClientUtils.CSS_ITALIC);
    }
    treeItemTable.setWidget(0, 0, leaf);
    treeItemTable.setStyleName("object-row");
    
    leaf.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        addStyleName("navigation-item-hover");
      }}, MouseOverEvent.getType()); 
    leaf.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        removeStyleName("navigation-item-hover");
      }}, MouseOutEvent.getType());
    
    leaf.addClickHandler(new ClickHandler() {
          
      @Override
      public void onClick(ClickEvent event) {
        TreeLevelRowView.this.setState(!TreeLevelRowView.this.getState());
      }
    });
    
    setWidget(treeItemTable);
  }
}
