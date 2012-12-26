package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.TemplateAttribute;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TemplateLinkWidget extends TemplateWidget implements HasBlurHandlers {
  
  private String LINK_HEIGHT = "25px";
  
  private FlowPanel linkPanel;
  private FlowPanel editBox;
  private TextBox linkBox;
  private Widget link;
  private String oldValue;
  
  private HandlerRegistration mouseOutLink;
  private HandlerRegistration mouseOverLink;

  public TemplateLinkWidget(ObjectTemplate objectTemplate, TemplateAttribute ta, 
      int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    if (isDisabled() || !getModel().getAppu().isWrite())
      getLinkPanel().addStyleDependentName(CSSConstants.SUFFIX_DISABLED);

    getLinkBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (!isInvalid()) {
          if (getValue() == null
              && getLinkBox().getText().trim().length() > 0) {
            setValue(new AValue(getModel().getObject().getId(),
                getTemplateAttribute().getOaId(), getLinkBox().getText()
                    .trim(), ClientUtils.DEFAULT_LANG));
            getValue().setRank(getRank());
          } else {
            if (getLinkBox().getText().trim().length() > 0) {
              getValue().setValueString(getLinkBox().getText().trim());
            } 
          }
        }
      }
    });

    addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (mouseOverLink == null)
          mouseOverLink = addMouseOverHandler(getLinkPanel(), LINK_HEIGHT);
        if (mouseOutLink == null)
          mouseOutLink = addMouseOutHandler(getLinkPanel());
        if (getModel().getObject() != null && !isInvalid() && isEdited()) {
          getModel().updateValue(getValue());
        } 
      }
    });
    
    add(getLinkPanel());
  }

  public HandlerRegistration addMouseOutHandler(FlowPanel widget) {
    return widget.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        getEditBox().getElement().getStyle().setProperty("maxHeight", "0");
      }}, MouseOutEvent.getType());
  }

  public HandlerRegistration addMouseOverHandler(FlowPanel widget, final String height) {
    return widget.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        getEditBox().getElement().getStyle().setProperty("maxHeight", height);
      }}, MouseOverEvent.getType());
  }

  @Override
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
    return getLinkBox().addBlurHandler(handler);
  }

  @Override
  public boolean isEdited() {
    return !getOldValue().equals(getLinkBox().getText().trim());
  }

  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null ||
        (getLinkBox().getText().trim().length() == 0
        && getTemplateAttribute().isMandatory());
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);
    if (value != null && value.getRank() == getRank() 
        && value.getValueString() != null) {
      getLinkBox().setText(value.getValueString());
      getAnchor(value.getValueString());
      setValue(value);
      if (value.getId() > 0)
        setOldValue(value.getValueString());
    } else {
      setOldValue("");
      clearValue();
    }
  }

  @Override
  public void clearValue() {
    super.clearValue();
    getLinkBox().setText("");
    getAnchor("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateLinkWidget(getObjectTemplate(),
        getTemplateAttribute(), getRelId(), rank, getWidgetToHighlight(),
        false);
  }

  @Override
  public void setTabIndex(int index) {
    //getAppTextBox().setTabIndex(index);
  }

  @Override
  public void setFocus() {
    //getAppTextBox().getTextBox().getElement().focus();
  }

  // getters and setters
  
  /**
   * @return the linkBox
   */
  public TextBox getLinkBox() {
    if (linkBox == null) {
      linkBox = new TextBox();
      linkBox.setStyleName(CSSConstants.CSS_EDIT_LINK);
      linkBox.addFocusHandler(new FocusHandler() {
        @Override
        public void onFocus(FocusEvent event) {
          if (mouseOverLink != null) {
            mouseOverLink.removeHandler();
            mouseOverLink = null;
          }
          if (mouseOutLink != null) {
            mouseOutLink.removeHandler();
            mouseOutLink = null;
          }
        }
      });
    }
    return linkBox;
  }

  /**
   * @return the anchor
   */
  public Widget getAnchor(String url) {
    if (link != null)
      link.removeFromParent();
    
    link = new Anchor(getTemplateAttribute().getName(), url, "_newtab");
    getLinkPanel().add(link);
    return link;
  }

  /**
   * @return the imageBox
   */
  public FlowPanel getLinkPanel() {
    if (linkPanel == null) {
      linkPanel = new FlowPanel();
      linkPanel.setStyleName(CSSConstants.CSS_PANEL_LINK);
      linkPanel.setWidth(getTemplateAttribute().getWidth()
          + getTemplateAttribute().getWidthUnit());
      linkPanel.add(getEditBox());
      
      mouseOverLink = addMouseOverHandler(linkPanel, LINK_HEIGHT);
      mouseOutLink = addMouseOutHandler(linkPanel); 
    }
    return linkPanel;
  }

  /**
   * @return the creditBox
   */
  public FlowPanel getEditBox() {
    if (editBox == null) {
      editBox = new FlowPanel();
      editBox.setStyleName(CSSConstants.CSS_PANEL_EDIT);
      editBox.add(getLinkBox());
    }
    return editBox;
  }

  /**
   * @return the oldValue
   */
  public String getOldValue() {
    return oldValue;
  }

  /**
   * @param oldValue the oldValue to set
   */
  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }
}
