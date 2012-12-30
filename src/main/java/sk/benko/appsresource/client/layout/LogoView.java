package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display the section name.
 *
 */
public class LogoView extends Label {
  
  /**
   * @param section
   */
  public LogoView(final String section) {
    getElement().setId(ClientUtils.CSS_LOGO);
    setStyleName(ClientUtils.CSS_LOGO + " " + ClientUtils.CSS_CLICKABLE);
    Element elem = getElement();
    elem.setInnerText(section);
  }
}
