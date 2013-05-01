package sk.benko.appsresource.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectRelationResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The RPC api available to the client. The asynchronous version that is used
 * directly by the client is {@link DbServiceAsync}.
 */
@RemoteServiceRelativePath("dbservice")
public interface DbService extends Service {

  /**
   * Get all object types for the currently logged in author.
   *
   * @return
   * @throws AccessDeniedException
   */
  List<ObjectType> getObjectTypes() throws AccessDeniedException;

  /**
   * Get all object filterAttributes for given object type.
   *
   * @param otId an id of object type
   * @return
   * @throws AccessDeniedException
   */
  List<ObjectAttribute> getObjectAttributes(int otId) throws AccessDeniedException;

  /**
   * Get all object relations for given object type.
   *
   * @param otId an id of object type
   * @return
   * @throws AccessDeniedException
   */
  List<ObjectRelation> getObjectRelations(int otId) throws AccessDeniedException;

  /**
   * Get all value types.
   *
   * @return
   * @throws AccessDeniedException
   */
  List<ValueType> getValueTypes() throws AccessDeniedException;

  /**
   * Get all units for the currently logged in author.
   *
   * @return
   * @throws AccessDeniedException
   */
  List<Unit> getUnits() throws AccessDeniedException;

  /**
   * Get all templates for the currently logged in author.
   *
   * @return
   * @throws AccessDeniedException
   */
  List<Template> getTemplates() throws AccessDeniedException;

  /**
   * Get all template groups for the currently logged in author.
   *
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateRelation> getTemplateRelations()  throws AccessDeniedException;

  List<TemplateGroup> getTemplateGroups(Template template) throws AccessDeniedException;

  /**
   * Create a new {@link ObjectType}.
   *
   * @param objectType the object type
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateObjectTypeResult createObjectType(ObjectType objectType, AppUser author);

  /**
   * Create a new {@link ObjectAttribute}.
   *
   * @param objectAttribute the object attribute
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateObjectAttributeResult createObjectAttribute(ObjectAttribute objectAttribute, AppUser author);

  /**
   * Create a new {@link ObjectRelation}.
   *
   * @param objectRelation the object relation
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateObjectRelationResult createObjectRelation(ObjectRelation objectRelation, AppUser author);

  /**
   * Create a new {@link ValueType}.
   *
   * @param valueType the value type
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateValueTypeResult createValueType(ValueType valueType, AppUser author);

  /**
   * Create a new {@link Unit}.
   *
   * @param unit the unit
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateUnitResult createUnit(Unit unit, AppUser author);

  /**
   * Create a new {@link Template}.
   *
   * @param template the template
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateTemplateResult createTemplate(Template template,
                                              HashMap<TemplateTree, ArrayList<TemplateTreeItem>> trees,
                                              HashMap<TemplateList, ArrayList<TemplateListItem>> lists,
                                              AppUser author);

  /**
   * Create a new {@link TemplateRelation}.
   *
   * @param templateRelation the template relation
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateTemplateRelationResult createTemplateRelation(TemplateRelation templateRelation, AppUser author);

  /**
   * Create a new {@link TemplateGroup}.
   *
   * @param templateGroup the template group
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateTemplateGroupResult createTemplateGroup(TemplateGroup templateGroup, AppUser author);

  /**
   * Create a new {@link TemplateAttribute}.
   *
   * @param templateAttribute the template attribute
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateTemplateAttributeResult createTemplateAttribute(TemplateAttribute templateAttribute, AppUser author);

  /**
   * Create a new {@link Application}.
   *
   * @param application the application
   * @param appts       the list of template belongs to the application
   * @return a result object
   * @throws AccessDeniedException
   */
  CreateOrUpdateApplicationResult createOrUpdateApplication(Application application,
                                                            ArrayList<ApplicationTemplate> appts, AppUser author);

  /**
   * Encapsulates a response from
   * {@link DbService#createObjectType(ObjectType, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateObjectTypeResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link ObjectType}
     * @param code       the code that was assigned to the new {@link ObjectType}
     * @param updateTime the time assigned to {@link ObjectType#getLastUpdatedAt()}
     */
    public CreateOrUpdateObjectTypeResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateObjectTypeResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link ObjectType}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link ObjectType}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link ObjectType#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createObjectAttribute(ObjectAttribute, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateObjectAttributeResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link ObjectAttribute}
     * @param code       the code that was assigned to the new {@link ObjectAttribute}
     * @param updateTime the time assigned to {@link ObjectAttribute#getLastUpdatedAt()}
     */
    public CreateOrUpdateObjectAttributeResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateObjectAttributeResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link ObjectAttribute}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link ObjectAttribute}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link ObjectAttribute#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createValueType(ValueType, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateValueTypeResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link ValueType}
     * @param code       the code that was assigned to the new {@link ValueType}
     * @param updateTime the time assigned to {@link ValueType#getLastUpdatedAt()}
     */
    public CreateOrUpdateValueTypeResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateValueTypeResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link ValueType}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link ValueType}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link ValueType#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createUnit(Unit, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateUnitResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link Unit}
     * @param updateTime the time assigned to {@link Unit#getLastUpdatedAt()}
     */
    public CreateOrUpdateUnitResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateUnitResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link Unit}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link Unit}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link Unit#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createTemplate(Template, java.util.HashMap, java.util.HashMap, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateTemplateResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link Template}
     * @param code       the code that was assigned to the new {@link Template}
     * @param updateTime the time assigned to {@link Template#getLastUpdatedAt()}
     */
    public CreateOrUpdateTemplateResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateTemplateResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link ObjectAttribute}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link ObjectAttribute}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link Template#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createTemplateRelation(TemplateRelation, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateTemplateRelationResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link TemplateRelation}
     * @param code       the code that was assigned to the new {@link TemplateRelation}
     * @param updateTime the time assigned to {@link TemplateRelation#getLastUpdatedAt()}
     */
    public CreateOrUpdateTemplateRelationResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateTemplateRelationResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link TemplateRelation}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link TemplateRelation}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link TemplateRelation#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createTemplateGroup(TemplateGroup, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateTemplateGroupResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link TemplateGroup}
     * @param code       the code that was assigned to the new {@link TemplateGroup}
     * @param updateTime the time assigned to {@link TemplateGroup#getLastUpdatedAt()}
     */
    public CreateOrUpdateTemplateGroupResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateTemplateGroupResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link TemplateGroup}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link TemplateGroup}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link TemplateGroup#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Returns the information needed to load the application template.
   *
   * @return a result object
   * @throws AccessDeniedException
   */
  //UserInfoResult getUserInfo() throws AccessDeniedException;  

  /**
   * Encapsulates a response from
   * {@link DbService#createTemplateAttribute(TemplateAttribute, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateTemplateAttributeResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link TemplateAttribute}
     * @param code       the code that was assigned to the new {@link TemplateAttribute}
     * @param updateTime the time assigned to {@link TemplateAttribute#getLastUpdatedAt()}
     */
    public CreateOrUpdateTemplateAttributeResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateTemplateAttributeResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link TemplateAttribute}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link TemplateAttribute}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link TemplateAttribute#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }

  /**
   * Encapsulates a response from
   * {@link DbService#createOrUpdateApplication(Application, java.util.ArrayList, AppUser)}.
   */
  @SuppressWarnings("serial")
  static class CreateOrUpdateApplicationResult implements Serializable {
    private int id;
    private String code;
    private Date updateTime;

    /**
     * Constructs a new result. This constructor can only be invoked on the
     * server.
     *
     * @param id         the key that was assigned to the new {@link Application}
     * @param code       the code that was assigned to the new {@link Application}
     * @param updateTime the time assigned to {@link Application#getLastUpdatedAt()}
     */
    public CreateOrUpdateApplicationResult(int id, String code, Date updateTime) {
      assert !GWT.isClient();
      this.id = id;
      this.code = code;
      this.updateTime = updateTime;
    }

    /**
     * Needed for RPC serialization.
     */
    @SuppressWarnings("unused")
    private CreateOrUpdateApplicationResult() {
    }

    /**
     * Returns the key that was assigned to the new {@link Application}.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Returns the code that was assigned to the new {@link Application}.
     *
     * @return
     */
    public String getCode() {
      return code;
    }

    /**
     * Returns the {@link Date} that was assigned to
     * {@link Application#getLastUpdatedAt()} by the server.
     *
     * @return
     */
    public Date getUpdateTime() {
      return updateTime;
    }
  }
}
