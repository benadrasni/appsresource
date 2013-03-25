package sk.benko.appsresource.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Tree.Resources;
import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.loader.ValueLoader;

import java.util.List;
import java.util.Map;

/**
 * A widget that displays the UI associated with the content of the application.
 */
public class ObjectView extends FlowPanel implements ApplicationModel.ValueObserver {

  private ApplicationModel model;
  // layout
  private ApplicationHeaderView ahv;
  private ObjectActionView oav;
  private ObjectContentView ocv;
  // data
  private Tree objectTree;

  //Timer t;

  /**
   * @param model the model to which the UI will bind itself
   */
  public ObjectView(ApplicationModel model) {
    getElement().setId(CSSConstants.CSS_APPLICATION);
    setModel(model);
    getModel().removeObservers();

    setObjectTree(new Tree((Resources) GWT.create(TreeResource.class), false));
    // disable tabIndex for tree
    getObjectTree().getElement().getFirstChildElement().setTabIndex(-1);

    setAhv(new ApplicationHeaderView(getModel()));
    add(getAhv());
    setOav(new ObjectActionView(this));
    add(getOav());
    setOcv(new ObjectContentView(this));
    add(getOcv());

    getModel().addValueObserver(this);
  }

  public void initialize() {
    getAhv().initialize(false);
    getOav().initialize();
    getOcv().initialize(false);

    if (getModel().getObject() != null) {
      ValueLoader vl = new ValueLoader(getModel());
      vl.start();
    }
  }

  @Override
  public void onValuesLoaded(AObject object, Map<Integer, Map<Integer, List<AValue>>> allValues) {
    getModel().getRels().put("0_0", object);
    for (FlexTable table : getObjectTemplate().getTables().values()) {
      while (table.getRowCount() > 1)
        table.removeRow(1);
    }
    getOcv().getOtempv().getOtemp().setFocus();
  }

  @Override
  public void onValueUpdated(AValue value) {
  }

  // getters and setters

  /**
   * @return the model
   */
  public ObjectTemplate getObjectTemplate() {
    return getOcv().getOtempv().getOtemp();
  }

  /**
   * @return the model
   */
  public ApplicationModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(ApplicationModel model) {
    this.model = model;
  }

  /**
   * @return the ahv
   */
  public ApplicationHeaderView getAhv() {
    return ahv;
  }

  /**
   * @param ahv the ahv to set
   */
  public void setAhv(ApplicationHeaderView ahv) {
    this.ahv = ahv;
  }

  /**
   * @return the oav
   */
  public ObjectActionView getOav() {
    return oav;
  }

  /**
   * @param oav the oav to set
   */
  public void setOav(ObjectActionView oav) {
    this.oav = oav;
  }

  /**
   * @return the ocv
   */
  public ObjectContentView getOcv() {
    return ocv;
  }

  /**
   * @param ocv the ocv to set
   */
  public void setOcv(ObjectContentView ocv) {
    this.ocv = ocv;
  }

  /**
   * @return the objectTree
   */
  public Tree getObjectTree() {
    return objectTree;
  }

  /**
   * @param objectTree the objectTree to set
   */
  public void setObjectTree(Tree objectTree) {
    this.objectTree = objectTree;
  }
}
