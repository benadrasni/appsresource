package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.result.GetTemplateListItemsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link TemplateListItem}s. 
 *
 */
public class TemplateListItemLoader implements 
    AsyncCallback<GetTemplateListItemsResult> {

  private final Model model;
  private final TemplateList tl;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public TemplateListItemLoader(Model model, TemplateList tl) {
    this.model = model;
    this.tl = tl;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTemplateListItems(tl, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetTemplateListItemsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateListItemsLoaded(tl, result.getTemplateListItems());
  }
}
