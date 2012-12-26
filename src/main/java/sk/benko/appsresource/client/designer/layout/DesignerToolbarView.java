package sk.benko.appsresource.client.designer.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.designer.Sections;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.ApplicationTemplateLoader;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.UserModel;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class DesignerToolbarView extends FlowPanel implements ChangeHandler,
    UserModel.ApplicationObserver, Model.ApplicationObserver, 
    DesignerModel.ApplicationObserver, DesignerModel.ObjectTypeObserver {
  private DesignerModel model;
  
  private FlexTable toolbar;
  private DropDownBox ddbApplication;
  private DropDownBox ddbTemplate;
  private DropDownBox ddbObjectType;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public DesignerToolbarView(DesignerModel model) {
    getElement().setId(CSSConstants.CSS_CONTENT_TOOLBAR);
    setModel(model);
    this.addDomHandler(this, ChangeEvent.getType());
  }
  
  public void initialize(Sections section) {
    clear();
    add(getToolbar(section));
  }

  public int getApplicationId() {
    return getDdbApplication().getSelection().getId(); 
  }

  public int getTemplateId() {
    return getDdbTemplate().getSelection().getId(); 
  }

  public int getObjectTypeId() {
    return getDdbObjectType().getSelection().getId(); 
  }

  @Override
  public void onChange(ChangeEvent event) {
    //if (getApplicationId() > 0)
    getModel().setApplication((Application)getDdbApplication().getSelection().getUserObject());
    if (getTemplateId() > 0)
      getModel().setTemplate((Template)getDdbTemplate().getSelection().getUserObject());
    if (getObjectTypeId() > 0)
      getModel().setObjectType((ObjectType)getDdbObjectType().getSelection().getUserObject());

    NativeEvent nevent = Document.get().createChangeEvent();
    DomEvent.fireNativeEvent(nevent, getParent());
  }
  
  @Override
  public void onApplicationCreated(Application application) {
    fillApplications(getModel().getUserModel().getApplications().values());
  }

  @Override
  public void onApplicationUpdated(Application application) {
    fillApplications(getModel().getUserModel().getApplications().values());
  }

  @Override
  public void onApplicationsLoaded(Collection<Application> applications) {
    fillApplications(applications);
  }

  @Override
  public void onApplicationTemplatesLoaded(ArrayList<ApplicationTemplate> appts) {
    fillTemplates(appts);
  }

  
  @Override
  public void onObjectTypeCreated(ObjectType objectType) {
    fillObjectTypes(getModel().getObjectTypes());
  }

  @Override
  public void onObjectTypeUpdated(ObjectType objectType) {
    fillObjectTypes(getModel().getObjectTypes());
  }

  @Override
  public void onObjectTypesLoaded(Collection<ObjectType> objectTypes) {
    fillObjectTypes(objectTypes);
  }  
  
  @Override
  public void onLoad() {
    getModel().addDataObserver(this);
    getModel().addObjectTypeObserver(this);
    fireEvent(new ChangeEvent() {});
  }

  @Override
  public void onUnload() {
    getModel().removeDataObserver(this);
    getModel().removeObjectTypeObserver(this);
  }

  // getters and setters
  
  /**
   * @return the model
   */
  public DesignerModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(DesignerModel model) {
    this.model = model;
  }

  /**
   * Getter for property 'toolbar'.
   * 
   * @return Value for property 'toolbar'.
   */
  protected FlexTable getToolbar(Sections section) {
    if (toolbar == null) 
      toolbar = new FlexTable();
    else
      toolbar.clear();
      
    switch (section) {
    case APPLICATION:
      break;
    case TEMPLATEGROUP:
    case TEMPLATEATTRIBUTE:
    case TEMPLATERELATION:
      if (getModel().getTemplate() != null) 
        getDdbTemplate().setSelection(new DropDownObjectImpl(getModel().getTemplate().getId(), 
            getModel().getTemplate().getName(), getModel().getTemplate()));
      else
        getDdbApplication().setSelection(new DropDownObjectImpl(0, Main.constants.chooseTemplate()));

      toolbar.setWidget(0, 1, getDdbTemplate());
      toolbar.getCellFormatter().setWidth(0, 0, "200px");
    case TEMPLATE:
      if (getModel().getApplication() != null) 
        getDdbApplication().setSelection(new DropDownObjectImpl(getModel().getApplication().getId(), 
            getModel().getApplication().getName(), getModel().getApplication()));
      else
        getDdbApplication().setSelection(new DropDownObjectImpl(0, Main.constants.chooseApplication()));

      toolbar.setWidget(0, 0, getDdbApplication());
      toolbar.getCellFormatter().setWidth(0, 0, "200px");
      break;
    case OBJECTTYPE:
      break;
    case OBJECTATTRIBUTE:
    case OBJECTRELATION:
      toolbar.setWidget(0, 0, getDdbObjectType());
      toolbar.getCellFormatter().setWidth(0, 0, "200px");
      break;
    case VALUETYPE:
      break;
    case UNIT:
      break;
    case NONE:
      break;

    default:
      break;
    }

    return toolbar;
  }
  
  /**
   * Getter for property 'ddbApplication'.
   * 
   * @return Value for property 'ddbApplication'.
   */
  protected DropDownBox getDdbApplication() {
    if (ddbApplication == null) {
      ddbApplication = new DropDownBox(this, Main.constants.application(),
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
              getDdbTemplate().setSelection(
                  new DropDownObjectImpl(0, Main.constants.chooseTemplate()));
              ArrayList<ApplicationTemplate> appts = getModel()
                  .getAppTemplatesByApp().get(
                      getDdbApplication().getSelection().getId());
              if (appts == null) {
                ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), 
                    getModel().getApplication());
                atl.start();
              } else
                fillTemplates(appts);
            }
          });

      if (getModel().getUserModel().getApplications() != null)
        fillApplications(getModel().getUserModel().getApplications().values());
      ddbApplication.setSelection(new DropDownObjectImpl(0, Main.constants
          .chooseApplication()));
    }
    return ddbApplication;
  }
  
  
  /**
   * Getter for property 'ddbTemplate'.
   * 
   * @return Value for property 'ddbTemplate'.
   */
  protected DropDownBox getDdbTemplate() {
    if (ddbTemplate == null) {
      ddbTemplate = new DropDownBox(this, Main.constants.template(), 
          CSSConstants.SUFFIX_DESIGNER);
      
      if (getModel().getTemplate() != null)
        ddbTemplate.setSelection(new DropDownObjectImpl(getModel().getTemplate().getId(), 
            getModel().getTemplate().getName(), getModel().getTemplate()));
      else
        ddbTemplate.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseTemplate()));
      
      if (getModel().getApplication() != null) {
        ArrayList<ApplicationTemplate> appts = getModel()
            .getAppTemplatesByApp().get(
                getDdbApplication().getSelection().getId());
        if (appts == null) {
          ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), 
              getModel().getApplication());
          atl.start();
        } else
          fillTemplates(appts);
      }
    }
    return ddbTemplate;
  }

  
  /**
   * Getter for property 'ddbObjectType'.
   * 
   * @return Value for property 'ddbObjectType'.
   */
  protected DropDownBox getDdbObjectType() {
    if (ddbObjectType == null) {
      ddbObjectType = new DropDownBox(this, Main.constants.objectType(), 
          CSSConstants.SUFFIX_DESIGNER);
      
      if (getModel().getObjectType() != null)
        ddbObjectType.setSelection(new DropDownObjectImpl(getModel().getObjectType().getId(), 
            getModel().getObjectType().getName(), getModel().getObjectType()));
      else
        ddbObjectType.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectType()));
      
      if (getModel().getObjectTypes() == null) {
        ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
        otl.start();    
      } else
        fillObjectTypes(getModel().getObjectTypes());
    }
    return ddbObjectType;
  }
  
  // private methods

  private void fillApplications (Collection<Application> applications) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (Application a : applications) {
      items.add(new DropDownObjectImpl(a.getId(), a.getName(), a));
    }
    items.add(new DropDownObjectImpl(0, Main.constants.abandonedTemplate(), 
        new Application(Main.constants.abandonedTemplate())));
    getDdbApplication().setItems(items);
  }

  private void fillTemplates (Collection<ApplicationTemplate> apptemplates) {
    ArrayList<Template> templates = new ArrayList<Template>();
    for (ApplicationTemplate appt : apptemplates) 
      templates.add(appt.getT());
    Collections.sort(templates);
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (Template t : templates) {
      items.add(new DropDownObjectImpl(t.getId(), t.getName(), t));
    }
    getDdbTemplate().setItems(items);
  }
  
  private void fillObjectTypes (Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    items.add(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
    }
    getDdbObjectType().setItems(items);
  }
}
