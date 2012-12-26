package sk.benko.appsresource.client.layout;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;

import java.util.List;

/**
 * A widget to display object in table row.
 */
public class ObjectRowView extends FlexTable {
  private NumberFormat numberFormat;
  private AObject object;

  /**
   * @param object    the object to show in a row
   * @param style     css style
   */
  public ObjectRowView(AObject object, String style) {
    setObject(object);
    setStyleName(style);
    setNumberFormat(NumberFormat.getFormat(ClientUtils.NUMBERFORMAT));
  }

  public void generateWidgetShort() {
    Label leaf = new Label(getObject().getLeaf());
    setWidget(0, 0, leaf);

    leaf.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        addStyleName("navigation-item-hover");
      }
    }, MouseOverEvent.getType());
    leaf.addDomHandler(new
                           MouseOutHandler() {
                             public void onMouseOut(MouseOutEvent event) {
                               removeStyleName("navigation-item-hover");
                             }
                           }, MouseOutEvent.getType());
  }

  public void generateWidgetRow(List<AValue> values) {
    for (int i = 0; i < values.size(); i++) {
      AValue value = values.get(i);
      Label lblValue = new Label();
      if (value.getValueString() != null)
        lblValue.setText(value.getValueString());
      else if (value.getValueDate() != null)
        lblValue.setText("" + value.getValueDate());
      else if (value.getValueDouble() != null) {
        lblValue.setText(getNumberFormat().format(value.getValueDouble()));
        lblValue.addStyleName(ClientUtils.CSS_ALIGN_RIGHT);
      } else if (value.getValueRef() > 0)
        lblValue.setText("" + value.getValueRef());
      setWidget(0, i, lblValue);
    }
  }

  /**
   * @return the numberFormat
   */
  public NumberFormat getNumberFormat() {
    return numberFormat;
  }

  /**
   * @param numberFormat the numberFormat to set
   */
  public void setNumberFormat(NumberFormat numberFormat) {
    this.numberFormat = numberFormat;
  }

  /**
   * @return the object
   */
  public AObject getObject() {
    return object;
  }

  /**
   * @param object the object to set
   */
  public void setObject(AObject object) {
    this.object = object;
  }
}
