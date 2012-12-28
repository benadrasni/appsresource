package sk.benko.appsresource.client.layout;

import java.util.ArrayList;
import java.util.Collection;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.designer.SubscriptionDialog;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class UserActionButtonsView extends FlowPanel implements 
    UserModel.ApplicationUserObserver {
  
  private UserModel model;
  private ButtonView bAdd;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UserActionButtonsView(final UserModel model) {
    setStyleName("buttons_bar");
    setModel(model);

    add(getBAdd());
  }

  @Override
  public void onApplicationUserCreated(ApplicationUser applicationUser) {
    disableAddButton();
  }

  @Override
  public void onApplicationUserUpdated(ApplicationUser applicationUser) {
  }

  @Override
  public void onApplicationUsersLoaded(ArrayList<ApplicationUser> applicationUsers) {
    disableAddButton();
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
   * Getter for property 'bAdd'.
   * 
   * @return Value for property 'bAdd'.
   */
  protected ButtonView getBAdd() {
    if (bAdd == null) {
      bAdd = new ButtonView(Main.constants.add(), 
          "button inline-block button-collapse-right", "80px",
          new ClickHandler() {
            public void onClick(ClickEvent event) {
              new SubscriptionDialog(model);
            }
      });
    }
    return bAdd;
  }

  // private methods

  private void disableAddButton() {
    if (!existsPublicApp())
      getBAdd().addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
  }
  
  private boolean existsPublicApp() {
    if (getModel().getApplications() != null) {
      Collection<ApplicationUser> appus = getModel().getApplicationUsers().values();
      Collection<Application> apps = getModel().getApplications().values();
      for (Application application : apps) {
        if (application.isPublic()) {
          boolean isSubscribed = false;
          for (ApplicationUser applicationUser : appus) {
            isSubscribed = applicationUser.getAppId() == application.getId();
            if (isSubscribed) break;
          }
          if (!isSubscribed)
            return true;
        }
      }
    }
    return false;
  }
}
