package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateRelation;

import java.util.List;

/**
 * Controls all aspects of loading the set of {@link TemplateRelation}s.
 */
public class TemplateRelationLoader implements AsyncCallback<List<TemplateRelation>> {

  private final Model model;
  private final Template t;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model   the model to which this loader is bound
   * @param t       the template
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
  public void onSuccess(List<TemplateRelation> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateRelationsLoaded(t, result);
  }
}
