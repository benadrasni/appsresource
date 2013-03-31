package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.ApplicationWidget;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabel;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 *
 */
public class SubscriptionDialog extends UserDialog {
  private static int DEFAULT_TOP = 10;
  private static int DEFAULT_LEFT = 10;
  private static int DEFAULT_WIDTH = 200;
  private static int DEFAULT_HEIGHT = 100;
  private static int DEFAULT_FLAGS = 1; // just visible

  private FlexTable widget;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public SubscriptionDialog(final UserModel model) {
    super(model);
    
    getHeader().add(new Label(Main.constants.subscription()));

    NavigationLabel menu1 = new NavigationLabel(
        Main.constants.all(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        model.notifyDialogNavigationItemClicked(event.getRelativeElement());
        Node oldChild = getBodyRight().getElement().getChild(0);
        getBodyRight().getElement().removeChild(oldChild);
        getBodyRight().getElement().appendChild(widget.getElement());
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);
    getBodyLeft().add(menu1);
    
    initializeApps();

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            for (int i = 0; i < widget.getRowCount(); i++) {
              for (int j = 0; j < widget.getCellCount(i); j++) {
                if (widget.getCellFormatter().getStyleName(i, j).equals("selected")) {
                  int appId = Integer.parseInt(widget.getWidget(i, j).getElement()
                      .getAttribute("id"));
                  ApplicationUser appu = new ApplicationUser(appId, 
                      model.getCurrentAuthor().getId());
                  fill(appu);
                  model.createOrUpdateApplicationUser(appu);
                }
              }
            }
            SubscriptionDialog.this.hide();
          }

        }, ClickEvent.getType());
    getBOk().getElement().setInnerText(Main.constants.subscribe());
    
    widget.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (widget.getCellForEvent(event) != null) {
          int column = widget.getCellForEvent(event).getCellIndex();
          int row = widget.getCellForEvent(event).getRowIndex();
          
          if (widget.getCellFormatter().getStyleName(row, column).equals("selected")) 
            widget.getCellFormatter().removeStyleName(row, column, "selected");
          else
            widget.getCellFormatter().setStyleName(row, column, "selected");
        }
      }
    });
  }

  private void initializeApps() {
    Collection<Application> apps = getModel().getApplications().values();
    Collection<ApplicationUser> appus = getModel().getApplicationUsers().values();
    boolean isSubscribed = false;
    int i = 0;
    for (Application application : apps) {
      if (application.isPublic()) {
        for (ApplicationUser applicationUser : appus) {
          isSubscribed = applicationUser.getAppId() == application.getId();
          if (isSubscribed) break;
        }
        if (!isSubscribed) {
          ApplicationWidget aw = new ApplicationWidget(application);
          widget.setWidget(i / 3, i % 3, aw.getApplicationAsSimplePanel());
          i++;
        }
      }
    }
  }
  
  private void fill(ApplicationUser appu) {
    appu.setApp(getModel().getApplication(appu.getAppId()));
    appu.setLeft(DEFAULT_LEFT);
    appu.setTop(DEFAULT_TOP);
    appu.setWidth(DEFAULT_WIDTH);
    appu.setHeight(DEFAULT_HEIGHT);
    // flags
    int flags = DEFAULT_FLAGS;
    if (appu.getAppUserId() == appu.getApp().getUserId()) {
      flags = ClientUtils.setFlag(ApplicationUser.FLAG_ADMIN, flags);
      flags = ClientUtils.setFlag(ApplicationUser.FLAG_WRITE, flags);
    }
    appu.setFlags(flags);
  }
}
