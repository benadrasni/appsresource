package sk.benko.appsresource.client.application;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.ApplicationView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.Template;

import java.util.Map;

public class SearchObjects extends FlowPanel implements ApplicationModel.SearchObjectCountObserver {
  private ApplicationView applicationView;
  private FlowPanel resultPanel;

  public SearchObjects(ApplicationView av) {
    setApplicationView(av);
    getElement().setId(CSSConstants.CSS_CONTENT_TEMPLATE);

    add(getResultPanel());
  }

  public void initialize() {
  }

  @Override
  public void onSearchObjectCountsLoaded(String searchString, Map<Template, Integer> counts) {
    getResultPanel().clear();
    for (Template t : counts.keySet()) {
      int count = counts.get(t);
      if (count > 0) {
        SearchObjectsList ol = new SearchObjectsList(
            getApplicationView().getModel(), searchString, t, count);

        getResultPanel().add(ol);
      } else {
        Label noobjs = new Label(Main.constants.noObjects());
        noobjs.setStyleName(ClientUtils.CSS_ITALIC);
        getResultPanel().add(noobjs);
      }
    }
  }

  public void onLoad() {
    getApplicationView().getModel().addDataObserver(this);
  }

  public void onUnload() {
    getApplicationView().getModel().removeDataObserver(this);
  }

  // getters and setters

  /**
   * @return the applicationView
   */
  public ApplicationView getApplicationView() {
    return applicationView;
  }

  /**
   * @param applicationView the applicationView to set
   */
  public void setApplicationView(ApplicationView applicationView) {
    this.applicationView = applicationView;
  }

  /**
   * @return the resultPanel
   */
  public FlowPanel getResultPanel() {
    if (resultPanel == null) {
      resultPanel = new FlowPanel();
    }
    return resultPanel;
  }
}
