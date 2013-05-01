package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;

import java.util.List;

/**
 * Controls all aspects of loading the set of {@link TemplateAttribute}s.
 */
public class TemplateAttributeLoader extends RetryTimer implements AsyncCallback<List<TemplateAttribute>> {

  private final Model model;
  private final Template t;
  private final TemplateRelation tr;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param t     the template to which template filterAttributes belong
   * @param tr    the template relation to which template filterAttributes belong
   */
  public TemplateAttributeLoader(Model model, Template t,
                                 TemplateRelation tr) {
    this.model = model;
    this.t = t;
    this.tr = tr;
  }

  public TemplateAttributeLoader(Model model, Template t) {
    this(model, t, null);
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTemplateAttributes(t, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(List<TemplateAttribute> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateAttributesLoaded(t, result, tr);
  }

  @Override
  protected void retry() {
    start();
  }
}
