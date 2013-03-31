package sk.benko.appsresource.client.model;

import java.util.*;

import sk.benko.appsresource.client.model.DbService.CreateOrUpdateApplicationResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateObjectAttributeResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateObjectTypeResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateTemplateAttributeResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateTemplateGroupResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateTemplateRelationResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateTemplateResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateUnitResult;
import sk.benko.appsresource.client.model.DbService.CreateOrUpdateValueTypeResult;
import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectRelationResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Encapsulates the entire application data controller for the application. The
 * model controls all RPC to the server and is responsible for keeping client
 * side copies of data synchronized with the server.
 *
 */
public class DesignerModel extends Model {

  /**
   * An observer interface to deliver all change events placed on {@link Unit}.
   */
  public interface UnitObserver {
    /**
     * Called when a new {@link Unit} is created.
     *
     * @param unit
     *          the unit that was created
     */
    void onUnitCreated(Unit unit);

    /**
     * Called when a new {@link Unit} is updated.
     *
     * @param unit
     *          the unit that was updated
     */
    void onUnitUpdated(Unit unit);

    /**
     * Called when a list of {@link Unit}s is loaded.
     *
     * @param units
     *          the list of units
     */
    void onUnitsLoaded(Collection<Unit> units);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link ValueType}.
   */
  public interface ValueTypeObserver {
    /**
     * Called when a new {@link ValueType} is created.
     *
     * @param valueType
     *          the value type that was created
     */
    void onValueTypeCreated(ValueType valueType);

    /**
     * Called when a new {@link ValueType} is updated.
     *
     * @param valueType
     *          the value type that was updated
     */
    void onValueTypeUpdated(ValueType valueType);

    /**
     * Called when a list of {@link ValueType}s is loaded.
     *
     * @param valueTypes
     *          the list of value types
     */
    void onValueTypesLoaded(Collection<ValueType> valueTypes);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link ObjectType}.
   */
  public interface ObjectTypeObserver {
    /**
     * Called when a new {@link ObjectType} is created.
     *
     * @param objectType
     *          the object type that was created
     */
    void onObjectTypeCreated(ObjectType objectType);

    /**
     * Called when a new {@link ObjectType} is updated.
     *
     * @param objectType
     *          the object type that was updated
     */
    void onObjectTypeUpdated(ObjectType objectType);

    /**
     * Called when a list of {@link ObjectType}s is loaded.
     *
     * @param objectTypes
     *          the list of object types
     */
    void onObjectTypesLoaded(Collection<ObjectType> objectTypes);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link ObjectAttribute}.
   */
  public interface ObjectAttributeObserver {
    /**
     * Called when a new {@link ObjectAttribute} is created.
     *
     * @param objectAttribute
     *          the object attribute that was created
     */
    void onObjectAttributeCreated(ObjectAttribute objectAttribute);

    /**
     * Called when a new {@link ObjectAttribute} is updated.
     *
     * @param objectAttribute
     *          the object attribute that was updated
     */
    void onObjectAttributeUpdated(ObjectAttribute objectAttribute);

    /**
     * Called when a list of {@link ObjectAttribute}s is loaded.
     *
     * @param otId
     *          object type id
     * @param objectAttributes
     *          the list of object attributes
     */
    void onObjectAttributesLoaded(int otId, 
        Collection<ObjectAttribute> objectAttributes);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link ObjectRelation}.
   */
  public interface ObjectRelationObserver {
    /**
     * Called when a new {@link ObjectRelation} is created.
     *
     * @param objectRelation
     *          the object relation that was created
     */
    void onObjectRelationCreated(ObjectRelation objectRelation);

    /**
     * Called when a new {@link ObjectRelation} is updated.
     *
     * @param objectRelation
     *          the object relation that was updated
     */
    void onObjectRelationUpdated(ObjectRelation objectRelation);

    /**
     * Called when a list of {@link ObjectRelation}s is loaded.
     *
     * @param otId
     *          object type id
     * @param objectRelations
     *          the list of object relations
     */
    void onObjectRelationsLoaded(int otId, 
        Collection<ObjectRelation> objectRelations);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link Template}.
   */
  public interface TemplateObserver {
    /**
     * Called when a new {@link Template} is created.
     *
     * @param template
     *          the template that was created
     */
    void onTemplateCreated(Template template);

    /**
     * Called when a new {@link Template} is updated.
     *
     * @param template
     *          the template that was updated
     */
    void onTemplateUpdated(Template template);

    /**
     * Called when a list of {@link Template}s is loaded.
     *
     * @param templates
     *          the list of templates
     */
    void onTemplatesLoaded(Collection<Template> templates);
  }
  
  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateRelation}.
   */
  public interface TemplateRelationObserver {
    /**
     * Called when a new {@link TemplateRelation} is created.
     *
     * @param templateRelation
     *          the template relation that was created
     */
    void onTemplateRelationCreated(TemplateRelation templateRelation);

    /**
     * Called when a new {@link TemplateRelation} is updated.
     *
     * @param templateRelation
     *          the template relation that was updated
     */
    void onTemplateRelationUpdated(TemplateRelation templateRelation);

    /**
     * Called when a list of {@link TemplateRelation}s is loaded.
     *
     * @param templateRelations
     *          the list of templateRelations
     */
    void onTemplateRelationsLoaded(Collection<TemplateRelation> templateRelations);
  }  
  
  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateGroup}.
   */
  public interface TemplateGroupObserver {
    /**
     * Called when a new {@link TemplateGroup} is created.
     *
     * @param templateGroup
     *          the template group that was created
     */
    void onTemplateGroupCreated(TemplateGroup templateGroup);

    /**
     * Called when a new {@link TemplateGroup} is updated.
     *
     * @param templateGroup
     *          the template group that was updated
     */
    void onTemplateGroupUpdated(TemplateGroup templateGroup);

    /**
     * Called when a list of {@link TemplateGroup}s is loaded.
     *
     * @param templateGroups
     *          the list of templateGroups
     */
    void onTemplateGroupsLoaded(Collection<TemplateGroup> templateGroups);
  }
  
  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateAttribute}.
   */
  public interface TemplateAttributeUpdateObserver {
    /**
     * Called when a new {@link TemplateAttribute} is created.
     *
     * @param templateAttribute
     *          the template attribute that was created
     */
    void onTemplateAttributeCreated(TemplateAttribute templateAttribute);

    /**
     * Called when a new {@link TemplateAttribute} is updated.
     *
     * @param templateAttribute
     *          the template attribute that was updated
     */
    void onTemplateAttributeUpdated(TemplateAttribute templateAttribute);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link TemplateRelation}.
   */
  public interface TemplateRelationUpdateObserver {
    /**
     * Called when a new {@link TemplateRelation} is created.
     *
     * @param templateRelation
     *          the template relation that was created
     */
    void onTemplateRelationCreated(TemplateRelation templateRelation);

    /**
     * Called when a new {@link TemplateRelation} is updated.
     *
     * @param templateRelation
     *          the template relation that was updated
     */
    void onTemplateRelationUpdated(TemplateRelation templateRelation);
  }

  /**
   * An observer interface to deliver all change events placed on 
   * {@link Application}.
   */
  public interface ApplicationObserver extends DataObserver {
    /**
     * Called when a new {@link Application} is created.
     *
     * @param application
     *          the application that was created
     */
    void onApplicationCreated(Application application);

    /**
     * Called when a new {@link Application} is updated.
     *
     * @param application
     *          the application that was updated
     */
    void onApplicationUpdated(Application application);
  }

  /**
   * A task that manages the call to the server to create or update 
   * an object type.
   */
  private class CreateOrUpdateObjectTypeTask extends Task implements
      AsyncCallback<CreateOrUpdateObjectTypeResult> {
    private final ObjectType objectType;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateObjectTypeTask(ObjectType objectType, AppUser author) {
      this.create = objectType.getId() == 0;
      this.author = author;
      this.objectType = objectType;
    }

    @Override
    public void execute() {
      api.createObjectType(objectType, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateObjectTypeResult result) {
      objectType.update(result.getId(), result.getCode(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      if (create) notifyObjectTypeCreated(objectType);
      else notifyObjectTypeUpdated(objectType);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * an object attribute.
   */
  private class CreateOrUpdateObjectAttributeTask extends Task implements
      AsyncCallback<CreateOrUpdateObjectAttributeResult> {
    private final ObjectAttribute objectAttribute;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateObjectAttributeTask(ObjectAttribute objectAttribute, AppUser author) {
      this.create = objectAttribute.getId() == 0;
      this.author = author;
      this.objectAttribute = objectAttribute;
    }

    @Override
    public void execute() {
      api.createObjectAttribute(objectAttribute, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateObjectAttributeResult result) {
      objectAttribute.update(result.getId(), result.getCode(),
          result.getUpdateTime());
      getQueue().taskSucceeded(this);
      if (create) notifyObjectAttributeCreated(objectAttribute);
      else  notifyObjectAttributeUpdated(objectAttribute);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * an object relation.
   */
  private class CreateOrUpdateObjectRelationTask extends Task implements
      AsyncCallback<CreateOrUpdateObjectRelationResult> {
    private final ObjectRelation objectRelation;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateObjectRelationTask(ObjectRelation objectRelation, AppUser author) {
      this.create = objectRelation.getId() == 0;
      this.author = author;
      this.objectRelation = objectRelation;
    }

    @Override
    public void execute() {
      api.createObjectRelation(objectRelation, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateObjectRelationResult result) {
      objectRelation.update(result.getId(), result.getCode(), 
          result.getUpdateTime());
      getQueue().taskSucceeded(this);
      if (create) notifyObjectRelationCreated(objectRelation);
      else  notifyObjectRelationUpdated(objectRelation);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * a value type.
   */
  private class CreateOrUpdateValueTypeTask extends Task implements
      AsyncCallback<CreateOrUpdateValueTypeResult> {
    private final ValueType valueType;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateValueTypeTask(ValueType valueType, AppUser author) {
      this.create = valueType.getId() == 0;
      this.author = author;
      this.valueType = valueType;
    }

    @Override
    public void execute() {
      api.createValueType(valueType, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateValueTypeResult result) {
      valueType.update(result.getId(), result.getCode(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      if (create) notifyValueTypeCreated(valueType);
      else notifyValueTypeUpdated(valueType);
    }
  }
  
  /**
   * A task that manages the call to the server to create or update an unit.
   */
  private class CreateOrUpdateUnitTask extends Task implements
      AsyncCallback<CreateOrUpdateUnitResult> {
    private final Unit unit;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateUnitTask(Unit unit, AppUser author) {
      this.create = unit.getId() == 0;
      this.author = author;
      this.unit = unit;
    }

    @Override
    public void execute() {
      api.createUnit(unit, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateUnitResult result) {
      unit.update(result.getId(), result.getCode(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      if (create) notifyUnitCreated(unit);
      else notifyUnitUpdated(unit);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * a template.
   */
  private class CreateOrUpdateTemplateTask extends Task implements
      AsyncCallback<CreateOrUpdateTemplateResult> {
    private final Template template;
    private final HashMap<TemplateTree,ArrayList<TemplateTreeItem>> trees;
    private final HashMap<TemplateList,ArrayList<TemplateListItem>> lists;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateTemplateTask(Template template, 
        HashMap<TemplateTree, ArrayList<TemplateTreeItem>> trees, 
        HashMap<TemplateList, ArrayList<TemplateListItem>> lists,
        AppUser author) {
      this.create = template.getId() == 0;
      this.author = author;
      this.template = template;
      this.trees = trees;
      this.lists = lists;
    }

    @Override
    public void execute() {
      api.createTemplate(template, trees, lists, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateTemplateResult result) {
      getQueue().taskSucceeded(this);
      template.update(result.getId(), result.getCode(), result.getUpdateTime());
      for (TemplateTree tt : trees.keySet()) 
        getTreeItems().remove(tt.getId());
      for (TemplateList tl : lists.keySet()) 
        getListItems().remove(tl.getId());
      
      if (create) notifyTemplateCreated(template);
      else  notifyTemplateUpdated(template);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * a template relation.
   */
  private class CreateOrUpdateTemplateRelationTask extends Task implements
      AsyncCallback<CreateOrUpdateTemplateRelationResult> {
    private final TemplateRelation templateRelation;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateTemplateRelationTask(TemplateRelation templateRelation, AppUser author) {
      this.create = templateRelation.getId() == 0;
      this.author = author;
      this.templateRelation = templateRelation;
    }

    @Override
    public void execute() {
      api.createTemplateRelation(templateRelation, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateTemplateRelationResult result) {
      getQueue().taskSucceeded(this);
      templateRelation.update(result.getId(), result.getCode(), result.getUpdateTime());
      if (create) notifyTemplateRelationCreated(templateRelation);
      else  notifyTemplateRelationUpdated(templateRelation);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * a template group.
   */
  private class CreateOrUpdateTemplateGroupTask extends Task implements
      AsyncCallback<CreateOrUpdateTemplateGroupResult> {
    private final TemplateGroup templateGroup;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateTemplateGroupTask(TemplateGroup templateGroup, AppUser author) {
      this.create = templateGroup.getId() == 0;
      this.author = author;
      this.templateGroup = templateGroup;
    }

    @Override
    public void execute() {
      api.createTemplateGroup(templateGroup, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateTemplateGroupResult result) {
      getQueue().taskSucceeded(this);
      templateGroup.update(result.getId(), result.getCode(), result.getUpdateTime());
      if (create) notifyTemplateGroupCreated(templateGroup);
      else  notifyTemplateGroupUpdated(templateGroup);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * a template attribute.
   */
  private class CreateOrUpdateTemplateAttributeTask extends Task implements
      AsyncCallback<CreateOrUpdateTemplateAttributeResult> {
    private final TemplateAttribute templateAttribute;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateTemplateAttributeTask(TemplateAttribute templateAttribute, AppUser author) {
      this.create = templateAttribute.getId() == 0;
      this.author = author;
      this.templateAttribute = templateAttribute;
    }

    @Override
    public void execute() {
      api.createTemplateAttribute(templateAttribute, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateTemplateAttributeResult result) {
      getQueue().taskSucceeded(this);
      templateAttribute.update(result.getId(), result.getCode(), result.getUpdateTime());
      if (create) notifyTemplateAttributeCreated(templateAttribute);
      else  notifyTemplateAttributeUpdated(templateAttribute);
    }
  }

  /**
   * A task that manages the call to the server to create or update 
   * an application.
   */
  private class CreateOrUpdateApplicationTask extends Task implements
      AsyncCallback<CreateOrUpdateApplicationResult> {
    private final Application application;
    private final ArrayList<ApplicationTemplate> appts;
    private final AppUser author;
    private boolean create;

    public CreateOrUpdateApplicationTask(Application application, 
        ArrayList<ApplicationTemplate> appts, AppUser author) {
      this.create = application.getId() == 0;
      this.author = author;
      this.application = application;
      this.appts = appts;
    }

    @Override
    public void execute() {
      api.createOrUpdateApplication(application, appts, author, this);
    }

    public void onFailure(Throwable caught) {
      getQueue().taskFailed(this,
          caught instanceof DbService.AccessDeniedException);
    }

    public void onSuccess(CreateOrUpdateApplicationResult result) {
      application.update(result.getId(), result.getCode(), result.getUpdateTime());
      getQueue().taskSucceeded(this);
      if (create) notifyApplicationCreated(application);
      else notifyApplicationUpdated(application);
    }
  }

  final UserModel umodel;
  
  /**
   * An rpc proxy for making calls to the server.
   */
  final DbServiceAsync api;

  /** Current application */
  private Application application;
  
  /** Current application */
  private Template template;

  /** Current object type */
  private ObjectType objectType;
  
  /** Object types */
  private List<ObjectType> objectTypes;

  /**
   * The list of the observers monitoring the model for object type related
   * events.
   */
  private final List<ObjectTypeObserver> objectTypeObservers = new ArrayList<ObjectTypeObserver>();

  /** Cache for object attributes indexed by object type id */
  private Map<Integer, List<ObjectAttribute>> objectAttributes;

  /**
   * The list of the observers monitoring the model for object attribute related
   * events.
   */
  private final List<ObjectAttributeObserver> objectAttributeObservers = new ArrayList<ObjectAttributeObserver>();

  /** Object relations */
  private Map<Integer, List<ObjectRelation>> objectRelations;

  /**
   * The list of the observers monitoring the model for object relation related
   * events.
   */
  private final List<ObjectRelationObserver> objectRelationObservers = new ArrayList<ObjectRelationObserver>();

  /** Value types*/
  private List<ValueType> valueTypes;

  /**
   * The list of the observers monitoring the model for value type related
   * events.
   */
  private final List<ValueTypeObserver> valueTypeObservers = new ArrayList<ValueTypeObserver>();

  /** Units */
  private List<Unit> units;

  /**
   * The list of the observers monitoring the model for data related events.
   */
  private final List<UnitObserver> unitObservers = new ArrayList<UnitObserver>();
  
  /** Templates */
  private List<Template> templates;

  /**
   * The list of the observers monitoring the model for data related events.
   */
  private final List<TemplateObserver> templateObservers = new ArrayList<TemplateObserver>();

  /** Cache for template groups indexed by template id */
  private Map<Integer, List<TemplateGroup>> templateGroups;

  /**
   * The list of the observers monitoring the model for data related events.
   */
  private final List<TemplateGroupObserver> templateGroupObservers = new ArrayList<TemplateGroupObserver>();

  /**
   * The list of the observers monitoring the model for data related events.
   */
  private final List<TemplateAttributeUpdateObserver> templateAttributeUpdateObservers = 
      new ArrayList<TemplateAttributeUpdateObserver>();

  private final List<TemplateRelationUpdateObserver> templateRelationUpdateObservers = 
      new ArrayList<TemplateRelationUpdateObserver>();

  public DesignerModel(UserModel umodel, DbServiceAsync api, 
      StatusObserver statusObserver) {
    super(statusObserver);
    this.api = api;
    this.umodel = umodel;
  }

  public UserModel getUserModel() {
    return umodel;
  }

  @Override
  public ServiceAsync getService() {
    return api;
  }

  /**
   * Subscribes a {@link UnitObserver} to receive data related events from this
   * {@link DesignerModel}.
   *
   * @param observer
   */
  public void addUnitObserver(UnitObserver observer) {
    unitObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link UnitObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeUnitObserver(UnitObserver observer) {
    unitObservers.remove(observer);
  }

  /**
   * Subscribes a {@link ValueTypeObserver} to receive data related events from this
   * {@link DesignerModel}.
   *
   * @param observer
   */
  public void addValueTypeObserver(ValueTypeObserver observer) {
    valueTypeObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link ValueTypeObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeValueTypeObserver(ValueTypeObserver observer) {
    valueTypeObservers.remove(observer);
  }

  /**
   * Subscribes a {@link ObjectAttributeObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addObjectAttributeObserver(ObjectAttributeObserver observer) {
    objectAttributeObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link ObjectAttributeObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeObjectAttributeObserver(ObjectAttributeObserver observer) {
    objectAttributeObservers.remove(observer);
  }

  /**
   * Subscribes a {@link ObjectRelationObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addObjectRelationObserver(ObjectRelationObserver observer) {
    objectRelationObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link ObjectRelationObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeObjectRelationObserver(ObjectRelationObserver observer) {
    objectRelationObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link ObjectTypeObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addObjectTypeObserver(ObjectTypeObserver observer) {
    objectTypeObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link ObjectTypeObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeObjectTypeObserver(ObjectTypeObserver observer) {
    objectTypeObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link TemplateObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addTemplateObserver(TemplateObserver observer) {
    templateObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeTemplateObserver(TemplateObserver observer) {
    templateObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link TemplateGroupObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addTemplateGroupObserver(TemplateGroupObserver observer) {
    templateGroupObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateGroupObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeTemplateGroupObserver(TemplateGroupObserver observer) {
    templateGroupObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link TemplateRelationObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addTemplateRelationUpdateObserver(TemplateRelationUpdateObserver observer) {
    templateRelationUpdateObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateRelationObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeTemplateRelationUpdateObserver(TemplateRelationUpdateObserver observer) {
    templateRelationUpdateObservers.remove(observer);
  }
  
  /**
   * Subscribes a {@link TemplateAttributeUpdateObserver} to receive data related events 
   * from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void addTemplateAttributeUpdateObserver(TemplateAttributeUpdateObserver observer) {
    templateAttributeUpdateObservers.add(observer);
  }

  /**
   * Unsubscribes a {@link TemplateAttributeUpdateObserver from this {@link DesignerModel}.
   *
   * @param observer
   */
  public void removeTemplateAttributeUpdateObserver(TemplateAttributeUpdateObserver observer) {
    templateAttributeUpdateObservers.remove(observer);
  }
  
  /**
   * Creates an {@link ObjectType} with the specified name and persists that 
   * change to the server.
   *
   * @param objectType
   */
  public void createOrUpdateObjectType(final ObjectType objectType) {
    taskQueue.post(new CreateOrUpdateObjectTypeTask(objectType, umodel.getCurrentAuthor()));
  }

  /**
   * Creates an {@link ObjectAttribute} with the specified name and persists that 
   * change to the server.
   *
   * @param objectRelation
   */
  public void createOrUpdateObjectRelation(final ObjectRelation objectRelation) {
    taskQueue.post(new CreateOrUpdateObjectRelationTask(objectRelation, umodel.getCurrentAuthor()));
  }

  /**
   * Creates an {@link ObjectAttribute} with the specified name and persists that 
   * change to the server.
   *
   * @param objectAttribute
   */
  public void createOrUpdateObjectAttribute(final ObjectAttribute objectAttribute) {
    taskQueue.post(new CreateOrUpdateObjectAttributeTask(objectAttribute, umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link ValueType} with the specified name and persists that 
   * change to the server.
   *
   * @param valueType
   */
  public void createOrUpdateValueType(final ValueType valueType) {
    taskQueue.post(new CreateOrUpdateValueTypeTask(valueType, umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link Unit} with the specified name and persists that 
   * change to the server.
   *
   * @param unit
   */
  public void createOrUpdateUnit(final Unit unit) {
    taskQueue.post(new CreateOrUpdateUnitTask(unit, umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link Template} with the specified name and persists that 
   * change to the server.
   *
   * @param template
   */
  public void createOrUpdateTemplate(final Template template, 
      HashMap<TemplateTree,ArrayList<TemplateTreeItem>> trees,
      HashMap<TemplateList,ArrayList<TemplateListItem>> lists) {
    taskQueue.post(new CreateOrUpdateTemplateTask(template, trees, lists, 
        umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link TemplateRelation} with the specified name and persists that 
   * change to the server.
   *
   * @param templateRelation
   */
  public void createOrUpdateTemplateRelation(final TemplateRelation templateRelation) {
    taskQueue.post(new CreateOrUpdateTemplateRelationTask(templateRelation, umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link TemplateGroup} with the specified name and persists that 
   * change to the server.
   *
   * @param templateGroup
   */
  public void createOrUpdateTemplateGroup(final TemplateGroup templateGroup) {
    taskQueue.post(new CreateOrUpdateTemplateGroupTask(templateGroup, umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link TemplateAttribute} with the specified name and persists that 
   * change to the server.
   *
   * @param templateAttribute
   */
  public void createOrUpdateTemplateAttribute(final TemplateAttribute templateAttribute) {
    taskQueue.post(new CreateOrUpdateTemplateAttributeTask(templateAttribute,umodel.getCurrentAuthor()));
  }

  /**
   * Creates a {@link Application} with the specified name and persists that 
   * change to the server.
   *
   * @param application
   * @param appts
   */
  public void createOrUpdateApplication(final Application application, 
      final ArrayList<ApplicationTemplate> appts) {
    taskQueue.post(new CreateOrUpdateApplicationTask(application, appts,
        umodel.getCurrentAuthor()));
  }

  /**
   * @return the application
   */
  public Application getApplication() {
    return application;
  }

  /**
   * @param application the application to set
   */
  public void setApplication(Application application) {
    this.application = application;
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
   * @return the objectType
   */
  public ObjectType getObjectType() {
    return objectType;
  }

  /**
   * @param objectType the objectType to set
   */
  public void setObjectType(ObjectType objectType) {
    this.objectType = objectType;
  }

  /**
   * Gets object types.
   *
   * @return
   */
  public List<ObjectType> getObjectTypes() {
    return objectTypes;
  }

  /**
   * @param objectTypes the objectTypes to set
   */
  public void setObjectTypes(List<ObjectType> objectTypes) {
    this.objectTypes = objectTypes;
  }

  /**
   * Gets object attributes.
   *
   * @return
   */
  public Map<Integer, List<ObjectAttribute>> getObjectAttributes() {
    if (objectAttributes == null)
      objectAttributes = new HashMap<Integer, List<ObjectAttribute>>();
    return objectAttributes;
  }

  /**
   * Gets object attributes.
   *
   * @return
   */
  public Map<Integer, List<ObjectRelation>> getObjectRelations() {
    if (objectRelations == null)
      objectRelations = new HashMap<Integer, List<ObjectRelation>>();
    return objectRelations;
  }

  /**
   * Gets value types.
   *
   * @return
   */
  public List<ValueType> getValueTypes() {
    return valueTypes;
  }

  /**
   * @param valueTypes the value types to set
   */
  public void setValueTypes(List<ValueType> valueTypes) {
    this.valueTypes = valueTypes;
  }

  /**
   * Gets units.
   *
   * @return units
   */
  public List<Unit> getUnits() {
    return units;
  }
  
  /**
   * @param units the units to set
   */
  public void setUnits(List<Unit> units) {
    this.units = units;
  }

  /**
   * Gets templates.
   *
   * @return
   */
  public List<Template> getTemplates() {
    return templates;
  }

  /**
   * @param templates the templates to set
   */
  public void setTemplates(List<Template> templates) {
    this.templates = templates;
  }

  /**
   * Gets template groups.
   *
   * @return
   */
  public Map<Integer, List<TemplateGroup>> getTemplateGroups() {
    if (templateGroups == null)
      templateGroups = new HashMap<Integer, List<TemplateGroup>>();
    return templateGroups;
  }

  public List<TemplateGroup> getTemplateGroup(int tId) {
    return getTemplateGroups().get(tId);
  }

  /**
   * Events
   * 
   */
  
  // object type events
  public void notifyObjectTypesLoaded(List<ObjectType> objectTypes) {
    setObjectTypes(objectTypes);
    List<ObjectTypeObserver> clones = new ArrayList<ObjectTypeObserver>(objectTypeObservers);
    for (ObjectTypeObserver objectTypeObserver : clones) {
      objectTypeObserver.onObjectTypesLoaded(objectTypes);
    }
  }

  public void notifyObjectTypeCreated(ObjectType objectType) {
    getObjectTypes().add(objectType);
    Collections.sort(getObjectTypes());

    List<ObjectTypeObserver> clones = new ArrayList<ObjectTypeObserver>(objectTypeObservers);
    for (ObjectTypeObserver objectTypeObserver : clones) {
      objectTypeObserver.onObjectTypeCreated(objectType);
    }
  }

  public void notifyObjectTypeUpdated(ObjectType objectType) {
    for (int i = 0; i < getObjectTypes().size(); i++)
      if (getObjectTypes().get(i).getId() == objectType.getId()) {
        getObjectTypes().remove(i);
        break;
      }
    getObjectTypes().add(objectType);
    Collections.sort(getObjectTypes());

    for (ObjectTypeObserver objectTypeObserver : objectTypeObservers) {
      objectTypeObserver.onObjectTypeUpdated(objectType);
    }
  }

  // object attribute events
  public void notifyObjectAttributesLoaded(int otId, List<ObjectAttribute> objectAttributes) {
    getObjectAttributes().put(otId, objectAttributes);
    List<ObjectAttributeObserver> clones = new ArrayList<ObjectAttributeObserver>(objectAttributeObservers);
    for (ObjectAttributeObserver objectAttributeObserver : clones) {
      objectAttributeObserver.onObjectAttributesLoaded(otId, objectAttributes);
    }
  }

  void notifyObjectAttributeCreated(ObjectAttribute objectAttribute) {
    if (getObjectAttributes().get(objectAttribute.getOtId()) != null) {
      List<ObjectAttribute> attrs = getObjectAttributes().get(objectAttribute.getOtId());
      attrs.add(objectAttribute);
      Collections.sort(attrs);
    }
    List<ObjectAttributeObserver> clones = new ArrayList<ObjectAttributeObserver>(objectAttributeObservers);
    for (ObjectAttributeObserver objectAttributeObserver : clones) {
      objectAttributeObserver.onObjectAttributeCreated(objectAttribute);
    }
  }

  void notifyObjectAttributeUpdated(ObjectAttribute objectAttribute) {
    if (getObjectAttributes().get(objectAttribute.getOtId()) != null) {
      List<ObjectAttribute> attrs = getObjectAttributes().get(objectAttribute.getOtId());
      for (int i = 0; i < attrs.size(); i++)
        if (attrs.get(i).getId() == objectAttribute.getId()) {
          attrs.remove(i);
          break;
        }
      attrs.add(objectAttribute);
      Collections.sort(attrs);
    }
    for (ObjectAttributeObserver objectAttributeObserver : objectAttributeObservers) {
      objectAttributeObserver.onObjectAttributeUpdated(objectAttribute);
    }
  }

  // object relation events
  public void notifyObjectRelationsLoaded(int otId, List<ObjectRelation> objectRelations) {
    getObjectRelations().put(otId, objectRelations);
    List<ObjectRelationObserver> clones = new ArrayList<ObjectRelationObserver>(objectRelationObservers);
    for (ObjectRelationObserver objectRelationObserver : clones) {
      objectRelationObserver.onObjectRelationsLoaded(otId, objectRelations);
    }
  }
  
  public void notifyObjectRelationCreated(ObjectRelation objectRelation) {
    if (getObjectRelations().get(objectRelation.getOt1Id()) != null) {
      List<ObjectRelation> rels = getObjectRelations().get(objectRelation.getOt1Id());
      rels.add(objectRelation);
      Collections.sort(rels);
    } else {
      ArrayList<ObjectRelation> rels = new ArrayList<ObjectRelation>();
      rels.add(objectRelation);
      getObjectRelations().put(objectRelation.getOt1Id(), rels);
    }
    
    List<ObjectRelationObserver> clones = new ArrayList<ObjectRelationObserver>(objectRelationObservers);
    for (ObjectRelationObserver objectRelationObserver : clones) {
      objectRelationObserver.onObjectRelationCreated(objectRelation);
    }
  }

  public void notifyObjectRelationUpdated(ObjectRelation objectRelation) {
    if (getObjectRelations().get(objectRelation.getOt1Id()) != null) {
      List<ObjectRelation> rels = getObjectRelations().get(objectRelation.getOt1Id());
      for (int i = 0; i < rels.size(); i++)
        if (rels.get(i).getId() == objectRelation.getId()) {
          rels.remove(i);
          break;
        }
      rels.add(objectRelation);
      Collections.sort(rels);
    }

    for (ObjectRelationObserver objectRelationObserver : objectRelationObservers) {
      objectRelationObserver.onObjectRelationUpdated(objectRelation);
    }
  }

  // value type events
  public void notifyValueTypesLoaded(List<ValueType> valueTypes) {
    sortValueTypes(valueTypes);
    List<ValueTypeObserver> clones = new ArrayList<ValueTypeObserver>(valueTypeObservers);
    for (ValueTypeObserver valueTypeObserver : clones) {
      valueTypeObserver.onValueTypesLoaded(getValueTypes());
    }
  }

  public void notifyValueTypeCreated(ValueType valueType) {
    if (getValueTypes() != null) {
      getValueTypes().add(valueType);
      sortValueTypes(getValueTypes());
    }
    List<ValueTypeObserver> clones = new ArrayList<ValueTypeObserver>(valueTypeObservers);
    for (ValueTypeObserver valueTypeObserver : clones) {
      valueTypeObserver.onValueTypeCreated(valueType);
    }
  }

  public void notifyValueTypeUpdated(ValueType valueType) {
    if (getValueTypes() != null) {
      for (int i = 0; i < getValueTypes().size(); i++)
        if (getValueTypes().get(i).getId() == valueType.getId()) {
          getValueTypes().remove(i);
          break;
        }
      getValueTypes().add(valueType);
      sortValueTypes(getValueTypes());
    }

    List<ValueTypeObserver> clones = new ArrayList<ValueTypeObserver>(valueTypeObservers);
    for (ValueTypeObserver valueTypeObserver : clones) {
      valueTypeObserver.onValueTypeUpdated(valueType);
    }
  }
  
  // unit events
  public void notifyUnitsLoaded(List<Unit> units) {
    setUnits(units);
    List<UnitObserver> clones = new ArrayList<UnitObserver>(unitObservers);
    for (UnitObserver unitObserver : clones) {
      unitObserver.onUnitsLoaded(units);
    }
  }

  public void notifyUnitCreated(Unit unit) {
    if (getUnits() != null) {
      getUnits().add(unit);
      Collections.sort(getUnits());
    }
    List<UnitObserver> clones = new ArrayList<UnitObserver>(unitObservers);
    for (UnitObserver unitObserver : clones) {
      unitObserver.onUnitCreated(unit);
    }
  }

  public void notifyUnitUpdated(Unit unit) {
    if (getUnits() != null) {
      for (int i = 0; i < getUnits().size(); i++)
        if (getUnits().get(i).getId() == unit.getId()) {
          getUnits().remove(i);
          break;
        }
      getUnits().add(unit);
      Collections.sort(getUnits());
    }

    List<UnitObserver> clones = new ArrayList<UnitObserver>(unitObservers);
    for (UnitObserver unitObserver : clones) {
      unitObserver.onUnitUpdated(unit);
    }
  }

  // template events
  public void notifyTemplatesLoaded(List<Template> templates) {
    setTemplates(templates);
    List<TemplateObserver> clones = new ArrayList<TemplateObserver>(templateObservers);
    for (TemplateObserver templateObserver : clones) {
      templateObserver.onTemplatesLoaded(templates);
    }
  }

  void notifyTemplateCreated(Template template) {
    if (getTemplates() != null) {
      getTemplates().add(template);
      Collections.sort(getTemplates());
    }
    
    getAppTemplatesByApp().remove(0);
    List<TemplateObserver> clones = new ArrayList<TemplateObserver>(templateObservers);
    for (TemplateObserver templateObserver : clones) {
      templateObserver.onTemplateCreated(template);
    }
  }

  void notifyTemplateUpdated(Template template) {
    if (getTemplates() != null) {
      for (int i = 0; i < getTemplates().size(); i++)
        if (getTemplates().get(i).getId() == template.getId())
          getTemplates().remove(i);
      getTemplates().add(template);
      Collections.sort(getTemplates());
    }
    
    for (List<ApplicationTemplate> appts : getAppTemplatesByApp().values()) {
      for (ApplicationTemplate appt : appts) {
        if (appt.getTId() == template.getId())
          appt.setT(template);
      }
    }
    for (TemplateObserver templateObserver : templateObservers) {
      templateObserver.onTemplateUpdated(template);
    }
  }

  // template group events
  void notifyTemplateGroupsLoaded(Template template, List<TemplateGroup> templateGroups) {
    getTemplateGroups().put(template.getId(), templateGroups);
    
    List<TemplateGroupObserver> clones = new ArrayList<TemplateGroupObserver>(templateGroupObservers);
    for (TemplateGroupObserver templateGroupObserver : clones) {
      templateGroupObserver.onTemplateGroupsLoaded(templateGroups);
    }
  }

  void notifyTemplateGroupCreated(TemplateGroup templateGroup) {
    if (getTemplateGroups().get(templateGroup.getTId()) != null) {
      List<TemplateGroup> tgs = getTemplateGroups().get(templateGroup.getTId());
      tgs.add(templateGroup);
      Collections.sort(tgs);
    }
    List<TemplateGroupObserver> clones = new ArrayList<TemplateGroupObserver>(templateGroupObservers);
    for (TemplateGroupObserver templateGroupObserver : clones) {
      templateGroupObserver.onTemplateGroupCreated(templateGroup);
    }
  }

  void notifyTemplateGroupUpdated(TemplateGroup templateGroup) {
    if (getTemplateGroups().get(templateGroup.getTId()) != null) {
      List<TemplateGroup> tgs = getTemplateGroups().get(templateGroup.getTId());
      for (int i = 0; i < tgs.size(); i++)
        if (tgs.get(i).getId() == templateGroup.getId()) {
          tgs.remove(i);
          break;
        }
      tgs.add(templateGroup);
      Collections.sort(tgs);
    }
    for (TemplateGroupObserver templateGroupObserver : templateGroupObservers) {
      templateGroupObserver.onTemplateGroupUpdated(templateGroup);
    }
  }

  // template attribute events

  void notifyTemplateAttributeCreated(TemplateAttribute templateAttribute) {
    if (getAttrsByTemplate().get(templateAttribute.getTg().getTId()) != null) {
      List<TemplateAttribute> tas = getAttrsByTemplate().get(templateAttribute.getTg().getTId());
      tas.add(templateAttribute);
      Collections.sort(tas);
    }
    List<TemplateAttributeUpdateObserver> clones = 
        new ArrayList<TemplateAttributeUpdateObserver>(templateAttributeUpdateObservers);
    for (TemplateAttributeUpdateObserver templateAttributeObserver : clones) {
      templateAttributeObserver.onTemplateAttributeCreated(templateAttribute);
    }
  }

  void notifyTemplateAttributeUpdated(TemplateAttribute templateAttribute) {
    if (getAttrsByTemplate().get(templateAttribute.getTg().getTId()) != null) {
      List<TemplateAttribute> tas = getAttrsByTemplate().get(templateAttribute.getTg().getTId());
      for (int i = 0; i < tas.size(); i++)
        if (tas.get(i).getId() == templateAttribute.getId()) {
          tas.remove(i);
          break;
        }
      tas.add(templateAttribute);
      Collections.sort(tas);
    }
    for (TemplateAttributeUpdateObserver templateAttributeObserver : templateAttributeUpdateObservers) {
      templateAttributeObserver.onTemplateAttributeUpdated(templateAttribute);
    }
  }
  
  // template relation events
  void notifyTemplateRelationCreated(TemplateRelation templateRelation) {
    if (getRelsByTemplate().get(templateRelation.getT1Id()) != null) {
      List<TemplateRelation> trs = getRelsByTemplate().get(templateRelation.getT1Id());
      trs.add(templateRelation);
      Collections.sort(trs);
    }
    List<TemplateRelationUpdateObserver> clones = 
        new ArrayList<TemplateRelationUpdateObserver>(templateRelationUpdateObservers);
    for (TemplateRelationUpdateObserver templateRelationUpdateObserver : clones) {
      templateRelationUpdateObserver.onTemplateRelationCreated(templateRelation);
    }
  }

  void notifyTemplateRelationUpdated(TemplateRelation templateRelation) {
    if (getRelsByTemplate().get(templateRelation.getT1Id()) != null) {
      List<TemplateRelation> trs = getRelsByTemplate().get(templateRelation.getT1Id());
      for (int i = 0; i < trs.size(); i++)
        if (trs.get(i).getId() == templateRelation.getId()) {
          trs.remove(i);
          break;
        }
      trs.add(templateRelation);
      Collections.sort(trs);
    }
    for (TemplateRelationUpdateObserver templateRelationUpdateObserver : templateRelationUpdateObservers) {
      templateRelationUpdateObserver.onTemplateRelationUpdated(templateRelation);
    }
  }
  
  // application events

  void notifyApplicationCreated(Application application) {
    getAppTemplatesByApp().remove(application.getId());
    ArrayList<Application> apps = new ArrayList<Application>(
        getUserModel().getApplications().values());
    apps.add(application);
    Collections.sort(apps);
    getUserModel().setApplications(new LinkedHashMap<Integer, Application>());
    for (Application app : apps) {
      getUserModel().getApplications().put(app.getId(), app);
    }
    
    List<DataObserver> clones = new ArrayList<DataObserver>(dataObservers);
    for (DataObserver dataObserver : clones) {
      if (dataObserver instanceof ApplicationObserver)
        ((ApplicationObserver)dataObserver).onApplicationCreated(application);
    }
  }

  void notifyApplicationUpdated(Application application) {
    getAppTemplatesByApp().remove(application.getId());
    getUserModel().getApplications().put(application.getId(), application);
    ArrayList<Application> apps = new ArrayList<Application>(
        getUserModel().getApplications().values());
    Collections.sort(apps);
    getUserModel().setApplications(new LinkedHashMap<Integer, Application>());
    for (Application app : apps) {
      getUserModel().getApplications().put(app.getId(), app);
    }
    
    for (DataObserver dataObserver : dataObservers) {
      if (dataObserver instanceof ApplicationObserver)
        ((ApplicationObserver)dataObserver).onApplicationUpdated(application);
    }
  }
  
  // private methods
  private void sortValueTypes(List<ValueType> valueTypes) {
    List<ValueType> vts = new ArrayList<ValueType>();
    List<ValueType> vts_custom = new ArrayList<ValueType>();
    for (ValueType vt : valueTypes) {
      if (vt.isSystem())
        vts.add(vt);
      else
        vts_custom.add(vt);
    }
    Collections.sort(vts_custom);
    vts.addAll(vts_custom);
    setValueTypes(vts);
  }
}
