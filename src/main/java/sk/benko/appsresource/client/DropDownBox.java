package sk.benko.appsresource.client;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

public class DropDownBox extends FocusPanel {
  private final static int ROW_HEIGHT = 27;
  private Widget parent;
  private PopupPanel list;
  private FlowPanel content;
  private Label caption;
  private ToolTipBox tooltipbox;
  private DropDownObject selection;

  public DropDownBox(Widget parent, String tooltip, final String suffix) {
    this.parent = parent;
    setStyleName(ClientUtils.CSS_DROPDOWN_HEAD);
    addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);
    addStyleDependentName(suffix);

    addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        addStyleDependentName(suffix + "-" + CSSConstants.SUFFIX_HOVER);
      }
    }, MouseOverEvent.getType());
    addDomHandler(new
                      MouseOutHandler() {
                        public void onMouseOut(MouseOutEvent event) {
                          removeStyleDependentName(suffix + "-" + CSSConstants.SUFFIX_HOVER);
                        }
                      }, MouseOutEvent.getType());

    final FlowPanel outerBox = new FlowPanel();
    outerBox.setStyleName(ClientUtils.CSS_TOOLBAR_OUTERBOX);
    outerBox.addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);
    if (tooltip != null) {
      tooltipbox = new ToolTipBox(this, tooltip);
      outerBox.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          tooltipbox.show();
        }
      }, MouseOverEvent.getType());
      outerBox.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          tooltipbox.hide();
        }
      }, MouseOutEvent.getType());
    }
    FlowPanel innerBox = new FlowPanel();
    innerBox.setStyleName(ClientUtils.CSS_TOOLBAR_INNERBOX);
    innerBox.addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);

    caption = new Label();
    caption.getElement().setId("0");
    caption.setStyleName(ClientUtils.CSS_DROPDOWN_CAPTION);
    caption.addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);
    innerBox.add(caption);

    Label button = new Label();
    button.setStyleName(ClientUtils.CSS_DROPDOWN_BUTTON);
    button.addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);
    innerBox.add(button);

    outerBox.add(innerBox);
    add(outerBox);

    addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (tooltipbox != null) tooltipbox.hide();
        showList(outerBox);
      }
    });
  }

  public DropDownBox(Widget parent, String tooltip, final String suffix,
                     ChangeHandler changeHandler) {
    this(parent, tooltip, suffix);
    addDomHandler(changeHandler, ChangeEvent.getType());
  }

  public void setItems(ArrayList<DropDownObject> items) {
    getContent().clear();
    getContent().setHeight(items.size() * ROW_HEIGHT + "px");
    for (final DropDownObject item : items) {
      final Label lblItem = new Label(item.getName());
      lblItem.setStyleName(ClientUtils.CSS_DROPDOWN_ITEM);
      if (item.getId() == 0)
        lblItem.addStyleDependentName(CSSConstants.SUFFIX_ITALIC);

      if (getSelection() == null)
        if (items.size() > 0) setSelection(items.get(0));
        else setSelection(new DropDownObjectImpl(0, ""));

      lblItem.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          if (item != getSelection()) {
            setSelection(item);
            parent.fireEvent(new ChangeEvent() {
            });
            fireEvent(new ChangeEvent() {
            });
          }
          getList().hide();
        }
      });

      lblItem.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          lblItem.addStyleName(ClientUtils.CSS_DROPDOWN_ITEMHIGHLIGHT);
        }
      }, MouseOverEvent.getType());
      lblItem.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          lblItem.removeStyleName(ClientUtils.CSS_DROPDOWN_ITEMHIGHLIGHT);
        }
      }, MouseOutEvent.getType());

      getContent().add(lblItem);
    }
  }

  public void clearItems() {
    getContent().clear();
    getContent().setHeight("0px");
  }

  public void showList(FlowPanel outerBox) {
    getList().show();

    int left = outerBox.getAbsoluteLeft();
    int height = Window.getClientHeight() * 2 / 3;
    int top = outerBox.getAbsoluteTop() + outerBox.getOffsetHeight();
    if (Window.getClientHeight() - top < outerBox.getAbsoluteTop()) {
      height = Math.min(outerBox.getAbsoluteTop() - 10, height);
      height = Math.min(getContent().getOffsetHeight(), height);
      top = outerBox.getAbsoluteTop() - height - 10;
    } else {
      height = Math.min(Window.getClientHeight() - top - 10, height);
      height = Math.min(getContent().getOffsetHeight(), height);
    }

    getList().setPopupPosition(left, top);
    getList().setHeight(height + "px");
    getList().getElement().getStyle().setOverflow(Overflow.AUTO);
  }

  /**
   * Getter for property 'list'.
   *
   * @return Value for property 'list'.
   */
  public PopupPanel getList() {
    if (list == null) {
      list = new PopupPanel(true);
      list.setStyleName(ClientUtils.CSS_DROPDOWN_LIST);
      list.add(getContent());
    }
    return list;
  }

  /**
   * Getter for property 'content'.
   *
   * @return Value for property 'content'.
   */
  public FlowPanel getContent() {
    if (content == null) {
      content = new FlowPanel();
      content.setStyleName(ClientUtils.CSS_DROPDOWN_CONTENT);
    }
    return content;
  }

  /**
   * @return the selection
   */
  public DropDownObject getSelection() {
    return selection;
  }

  /**
   * @param selection the selection to set
   */
  public void setSelection(DropDownObject selection) {
    this.caption.setText(selection.getName());
    if (selection.getId() == 0)
      caption.addStyleDependentName(CSSConstants.SUFFIX_ITALIC);
    else
      caption.removeStyleDependentName(CSSConstants.SUFFIX_ITALIC);
    this.selection = selection;
  }
}
