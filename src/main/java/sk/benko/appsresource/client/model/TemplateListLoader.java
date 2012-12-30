package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;

import java.util.List;

/**
 * Controls all aspects of loading the set of {@link TemplateTree}s.
 */
public class TemplateListLoader implements AsyncCallback<List<TemplateList>> {

  private final String ID;
  private final Model model;
  private final int tId;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param ID    the id of UI component which is loading template lists
   * @param model the model to which this loader is bound
   * @param tId   the template's id
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
  public void onSuccess(List<TemplateList> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateListsLoaded(ID, tId, result);
  }
}
