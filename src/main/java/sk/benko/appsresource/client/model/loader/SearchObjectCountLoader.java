package sk.benko.appsresource.client.model.loader;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.result.GetSearchCountsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Controls all aspects of loading the set of related object counts. 
 *
 */
public class SearchObjectCountLoader implements AsyncCallback<GetSearchCountsResult> {

  private final ApplicationModel model;
  private final String searchString;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model the model to which this loader is bound
   * @param id of the application to which templates belongs
   */
  public SearchObjectCountLoader(ApplicationModel model, String searchString) {
    this.model = model;
    this.searchString = searchString;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getSearchObjectCounts(model.getAppu().getAppId(), searchString, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(GetSearchCountsResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifySearchCountsLoaded(searchString, result.getCounts());
  }
}
