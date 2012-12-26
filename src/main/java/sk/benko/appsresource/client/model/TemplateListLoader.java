package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.result.GetTemplateListsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link TemplateTree}s. 
 *
 */
public class TemplateListLoader implements 
    AsyncCallback<GetTemplateListsResult> {

  private final String ID;
  private final Model model;
  private final int tId;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public TemplateListLoader(String ID, Model model, int tId) {
    this.ID = ID;
    this.model = model;
    this.tId = tId;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTemplateLists(tId, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetTemplateListsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateListsLoaded(ID, tId, result.getTemplateLists());
  }
}
