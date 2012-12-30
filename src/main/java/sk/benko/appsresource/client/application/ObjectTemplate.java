package sk.benko.appsresource.client.application;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.loader.RelatedObjectCountLoader;
import sk.benko.appsresource.client.model.loader.TemplateAttributeLoader;
import sk.benko.appsresource.client.model.loader.TemplateRelationLoader;
import sk.benko.appsresource.client.ui.widget.TemplateWidget;

import java.util.HashMap;
import java.util.List;

/**
 * A widget to display the object's template.
 */
public class ObjectTemplate extends TabPanel implements
    ApplicationModel.TemplateRelationObserver,
    Model.TemplateAttributeObserver,
    HasChangeHandlers {

  private ObjectView objectView;
  private Template template;
  private ApplicationModel model;
  //private AObject object;
  //private HashMap<Integer, HashMap<Integer, ArrayList<AValue>>> allvalues;
  //private HashMap<String, AObject> rels;
  private HashMap<Integer, FlexTable> tables;
  private boolean withRelations;

  public ObjectTemplate(final ApplicationModel model) {
    setStyleName("template");
    setModel(model);
    setWithRelations(false);

    getModel().addTemplateAttributeObserver(this);
    getModel().addTemplateRelationObserver(this);
  }

  public ObjectTemplate(final ObjectView objectView, boolean withRelations) {
    this(objectView.getModel());
    setObjectView(objectView);
    setWithRelations(withRelations);
  }

  public void initialize(Template t) {
    clear();
    getTables().clear();
    setTemplate(t);

    List<TemplateAttribute> templateAttributes = getModel().getAttrsByTemplate().get(getTemplate().getId());

    if (templateAttributes == null) {
      // load all attributes at once
      TemplateAttributeLoader tgl = new TemplateAttributeLoader(getModel(),
          getTemplate(), null);
      tgl.start();
    } else {
      addTabs(templateAttributes, null);

      List<TemplateRelation> trs = getModel().getRelsByTemplate().get(getTemplate().getId());
      if (trs != null)
        addRelTabs(trs);
    }
  }

  public boolean isInvalid() {
    for (int i = 0; i < getWidgetCount(); i++) {
      if (getWidget(i) instanceof ObjectGroupTab) {
        if (((ObjectGroupTab) getWidget(i)).isInvalid())
          return true;
      }
    }
    return false;
  }

  public void disableTabOrder() {
    setTabIndex(-1);
    for (int i = 0; i < getWidgetCount(); i++) {
      if (getWidget(i) instanceof ObjectGroupTab) {
        ((ObjectGroupTab) getWidget(i)).disableTabOrder();
      }
    }
  }

  public void enableTabOrder() {
    setTabIndex(0);
    for (int i = 0; i < getWidgetCount(); i++) {
      if (getWidget(i) instanceof ObjectGroupTab) {
        ((ObjectGroupTab) getWidget(i)).enableTabOrder();
      }
    }
  }

  public void setFocus() {
    for (int i = 0; i < getWidgetCount(); i++) {
      if (getWidget(i) instanceof ObjectGroupTab) {
        ObjectGroupTab otv = (ObjectGroupTab) getWidget(i);
        for (int j = 0; j < otv.getPanel().getWidgetCount(); j++) {
          Widget w = otv.getPanel().getWidget(j);
          if (w instanceof TemplateWidget) {
            ((TemplateWidget) w).setFocus();
            return;
          }
        }
      }
    }
  }

  public FlexTable getTable(int taId) {
    FlexTable table = getTables().get(taId);
    if (table == null) {
      table = new FlexTable();
      getTables().put(taId, table);
    }
    return table;
  }

  @Override
  public void onTemplateAttributesLoaded(Template t, List<TemplateAttribute> tas, TemplateRelation tr) {

    if ((tr == null && t.getId() != getTemplate().getId())
        || (tr != null && t.getId() != tr.getT2Id()))
      return;

    addTabs(tas, tr);

    if (tr == null && isWithRelations()) {
      // load relations
      TemplateRelationLoader trl = new TemplateRelationLoader(getModel(),
          getTemplate());
      trl.start();
    }
  }

  @Override
  public void onTemplateRelationsLoaded(Template t, List<TemplateRelation> trs) {
    addRelTabs(trs);
  }

  @Override
  public void onUnload() {
    getModel().setTemplateRelation(null);
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addHandler(handler, ChangeEvent.getType());
  }

  // getters and setters

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
   * @return the objectView
   */
  public ObjectView getObjectView() {
    return objectView;
  }

  /**
   * @param objectView the objectView to set
   */
  public void setObjectView(ObjectView objectView) {
    this.objectView = objectView;
  }

  /**
   * @return the template
   */
  public Template getTemplate() {
    return template;
  }

  /**
   * @param template the template to set
   */
  public void setTemplate(Template template) {
    this.template = template;
  }

  /**
   * Getter for property 'tables'.
   *
   * @return Value for property 'tables'.
   */
  public HashMap<Integer, FlexTable> getTables() {
    if (tables == null) {
      tables = new HashMap<Integer, FlexTable>();
    }
    return tables;
  }

  /**
   * @return the withRelations
   */
  public boolean isWithRelations() {
    return withRelations;
  }

  /**
   * @param withRelations the withRelations to set
   */
  public void setWithRelations(boolean withRelations) {
    this.withRelations = withRelations;
  }

  // private methods

  private void setTabIndex(int index) {
    NodeList<Element> nl = getTabBar().getElement().getElementsByTagName("div");
    for (int i = 0; i < nl.getLength(); i++) {
      Element e = nl.getItem(i);
      if (e.getAttribute("tabindex") != null
          && e.getAttribute("tabindex").length() > 0)
        e.setAttribute("tabindex", "" + index);
    }
  }

  private void addTabs(List<TemplateAttribute> templateAttributes, TemplateRelation tr) {
    // template attributes are ordered by template group's rank and subrank
    ObjectGroupTab otv = null;
    int prevTgId = 0;
    for (TemplateAttribute ta : templateAttributes) {
      if (ta.getTgId() != prevTgId) {
        prevTgId = ta.getTgId();
        if (ta.getTg().getSubRank() == 0) {
          otv = tr == null ? new ObjectGroupTab(this, ta.getTg())
              : new ObjectRelationGroupTab(this, tr, ta.getTg());
          add(otv, ta.getTg().getName());
          final int iTab = getTabBar().getTabCount() - 1;
          getTabBar().getTab(iTab).addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
              getModel().setTemplateRelation(null);
            }
          });
        }
        otv.displayGroupLabel(ta.getTg());
      }

      TemplateWidget tw = otv.display(ta);
      if (tw != null)
        tw.initialize();
    }
    if (getTabBar().getTabCount() > 0)
      selectTab(0);
  }

  private void addRelTabs(List<TemplateRelation> trs) {

    for (final TemplateRelation tr : trs) {
      if (tr.getOr().getType() == ObjectRelation.RT_11
          || tr.getOr().getType() == ObjectRelation.RT_N1) {
        List<TemplateAttribute> templateAttributes = getModel().getAttrsByTemplate().get(tr.getT2Id());

        if (templateAttributes == null) {
          // load all attributes for related template at once
          TemplateAttributeLoader tal = new TemplateAttributeLoader(getModel(),
              tr.getT2(), tr);
          tal.start();
        } else
          addTabs(templateAttributes, tr);

      } else {
        final ObjectRelationTab ort = new ObjectRelationTab(this, tr);
        add(ort, tr.getName());

        final int iTab = getTabBar().getTabCount() - 1;
        getTabBar().getTab(iTab).addClickHandler(new ClickHandler() {

          @Override
          public void onClick(ClickEvent event) {
            getModel().setTemplateRelation(tr);
            if (getModel().getObject() != null && (ort.getAObject() == null
                || ort.getAObject().getId() != getModel().getObject().getId())) {
              RelatedObjectCountLoader rocl = new RelatedObjectCountLoader(
                  getModel(), tr.getOrId(), tr.getT2());
              rocl.start();
            }
          }
        });
      }
    }
  }
}