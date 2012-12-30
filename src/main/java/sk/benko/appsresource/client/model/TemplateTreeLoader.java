package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;

import java.util.List;

/**
 * Controls all aspects of loading the set of {@link TemplateTree}s.
 */
public class TemplateTreeLoader implements AsyncCallback<List<TemplateTree>> {

  private final Model model;
  private final int tId;
  private final boolean isChooseTree;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model     the model to which this loader is bound
   * @param tId       the template's id
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
  public void onSuccess(List<TemplateTree> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    if (isChooseTree)
      model.notifyChooseTemplateTreesLoaded(tId, result);
    else
      model.notifyTemplateTreesLoaded(tId, result);
  }
}
