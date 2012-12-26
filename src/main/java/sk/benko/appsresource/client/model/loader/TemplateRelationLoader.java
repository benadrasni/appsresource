package sk.benko.appsresource.client.model.loader;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.result.GetTemplateRelationsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link TemplateRelation}s. 
 *
 */
public class TemplateRelationLoader implements 
    AsyncCallback<GetTemplateRelationsResult> {

  private final Model model;
  private final Template t;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public TemplateRelationLoader(Model model, Template t) {
    this.model = model;
    this.t = t;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTemplateRelations(t, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetTemplateRelationsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateRelationsLoaded(t, result.getTemplateRelations());
  }
}
