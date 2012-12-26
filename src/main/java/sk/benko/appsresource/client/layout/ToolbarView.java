package sk.benko.appsresource.client.layout;

import java.util.ArrayList;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Language;
import sk.benko.appsresource.client.model.UserModel;
import sk.benko.appsresource.client.model.UserModel.LanguageObserver;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A widget that displays the UI associated with the toolbar of the application.
 *
 */
public class ToolbarView extends FlowPanel implements 
    LanguageObserver, ChangeHandler {
  
  /**
   * A view to display the current user's name and a signout link.
   */
  private static class LoginInfoView extends FlowPanel {
    private final Label userElem;
    private UserDialog ud = new UserDialog();


    public LoginInfoView(final DesignerModel model) {
      assert model.getUserModel().getCurrentAuthor() != null;
      getElement().setId("login-info");
      ud.setName(model.getUserModel().getCurrentAuthor().getName());
      ud.setEmail(model.getUserModel().getCurrentAuthor().getEmail());
      ud.setLogoutClickHandler(new ClickHandler(){
        public void onClick(ClickEvent event) {
          Window.Location.assign(model.getUserModel().getLogoutUrl());
        }
      });
      ud.setDesignerClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          ud.hide();
          DesignerView dv = new DesignerView(model);
          Document.get().getElementById("appcontent").removeFromParent();
          final RootPanel root = RootPanel.get();
          root.add(dv);
        }
      });
      ud.addDesignerLink();
      userElem = new Label(model.getUserModel().getCurrentAuthor().getName());
      userElem.getElement().setId("login-info-name");
      userElem.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          ud.show();
        }
      });
      add(userElem);
    }
    
    /**
     */
    public LoginInfoView(final UserModel model) {
      assert model.getCurrentAuthor() != null;
      getElement().setId("login-info");
      ud.setName(model.getCurrentAuthor().getName());
      ud.setEmail(model.getCurrentAuthor().getEmail());
      ud.setLogoutClickHandler(new ClickHandler(){
        public void onClick(ClickEvent event) {
          Window.Location.assign(model.getLogoutUrl());
        }
      });
      userElem = new Label(model.getCurrentAuthor().getName());
      userElem.getElement().setId("login-info-name");
      userElem.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          ud.show();
        }
      });
      add(userElem);
    }
  }

  /**
   * A view to display the link to applications.
   */
  private static class AppsView extends FlowPanel {
    private final Label appsElem;

    /**
     * @param author
     *          the current author
     * @param logoutUrl
     *          a url that can be used to logout
     */
    public AppsView(final UserModel model) {
      getElement().setId("apps-info");
      appsElem = new Label(Main.constants.userMode());
      appsElem.getElement().setId("apps-info-name");
      appsElem.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          Document.get().getElementById("appcontent").removeFromParent();
          final RootPanel root = RootPanel.get();
          root.add(new UserView(model));
        }
      });
      add(appsElem);
    }
  }
  
  private UserModel model;
  private static DropDownBox ddbLanguages;
  
  /**
   * @param parent
   *          the parent for this widget
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ToolbarView(final DesignerModel dmodel, final UserModel umodel) {
    final Element elem = getElement();
    elem.setId(CSSConstants.CSS_TOOLBAR);
    setModel(umodel);
    addDomHandler(this, ChangeEvent.getType());

    add(new AppsView(umodel));
    add(new LoginInfoView(dmodel));
    
    FlowPanel langs = new FlowPanel();
    langs.getElement().setId("lang-info");
    langs.add(getDdbLanguages());
    add(langs);
  }

  /**
   * @param parent
   *          the parent for this widget
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ToolbarView(final UserModel umodel) {
    final Element elem = getElement();
    elem.setId(CSSConstants.CSS_TOOLBAR);
    setModel(umodel);
    addDomHandler(this, ChangeEvent.getType());

    add(new AppsView(umodel));
    add(new LoginInfoView(umodel));

    FlowPanel langs = new FlowPanel();
    langs.getElement().setId("lang-info");
    langs.add(getDdbLanguages());
    add(langs);
  }

  @Override
  public void onChange(ChangeEvent event) {
    Main.language = getDdbLanguages().getSelection().getId();
  }

  @Override
  public void onLanguagesLoaded(ArrayList<Language> languages) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 

    for (Language language : getModel().getLanguages().values()) 
      items.add(new DropDownObjectImpl(language.getId(), language.getName()));
    
    getDdbLanguages().setItems(items);
  }

  @Override
  public void onLoad() {
    getModel().addDataObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeDataObserver(this);
  }

  /**
   * @return the model
   */
  public UserModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(UserModel model) {
    this.model = model;
  }
  
  /**
   * Getter for property 'ddbLanguages'.
   * 
   * @return Value for property 'ddbLanguages'.
   */
  public DropDownBox getDdbLanguages() {
    if (ddbLanguages == null) {
      ddbLanguages = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbLanguages.setWidth("100%");
    }
    return ddbLanguages;
  }

}
