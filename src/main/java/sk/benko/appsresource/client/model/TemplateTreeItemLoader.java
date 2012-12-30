package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;

import java.util.List;

/**
 * Controls all aspects of loading the set of {@link TemplateTreeItem}s.
 */
public class TemplateTreeItemLoader implements AsyncCallback<List<TemplateTreeItem>> {

  private final Model model;
  private final TemplateTree tt;
  private final boolean isChooseTree;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param tt    the template tree
   */
  public TemplateTreeItemLoader(Model model, TemplateTree tt) {
    this.model = model;
    this.tt = tt;
    this.isChooseTree = false;
  }

  public TemplateTreeItemLoader(Model model, TemplateTree tt, boolean isChooseTree) {
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
  public void onSuccess(List<TemplateTreeItem> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    if (isChooseTree)
      model.notifyChooseTemplateTreeItemsLoaded(tt, result);
    else
      model.notifyTemplateTreeItemsLoaded(tt, result);
  }
}
