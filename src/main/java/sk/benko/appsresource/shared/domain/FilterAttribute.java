package sk.benko.appsresource.shared.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 18.4.2013
 * Time: 19:54
 *
 * Basic filter attribute
 */
@XmlRootElement
public class FilterAttribute {
  public int attributeId;
  public int valueId;

  public FilterAttribute() {}

  public FilterAttribute(int attributeId, int valueId) {
    this.attributeId = attributeId;
    this.valueId = valueId;
  }

  public int getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(int attributeId) {
    this.attributeId = attributeId;
  }

  public int getValueId() {
    return valueId;
  }

  public void setValueId(int valueId) {
    this.valueId = valueId;
  }
}
