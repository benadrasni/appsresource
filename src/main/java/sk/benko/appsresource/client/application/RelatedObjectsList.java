package sk.benko.appsresource.client.application;

import java.util.*;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectRowView;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.loader.RelatedObjectLoader;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;

public class RelatedObjectsList extends ObjectsList implements ApplicationModel.RelatedObjectObserver {
  private int rel;
  
  public RelatedObjectsList(ObjectView ov, int rel, Template t, int count) {
    super(ov.getModel(), t, count);
    setRel(rel);
  }
  
  protected void load() {
    RelatedObjectLoader rol = new RelatedObjectLoader(toString(), getModel(), 
        Main.language, getModel().getObject().getId(), 
        getRel(), getDdbTemplate().getSelection().getId(), getFrom()*columns, 
        getPerPage()*columns);
    rol.start();
  }

  @Override
  public void onRelatedObjectsLoaded(String ID, int rel, Map<AObject, List<AValue>> objectValues) {
    if (!toString().equals(ID)) return;
    list.clear();
    for (Iterator<AObject> iterator = objectValues.keySet().iterator(); 
        iterator.hasNext();) {
      final AObject aobject = iterator.next();
      List<AValue> values = objectValues.get(aobject);
      if (values.size() > 0) {
        ObjectRowView orv = new ObjectRowView(aobject, "content-row");
        orv.generateWidgetRow(values);
        orv.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
            getModel().setAppt(getModel().getAppTemplateByTemplate(t.getId()));
            getModel().setObject(aobject);
            ObjectView ov = new ObjectView(model);
            ov.initialize();
            Document.get().getElementById("appcontent").removeFromParent();
            final RootPanel root = RootPanel.get();
            root.add(ov);
          }});
        list.add(orv);
      }
    }
  }

  /**
   * @return the rel
   */
  public int getRel() {
    return rel;
  }

  /**
   * @param rel the rel to set
   */
  public void setRel(int rel) {
    this.rel = rel;
  }
  
}
