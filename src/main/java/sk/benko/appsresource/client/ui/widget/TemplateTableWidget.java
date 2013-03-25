package sk.benko.appsresource.client.ui.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.TemplateAttribute;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class TemplateTableWidget extends TemplateWidget {

  private FlexTable table;
  
  public TemplateTableWidget(ObjectTemplate objectTemplate, TemplateAttribute ta,
      int relId, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, 0, widgetToHighlight, isDisabled);
    
    setTable(getObjectTemplate().getTable(ta.getId()));

    getTable().setWidth(ta.getWidth()+ta.getWidthUnit());
    add(getTable());
  }

  @Override
  public boolean isEdited() {
    return false;
  }

  @Override
  public boolean isInvalid() {
    if (getModel().getObject() != null) {
      for (int i = 0; i < getTable().getRowCount(); i++) {
        for (int j = 0; j < getTable().getCellCount(i); j++) {
          if (getTable().getWidget(i, j) != null) {
            TemplateWidget tw = (TemplateWidget)getTable().getWidget(i, j);
            if (tw.isInvalid())
              return true;
          }
        }
      }
    }
    return false;
  }
  
  @Override
  public void addEdited(ArrayList<AValue> values) {
    for (int i = 0; i < getTable().getRowCount(); i++) {
      for (int j = 0; j < getTable().getCellCount(i); j++) {
        if (getTable().getWidget(i, j) != null) {
          TemplateWidget tw = (TemplateWidget)getTable().getWidget(i, j);
          tw.addEdited(values);
        }
      }
    }
  }
  
  @Override
  public void initialize(Map<Integer, Map<Integer, List<AValue>>> allValues) {
//    for (int i = 0; i < getTable().getRowCount(); i++) {
//      for (int j = 0; j < getTable().getCellCount(i); j++) {
//        if (getTable().getWidget(i, j) != null) {
//          TemplateWidget tw = (TemplateWidget)getTable().getWidget(i, j);
//          tw.initialize(allValues);
//        }
//      }
//    }
  }
  
  @Override
  public TemplateWidget copy(int rank) {
    return this;
  }
  
  @Override
  public void setTabIndex(int index) {
    // set tabindex for all cells
    for (int i = 0; i < getTable().getRowCount(); i++) {
      for (int j = 0; j < getTable().getCellCount(i); j++) {
        if (getTable().getWidget(i, j) != null) {
          TemplateWidget tw = (TemplateWidget)getTable().getWidget(i, j);
          tw.setTabIndex(index);
        }
      }
    }
  }

  @Override
  public void setFocus() {
    // set focus to first cell
    for (int i = 0; i < getTable().getRowCount(); i++) {
      for (int j = 0; j < getTable().getCellCount(i); j++) {
        if (getTable().getWidget(i, j) != null) {
          TemplateWidget tw = (TemplateWidget)getTable().getWidget(i, j);
          tw.setFocus();
          return;
        }
      }
    }
  }

  // getters and setters

  /**
   * @return the table
   */
  public FlexTable getTable() {
    return table;
  }

  /**
   * @param table the table to set
   */
  public void setTable(FlexTable table) {
    this.table = table;
  }
}
