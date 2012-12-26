package sk.benko.appsresource.client.application;

import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectRowView;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.loader.SearchObjectLoader;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;

public class SearchObjectsList extends ObjectsList implements ApplicationModel.SearchObjectObserver {

  private String searchString;
 
  public SearchObjectsList(ApplicationModel model, String searchString, 
      Template t, int count) {
    super(model, t, count);
    this.searchString = searchString;
  }
  
  protected void load() {
    SearchObjectLoader sol = new SearchObjectLoader(toString(), getModel(), 
        Main.language, this.searchString,  getDdbTemplate().getSelection().getId(), 
        getFrom()*columns, getPerPage()*columns);
    sol.start();
  }

  @Override
  public void onSearchObjectsLoaded(String ID, String searchString, int tlId, Map<AObject, List<AValue>> objectValues) {
    if (!toString().equals(ID)) return;
    list.clear();
    for (final AObject aobject : objectValues.keySet()) {
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
}
