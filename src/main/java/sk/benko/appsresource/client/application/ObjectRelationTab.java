package sk.benko.appsresource.client.application;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateRelation;

import java.util.Map;

public class ObjectRelationTab extends ScrollPanel implements ApplicationModel.RelatedObjectCountObserver {

  private ObjectTemplate objectTemplate;
  private AObject aObject;
  private TemplateRelation templateRelation;
  private FlowPanel resultPanel;

  public ObjectRelationTab(ObjectTemplate ot, TemplateRelation tr) {
    setObjectTemplate(ot);
    setTemplateRelation(tr);

    setStyleName("content-table");
    add(getResultPanel());
    setHeight(Window.getClientHeight() - getAbsoluteTop() - 205 + "px");
  }

  @Override
  public void onRelatedObjectCountsLoaded(int rel, Map<Template, Integer> counts) {
    if (getTemplateRelation().getOrId() != rel) return;
    setAObject(getModel().getObject());
    getResultPanel().clear();
    for (Template t : counts.keySet()) {

      int count = counts.get(t);

      if (count > 0) {
        RelatedObjectsList ol = new RelatedObjectsList(getObjectView(),
            getTemplateRelation().getOrId(), t, count);

        getResultPanel().add(ol);
      } else {
        Label noObjects = new Label(Main.constants.noObjects());
        noObjects.setStyleName(ClientUtils.CSS_ITALIC);
        getResultPanel().add(noObjects);
      }
    }
  }

  public void onLoad() {
    getModel().addDataObserver(this);
  }

  public void onUnload() {
    getModel().removeDataObserver(this);
  }

  // getters and setters

  /**
   * @return the objectView
   */
  public ObjectView getObjectView() {
    return getObjectTemplate().getObjectView();
  }

  /**
   * @return the application model
   */
  public ApplicationModel getModel() {
    return getObjectTemplate().getModel();
  }

  /**
   * @return the objectTemplate
   */
  public ObjectTemplate getObjectTemplate() {
    return objectTemplate;
  }

  /**
   * @param objectTemplate the objectTemplate to set
   */
  public void setObjectTemplate(ObjectTemplate objectTemplate) {
    this.objectTemplate = objectTemplate;
  }

  /**
   * @return      the object
   */
  public AObject getAObject() {
    return aObject;
  }

  /**
   * @param aObject     the object to set
   */
  public void setAObject(AObject aObject) {
    this.aObject = aObject;
  }

  /**
   * @return the templateRelation
   */
  public TemplateRelation getTemplateRelation() {
    return templateRelation;
  }

  /**
   * @param templateRelation the templateRelation to set
   */
  public void setTemplateRelation(TemplateRelation templateRelation) {
    this.templateRelation = templateRelation;
  }

  /**
   * @return the resultPanel
   */
  public FlowPanel getResultPanel() {
    if (resultPanel == null)
      resultPanel = new FlowPanel();
    return resultPanel;
  }
}
