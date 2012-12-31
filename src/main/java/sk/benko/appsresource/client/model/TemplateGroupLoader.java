package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls all aspects of loading the set of {@link TemplateGroup}s. This class
 * takes care of performing (and possibly retrying) a query for the initial set
 * of TemplateGroups.
 */
public class TemplateGroupLoader extends RetryTimer implements AsyncCallback<List<TemplateGroup>> {

  private final DesignerModel model;
  private final Template template;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   */
  public TemplateGroupLoader(DesignerModel model, Template template) {
    this.model = model;
    this.template = template;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    ((DbServiceAsync) model.getService()).getTemplateGroups(template, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof DbService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(List<TemplateGroup> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateGroupsLoaded(template, result);
  }

  @Override
  protected void retry() {
    start();
  }
}
