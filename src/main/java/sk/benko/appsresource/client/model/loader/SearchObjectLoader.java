package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationService;

import java.util.List;
import java.util.Map;

/**
 * Controls all aspects of loading the set of {@link AObject}s.
 */
public class SearchObjectLoader implements AsyncCallback<Map<AObject, List<AValue>>> {

  private final String ID;
  private final ApplicationModel model;
  private final int langId;
  private final String searchString;
  private final int tlId;
  private final int from;
  private final int perPage;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param ID           id of UI component which is loading search results
   * @param model        the model to which this loader is bound
   * @param langId
   * @param searchString
   * @param tlId
   * @param from
   * @param perPage
   */
  public SearchObjectLoader(String ID, ApplicationModel model, int langId,
                            String searchString, int tlId, int from, int perPage) {
    this.ID = ID;
    this.model = model;
    this.langId = langId;
    this.searchString = searchString;
    this.tlId = tlId;
    this.from = from;
    this.perPage = perPage;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getSearchObjects(langId, searchString, tlId, from,
        perPage, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);
  }

  @Override
  public void onSuccess(Map<AObject, List<AValue>> result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifySearchObjectsLoaded(ID, searchString, tlId, result);
  }
}
