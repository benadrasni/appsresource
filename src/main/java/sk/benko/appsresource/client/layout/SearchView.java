package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.loader.SearchObjectCountLoader;
import sk.benko.appsresource.client.ui.widget.theme.ThemeImage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A widget to display the section name.
 *
 */
public class SearchView extends FlowPanel {
  private ApplicationModel amodel;
  
  private TextBox searchBox; 
  private ButtonView bSearch;
  
  private String searchedText;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public SearchView(final ApplicationModel amodel) {
    getElement().setId(ClientUtils.CSS_SEARCH);
    setStyleName(ClientUtils.CSS_SEARCH);
    setModel(amodel);
    
    add(getSearchBox());
    add(getBSearch());
    
    Event.addNativePreviewHandler(new NativePreviewHandler() { 
      public void onPreviewNativeEvent(NativePreviewEvent event) { 
        if (!event.isCanceled() && event.getTypeInt() == Event.ONKEYDOWN
            && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
          event.cancel();
          getBSearch().fireEvent(new ClickEvent() {});
        }
      }
    });
  }
  
  public void setFocus() {
    getSearchBox().setFocus(true);
  }

  /**
   * @return the applicationModel
   */
  public ApplicationModel getModel() {
    return amodel;
  }

  /**
   * @param amodel the application modelto set
   */
  public void setModel(ApplicationModel amodel) {
    this.amodel = amodel;
  }

  /**
   * @return the searchedText
   */
  public String getSearchedText() {
    return searchedText;
  }

  /**
   * @param searchedText the searchedText to set
   */
  public void setSearchedText(String searchedText) {
    this.searchedText = searchedText;
  }

  /**
   * Getter for property 'searchBox'.
   * 
   * @return Value for property 'searchBox'.
   */
  public TextBox getSearchBox() {
    if (searchBox == null) {
      searchBox = new TextBox();
      searchBox.setWidth("300px");
    }
    return searchBox;
  }
  
  /**
   * Getter for property 'bSearch'.
   * 
   * @return Value for property 'bSearch'.
   */
  public ButtonView getBSearch() {
    if (bSearch == null) {
      bSearch = new ButtonView("inline-block button", 
          "70px", new ThemeImage("search.png"), 
          new ClickHandler() {
            public void onClick(ClickEvent event) {
              setSearchedText(getSearchBox().getText());
              SearchObjectCountLoader socl = new SearchObjectCountLoader(
                  getModel(), getSearchedText());
              socl.start();
            }
      });
      bSearch.setHeight("20px");
    }
    return bSearch;
  }
}
