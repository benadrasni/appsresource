package sk.benko.appsresource.client.model.loader;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.RetryTimer;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.result.GetTemplateAttributesResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of {@link TemplateAttribute}s. 
 *
 */
public class TemplateAttributeLoader extends RetryTimer implements 
    AsyncCallback<GetTemplateAttributesResult> {

  private final Model model;
  private final Template t;
  private final TemplateRelation tr;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param the template to which template attributes belong
   * @param the template relation to which template attributes belong
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
  public void onSuccess(GetTemplateAttributesResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyTemplateAttributesLoaded(t, result.getTemplateAttributes(), tr);
  }
  
  @Override
  protected void retry() {
    start();
  }
}
