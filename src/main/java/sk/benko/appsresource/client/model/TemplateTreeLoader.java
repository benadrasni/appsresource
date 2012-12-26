package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.result.GetTemplateTreesResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link TemplateTree}s. 
 *
 */
public class TemplateTreeLoader implements 
    AsyncCallback<GetTemplateTreesResult> {

  private final Model model;
  private final int tId;
  private final boolean isChooseTree;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public TemplateTreeLoader(Model model, int tId) {
    this.model = model;
    this.tId = tId;
    this.isChooseTree = false;
  }

  public TemplateTreeLoader(Model model, int tId, boolean isChooseTree) {
    this.model = model;
    this.tId = tId;
    this.isChooseTree = isChooseTree;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTemplateTrees(tId, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetTemplateTreesResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    if (isChooseTree)
      model.notifyChooseTemplateTreesLoaded(tId, result.getTemplateTrees());
    else
      model.notifyTemplateTreesLoaded(tId, result.getTemplateTrees());
  }
}
