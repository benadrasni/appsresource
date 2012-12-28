package sk.benko.appsresource.client.layout;

import java.util.ArrayList;
import java.util.Collection;

import sk.benko.appsresource.client.dnd.SurfaceDropController;
import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.UserModel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

public class SurfaceView extends AbsolutePanel implements UserModel.ApplicationUserObserver {

  private UserModel model;
  private PickupDragController appDragController;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public SurfaceView(UserModel model) {
    setModel(model);
    final Element elem = getElement();
    elem.setId("surface");
    elem.getStyle().setProperty("position", "absolute");
    
    appDragController = new PickupDragController(this, true);
    SurfaceDropController appDropController = new SurfaceDropController(this, model);
    appDragController.registerDropController(appDropController);
    appDragController.setBehaviorConstrainedToBoundaryPanel(true);
    appDragController.setBehaviorMultipleSelection(false);
    appDragController.setBehaviorDragStartSensitivity(5);
    displayAppus();
  }

  private void displayAppus() {
    if (this.model.getApplicationUsers() != null) {
      Collection<ApplicationUser> appus = this.model.getApplicationUsers().values();
      for (ApplicationUser applicationUser : appus) {
        ApplicationWidget aw = new ApplicationWidget(appDragController, applicationUser);
        add(aw.getApplicationAsHTML(this.model));
      }
    }
  }
  
  private void deleteAppus() {
    WidgetCollection widgets = getChildren();
    for (Widget widget : widgets) {
      if (widget instanceof HTML)
        widget.removeFromParent();
    }
  }

  
  @Override
  public void onApplicationUserCreated(ApplicationUser applicationUser) {
    ApplicationWidget aw = new ApplicationWidget(appDragController, applicationUser);
    add(aw.getApplicationAsHTML(this.model));
  }

  @Override
  public void onApplicationUserUpdated(ApplicationUser applicationUser) {
  }

  @Override
  public void onApplicationUsersLoaded(
      ArrayList<ApplicationUser> applicationUsers) {
    deleteAppus();
    displayAppus();
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
}
