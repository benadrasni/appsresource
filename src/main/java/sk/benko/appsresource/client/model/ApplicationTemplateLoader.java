package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.result.GetApplicationTemplatesResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link ApplicationTemplate}s. 
 *
 */
public class ApplicationTemplateLoader implements 
    AsyncCallback<GetApplicationTemplatesResult> {

  private final Model model;
  private final Application app;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public ApplicationTemplateLoader(Model model, Application app) {
    this.model = model;
    this.app = app;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getApplicationTemplates(app, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetApplicationTemplatesResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyApplicationTemplatesLoaded(app, result.getApplicationTemplates());
  }
}
