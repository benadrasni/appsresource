package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ui.widget.theme.ThemeImage;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * This is a basic class for all text boxes with a button.
 * 
 * @see sk.benko.appsresource.client.ui.widget.DatePicker
 * @see sk.benko.appsresource.client.ui.widget.ObjectPicker
 * 
 */
public abstract class TextButtonPanel extends SimplePanel {
  /** widget layout */
  private FlexTable layout;
  /** a selected value box */
  private TextBox selectedValue;
  /** a choice button */
  private ToggleButton choiceButton;
  /** a choice button image */
  private Image choiceButtonImage;
  /** this flag means whether it's possible to enter a custom text */
  private boolean customTextAllowed;
  /** a flag meaning whether the widget locked */
  private boolean locked;
  /** a locking panel to lock the screen */
  private LockingPanel lockingPanel;
  /** choice button visibility flag */
  private boolean choiceButtonVisible;
  /** widget width */
  private String width;
  /** widget height */
  private String height;
  /** enabled panel controls flag */
  private boolean enabled;

  protected TextButtonPanel() {
    getSelectedValue().setStyleName(CSSConstants.CSS_TEXTBOX);
    getLayout().setWidget(0, 0, getSelectedValue());
    setChoiceButtonVisible(true);
    setCustomTextAllowed(false);
    setStyleName(CSSConstants.CSS_PANEL_TEXTBUTTON);
    setWidget(getLayout());
    addComponentListeners();
  }

  /**
   * Getter for property 'customTextAllowed'.
   * 
   * @return Value for property 'customTextAllowed'.
   */
  public boolean isCustomTextAllowed() {
    return customTextAllowed;
  }

  /**
   * Setter for property 'customTextAllowed'.
   * 
   * @param customTextAllowed
   *          Value to set for property 'customTextAllowed'.
   */
  public void setCustomTextAllowed(boolean customTextAllowed) {
    this.customTextAllowed = customTextAllowed;
    prepareSelectedValue();
  }

  /**
   * Setter for property 'choiceButtonImage'.
   * 
   * @param choiceButtonImage
   *          Value to set for property 'choiceButtonImage'.
   */
  public void setChoiceButtonImage(Image choiceButtonImage) {
    this.choiceButtonImage = choiceButtonImage;
  }

  /**
   * Getter for property 'choiceButtonVisible'.
   * 
   * @return Value for property 'choiceButtonVisible'.
   */
  public boolean isChoiceButtonVisible() {
    return choiceButtonVisible;
  }

  /**
   * Setter for property 'choiceButtonVisible'.
   * 
   * @param choiceButtonVisible
   *          Value to set for property 'choiceButtonVisible'.
   */
  public void setChoiceButtonVisible(boolean choiceButtonVisible) {
    if (!choiceButtonVisible && isChoiceButtonVisible()) {
      getLayout().removeCell(0, 1);
    }
    else if (choiceButtonVisible && !isChoiceButtonVisible()) {
      getLayout().setWidget(0, 1, getChoiceButton());
      prepareChoiceButton();
    }
    this.choiceButtonVisible = choiceButtonVisible;
  }

  /** {@inheritDoc} */
  public void setWidth(String width) {
    super.setWidth(width);
    this.width = width;
    prepareSelectedValue();
  }

  /** {@inheritDoc} */
  public void setHeight(String height) {
    super.setHeight(height);
    this.height = height;
    prepareSelectedValue();
  }

  /**
   * This method gets a maximum length of the text box.
   * <p/>
   * It makes sence if you allow custom values entering.
   * <p/>
   * See also {@link #isCustomTextAllowed()} and
   * {@link #setCustomTextAllowed(boolean)}.
   * 
   * @return a maximum length of the text box.
   */
  public int getMaxLength() {
    return getSelectedValue().getMaxLength();
  }

  /**
   * This method sets a maximum length of the text box.
   * <p/>
   * It makes sence if you allow custom values entering.
   * <p/>
   * See also {@link #isCustomTextAllowed()} and
   * {@link #setCustomTextAllowed(boolean)}.
   * 
   * @param length
   *          is a maximum length of the text box.
   */
  public void setMaxLength(int length) {
    getSelectedValue().setMaxLength(length);
  }

  /**
   * This method sets a tab index for this component.
   * 
   * @param index
   *          is a tab order number.
   */
  public void setTabIndex(int index) {
    getSelectedValue().setTabIndex(index);
  }

  /**
   * Checks whether the controls palced on this panel are enabled.
   * 
   * @return a result of check.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Enables or disables the controls inside the panel.
   * 
   * @param enabled
   *          is a flag that means whether the controls must be enabled.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    getSelectedValue().setEnabled(enabled);
    getChoiceButton().setEnabled(enabled);
  }

  /**
   * Cleans all the data displayed in the widget.
   */
  public void cleanSelection() {
    getSelectedValue().setText("");
  }

  /**
   * This method should returns a default button image name.
   * 
   * @return an image name.
   */
  protected abstract String getDefaultImageName();

  /**
   * This method adds component listeners.
   */
  protected abstract void addComponentListeners();

  /**
   * Prepares the selected value box for displaying.
   */
  protected void prepareSelectedValue() {
    TextBox selectedValue = getSelectedValue();
    selectedValue.setReadOnly(!isCustomTextAllowed());
    selectedValue.setStyleName(CSSConstants.CSS_TEXTBOX);

    if (getHeight() != null) {
      getLayout().setHeight("100%");
      getLayout().getCellFormatter().setHeight(0, 0, "100%");
      getSelectedValue().setHeight("100%");
    }

    if (getWidth() != null) {
      getLayout().setWidth("100%");
      getLayout().getCellFormatter().setWidth(0, 0, "100%");
      getSelectedValue().setWidth("100%");
    }
  }

  /**
   * Prepares the drop down button for displaying.
   */
  protected void prepareChoiceButton() {
    ToggleButton dropDownButton = getChoiceButton();
    dropDownButton.getUpFace().setImage(getChoiceButtonImage());
    dropDownButton.getDownFace().setImage(getChoiceButtonImage());
    dropDownButton.setStyleName(CSSConstants.CSS_CHOICEBUTTON);
  }

  /**
   * Getter for property 'layout'.
   * 
   * @return Value for property 'layout'.
   */
  protected FlexTable getLayout() {
    if (layout == null) {
      layout = new FlexTable();
      layout.setCellPadding(0);
      layout.setCellSpacing(0);
    }
    return layout;
  }

  /**
   * Getter for property 'selectedValue'.
   * 
   * @return Value for property 'selectedValue'.
   */
  protected TextBox getSelectedValue() {
    if (selectedValue == null) {
      selectedValue = new TextBox();
      selectedValue.getElement().setAttribute("autocomplete", "off");
    }
    return selectedValue;
  }

  /**
   * Getter for property 'choiceButton'.
   * 
   * @return Value for property 'choiceButton'.
   */
  protected ToggleButton getChoiceButton() {
    if (choiceButton == null)
      choiceButton = new ToggleButton();
    return choiceButton;
  }

  /**
   * Getter for property 'choiceButtonImage'.
   * 
   * @return Value for property 'choiceButtonImage'.
   */
  protected Image getChoiceButtonImage() {
    if (choiceButtonImage == null)
      choiceButtonImage = new ThemeImage(getDefaultImageName());
    choiceButtonImage.setStyleName(CSSConstants.CSS_IMAGE);
    return choiceButtonImage;
  }

  /**
   * Getter for property 'locked'.
   * 
   * @return Value for property 'locked'.
   */
  public boolean isLocked() {
    return locked;
  }

  /**
   * This method locks the screen.
   */
  public void lock() {
    setLocked(true);
    getLockingPanel().lock();
  }

  /**
   * This method unlocks the screen and redisplays the widget.
   */
  public void unlock() {
    getLockingPanel().unlock();
    setLocked(false);
  }

  /**
   * Setter for property 'locked'.
   * 
   * @param locked
   *          Value to set for property 'locked'.
   */
  protected void setLocked(boolean locked) {
    this.locked = locked;
  }

  /**
   * Getter for property 'lockingPanel'.
   * 
   * @return Value for property 'lockingPanel'.
   */
  protected LockingPanel getLockingPanel() {
    if (lockingPanel == null)
      lockingPanel = new LockingPanel();
    return lockingPanel;
  }

  /**
   * Getter for property 'width'.
   * 
   * @return Value for property 'width'.
   */
  protected String getWidth() {
    return width;
  }

  /**
   * Getter for property 'height'.
   * 
   * @return Value for property 'height'.
   */
  protected String getHeight() {
    return height;
  }
}
