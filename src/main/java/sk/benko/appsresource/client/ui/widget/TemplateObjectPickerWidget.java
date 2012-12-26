package sk.benko.appsresource.client.ui.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.ui.ObjectListener;

import java.util.List;
import java.util.Map;

/**
 * This class represents UI widget which is responsible for picking related
 * object from object's tree. It could hold only reference value with rank = 0
 *
 * @author Adrian Benko
 */
public class TemplateObjectPickerWidget extends TemplateWidget implements ObjectListener<ObjectsTree>, HasChangeHandlers {

  private ObjectPicker objectPicker;
  private int oldValue;

  public TemplateObjectPickerWidget(ObjectTemplate objectTemplate, TemplateAttribute ta,
                                    int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    setObjectPicker(new ObjectPicker(objectTemplate, ta,
        isDisabled() || !getModel().getAppu().isWrite()));

    setOldValue(0);

    addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        if (getModel().getObject() != null && !isInvalid() && isEdited()) {
          getModel().updateValue(getValue());
        }
      }
    });

    add(getObjectPicker());
    getObjectPicker().getObjectsTree().addObjectListener(this);
  }

  @Override
  public boolean isEdited() {
    return (getValue() == null && getOldValue() > 0)
        || (getValue() != null && getOldValue() != getValue().getValueRef());
  }

  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null
        || (getObjectPicker().getSelectedValue().getText().trim().length() == 0
        && getTemplateAttribute().isMandatory());
  }

  @Override
  public void initialize(Map<Integer, Map<Integer, List<AValue>>> allValues) {
    getObjectPicker().setValues(allValues);
    AObject object = getModel().getRels().get(getRelId() + "_0");
    if (allValues != null && object != null) {
      Map<Integer, List<AValue>> values = allValues.get(object.getId());
      if (values != null) {
        List<AValue> vals = values.get(getTemplateAttribute().getOaId());
        if (vals != null && vals.size() > 0) {
          int i = 0;
          if (getTemplateAttribute().getShared5() > 0) {
            while (i < vals.size() && vals.get(i).getRank() < getRank())
              i++;
            if (i == vals.size()) {
              initializeValue(null);
              return;
            }
          } else {
            if (getTemplateAttribute().isDescendant()) {
              i = vals.size() - 1;
              int rank = vals.get(i).getRank();
              while (i >= 0 && vals.get(i).getRank() == rank) i--;
            }
          }

          AValue value = vals.get(i);
          if (value.getValueRef() > 0) {
            // get values of related object
            Map<Integer, List<AValue>> objValues = allValues.get(value.getValueRef());
            List<AValue> relValues = objValues.get(getTemplateAttribute().getShared2());

            // find proper language mutation
            AValue valueRef = null;
            if (relValues != null && relValues.size() > 0) {
              int j = 0;
              valueRef = relValues.get(j);
              AValue defValue = valueRef;
              while (j < relValues.size()
                  && valueRef.getLangId() != Main.language) {
                valueRef = ++j < relValues.size() ? relValues.get(j) : defValue;
              }
            }

            // set related object
            AObject relObject = new AObject(value.getValueRef(),
                getTemplateAttribute().getOa().getShared1(), 0,
                relValues != null && relValues.size() > 0 ? valueRef.getValueString() : "");
            getModel().getRels().put(
                getTemplateAttribute().getOa().getShared2() + "_" + value.getRank(), relObject);
            getObjectPicker().setObject(relObject);
            getObjectPicker().getSelectedValue().setText(relObject.getLeaf());
          }

          initializeValue(value);
          return;
        }
      }
    }
    initializeValue(null);
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);

    if (value != null && value.getRank() == getRank()
        && value.getValueRef() > 0) {
      setValue(value);
      if (value.getId() > 0)
        setOldValue(value.getValueRef());
    } else {
      setOldValue(0);
      clearValue();
    }
  }

  @Override
  public void clearValue() {
    super.clearValue();
    emptyValue();
  }

  @Override
  public TemplateWidget copy(int rank) {
    TemplateWidget copy = new TemplateObjectPickerWidget(getObjectTemplate(),
        getTemplateAttribute(), getRelId(), rank, getWidgetToHighlight(),
        false);
    return copy;
  }

  @Override
  public void setFocus() {
    getObjectPicker().getChoiceButton().getElement().focus();
  }

  @Override
  public void setTabIndex(int index) {
    getObjectPicker().getChoiceButton().setTabIndex(index);
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addHandler(handler, ChangeEvent.getType());
  }

  @Override
  public void onChange(ObjectsTree sender, AObject oldValue) {
    if (sender.getObject().getId() > 0) {
      if (getValue() == null) {
        setValue(new AValue(getModel().getObject().getId(),
            getTemplateAttribute().getOaId(), sender.getObject().getId()));
        getValue().setRank(getRank());
      } else
        getValue().setValueRef(sender.getObject().getId());

//      getObjectTemplate().getRels().put(getTemplateAttribute().getOa().getShared2(), 
//          sender.getObject());
      getObjectPicker().getSelectedValue().setText(sender.getObject().getLeaf());
    } else if (getValue() != null) {
      getValue().setValueRef(0);
      emptyValue();
    }

    fireEvent(new ChangeEvent() {
    });
    getObjectTemplate().fireEvent(new
                                      ChangeEvent() {
                                      });
  }

  @Override
  public void onCancel(ObjectsTree sender) {
  }

  // getters and setters

  /**
   * @return the oldValue
   */
  public int getOldValue() {
    return oldValue;
  }

  /**
   * @param oldValue the oldValue to set
   */
  public void setOldValue(int oldValue) {
    this.oldValue = oldValue;
  }

  /**
   * @return the objectPicker
   */
  public ObjectPicker getObjectPicker() {
    return objectPicker;
  }

  /**
   * @param objectPicker the objectPicker to set
   */
  public void setObjectPicker(ObjectPicker objectPicker) {
    this.objectPicker = objectPicker;
  }

  private void emptyValue() {
    //getObjectTemplate().getRels().remove(getTemplateAttribute().getOa().getShared2());
    AObject object = new AObject(0, getTemplateAttribute().getOa().getShared1(),
        0, Main.constants.noDefined());
    getObjectPicker().setObject(object);
    getObjectPicker().getSelectedValue().setText("");
  }

}
