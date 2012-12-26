package sk.benko.appsresource.client.application;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.ValueType;
import sk.benko.appsresource.client.ui.widget.TemplateDatePickerWidget;
import sk.benko.appsresource.client.ui.widget.TemplateDoubleBoxWidget;
import sk.benko.appsresource.client.ui.widget.TemplateImageWidget;
import sk.benko.appsresource.client.ui.widget.TemplateIntegerBoxWidget;
import sk.benko.appsresource.client.ui.widget.TemplateLinkWidget;
import sk.benko.appsresource.client.ui.widget.TemplateObjectPickerWidget;
import sk.benko.appsresource.client.ui.widget.TemplateTableWidget;
import sk.benko.appsresource.client.ui.widget.TemplateTextAreaWidget;
import sk.benko.appsresource.client.ui.widget.TemplateTextBoxWidget;
import sk.benko.appsresource.client.ui.widget.TemplateWidget;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class ObjectGroupTab extends ScrollPanel implements
    HasChangeHandlers {

  private TemplateGroup tg;
  private TemplateRelation tr;

  private boolean isDisabled;

  private ObjectTemplate objectTemplate;
  private AbsolutePanel panel;

  public ObjectGroupTab(ObjectTemplate ot, TemplateGroup tg) {
    setObjectTemplate(ot);

    add(getPanel());
    getPanel().setHeight(Window.getClientHeight() - getAbsoluteTop() - 205 + "px");
  }

  public void displayGroupLabel(TemplateGroup tg) {
    if (tg.isLabelVisible()) {
      Label label = new Label();
      label.setText(tg.getName());
      label.setStyleName(CSSConstants.CSS_GROUPLABEL);
      label.setWidth(tg.getLabelWidth() + tg.getLabelWidthUnit());
      getPanel().add(label, tg.getLabelLeft(), tg.getLabelTop());
    }
  }

  public Label displayLabel(TemplateAttribute ta) {
    Label label = null;
    if (ta.isLabelVisible()) {
      label = new Label();
      String text = ta.getName();
      label.setStyleName(CSSConstants.CSS_LABEL);
      label.setWidth(ta.getLabelWidth() + ta.getLabelWidthUnit());
      getPanel().add(label, ta.getLabelLeft(), ta.getLabelTop());
      if (ta.isUnitVisible() && ta.getOa().getUnit() != null
          && ta.getShared5() > 0) {
        text = text + " (" + ta.getOa().getUnit().getSymbol() + ")";
      }
      label.setText(text);
    }
    return label;
  }

  public void displayUnit(TemplateAttribute ta) {
    if (ta.isUnitVisible() && ta.getOa().getUnit() != null
        && ta.getShared5() == 0) {
      Label unit = new Label();
      unit.setText(ta.getOa().getUnit().getSymbol());
      unit.setStyleName(ClientUtils.CSS_OBJECT_UNIT);
      unit.setWidth(ta.getUnitWidth() + ta.getUnitWidthUnit());
      getPanel().add(unit, ta.getUnitLeft(), ta.getUnitTop());
    }
  }

  public TemplateWidget display(TemplateAttribute ta) {

    // label
    Label label = displayLabel(ta);
    // unit
    displayUnit(ta);

    // widget
    TemplateWidget tw = null;
    if (ta.isValueVisible()) {
      switch (ta.getStyle()) {
      case TemplateAttribute.STYLE_DATEPICKER:
        switch (ta.getOa().getVt().getType()) {
        case ValueType.VT_TIME:
          tw = new TemplateDatePickerWidget(getObjectTemplate(), ta, getOrId(),
              0, label, isDisabled(), true);
          break;
        case ValueType.VT_DATETIME:
          tw = new TemplateDatePickerWidget(getObjectTemplate(), ta, getOrId(),
              0, label, isDisabled(), true);
          break;
        case ValueType.VT_DATE:
        default:
          tw = new TemplateDatePickerWidget(getObjectTemplate(), ta, getOrId(),
              0, label, isDisabled(), false);
          break;
        }

        if (ta.getShared5() == 0)
          getPanel().add(tw, ta.getLeft(), ta.getTop());
        else
          getObjectTemplate().getTable(ta.getShared5()).setWidget(tw.getRank(),
              ta.getTop(), tw);
        break;
      case TemplateAttribute.STYLE_DYNAMICCOMBO:
        if (ta.getOa().getVt().getType() == ValueType.VT_REF) {
          tw = new TemplateObjectPickerWidget(getObjectTemplate(), ta,
              getOrId(), 0, label, isDisabled());

          if (ta.getShared5() == 0)
            getPanel().add(tw, ta.getLeft(), ta.getTop());
          else
            getObjectTemplate().getTable(ta.getShared5()).setWidget(
                tw.getRank(), ta.getTop(), tw);
        }
        break;
      case TemplateAttribute.STYLE_TABLE:
        tw = new TemplateTableWidget(getObjectTemplate(), ta, getOrId(), label,
            isDisabled());
        getPanel().add(tw, ta.getLeft(), ta.getTop());
        break;
      case TemplateAttribute.STYLE_IMAGE:
        tw = new TemplateImageWidget(getObjectTemplate(), ta, getOrId(), 0,
            label, isDisabled());
        getPanel().add(tw, ta.getLeft(), ta.getTop());
        break;
      case TemplateAttribute.STYLE_TEXTAREA:
        tw = new TemplateTextAreaWidget(getObjectTemplate(), ta, getOrId(), 0,
            label, isDisabled());
        getPanel().add(tw, ta.getLeft(), ta.getTop());
        break;
      case TemplateAttribute.STYLE_LINK:
        tw = new TemplateLinkWidget(getObjectTemplate(), ta, getOrId(), 0,
            label, isDisabled());
        getPanel().add(tw, ta.getLeft(), ta.getTop());
        break;
      case TemplateAttribute.STYLE_LABEL:
      case TemplateAttribute.STYLE_TEXTBOX:
      default:
        switch (ta.getOa().getVt().getType()) {
        case ValueType.VT_INT:
          tw = new TemplateIntegerBoxWidget(getObjectTemplate(), ta, getOrId(),
              0, label, isDisabled());
          break;
        case ValueType.VT_REAL:
          tw = new TemplateDoubleBoxWidget(getObjectTemplate(), ta, getOrId(),
              0, label, isDisabled());
          break;
        case ValueType.VT_STRING:
        default:
          tw = new TemplateTextBoxWidget(getObjectTemplate(), ta, getOrId(), 0,
              label, isDisabled());
          break;
        }

        if (ta.getShared5() == 0)
          getPanel().add(tw, ta.getLeft(), ta.getTop());
        else
          getObjectTemplate().getTable(ta.getShared5()).setWidget(tw.getRank(),
              ta.getTop(), tw);
        break;
      }
    }

    return tw;
  }

  public boolean isInvalid() {
    for (int i = 0; i < panel.getWidgetCount(); i++) {
      Widget w = panel.getWidget(i);
      if (w instanceof TemplateWidget) {
        if (((TemplateWidget) w).isInvalid())
          return true;
      }
    }
    return false;
  }

  public void disableTabOrder() {
    for (int i = 0; i < panel.getWidgetCount(); i++) {
      Widget w = panel.getWidget(i);
      if (w instanceof TemplateWidget) {
        ((TemplateWidget) w).setTabIndex(-1);
      }
    }
  }

  public void enableTabOrder() {
    for (int i = 0; i < panel.getWidgetCount(); i++) {
      Widget w = panel.getWidget(i);
      if (w instanceof TemplateWidget) {
        TemplateWidget tw = ((TemplateWidget) w);
        tw.setTabIndex(tw.getTemplateAttribute().getTabIndex());
      }
    }
  }

  public String getTabName() {
    return getTg().getName();
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addHandler(handler, ChangeEvent.getType());
  }

  // getters and setters

  /**
   * @return the tg
   */
  public TemplateGroup getTg() {
    return tg;
  }

  /**
   * @param tg
   *          the tg to set
   */
  public void setTg(TemplateGroup tg) {
    this.tg = tg;
  }

  /**
   * @return the tr
   */
  public TemplateRelation getTr() {
    return tr;
  }

  /**
   * @param tr
   *          the tr to set
   */
  public void setTr(TemplateRelation tr) {
    this.tr = tr;
  }

  /**
   * @return the id of object relation if exists, otherwise 0
   */
  public int getOrId() {
    if (getTr() == null)
      return 0;
    else
      return getTr().getOrId();
  }

  /**
   * @return the objectTemplate
   */
  public ObjectTemplate getObjectTemplate() {
    return objectTemplate;
  }

  /**
   * @param objectTemplate
   *          the objectTemplate to set
   */
  public void setObjectTemplate(ObjectTemplate objectTemplate) {
    this.objectTemplate = objectTemplate;
  }

  /**
   * @return the objectView
   */
  public ApplicationModel getModel() {
    return getObjectTemplate().getModel();
  }

  /**
   * @return the panel
   */
  public AbsolutePanel getPanel() {
    if (panel == null) {
      panel = new AbsolutePanel();
      panel.setStyleName(CSSConstants.CSS_PANEL);
      panel.getElement().getStyle().setOverflow(Overflow.AUTO);
    }
    return panel;
  }

  /**
   * @return the isDisabled
   */
  public boolean isDisabled() {
    return isDisabled;
  }

  /**
   * @param isDisabled
   *          the isDisabled to set
   */
  public void setDisabled(boolean isDisabled) {
    this.isDisabled = isDisabled;
  }
}
