package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.AppsresourceConstants;
import sk.benko.appsresource.client.AppsresourceMessages;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.model.AppUserService;
import sk.benko.appsresource.client.model.AppUserService.UserInfoResult;
import sk.benko.appsresource.client.model.AppUserServiceAsync;
import sk.benko.appsresource.client.model.DbService;
import sk.benko.appsresource.client.model.DbServiceAsync;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.LoadObserver;
import sk.benko.appsresource.client.model.RetryTimer;
import sk.benko.appsresource.client.model.StatusObserver;
import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;

/**
 * The entry point for the Application resource application.
 *
 */
public class Main extends RetryTimer implements EntryPoint, LoadObserver,
    StatusObserver {
  
  /**
   * Provides Ui to notify the user of model based events. These include tasks
   * (like loading a surface) and also errors (like lost communication to the
   * server).
   */
  public static class StatusView extends SimplePanel {
    private final DivElement taskStatusElement;
    private final DivElement errorStatusElement;
    private int counter;

    public StatusView() {
      final Document document = Document.get();
      final Element element = getElement();
      taskStatusElement = element.appendChild(document.createDivElement());
      errorStatusElement = element.appendChild(document.createDivElement());
      errorStatusElement.setInnerText("No response from server");

      setStyleName("status-view");
      taskStatusElement.setClassName("status-view-task");
      errorStatusElement.setClassName("status-view-error");

      hideErrorStatus();
      hideTaskStatus();
    }

    /**
     * Hides the Ui for server communication lost errors.
     */
    public void hideErrorStatus() {
      UIObject.setVisible(errorStatusElement, false);
    }

    /**
     * Hides the task status Ui.
     */
    public void hideTaskStatus() {
      counter--;
      if (counter <= 0) {
        UIObject.setVisible(taskStatusElement, false);
        counter = 0;
      }
    }

    /**
     * Displays the Ui for server communication lost errors.
     */
    public void showErrorStatus() {
      counter = 0;
      UIObject.setVisible(errorStatusElement, true);
    }

    /**
     * Displays the the Ui for a task status.
     *
     * @param text
     *          the text to be displayed
     */
    public void showTaskStatus(String text) {
      if (counter == 0) 
        UIObject.setVisible(taskStatusElement, true);
      
      taskStatusElement.setInnerText(text);
      counter++;
    }
  }

  public static int language = ClientUtils.DEFAULT_LANG;
  public static final StatusView status = new StatusView();
  public static final AppsresourceConstants constants = GWT.create(AppsresourceConstants.class);
  public static final AppsresourceMessages messages = GWT.create(AppsresourceMessages.class);
  
  private static ToolbarView toolbar;
  private static UserView userView;
  
  /**
   * Provides an asynchronous factory for loading a {@link DesignerModel}.
   *
   * @param loadObserver
   *          a callback to receive load events
   */
  public static void load(final LoadObserver loadObserver) {
    final AppUserServiceAsync api = GWT.create(AppUserService.class);
    api.getUserInfo(new AsyncCallback<AppUserService.UserInfoResult>() {
      public void onFailure(Throwable caught) {
        loadObserver.onModelLoadFailed();
      }

      public void onSuccess(UserInfoResult result) {
        loadObserver.onModelLoaded(result);
      }
    });
  }

  public void onModelLoaded(UserInfoResult result) {
    status.hideTaskStatus();
    status.hideErrorStatus();

    Window.enableScrolling(false);

    final AppUserServiceAsync uApi = GWT.create(AppUserService.class);
    UserModel umodel = new UserModel(result.getAuthor(), 
        result.getLogoutUrl(), uApi, this);
    
    final RootPanel root = RootPanel.get();
    if (umodel.getCurrentAuthor().isDesigner()) {
      final DbServiceAsync dbApi = GWT.create(DbService.class);
      DesignerModel dmodel = new DesignerModel(umodel, dbApi, this);
      toolbar = new ToolbarView(dmodel, umodel); 
      root.add(toolbar);
      DesignerView dv = new DesignerView(dmodel); 
      root.add(dv);
    }
    else {
      toolbar = new ToolbarView(umodel);
      root.add(toolbar);
      userView = new UserView(umodel); 
      root.add(userView);
    }
  }

  public void onModelLoadFailed() {
    retryLater();
    status.showErrorStatus();
  }

  public void onModuleLoad() {
    RootPanel.get().add(status);
    status.showTaskStatus(constants.loading());
    load(this);
  }

  public void onServerCameBack() {
    status.hideErrorStatus();
  }

  public void onServerWentAway() {
    status.showErrorStatus();
  }

  public void onTaskFinished() {
    status.hideTaskStatus();
  }

  public void onTaskStarted(String description) {
    status.showTaskStatus(description);
  }

  @Override
  protected void retry() {
    load(this);
  }
  
  
  /**
   * Getter for property 'toolbar'.
   * 
   * @return Value for property 'toolbar'.
   */
  public static ToolbarView getToolbar() {
    return toolbar;
  }
  
  /**
   * Getter for property 'userView'.
   * 
   * @return Value for property 'userView'.
   */
  public static UserView getUserView() {
    return userView;
  }
}
