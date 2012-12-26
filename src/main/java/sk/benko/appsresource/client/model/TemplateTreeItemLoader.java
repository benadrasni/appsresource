package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.result.GetTemplateTreeItemsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link TemplateTreeItem}s. 
 *
 */
public class TemplateTreeItemLoader implements 
    AsyncCallback<GetTemplateTreeItemsResult> {

  private final Model model;
  private final TemplateTree tt;
  private final boolean isChooseTree;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public TemplateTreeItemLoader(Model model, TemplateTree tt) {
    this.model = model;
    this.tt = tt;
    this.isChooseTree = false;
  }

  public TemplateTreeItemLoader(Model model, TemplateTree tt,
      boolean isChooseTree) {
    this.model = model;
    this.tt = tt;
    this.isChooseTree = isChooseTree;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTemplateTreeItems(tt, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetTemplateTreeItemsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    if (isChooseTree)
      model.notifyChooseTemplateTreeItemsLoaded(tt, result.getTemplateTreeItems());
    else
      model.notifyTemplateTreeItemsLoaded(tt, result.getTemplateTreeItems());
  }
}
