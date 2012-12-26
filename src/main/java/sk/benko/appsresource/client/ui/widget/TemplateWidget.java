package sk.benko.appsresource.client.ui.widget;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class TemplateWidget extends SimplePanel implements
    ApplicationModel.ObjectObserver,
    ApplicationModel.ValueObserver {

  private ObjectTemplate objectTemplate;
  private TemplateAttribute templateAttribute;
  private AValue value;
  private int rank;
  private int relId;
  private boolean isDisabled;
  private Widget widgetToHighlight;

  public TemplateWidget(ObjectTemplate objectTemplate, TemplateAttribute ta,
                        int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    setObjectTemplate(objectTemplate);
    setTemplateAttribute(ta);
    setRank(rank);
    setRelId(relId);
    setWidgetToHighlight(widgetToHighlight);
    setDisabled(isDisabled);

    if (getTemplateAttribute().getShared5() > 0)
      getObjectTemplate().getTable(getTemplateAttribute().getShared5()).setWidget(
          getRank(), getTemplateAttribute().getTop(), this);

    getModel().addObjectObserver(this);
    getModel().addValueObserver(this);
  }

  public abstract boolean isEdited();

  public abstract boolean isInvalid();

  public abstract void setTabIndex(int index);

  public abstract void setFocus();

  public abstract TemplateWidget copy(int rank);

  public void initialize(Map<Integer, Map<Integer, List<AValue>>> allValues) {

    AObject object = getModel().getRels().get(getRelId() + "_0");
    if (allValues != null && object != null) {
      Map<Integer, List<AValue>> values = allValues.get(object.getId());
      if (values != null) {
        List<AValue> objValues = values.get(getTemplateAttribute().getOaId());
        if (objValues != null && objValues.size() > 0) {
          int i = 0;
          if (getTemplateAttribute().getShared5() > 0) {
            while (i < objValues.size() && objValues.get(i).getRank() < getRank())
              i++;
            if (i == objValues.size()) {
              initialize(null);
              return;
            }
          } else {
            if (getTemplateAttribute().isDescendant()) {
              i = objValues.size() - 1;
              int rank = objValues.get(i).getRank();
              while (i >= 0 && objValues.get(i).getRank() == rank) i--;
            }
          }

          AValue value = objValues.get(i);
          // find proper language mutation
          AValue defValue = value;
          while (i < objValues.size()
              && defValue.getRank() == value.getRank()
              && value.getLangId() != Main.language) {
            value = ++i < objValues.size() ? objValues.get(i) : defValue;
          }
          if (defValue.getRank() != value.getRank())
            value = defValue;
          setRank(value.getRank());
          initializeValue(value);
          return;
        }
      }
    }
    initializeValue(null);
  }

  public void initialize() {
    initialize(getModel().getAllValues());
  }

  public void initializeValue(AValue value) {
    if (value != null) {
      //setRank(value.getRank());
      if (getTemplateAttribute().getShared5() > 0) {
        setTableWidget();
      }
    }
  }

  public void addEdited(ArrayList<AValue> values) {
    if (isEdited())
      values.add(getValue());
  }

  @Override
  public void onObjectCreated(AObject object, Map<Integer, Map<Integer, List<AValue>>> values) {
    initialize(values);
  }

  @Override
  public void onObjectDeleted(AObject object) {
    clearValue();
  }

  @Override
  public void onValuesLoaded(AObject object, Map<Integer, Map<Integer, List<AValue>>> values) {
    initialize(values);
  }

  @Override
  public void onValueUpdated(AValue value) {
    if (getValue() != null && getValue().getId() == value.getId()) {

      if (value.isEmpty())
        initializeValue(null);
      else
        initializeValue(value);

      // update value in object's values
      List<AValue> values = getModel().getValues().get(getTemplateAttribute().getOaId());
      if (values != null) {
        for (AValue iValue : values) {
          if (iValue.getRank() == value.getRank()
              && iValue.getLangId() == value.getLangId()) {
            values.remove(iValue);
            break;
          }
        }
      } else {
        values = new ArrayList<AValue>();
        getModel().getValues().put(getTemplateAttribute().getOaId(),
            values);
      }
      if (!value.isEmpty()) {
        values.add(value);
        Collections.sort(values, new AValueComparator());
      } else if (getTemplateAttribute().getShared5() > 0)
        cleanTableWidget();
    }
  }

  public void clearValue() {
    setValue(null);
  }

  // private methods

  private void setTableWidget() {
    FlexTable table = getObjectTemplate().getTable(
        getTemplateAttribute().getShared5());
    for (int j = table.getRowCount(); j <= (isDisabled() ? getRank() : getRank() + 1); j++) {
      for (int i = 0; i < table.getCellCount(0); i++) {
        ((TemplateWidget) table.getWidget(0, i)).copy(j);
      }
      for (int i = 0; i < table.getCellCount(0); i++) {
        TemplateWidget tw = ((TemplateWidget) table.getWidget(j, i));
        tw.initialize();
      }
    }
  }

  private void cleanTableWidget() {
    FlexTable table = getObjectTemplate().getTable(getTemplateAttribute().getShared5());
    int row = 0;
    boolean isEmptyLine = true;
    boolean isRowExists = false;
    for (int i = 0; i < table.getRowCount(); i++) {
      for (int j = 0; j < table.getCellCount(i); j++) {
        TemplateWidget tw = (TemplateWidget) table.getWidget(i, j);
        if (tw.getRank() < getRank())
          break;
        else if (tw.getRank() == getRank()) {
          isEmptyLine = isEmptyLine && tw.getValue() == null;
          row = i;
          isRowExists = true;
        } else if (isEmptyLine && isRowExists) {
          tw.setRank(tw.getRank() - 1);
          if (tw.getValue() != null)
            tw.getValue().setRank(tw.getRank());
        } else
          break;
      }
    }
    if (isEmptyLine && isRowExists)
      table.removeRow(row);
  }

  // getters and setters

  /**
   * @return the objectView
   */
  public ApplicationModel getModel() {
    return getObjectTemplate().getModel();
  }

  /**
   * @return the objectTemplate
   */
  public ObjectTemplate getObjectTemplate() {
    return objectTemplate;
  }

  /**
   * @param objectTemplate the objectTemplate to set
   */
  public void setObjectTemplate(ObjectTemplate objectTemplate) {
    this.objectTemplate = objectTemplate;
  }

  /**
   * @return the templateAttribute
   */
  public TemplateAttribute getTemplateAttribute() {
    return templateAttribute;
  }

  /**
   * @param templateAttribute the templateAttribute to set
   */
  public void setTemplateAttribute(TemplateAttribute templateAttribute) {
    this.templateAttribute = templateAttribute;
  }

  /**
   * @return the value
   */
  public AValue getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(AValue value) {
    this.value = value;
  }

  /**
   * @return the rank
   */
  public int getRank() {
    return rank;
  }

  /**
   * @param rank the rank to set
   */
  public void setRank(int rank) {
    this.rank = rank;
  }

  /**
   * @return the relId
   */
  public int getRelId() {
    return relId;
  }

  /**
   * @param relId the relId to set
   */
  public void setRelId(int relId) {
    this.relId = relId;
  }

  /**
   * @return the isDisabled
   */
  public boolean isDisabled() {
    return isDisabled;
  }

  /**
   * @param isDisabled the isDisabled to set
   */
  public void setDisabled(boolean isDisabled) {
    this.isDisabled = isDisabled;
  }

  /**
   * @return the widgetToHighlight
   */
  public Widget getWidgetToHighlight() {
    return widgetToHighlight;
  }

  /**
   * @param widgetToHighlight the widgetToHighlight to set
   */
  public void setWidgetToHighlight(Widget widgetToHighlight) {
    this.widgetToHighlight = widgetToHighlight;
  }
}
