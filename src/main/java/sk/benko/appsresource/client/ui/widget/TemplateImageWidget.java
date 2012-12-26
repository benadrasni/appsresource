package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.layout.Main;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.reveregroup.gwt.imagepreloader.FitImage;

public class TemplateImageWidget extends TemplateWidget implements HasBlurHandlers {
  
  private String CREDIT_HEIGHT = "25px";
  private String LINK_HEIGHT = "50px";
  
  private FlowPanel imageBox;
  private FlowPanel creditBox;
  private TextBox linkBox;
  private FitImage image;
  private Label credit;
  private String oldValue;
  
  private HandlerRegistration mouseOutImage;
  private HandlerRegistration mouseOverImage;
  private HandlerRegistration mouseOutCredit;
  private HandlerRegistration mouseOverCredit;

  public TemplateImageWidget(ObjectTemplate objectTemplate, TemplateAttribute ta, 
      int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    if (isDisabled() || !getModel().getAppu().isWrite())
      getImageBox().addStyleDependentName(CSSConstants.SUFFIX_DISABLED);

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
          } else if (getLinkBox().getText().trim().length() > 0)
              getValue().setValueString(getLinkBox().getText().trim());

        }
      }
    });

    
    addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (mouseOverImage == null)
          mouseOverImage = addMouseOverHandler(getImageBox(), CREDIT_HEIGHT);
        if (mouseOutImage == null)
          mouseOutImage = addMouseOutHandler(getImageBox());
        if (mouseOverCredit == null)
          mouseOverCredit = addMouseOverHandler(getCreditBox(), LINK_HEIGHT);
        if (mouseOutCredit == null)
          mouseOutCredit = addMouseOutHandler(getCreditBox()); 
        if (getModel().getObject() != null && !isInvalid() && isEdited()) {
          getModel().updateValue(getValue());
        } 
      }
    });
    
    add(getImageBox());
  }

  public HandlerRegistration addMouseOutHandler(FlowPanel widget) {
    return widget.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        getCreditBox().getElement().getStyle().setProperty("maxHeight", "0");
      }}, MouseOutEvent.getType());
  }

  public HandlerRegistration addMouseOverHandler(FlowPanel widget, final String height) {
    return widget.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        getCreditBox().getElement().getStyle().setProperty("maxHeight", height);
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
      getImage(value.getValueString());
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
    getCredit().setText("");
    getImage("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateImageWidget(getObjectTemplate(),
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
      linkBox.setStyleName(CSSConstants.CSS_IMAGE_LINK);
      linkBox.addFocusHandler(new FocusHandler() {
        @Override
        public void onFocus(FocusEvent event) {
          if (mouseOverImage != null) {
            mouseOverImage.removeHandler();
            mouseOverImage = null;
          }
          if (mouseOverCredit != null) {
            mouseOverCredit.removeHandler();
            mouseOverCredit = null;
          }
          if (mouseOutImage != null) {
            mouseOutImage.removeHandler();
            mouseOutImage = null;
          }
          if (mouseOutCredit != null) {
            mouseOutCredit.removeHandler();
            mouseOutCredit = null;
          }
        }
      });
    }
    return linkBox;
  }

  /**
   * @return the image
   */
  public Image getImage(String url) {
    if (image != null)
      image.removeFromParent();
    
    image = new FitImage(url, getTemplateAttribute().getWidth(), 
        getTemplateAttribute().getLength());
    if (url.length() > 0) {
      getCredit().setText(
          Main.constants.credit()
              + image.getUrl().substring(image.getUrl().indexOf(".") + 1,
                  image.getUrl().indexOf("/", image.getUrl().indexOf("."))));
    }
    
    getImageBox().add(image);
    return image;
  }

  /**
   * @return the imageBox
   */
  public FlowPanel getImageBox() {
    if (imageBox == null) {
      imageBox = new FlowPanel();
      imageBox.setStyleName(CSSConstants.CSS_PANEL_IMAGE);
      //imageBox.addStyleDependentName(CSSConstants.SUFFIX_BORDER);
      imageBox.setWidth(getTemplateAttribute().getWidth()
          + getTemplateAttribute().getWidthUnit());
      imageBox.setHeight(getTemplateAttribute().getLength()
          + getTemplateAttribute().getWidthUnit());
      imageBox.add(getCreditBox());
      
      mouseOverImage = addMouseOverHandler(imageBox, CREDIT_HEIGHT);
      mouseOutImage = addMouseOutHandler(imageBox); 
    }
    return imageBox;
  }

  /**
   * @return the creditBox
   */
  public FlowPanel getCreditBox() {
    if (creditBox == null) {
      creditBox = new FlowPanel();
      creditBox.setStyleName(CSSConstants.CSS_PANEL_CREDIT);
      creditBox.add(getCredit());
      creditBox.add(getLinkBox());

      mouseOverCredit = addMouseOverHandler(creditBox, LINK_HEIGHT);
      mouseOutCredit = addMouseOutHandler(creditBox);
    }
    return creditBox;
  }

  /**
   * @return the credit
   */
  public Label getCredit() {
    if (credit == null) {
      credit = new Label();
      credit.setStyleName(CSSConstants.CSS_IMAGE_CREDIT);
    }
    return credit;
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
