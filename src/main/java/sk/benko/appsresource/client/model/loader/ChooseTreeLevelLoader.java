package sk.benko.appsresource.client.model.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import sk.benko.appsresource.client.TreeItemData;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.result.GetTreeLevelResult;

import java.util.List;
import java.util.Map;

/**
 * Controls all aspects of loading the set of {@link AObject}s.
 */
public class ChooseTreeLevelLoader extends RetryTimer
    implements AsyncCallback<GetTreeLevelResult> {

  private final ApplicationModel model;
  private final int langId;
  private final int tIdSource;
  private final int tId;
  private final TreeItem ti;
  private final String key;
  private final TemplateAttribute ta;
  private Map<Integer, List<AValue>> values;

  /**
   * Creates a new loader that is bound to the given model.
   *
   * @param model     the model to which this loader is bound
   * @param langId    language's id
   * @param tIdSource source template's id
   * @param values    values
   * @param tId       template's id
   * @param key       key
   * @param ta        - template attribute
   */
  public ChooseTreeLevelLoader(ApplicationModel model, int langId, int tIdSource,
                               Map<Integer, List<AValue>> values, int tId, TreeItem ti, String key, TemplateAttribute ta) {
    this.model = model;
    this.langId = langId;
    this.tIdSource = tIdSource;
    this.values = values;
    this.tId = tId;
    this.key = key;
    this.ti = ti;
    this.ta = ta;
  }

  public void start() {
    Main.status.showTaskStatus(Main.constants.loading());
    model.getService().getTreeLevel(langId, tIdSource, values, tId,
        ((TreeItemData) ti.getUserObject()).getPath(), ta, this);
  }

  @Override
  public void onFailure(Throwable caught) {
    model.onServerFailed(caught instanceof ApplicationService.AccessDeniedException);

    retryLater();
  }

  @Override
  public void onSuccess(GetTreeLevelResult result) {
    model.onServerSucceeded();
    model.getStatusObserver().onTaskFinished();
    model.notifyChooseTreeLevelLoaded(ti, key, ta, result.getTreeLevel());
  }

  @Override
  protected void retry() {
    start();
  }
}
