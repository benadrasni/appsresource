package sk.benko.appsresource.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.appengine.api.rdbms.AppEngineDriver;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class StoreDB {
  private static final Logger log = Logger.getLogger(StoreDB.class.getName());
  private static final String CODE_SEPARATOR = "-";
  private static final int ID_OFFSET = 7;
  private static final int FILTER_OFFSET = 100;
  private static final int BUFFER_SIZE = 200;

  //private static int LOG_OBJECT_CREATE = 0;
  //private static int LOG_OBJECT_EDIT = 1;
  private static final int LOG_OBJECT_DELETE = 2;
  //private static int LOG_VALUE_CREATE = 3;
  //private static int LOG_VALUE_UPDATE = 4;
  private static final int LOG_VALUE_DELETE = 5;

  public class Api {
    private static final String URL = "jdbc:google:rdbms://appsres:appsres2/appsresource";
    private static final String USER = "appsres";
    private static final String PASSWORD = "appsres";

    private Connection connection;

    private Api () {
      connection = getConnection();
    }

    private Connection getConnection() {
      try {
        DriverManager.registerDriver(new AppEngineDriver());

        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
      }
      catch (SQLException ex) {
        log.error(ex.getLocalizedMessage(), ex);
      }

      return connection;
    }

    public void commit() throws SQLException {
      connection.commit();
    }

    public void rollback() {
      try {
        connection.rollback();
      } catch (Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
      }
    }

    public void close() {
      try {
        connection.close();
      } catch (Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
      }
    }
    /**
     * Gets an user from database.
     *
     * @return      application user
     */
    public AppUser getUser(String email) throws SQLException {
      AppUser appUser = AppUser.load(connection, email);
      if (appUser == null) {
        appUser = new AppUser(email);
        appUser.save(connection);
      }
      return appUser;
    }

    /**
     * Gets all languages from the database.
     *
     * @return      list of all languages
     */
    public ArrayList<Language> getLanguages() throws SQLException {
      return Language.loadLanguages(connection);
    }

    /**
     * Save an object type to the database.
     *
     * @param objectType    the object type to be persisted
     * @return              <code>objectType</code>, for call chaining
     */
    public ObjectType saveObjectType(ObjectType objectType) throws SQLException {
      objectType.setLastUpdatedAt(new Date());
      return objectType.save(connection);
    }

    /**
     * Gets all object types from the database.
     *
     * @return      list of all object types
     */
    public ArrayList<ObjectType> getObjectTypes() throws SQLException {
      return ObjectType.loadObjectTypes(connection);
    }

    /**
     * Save an object attribute to the database.
     *
     * @param objectAttribute   the object attribute to be persisted
     * @return                  <code>objectAttribute</code>, for call chaining
     */
    public ObjectAttribute saveObjectAttribute(ObjectAttribute objectAttribute)
        throws SQLException {
      objectAttribute.setLastUpdatedAt(new Date());
      return objectAttribute.save(connection);
    }

    /**
     * Gets all object filterAttributes from the database.
     *
     * @param otId  object type's id
     * @return      list of all object type's filterAttributes
     */
    public ArrayList<ObjectAttribute> getObjectAttributes(int otId)
        throws SQLException {
      return ObjectAttribute.loadObjectAttributes(connection, otId);
    }

    /**
     * Save an object attribute to the database.
     *
     * @param objectRelation      the object relation to be persisted
     * @return                    <code>objectRelation</code>, for call chaining
     */
    public ObjectRelation saveObjectRelation(ObjectRelation objectRelation)
        throws SQLException {
      objectRelation.setLastUpdatedAt(new Date());
      return objectRelation.save(connection);
    }

    /**
     * Gets all object relations from the database.
     *
     * @param otId  object type's id
     * @return      list of all object type's relations
     */
    public ArrayList<ObjectRelation> getObjectRelations(int otId) throws SQLException {
      return ObjectRelation.loadObjectRelations(connection, otId);
    }

    /**
     * Save a value type to the database.
     *
     * @param valueType     the value type to be persisted
     * @return              <code>valueType</code>, for call chaining
     */
    public ValueType saveValueType(ValueType valueType) throws SQLException {
      valueType.setLastUpdatedAt(new Date());
      return valueType.save(connection);
    }

    /**
     * Gets all value types from the database.
     *
     * @return      list of all value types
     */
    public ArrayList<ValueType> getValueTypes() throws SQLException {
      return ValueType.loadValueTypes(connection);
    }

    /**
     * Save an unit to the database.
     *
     * @param unit      the unit to be persisted
     * @return          <code>unit</code>, for call chaining
     */
    public Unit saveUnit(Unit unit) throws SQLException {
      unit.setLastUpdatedAt(new Date());
      return unit.save(connection);
    }

    /**
     * Gets all units from the database.
     *
     * @return      list of all units
     */
    public ArrayList<Unit> getUnits() throws SQLException {
      return Unit.loadUnits(connection);
    }

    /**
     * Save a template to the database.
     *
     * @param template      the template to be persisted
     * @return              <code>template</code>, for call chaining
     */
    public Template saveTemplate(Template template)
        throws SQLException {
      template.setLastUpdatedAt(new Date());
      return template.save(connection);
    }

    /**
     * Gets template from the database.
     *
     * @return      <code>template</code>
     */
    public Template getTemplate(int tId) throws SQLException {
      return Template.load(connection, tId);
    }

    /**
     * Gets all templates from the database.
     *
     * @return      list of all templates
     */
    public ArrayList<Template> getTemplates() throws SQLException {
      return Template.loadTemplates(connection);
    }

    /**
     * Save a template relation to the database.
     *
     * @param templateRelation      the template relation to be persisted
     * @return                      <code>templateRelation</code>, for call chaining
     */
    public TemplateRelation saveTemplateRelation(TemplateRelation templateRelation) throws SQLException {
      templateRelation.setLastUpdatedAt(new Date());
      return templateRelation.save(connection);
    }

    /**
     * Gets all template relations from the database.
     *
     * @return      list of relations for all templates
     */
    public ArrayList<TemplateRelation> getTemplateRelations() throws SQLException {
      return TemplateRelation.loadTemplateRelations(connection);
    }

    /**
     * Gets all template relations belongs to given template from the database.
     *
     * @param tId     template's id
     * @return        list of relations belongs to the template
     */
    public ArrayList<TemplateRelation> getTemplateRelations(int tId) throws SQLException {
      return TemplateRelation.loadTemplateRelations(connection, tId);
    }

    /**
     * Save a template group to the database.
     *
     * @param templateGroup
     *          the template group to be persisted
     * @return <code>templateGroup</code>, for call chaining
     */
    public TemplateGroup saveTemplateGroup(TemplateGroup templateGroup) throws SQLException {
      templateGroup.setLastUpdatedAt(new Date());
      return templateGroup.save(connection);
    }

    /**
     * Gets all template groups from the database.
     *
     * @return      list of attribute groups for all templates
     */
    public ArrayList<TemplateGroup> getTemplateGroups() throws SQLException {
      return TemplateGroup.loadTemplateGroups(connection);
    }

    /**
     * Gets all template groups belongs to given template from the database.
     *
     * @param tId     template's id
     * @return        list of attribute groups belongs to the template
     */
    public ArrayList<TemplateGroup> getTemplateGroups(int tId) throws SQLException {
      return TemplateGroup.loadTemplateGroups(connection, tId);
    }

    /**
     * Save a template attribute to the database.
     *
     * @param templateAttribute     the template attribute to be persisted
     * @return                      <code>templateAttribute</code>, for call chaining
     */
    public TemplateAttribute saveTemplateAttribute(TemplateAttribute templateAttribute)
        throws SQLException {
      templateAttribute.setLastUpdatedAt(new Date());
      return templateAttribute.save(connection);
    }

    /**
     * Gets all template filterAttributes from the database.
     *
     * @return      list of all template's filterAttributes for all templates
     */
    public ArrayList<TemplateAttribute> getTemplateAttributes() throws SQLException {
      return TemplateAttribute.loadTemplateAttributes(connection);
    }

    /**
     * Gets all template filterAttributes belongs to template group from the database.
     *
     * @param tId     template's id
     * @return        list of filterAttributes belongs to the template
     */
    public ArrayList<TemplateAttribute> getTemplateAttributes(int tId) throws SQLException {
      return TemplateAttribute.loadTemplateAttributes(connection, tId);
    }

    /**
     * Save a template tree to the database.
     *
     * @param templateTree      the template tree to be persisted
     * @return                  <code>templateTree</code>, for call chaining
     */
    public TemplateTree saveTemplateTree(TemplateTree templateTree) throws SQLException {
      templateTree.setLastUpdatedAt(new Date());
      return templateTree.save(connection);
    }

    /**
     * Gets all template's trees for the template.
     *
     * @param tId     template's id
     * @return        list of all trees defined for the template
     */
    public ArrayList<TemplateTree> getTemplateTrees(int tId) throws SQLException {
      return TemplateTree.loadTemplateTrees(connection, tId);
    }

    /**
     * Save a template tree item to the database.
     *
     * @param templateTreeItem      the template tree iten to be persisted
     * @return                      <code>templateTreeItem</code>, for call chaining
     */
    public TemplateTreeItem saveTemplateTreeItem(TemplateTreeItem templateTreeItem)
        throws SQLException {
      return templateTreeItem.save(connection);
    }

    /**
     * Gets all application's templates from the database.
     *
     * @param ttId    template tree's id
     * @return        list of items for the template tree
     */
    public ArrayList<TemplateTreeItem> getTemplateTreeItems(int ttId) throws SQLException {
      return TemplateTreeItem.loadTemplateTreeItems(connection, ttId);
    }

    /**
     * delete all template tree items from template tree.
     *
     * @param templateTree      the template tree
     * @return                  <code>templateTree</code>, for call chaining
     */
    public void deleteTemplateTreeItems(TemplateTree templateTree) throws SQLException {
      templateTree.setLastUpdatedAt(new Date());
      TemplateTreeItem.deleteTemplateTreeItems(connection, templateTree.getId());
    }

    /**
     * Save a template list to the database.
     *
     * @param templateList      the template list to be persisted
     * @return                  <code>templateList</code>, for call chaining
     */
    public TemplateList saveTemplateList(TemplateList templateList)
        throws SQLException {
      templateList.setLastUpdatedAt(new Date());
      return templateList.save(connection);
    }

    /**
     * Gets all template's lists from the database.
     *
     * @param tId       template's id
     * @return          list of all template lists for the template
     */
    public ArrayList<TemplateList> getTemplateLists(int tId) throws SQLException {
      return TemplateList.loadTemplateLists(connection, tId);
    }

    /**
     * Save a template list item to the database.
     *
     * @param templateListItem      the template list item to be persisted
     * @return                      <code>templateListItem</code>, for call chaining
     */
    public TemplateListItem saveTemplateListItem(TemplateListItem templateListItem) throws SQLException {
      return templateListItem.save(connection);
    }

    /**
     * Gets all template list's items from the database.
     *
     * @param tlId      template list's id
     * @return          list of items for the template list
     */
    public ArrayList<TemplateListItem> getTemplateListItems(int tlId)throws SQLException {
      return TemplateListItem.loadTemplateListItems(connection, tlId);
    }

    /**
     * delete all template list items from template tree.
     *
     * @param templateList      the template list
     * @return                  <code>templateList</code>, for call chaining
     */
    public void deleteTemplateListItems(TemplateList templateList)throws SQLException {
      templateList.setLastUpdatedAt(new Date());
      TemplateListItem.deleteTemplateListItems(connection, templateList.getId());
    }

    /**
     * Save an application to the database.
     *
     * @param application     the application to be persisted
     * @return                <code>application</code>, for call chaining
     */
    public Application saveApplication(Application application) throws SQLException {
      application.setLastUpdatedAt(new Date());
      return application.save(connection);
    }

    /**
     * Gets all application from the database.
     *
     * @return      list of all applications
     */
    public ArrayList<Application> getApplications() throws SQLException {
      return Application.loadApplications(connection);
    }

    /**
     * Save a pair of application and template to the database.
     *
     * @param applicationTemplate     the application template to be persisted
     * @return                        <code>applicationTemplate</code>, for call chaining
     */
    public ApplicationTemplate saveApplicationTemplate(ApplicationTemplate applicationTemplate)
        throws SQLException {
      applicationTemplate.setLastUpdatedAt(new Date());
      return applicationTemplate.save(connection);
    }

    /**
     * delete all templates from application.
     *
     * @param application     the application
     */
    public void deleteApplicationTemplates(Application application)
        throws SQLException {
      application.setLastUpdatedAt(new Date());
      ApplicationTemplate.deleteTemplates(connection, application.getId());
    }

    /**
     * Gets all templates belongs to given application from the database.
     *
     * @param appId     application's id
     * @return          list of all templates which belong to the application
     */
    public ArrayList<ApplicationTemplate> getAppTemplatesByApp(int appId) throws SQLException {
      return ApplicationTemplate.loadAppTemplatesByApp(connection, appId);
    }

    /**
     * Gets all applications which contains given template from the database.
     *
     * @param tId     template's id
     * @return        list of <code>ApplicationTemplate</code>s
     */
    public ArrayList<ApplicationTemplate> getAppTemplatesByTemplate(int tId) throws SQLException {
      return ApplicationTemplate.loadAppTemplatesByTemplate(connection, tId);
    }

    /**
     * Gets all user's application from the database.
     *
     * @param userId      user's id
     * @return            list of all users for the application
     */
    public ArrayList<ApplicationUser> getUserApplications(int userId) throws SQLException {
      return ApplicationUser.loadUserApplications(connection, userId);
    }

    /**
     * Gets all user's application from the database.
     *
     * @param appId application's id
     * @return      list of users for the application
     */
    public ArrayList<ApplicationUser> getApplicationUsers(int appId) throws SQLException {
      return ApplicationUser.loadApplicationUsers(connection, appId);
    }

    /**
     * Save a pair of application and user to the database.
     *
     * @param applicationUser     the application user to be persisted
     * @return                    <code>applicationUser</code>, for call chaining
     */
    public ApplicationUser saveApplicationUser(ApplicationUser applicationUser) throws SQLException {
      applicationUser.setLastUpdatedAt(new Date());
      return applicationUser.save(connection);
    }

    /**
     * Save an object to the database.
     *
     * @param object      the object to be persisted
     * @return            <code>object</code>, for call chaining
     */
    public AObject saveObject(AObject object) throws SQLException {
      object.setLastUpdatedAt(new Date());
      return object.save(connection);
    }

    /**
     * delete an object from the database.
     *
     * @param objectId
     *          the id of object to be deleted
     * @return
     */
    public int deleteObject(int otId, int objectId, int userId)
        throws SQLException {
      return AObject.delete(connection, otId, objectId, userId);
    }

    /**
     * Gets all object of given type from the database.
     *
     * @return
     */
    public List<TreeLevel> getTreeLevelString(int langId, List<AValue> filter, int tId, List<TreeLevel> path,
        TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      return TreeLevel.loadTreeLevelString(connection, langId, filter, tId, path, tti, ta);
    }

    public List<TreeLevel> getTreeLevelDate(List<AValue> filter, int tId, List<TreeLevel> path,
        TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      return TreeLevel.loadTreeLevelDate(connection, filter, tId, path, tti, ta);
    }

    public List<TreeLevel> getTreeLevelNumber(List<AValue> filter, int tId, List<TreeLevel> path,
        TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      return TreeLevel.loadTreeLevelNumber(connection, filter, tId, path, tti, ta);
    }

    public List<TreeLevel> getTreeLevelRef(int langId, List<AValue> filter, int tId, List<TreeLevel> path,
        TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      return TreeLevel.loadTreeLevelRef(connection, langId, filter, tId, path, tti, ta);
    }

    /**
     * Gets all objects of given type from the database.
     *
     * @return
     */
    public List<AObject> getObjects(int langId, int tId, List<TreeLevel> path, int oaId) throws SQLException {
      return AObject.loadObjects(connection, langId, tId, path, oaId);
    }

    /**
     * Gets related object counts from the database.
     *
     * @return
     */
    public HashMap<Template, Integer> getSearchObjectCounts(int appId, String searchString)
        throws SQLException {
      return AObject.loadSearchObjectCounts(connection, appId, searchString);
    }

    /**
     * Gets all related objects from the database.
     *
     * @return
     */
    public Map<AObject, List<AValue>> getSearchObjects(int langId,
        String searchString, int tlId, int from, int perPage)
            throws SQLException {
      return AObject.loadSearchObjects(connection, langId, searchString, tlId, from, perPage);
    }

    /**
     * Gets related object counts from the database.
     *
     * @return
     */
    public int getRelatedObjectCounts(int objId, int rel)
        throws SQLException {
      return AObject.loadRelatedObjectCounts(connection, objId, rel);
    }

    /**
     * Gets all related objects from the database.
     *
     * @return
     */
    public Map<AObject, List<AValue>> getRelatedObjects(
        int langId, int objId, int rel, int tlId, int from, int perPage)
            throws SQLException {
      return AObject.loadRelatedObjects(connection, langId, objId, rel, tlId, from, perPage);
    }

    /**
     * Save a value to the database.
     *
     * @param value
     *          the value to be persisted
     * @return <code>value</code>, for call chaining
     */
    public AValue saveValue(AValue value)
        throws SQLException {
      value.setLastUpdatedAt(new Date());
      return value.save(connection);
    }

    /**
     * Gets all values for given object from the database.
     *
     * @return
     */
    public Map<Integer, List<AValue>> getValues(int oId) throws SQLException {
      return AValue.loadValues(connection, oId);
    }

    /**
     * Save an import header to the database.
     *
     * @param userId
     * @param appId
     * @param tId
     * @param filename
     * @return <code>id</code>, for call chaining
     */
    public int saveImportHeader(String userId, String appId, String tId,
        String filename) throws SQLException {
      return Import.saveHeader(connection, userId, appId, tId, filename);
    }

    /**
     * Save an import row to the database.
     *
     * @param headerId
     * @param rowNumber
     * @param row
     * @return <code>id</code>, for call chaining
     */
    public int saveImportRow(int headerId, int rowNumber, String[] row)
        throws SQLException {
      return Import.saveRow(connection, headerId, rowNumber, row);
    }


    /**
     * Import objects to the database.
     *
     * @param userId
     * @param app
     * @param t
     * @param filename
     * @param map
     * @param keys
     * @param onlyUpdate
     * @return              list of
     */
    public List<Integer> importObjects(int userId, Application app, Template t, String filename,
        Map<Integer, TemplateAttribute> map, Map<Integer, TemplateAttribute> keys,
        boolean onlyUpdate) throws SQLException {
      return Import.importObjects(connection, userId, app, t, filename, map, keys, onlyUpdate);
    }

    public Map<Integer, TemplateAttribute> getCommonAttributes(int t1, int t2) throws SQLException {
      return StoreDB.getCommonAttributes(connection, t1, t2);
    }
  }

  /**
   * An ORM object representing an user.
   */
  public static class AppUser {
    public static final String TABLE = "users";

    public static final String ID = "user_id";
    public static final String EMAIL = "user_email";
    public static final String FLAGS = "user_flags";

    /**
     * The user's id. Serves as the primary key for this object.
     */
    private int id;

    /**
     * The user's email.
     */
    private String email;

    /**
     * The user's flags.
     */
    private int flags;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();

    /**
     * Construct a new author.
     *
     * @param id
     *          the author's id
     * @param email
     *          the author's email
     */
    public AppUser(int id, String email, int flags) {
      setId(id);
      setEmail(email);
      setFlags(flags);
    }

    /**
     * Construct a new author.
     *
     * @param email
     *          the author's email
     */
    public AppUser(String email) {
      setEmail(email);
    }

    /**
     * Create a new author.
     *
     * @param rs
     * @throws SQLException
     */
    public AppUser(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setEmail(rs.getString(EMAIL));
      setFlags(rs.getInt(FLAGS));
    }

    /**
     * Gets the author's name.
     *
     * @return
     */
    public int getId() {
      return id;
    }

    /**
     * Gets the author's email.
     *
     * @return
     */
    public String getEmail() {
      return email;
    }

    /**
     * Sets the author's id.
     *
     * @param id
     */
    public void setId(int id) {
      this.id = id;
    }

    /**
     * Sets the author's email.
     *
     * @param email
     */
    public void setEmail(String email) {
      this.email = email;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
      return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
      this.flags = flags;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static AppUser load(Connection c, String email) throws SQLException {
      AppUser result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + EMAIL + "= '" + email + "'";
      log.info(query);
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new AppUser(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public AppUser save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + EMAIL + ")"
          + " VALUES ('" + getEmail() + "')");
        setId(getIdentity(stmt));
      }
      stmt.close();
      return this;
    }
  }

  /**
   * An ORM object representing a language.
   */
  public static class Language {
    public static final String TABLE = "languages";

    public static final String ID = "lang_id";
    public static final String CODE = "lang_code";
    public static final String NAME = "lang_name";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the language.
     */
    private String code;

    /**
     * The name of the language.
     */
    private String name;

    /**
     * Create a new application.
     *
     * @param id
     * @param code
     * @param name
     */
    public Language(int id, String code, String name) {
      setId(id);
      setCode(code);
      setName(name);
    }

    /**
     * Create a new language.
     *
     * @param rs
     * @throws SQLException
     */
    public Language(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public static ArrayList<Language> loadLanguages(Connection c) throws SQLException {
      ArrayList<Language> result = new ArrayList<Language>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new Language(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }
  }


  /**
   * An ORM object representing a object type.
   */
  public static class ObjectType {
    public static final String OBJECT_CODE = "OT";
    public static final String TABLE = "object_types";

    public static final String ID = "ot_id";
    public static final String CODE = "ot_code";
    public static final String NAME = "ot_name";
    public static final String DESC = "ot_desc";
    public static final String PARENT_ID = "ot_ot_id";
    public static final String USER_ID = "ot_user_id";
    public static final String LASTUPDATEDAT = "ot_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the objectType.
     */
    private String code;

    /**
     * The name of the objectType.
     */
    private String name;

    /**
     * The description of the objectType.
     */
    private String desc;

    /**
     * The key of parent objectType.
     */
    private int parentId;
    private ObjectType parent;

    /**
     * The key of the author created this objectType.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new objectType.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param parent
     * @param userId
     *          the author who created this object type
     */
    public ObjectType(int id, String code, String name, String desc,
        int parentId, ObjectType parent, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setParentId(parentId);
      setParent(parent);
      setUserId(userId);
    }

    /**
     * Create a new objectType.
     *
     * @param rs
     * @throws SQLException
     */
    public ObjectType(ResultSet rs, boolean withParent) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setParentId(rs.getInt(PARENT_ID));
      if (withParent && getParentId() > 0)
        setParent(new ObjectType(rs, "parent"));
      setUserId(rs.getInt(USER_ID));
    }

    public ObjectType(ResultSet rs, String alias) throws SQLException {
      setId(rs.getInt(alias+"_"+ID));
      setCode(rs.getString(alias+"_"+CODE));
      setName(rs.getString(alias+"_"+NAME));
      setDesc(rs.getString(alias+"_"+DESC));
      setParentId(rs.getInt(alias+"_"+PARENT_ID));
      setUserId(rs.getInt(alias+"_"+USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getParentId() {
      return parentId;
    }

    public void setParentId(int parentId) {
      this.parentId = parentId;
    }

    public ObjectType getParent() {
      return parent;
    }

    public void setParent(ObjectType parent) {
      this.parent = parent;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static ObjectType load(Connection c, int id) throws SQLException {
      ObjectType result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new ObjectType(rs, false);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public ObjectType save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + PARENT_ID + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + (getParentId() == 0 ? "null" : "" + getParentId())
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + PARENT_ID + "=" + (getParentId() == 0 ? "null, " : getParentId() + ", ")
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<ObjectType> loadObjectTypes(Connection c) throws SQLException {
      ArrayList<ObjectType> result = new ArrayList<ObjectType>();

      Statement stmt = c.createStatement();

      String query = "SELECT ot.*, "
          + " parent.ot_id as parent_ot_id, parent.ot_ot_id as parent_ot_ot_id, "
          + " parent.ot_code as parent_ot_code, parent.ot_name as parent_ot_name, "
          + " parent.ot_desc as parent_ot_desc, parent.ot_user_id as parent_ot_user_id, "
          + " parent.ot_lastupdateat as parent_ot_lastupdateat "
      		+ " FROM " + TABLE + " ot left outer join " + TABLE + " parent on (ot."
          + PARENT_ID + "= parent." + ID + ")"
          + " ORDER BY ot." + NAME;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ObjectType(rs, true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a object attribute.
   */
  public static class ObjectAttribute {
    public static final String OBJECT_CODE = "OA";
    public static final String TABLE = "object_attributes";

    public static final String ID = "oa_id";
    public static final String CODE = "oa_code";
    public static final String NAME = "oa_name";
    public static final String DESC = "oa_desc";
    public static final String OT_ID = "oa_ot_id";
    public static final String TYPE_ID = "oa_vt_id";
    public static final String UNIT_ID = "oa_unit_id";
    public static final String SHARED1 = "oa_shared1";
    public static final String SHARED2 = "oa_shared2";
    public static final String SHARED3 = "oa_shared3";
    public static final String SHARED4 = "oa_shared4";
    public static final String SHARED5 = "oa_shared5";
    public static final String USER_ID = "oa_user_id";
    public static final String LASTUPDATEDAT = "oa_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the objectAttribute.
     */
    private String code;

    /**
     * The name of the objectAttribute.
     */
    private String name;

    /**
     * The description of the objectAttribute.
     */
    private String desc;

    /**
     * The object type to which the objectAttribute belongs.
     */
    private int otId;
    private ObjectType ot;

    /**
     * The type of the objectAttribute.
     */
    private int vtId;
    private ValueType vt;

    /**
     * The unit of the objectAttribute.
     */
    private int unitId;
    private Unit unit;

    /**
     * Shared values are used for specific styles (like reference, ...)
     */
    private int shared1;
    private int shared2;
    private int shared3;
    private int shared4;
    private int shared5;

    /**
     * The key of the author created this objectAttribute.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new objectAttribute.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param otId
     * @param ot
     * @param vtId
     * @param vt
     * @param unitId
     * @param unit
     * @param shared1
     * @param shared2
     * @param shared3
     * @param shared4
     * @param shared5
     * @param userId
     *          the author who created this object type
     */
    public ObjectAttribute(int id, String code, String name, String desc,
        int otId, ObjectType ot, int vtId, ValueType vt, int unitId, Unit unit,
        int shared1, int shared2, int shared3, int shared4, int shared5,
        int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setOtId(otId);
      setOt(ot);
      setVtId(vtId);
      setVt(vt);
      setUnitId(unitId);
      setUnit(unit);
      setShared1(shared1);
      setShared2(shared2);
      setShared3(shared3);
      setShared4(shared4);
      setShared5(shared5);
      setUserId(userId);
    }

    /**
     * Create a new objectAttribute.
     *
     * @param rs
     * @throws SQLException
     */
    public ObjectAttribute(ResultSet rs)
        throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setOtId(rs.getInt(OT_ID));
      setVtId(rs.getInt(TYPE_ID));
      setUnitId(rs.getInt(UNIT_ID));
      setShared1(rs.getInt(SHARED1));
      setShared2(rs.getInt(SHARED2));
      setShared3(rs.getInt(SHARED3));
      setShared4(rs.getInt(SHARED4));
      setShared5(rs.getInt(SHARED5));
      setUserId(rs.getInt(USER_ID));

      setOt(new ObjectType(rs, false));
      setVt(new ValueType(rs));
      if (getUnitId() > 0) setUnit(new Unit(rs));


    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getOtId() {
      return otId;
    }

    public void setOtId(int otId) {
      this.otId = otId;
    }

    public ObjectType getOt() {
      return ot;
    }

    public void setOt(ObjectType ot) {
      this.ot = ot;
    }

    public int getVtId() {
      return vtId;
    }

    public void setVtId(int vtId) {
      this.vtId = vtId;
    }

    public ValueType getVt() {
      return vt;
    }

    public void setVt(ValueType vt) {
      this.vt = vt;
    }

    public int getUnitId() {
      return unitId;
    }

    public void setUnitId(int unitId) {
      this.unitId = unitId;
    }

    public Unit getUnit() {
      return unit;
    }

    public void setUnit(Unit unit) {
      this.unit = unit;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public int getShared1() {
      return shared1;
    }

    public void setShared1(int shared1) {
      this.shared1 = shared1;
    }

    public int getShared2() {
      return shared2;
    }

    public void setShared2(int shared2) {
      this.shared2 = shared2;
    }

    public int getShared3() {
      return shared3;
    }

    public void setShared3(int shared3) {
      this.shared3 = shared3;
    }

    public int getShared4() {
      return shared4;
    }

    public void setShared4(int shared4) {
      this.shared4 = shared4;
    }

    public int getShared5() {
      return shared5;
    }

    public void setShared5(int shared5) {
      this.shared5 = shared5;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public ObjectAttribute save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + OT_ID + ", " + TYPE_ID + ", "
            + UNIT_ID + "," + SHARED1 + ", " + SHARED2 + ", " + SHARED3 + ", "
            + SHARED4 + "," + SHARED5 + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getOtId()
          + "," + getVtId()
          + "," + (getUnitId() == 0 ? "null" : "" + getUnitId())
          + "," + (getShared1() > 0 ? getShared1() : "null")
          + "," + (getShared2() > 0 ? getShared2() : "null")
          + "," + (getShared3() > 0 ? getShared3() : "null")
          + "," + (getShared4() > 0 ? getShared4() : "null")
          + "," + (getShared5() > 0 ? getShared5() : "null")
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + OT_ID + "=" + getOtId() + ", "
            + TYPE_ID + "=" + getVtId() + ", "
            + UNIT_ID + "=" + (getUnitId() == 0 ? "null, " : getUnitId() + ", ")
            + SHARED1 + "=" + (getShared1() > 0 ? ""+getShared1() : "null") + ", "
            + SHARED2 + "=" + (getShared2() > 0 ? ""+getShared2() : "null") + ", "
            + SHARED3 + "=" + (getShared3() > 0 ? ""+getShared3() : "null") + ", "
            + SHARED4 + "=" + (getShared4() > 0 ? ""+getShared4() : "null") + ", "
            + SHARED5 + "=" + (getShared5() > 0 ? ""+getShared5() : "null") + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<ObjectAttribute> loadObjectAttributes(Connection c, int otId)
        throws SQLException {
      ArrayList<ObjectAttribute> result = new ArrayList<ObjectAttribute>();

      Statement stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("CALL get_object_attributes(" + otId + ")");
      while (rs.next()) {
        result.add(new ObjectAttribute(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a object relation.
   */
  public static class ObjectRelation {
    public static final String OBJECT_CODE = "OR";
    public static final String TABLE = "object_relations";

    public static final String ID = "or_id";
    public static final String CODE = "or_code";
    public static final String NAME = "or_name";
    public static final String DESC = "or_desc";
    public static final String OT1_ID = "or_ot1_id";
    public static final String OT2_ID = "or_ot2_id";
    public static final String TYPE = "or_type";
    public static final String COMPREL_ID = "or_or_id";
    public static final String USER_ID = "or_user_id";
    public static final String LASTUPDATEDAT = "or_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the object relation.
     */
    private String code;

    /**
     * The name of the object relation.
     */
    private String name;

    /**
     * The description of the object relation.
     */
    private String desc;

    /**
     * The object type (source) to which the object relation belongs.
     */
    private int ot1Id;
    private ObjectType ot1;

    /**
     * The object type (target) to which the object relation belongs.
     */
    private int ot2Id;
    private ObjectType ot2;

    /**
     * The type of the object relation.
     */
    private int type;

    /**
     * The complementary relation of the object relation.
     */
    private int orId;
    private ObjectRelation or;

    /**
     * The key of the author created this objectAttribute.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new objectAttribute.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param ot1Id
     * @param ot1
     * @param ot2Id
     * @param ot2
     * @param type
     * @param orId
     * @param or
     * @param userId
     *          the author who created this object type
     */
    public ObjectRelation(int id, String code, String name, String desc,
        int ot1Id, ObjectType ot1, int ot2Id, ObjectType ot2, int type,
        int orId, ObjectRelation or, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setOt1Id(ot1Id);
      setOt1(ot1);
      setOt2Id(ot2Id);
      setOt2(ot2);
      setType(type);
      setOrId(orId);
      setOr(or);
      setUserId(userId);
    }

    /**
     * Create a new objectAttribute.
     *
     * @param rs
     * @throws SQLException
     */
    public ObjectRelation(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setOt1Id(rs.getInt(OT1_ID));
      setOt1(new ObjectType(rs, "ot1"));
      setOt2Id(rs.getInt(OT2_ID));
      setOt2(new ObjectType(rs, "ot2"));
      setType(rs.getInt(TYPE));
      setOrId(rs.getInt(COMPREL_ID));
      setUserId(rs.getInt(USER_ID));
    }

    public ObjectRelation(ResultSet rs, String alias, boolean withComprel)
        throws SQLException {
      setId(rs.getInt(alias+"_"+ID));
      setCode(rs.getString(alias+"_"+CODE));
      setName(rs.getString(alias+"_"+NAME));
      setDesc(rs.getString(alias+"_"+DESC));
      setOt1Id(rs.getInt(alias+"_"+OT1_ID));
      setOt2Id(rs.getInt(alias+"_"+OT2_ID));
      setType(rs.getInt(alias+"_"+TYPE));
      setOrId(rs.getInt(alias+"_"+COMPREL_ID));
      setUserId(rs.getInt(alias+"_"+USER_ID));
      if (withComprel) {
        setOt1(new ObjectType(rs, "ot1"));
        setOt2(new ObjectType(rs, "ot2"));
        setOr(new ObjectRelation(rs, "comprel", false));
      }
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getOt1Id() {
      return ot1Id;
    }

    public void setOt1Id(int ot1Id) {
      this.ot1Id = ot1Id;
    }

    public ObjectType getOt1() {
      return ot1;
    }

    public void setOt1(ObjectType ot1) {
      this.ot1 = ot1;
    }

    public int getOt2Id() {
      return ot2Id;
    }

    public void setOt2Id(int ot2Id) {
      this.ot2Id = ot2Id;
    }

    public ObjectType getOt2() {
      return ot2;
    }

    public void setOt2(ObjectType ot2) {
      this.ot2 = ot2;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public int getOrId() {
      return orId;
    }

    public void setOrId(int orId) {
      this.orId = orId;
    }

    public ObjectRelation getOr() {
      return or;
    }

    public void setOr(ObjectRelation or) {
      this.or = or;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public ObjectRelation save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + OT1_ID + ", "
            + OT2_ID + ", " + TYPE + ", " + COMPREL_ID + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getOt1Id()
          + "," + getOt2Id()
          + "," + getType()
          + "," + (getOrId() == 0 ? "null" : "" + getOrId())
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + OT1_ID + "=" + getOt1Id() + ", "
            + OT2_ID + "=" + getOt2Id() + ", "
            + TYPE + "=" + getType() + ", "
            + COMPREL_ID + "=" + (getOrId() == 0 ? "null, " : getOrId() + ", ")
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<ObjectRelation> loadObjectRelations(Connection c,
        int otId) throws SQLException {
      ArrayList<ObjectRelation> result = new ArrayList<ObjectRelation>();

      Statement stmt = c.createStatement();

      String query = "SELECT orel.or_id as orel_or_id, orel.or_code as orel_or_code, "
            + " orel.or_name as orel_or_name, orel.or_desc as orel_or_desc, "
            + " orel.or_ot1_id as orel_or_ot1_id, orel.or_ot2_id as orel_or_ot2_id, "
            + " orel.or_type as orel_or_type, orel.or_or_id as orel_or_or_id, "
            + " orel.or_user_id as orel_or_user_id, "
          + " comprel.or_id as comprel_or_id, comprel.or_code as comprel_or_code, "
            + " comprel.or_name as comprel_or_name, comprel.or_desc as comprel_or_desc, "
            + " comprel.or_ot1_id as comprel_or_ot1_id, comprel.or_ot2_id as comprel_or_ot2_id, "
            + " comprel.or_type as comprel_or_type, comprel.or_or_id as comprel_or_or_id, "
            + " comprel.or_user_id as comprel_or_user_id, "
          + " ot1.ot_id as ot1_ot_id, ot1.ot_ot_id as ot1_ot_ot_id, "
          + " ot1.ot_code as ot1_ot_code, ot1.ot_name as ot1_ot_name, "
          + " ot1.ot_desc as ot1_ot_desc, ot1.ot_user_id as ot1_ot_user_id, "
          + " ot1.ot_lastupdateat as ot1_ot_lastupdateat, "
          + " ot2.ot_id as ot2_ot_id, ot2.ot_ot_id as ot2_ot_ot_id, "
          + " ot2.ot_code as ot2_ot_code, ot2.ot_name as ot2_ot_name, "
          + " ot2.ot_desc as ot2_ot_desc, ot2.ot_user_id as ot2_ot_user_id, "
          + " ot2.ot_lastupdateat as ot2_ot_lastupdateat"
          + " FROM " + TABLE + " orel left outer join " + TABLE + " comprel on (orel."
          + COMPREL_ID + "=comprel." + ID + "), " + ObjectType.TABLE + " ot1" + ", "
          + ObjectType.TABLE + " ot2"
          + " WHERE orel." + OT1_ID + "=" + "ot1." + ObjectType.ID
          + " AND orel." + OT2_ID + "=" + "ot2." + ObjectType.ID
          + " AND (orel." + OT1_ID + "=" + otId + " OR orel." + OT2_ID + "=" + otId
          + ") ORDER BY orel." + NAME;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ObjectRelation(rs, "orel", true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a value type.
   */
  public static class ValueType {
    public static final int FLAG_SYSTEM = 0;
    public static final int FLAG_SHAREABLE = 1;

    public static final int VT_INT = 1;
    public static final int VT_REAL = 2;
    public static final int VT_STRING = 3;
    public static final int VT_DATE = 4;
    public static final int VT_REF = 5;
    public static final int VT_TEXT = 6;
    public static final int VT_DATETIME = 7;
    public static final int VT_TIME = 8;

    public static final String OBJECT_CODE = "VT";
    public static final String TABLE = "value_types";

    public static final String ID = "vt_id";
    public static final String CODE = "vt_code";
    public static final String NAME = "vt_name";
    public static final String DESC = "vt_desc";
    public static final String TYPE = "vt_type";
    public static final String FLAGS = "vt_flags";
    public static final String USER_ID = "vt_user_id";
    public static final String LASTUPDATEDAT = "vt_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the valueType.
     */
    private String code;

    /**
     * The name of the valueType.
     */
    private String name;

    /**
     * The description of the valueType.
     */
    private String desc;

    /**
     * The type of valueType.
     */
    private int type;

    /**
     * The flags of valueType.
     */
    private int flags;

    /**
     * The key of the author created this value type.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new value type.
     *
     * @param id
     * @param code
     * @param name
     * @param type
     * @param userId
     *          the author who created/updated this value type
     */
    public ValueType(int id, String code, String name, String desc, int type,
        int flags, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setType(type);
      setFlags(flags);
      setUserId(userId);
    }

    /**
     * Create a new value type.
     *
     * @param rs
     * @throws SQLException
     */
    public ValueType(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setType(rs.getInt(TYPE));
      setFlags(rs.getInt(FLAGS));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public int getFlags() {
      return flags;
    }

    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static ValueType load(Connection c, int id) throws SQLException {
      ValueType result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new ValueType(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public ValueType save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + TYPE + ", " + FLAGS + ", "
            + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getType()
          + "," + getFlags()
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + TYPE + "=" + getType() + ", "
            + FLAGS + "=" + getFlags() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<ValueType> loadValueTypes(Connection c) throws SQLException {
      ArrayList<ValueType> result = new ArrayList<ValueType>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ValueType(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a unit.
   */
  public static class Unit {
    public static final String OBJECT_CODE = "UN";
    public static final String TABLE = "units";

    public static final String ID = "unit_id";
    public static final String CODE = "unit_code";
    public static final String NAME = "unit_name";
    public static final String DESC = "unit_desc";
    public static final String SYMBOL = "unit_symbol";
    public static final String TYPE = "unit_category";
    public static final String CONVERSION = "unit_conversion";
    public static final String USER_ID = "unit_user_id";
    public static final String LASTUPDATEDAT = "unit_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the unit.
     */
    private String code;

    /**
     * The name of the unit.
     */
    private String name;

    /**
     * The description of the unit.
     */
    private String desc;

    /**
     * The symbol of the unit.
     */
    private String symbol;

    /**
     * The type of the unit.
     */
    private int type;

    /**
     * The conversion of the unit to SI unit.
     */
    private float conversion;

    /**
     * The key of the author created this unit.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new unit.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param symbol
     * @param userId
     *          the author who created/updated this unit
     */
    public Unit(int id, String code, String name, String desc, String symbol,
        int type, float conversion, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setSymbol(symbol);
      setType(type);
      setConversion(conversion);
      setUserId(userId);
    }

    /**
     * Create a new objectType.
     *
     * @param rs
     * @throws SQLException
     */
    public Unit(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setSymbol(rs.getString(SYMBOL));
      setType(rs.getInt(TYPE));
      setConversion(rs.getFloat(CONVERSION));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public String getSymbol() {
      return symbol;
    }

    public void setSymbol(String symbol) {
      this.symbol = symbol;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public float getConversion() {
      return conversion;
    }

    public void setConversion(float conversion) {
      this.conversion = conversion;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public Unit save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + SYMBOL + ", "
            + TYPE + ", " + CONVERSION + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + ",'" + ServerUtils.mySQLFilter(getSymbol()) + "'"
          + "," + getType()
          + "," + getConversion()
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + SYMBOL + "='" + ServerUtils.mySQLFilter(getSymbol()) + "', "
            + TYPE + "=" + getType() + ", "
            + CONVERSION + "=" + getConversion() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<Unit> loadUnits(Connection c) throws SQLException {
      ArrayList<Unit> result = new ArrayList<Unit>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + " ORDER BY " + NAME;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new Unit(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a template.
   */
  public static class Template {
    public static final String OBJECT_CODE = "TP";
    public static final String TABLE = "templates";

    public static final String ID = "t_id";
    public static final String CODE = "t_code";
    public static final String NAME = "t_name";
    public static final String DESC = "t_desc";
    public static final String OT_ID = "t_ot_id";
    public static final String OA_ID = "t_oa_id";
    public static final String USER_ID = "t_user_id";
    public static final String LASTUPDATEDAT = "t_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the template.
     */
    private String code;

    /**
     * The name of the template.
     */
    private String name;

    /**
     * The description of the template.
     */
    private String desc;

    /**
     * The object type to which the template belongs.
     */
    private int otId;
    private ObjectType ot;

    /**
     * The leaf template attribute.
     */
    private int oaId;
    private ObjectAttribute oa;

    /**
     * The key of the author created this template.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new template.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param otId
     * @param oa
     * @param userId
     *          the author who created this template
     */
    public Template(int id, String code, String name, String desc, int otId,
        ObjectType ot, int taId, ObjectAttribute oa, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setOtId(otId);
      setOt(ot);
      setOaId(taId);
      setOa(oa);
      setUserId(userId);
    }

    /**
     * Create a new template.
     *
     * @param rs
     * @param withTables
     * @throws SQLException
     */
    public Template(ResultSet rs, boolean withTables) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setOtId(rs.getInt(OT_ID));
      setOaId(rs.getInt(OA_ID));
      setUserId(rs.getInt(USER_ID));
      if (withTables) {
        if (getOtId() > 0) setOt(new ObjectType(rs, false));
        if (getOaId() > 0) setOa(new ObjectAttribute(rs));
      }
    }

    public Template(ResultSet rs, String alias) throws SQLException {
      setId(rs.getInt(alias+"_"+ID));
      setCode(rs.getString(alias+"_"+CODE));
      setName(rs.getString(alias+"_"+NAME));
      setDesc(rs.getString(alias+"_"+DESC));
      setOtId(rs.getInt(alias+"_"+OT_ID));
      setOaId(rs.getInt(alias+"_"+OA_ID));
      setUserId(rs.getInt(alias+"_"+USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getOtId() {
      return otId;
    }

    public void setOtId(int otId) {
      this.otId = otId;
    }

    public ObjectType getOt() {
      return ot;
    }

    public void setOt(ObjectType ot) {
      this.ot = ot;
    }

    public int getOaId() {
      return oaId;
    }

    public void setOaId(int oaId) {
      this.oaId = oaId;
    }

    public ObjectAttribute getOa() {
      return oa;
    }

    public void setOa(ObjectAttribute oa) {
      this.oa = oa;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static Template load(Connection c, int id) throws SQLException {
      Template result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new Template(rs, false);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public Template save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      StringBuilder query = new StringBuilder(BUFFER_SIZE);
      if (this.getId() == 0) {
        // insert new record
        setCode(getNewCode(c));
        query.append("INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + OT_ID + ", " + OA_ID + ", "
            + USER_ID + ")");
        query.append(" VALUES (");
        query.append("'").append(getCode()).append("'");
        query.append(",'").append(ServerUtils.mySQLFilter(getName())).append("'");
        query.append(",").append(getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null");
        query.append(",").append(getOtId() == 0 ? "null" : ""+getOtId());
        query.append(",").append(getOaId() == 0 ? "null" : ""+getOaId());
        query.append(",").append(getUserId()).append(")");
        stmt.executeUpdate(query.toString());
        setId(getIdentity(stmt));
      } else {
        // update an existing record
        query.append("UPDATE " + TABLE + " SET ");
        query.append(NAME + "='").append(ServerUtils.mySQLFilter(getName())).append("', ");
        query.append(DESC + "=").append(getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ");
        query.append(OT_ID + "=").append(getOtId() == 0 ? "null, " : getOtId() + ", ");
        query.append(OA_ID + "=").append(getOaId() == 0 ? "null, " : getOaId() + ", ");
        query.append(USER_ID + "=").append(getUserId()).append(", ");
        query.append(LASTUPDATEDAT + "= now()");
        query.append(" WHERE " + ID + "=").append(getId());
        log.info(TABLE + ": " + query.toString());
        stmt.executeUpdate(query.toString());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<Template> loadTemplates(Connection c) throws SQLException {
      ArrayList<Template> result = new ArrayList<Template>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + " left outer join "
          + ObjectAttribute.TABLE + " on (" + OA_ID + "="
          + ObjectAttribute.ID + ") left outer join " + ObjectType.TABLE
          + " on (" + OT_ID + "=" + ObjectType.ID
          + ") left outer join " + ValueType.TABLE
            + " on (" + ObjectAttribute.TYPE_ID + "=" + ValueType.ID
          + ") left outer join " + Unit.TABLE
            + " on (" + ObjectAttribute.UNIT_ID + "=" + Unit.ID + ")"
          + " ORDER BY " + NAME ;
      log.info(TABLE + ": " + query);
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new Template(rs, true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a template relation.
   */
  public static class TemplateRelation {
    public static final String OBJECT_CODE = "TR";
    public static final String TABLE = "template_relations";

    public static final String ID = "tr_id";
    public static final String CODE = "tr_code";
    public static final String NAME = "tr_name";
    public static final String DESC = "tr_desc";
    public static final String T1_ID = "tr_t1_id";
    public static final String T2_ID = "tr_t2_id";
    public static final String OR_ID = "tr_or_id";
    public static final String FLAGS = "tr_flags";
    public static final String RANK = "tr_rank";
    public static final String SUBRANK = "tr_subrank";
    public static final String USER_ID = "tr_user_id";
    public static final String LASTUPDATEDAT = "tr_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the template relation.
     */
    private String code;

    /**
     * The name of the template relation.
     */
    private String name;

    /**
     * The description of the template relation.
     */
    private String desc;

    /**
     * The template to which the template relation belongs.
     */
    private int t1Id;
    private Template t1;

    /**
     * The template to which the template is connected.
     */
    private int t2Id;
    private Template t2;

    /**
     * The object relation to which the template relation belongs.
     */
    private int orId;
    private ObjectRelation or;

    /**
     * The flags of the template relation.
     */
    private int flags;

    /**
     * The rank of the template relation.
     */
    private int rank;

    /**
     * The subrank of the template relation within a tab.
     */
    private int subrank;

    /**
     * The key of the author created this template relation.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new template.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param t1Id
     * @param t1
     * @param t2Id
     * @param t2
     * @param orId
     * @param or
     * @param flags
     * @param rank
     * @param subrank
     * @param userId
     *          the author who created this template
     */
    public TemplateRelation(int id, String code, String name, String desc,
        int t1Id, Template t1, int t2Id, Template t2, int orId,
        ObjectRelation or, int flags, int rank, int subrank, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setT1Id(t1Id);
      setT1(t1);
      setT2Id(t2Id);
      setT2(t2);
      setOrId(orId);
      setOr(or);
      setFlags(flags);
      setRank(rank);
      setSubrank(subrank);
      setUserId(userId);
    }

    /**
     * Create a new template.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateRelation(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setT1Id(rs.getInt(T1_ID));
      setT1(new Template(rs, "t1"));
      setT2Id(rs.getInt(T2_ID));
      setT2(new Template(rs, "t2"));
      setOrId(rs.getInt(OR_ID));
      setOr(new ObjectRelation(rs));
      setFlags(rs.getInt(FLAGS));
      setRank(rs.getInt(RANK));
      setSubrank(rs.getInt(SUBRANK));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getT1Id() {
      return t1Id;
    }

    public void setT1Id(int t1Id) {
      this.t1Id = t1Id;
    }

    public Template getT1() {
      return t1;
    }

    public void setT1(Template t1) {
      this.t1 = t1;
    }

    public int getT2Id() {
      return t2Id;
    }

    public void setT2Id(int t2Id) {
      this.t2Id = t2Id;
    }

    public Template getT2() {
      return t2;
    }

    public void setT2(Template t2) {
      this.t2 = t2;
    }

    public int getOrId() {
      return orId;
    }

    public void setOrId(int orId) {
      this.orId = orId;
    }

    public ObjectRelation getOr() {
      return or;
    }

    public void setOr(ObjectRelation or) {
      this.or = or;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
      return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
      this.flags = flags;
    }

    /**
     * @return the rank
     */
    public int getRank() {
      return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
      this.rank = rank;
    }

    /**
     * @return the subrank
     */
    public int getSubrank() {
      return subrank;
    }

    /**
     * @param subrank the subrank to set
     */
    public void setSubrank(int subrank) {
      this.subrank = subrank;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public TemplateRelation save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + T1_ID + ", " + T2_ID + ", "
            + OR_ID + ", " + FLAGS + ", "  + RANK + ", " + SUBRANK + ", "
            + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getT1Id()
          + "," + getT2Id()
          + "," + getOrId()
          + "," + getFlags()
          + "," + getRank()
          + "," + getSubrank()
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + T1_ID + "=" + getT1Id() + ", "
            + T2_ID + "=" + getT2Id() + ", "
            + OR_ID + "=" + getOrId() + ", "
            + FLAGS + "=" + getFlags() + ", "
            + RANK + "=" + getRank() + ", "
            + SUBRANK + "=" + getSubrank() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<TemplateRelation> loadTemplateRelations(Connection c) throws SQLException {
      ArrayList<TemplateRelation> result = new ArrayList<TemplateRelation>();

      Statement stmt = c.createStatement();

      String query = "SELECT trel.*, orel.*, rt.*, "
          + " t1.t_id as t1_t_id, t1.t_ot_id as t1_t_ot_id, "
          + " t1.t_code as t1_t_code, t1.t_name as t1_t_name, "
          + " t1.t_desc as t1_t_desc, t1.t_user_id as t1_t_user_id, "
          + " t1.t_oa_id as t1_t_oa_id, "
          + " t1.t_lastupdateat as t1_t_lastupdateat, "
          + " t2.t_id as t2_t_id, t2.t_ot_id as t2_t_ot_id, "
          + " t2.t_code as t2_t_code, t2.t_name as t2_t_name, "
          + " t2.t_desc as t2_t_desc, t2.t_user_id as t2_t_user_id, "
          + " t2.t_oa_id as t2_t_oa_id, "
          + " t2.t_lastupdateat as t2_t_lastupdateat, "
          + " ot1.ot_id as ot1_ot_id, ot1.ot_ot_id as ot1_ot_ot_id, "
          + " ot1.ot_code as ot1_ot_code, ot1.ot_name as ot1_ot_name, "
          + " ot1.ot_desc as ot1_ot_desc, ot1.ot_user_id as ot1_ot_user_id, "
          + " ot1.ot_lastupdateat as ot1_ot_lastupdateat, "
          + " ot2.ot_id as ot2_ot_id, ot2.ot_ot_id as ot2_ot_ot_id, "
          + " ot2.ot_code as ot2_ot_code, ot2.ot_name as ot2_ot_name, "
          + " ot2.ot_desc as ot2_ot_desc, ot2.ot_user_id as ot2_ot_user_id, "
          + " ot2.ot_lastupdateat as ot2_ot_lastupdateat"
          + " FROM " + TABLE + " trel, " + ObjectRelation.TABLE + " orel, "
          + Template.TABLE + " t1, "
          + Template.TABLE + " t2, " + ObjectType.TABLE + " ot1, "
          + ObjectType.TABLE + " ot2"
          + " WHERE " + OR_ID + "=" + ObjectRelation.ID
          + " AND " + T1_ID + "=" + "t1." + Template.ID
          + " AND " + T2_ID + "=" + "t2." + Template.ID
          + " AND " + ObjectRelation.OT1_ID + "=" + "ot1." + ObjectType.ID
          + " AND " + ObjectRelation.OT2_ID + "=" + "ot2." + ObjectType.ID;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateRelation(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static ArrayList<TemplateRelation> loadTemplateRelations(Connection c, int tId) throws SQLException {
      ArrayList<TemplateRelation> result = new ArrayList<TemplateRelation>();

      Statement stmt = c.createStatement();

      String query = "SELECT trel.*, orel.*, "
          + " t1.t_id as t1_t_id, t1.t_ot_id as t1_t_ot_id, "
          + " t1.t_code as t1_t_code, t1.t_name as t1_t_name, "
          + " t1.t_desc as t1_t_desc, t1.t_user_id as t1_t_user_id, "
          + " t1.t_oa_id as t1_t_oa_id, "
          + " t1.t_lastupdateat as t1_t_lastupdateat, "
          + " t2.t_id as t2_t_id, t2.t_ot_id as t2_t_ot_id, "
          + " t2.t_code as t2_t_code, t2.t_name as t2_t_name, "
          + " t2.t_desc as t2_t_desc, t2.t_user_id as t2_t_user_id, "
          + " t2.t_oa_id as t2_t_oa_id, "
          + " t2.t_lastupdateat as t2_t_lastupdateat, "
          + " ot1.ot_id as ot1_ot_id, ot1.ot_ot_id as ot1_ot_ot_id, "
          + " ot1.ot_code as ot1_ot_code, ot1.ot_name as ot1_ot_name, "
          + " ot1.ot_desc as ot1_ot_desc, ot1.ot_user_id as ot1_ot_user_id, "
          + " ot1.ot_lastupdateat as ot1_ot_lastupdateat, "
          + " ot2.ot_id as ot2_ot_id, ot2.ot_ot_id as ot2_ot_ot_id, "
          + " ot2.ot_code as ot2_ot_code, ot2.ot_name as ot2_ot_name, "
          + " ot2.ot_desc as ot2_ot_desc, ot2.ot_user_id as ot2_ot_user_id, "
          + " ot2.ot_lastupdateat as ot2_ot_lastupdateat"
          + " FROM " + TABLE + " trel, " + ObjectRelation.TABLE + " orel, "
          + ObjectType.TABLE + " ot1, "
          + ObjectType.TABLE + " ot2, " + Template.TABLE + " t1, "
          + Template.TABLE + " t2 "
          + " WHERE " + T1_ID + "=" + tId
          + " AND " + T1_ID + "=" + "t1." + Template.ID
          + " AND " + T2_ID + "=" + "t2." + Template.ID
          + " AND " + OR_ID + "=" + ObjectRelation.ID
          + " AND " + ObjectRelation.OT1_ID + "=" + "ot1." + ObjectType.ID
          + " AND " + ObjectRelation.OT2_ID + "=" + "ot2." + ObjectType.ID
          + " ORDER BY " + RANK + ", " + SUBRANK;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateRelation(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a template group.
   */
  public static class TemplateGroup {
    public static final String OBJECT_CODE = "TG";
    public static final String TABLE = "template_groups";

    public static final String ID = "tg_id";
    public static final String CODE = "tg_code";
    public static final String NAME = "tg_name";
    public static final String DESC = "tg_desc";
    public static final String T_ID = "tg_t_id";
    public static final String FLAGS = "tg_flags";
    public static final String RANK = "tg_rank";
    public static final String SUBRANK = "tg_subrank";
    public static final String LABELTOP = "tg_label_top";
    public static final String LABELLEFT = "tg_label_left";
    public static final String LABELWIDTH = "tg_label_width";
    public static final String LABELWIDTHUNIT = "tg_label_width_unit";
    public static final String LABELALIGN = "tg_label_align";
    public static final String USER_ID = "tg_user_id";
    public static final String LASTUPDATEDAT = "tg_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the template group.
     */
    private String code;

    /**
     * The name of the template group.
     */
    private String name;

    /**
     * The description of the template group.
     */
    private String desc;

    /**
     * The template to which the template group belongs.
     */
    private int tId;
    private Template t;

    /**
     * The flags of the template group.
     */
    private int flags;

    /**
     * The rank of the template group.
     */
    private int rank;

    /**
     * The subrank of the template group within a tab.
     */
    private int subrank;

    /**
     * The position's top of the template group's label.
     */
    private int labelTop;

    /**
     * The position's left of the template group's label.
     */
    private int labelLeft;

    /**
     * The width of the template group's label.
     */
    private int labelWidth;

    /**
     * The unit of the width of the template group's label.
     */
    private String labelWidthUnit;

    /**
     * The align of the template group's label.
     */
    private String labelAlign;

    /**
     * The key of the author created this template.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new template.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param tId
     * @param flags
     * @param rank
     * @param subrank
     * @param labelTop
     * @param labelLeft
     * @param labelWidth
     * @param labelWidthUnit
     * @param labelAlign
     * @param userId
     *          the author who created this template
     */
    public TemplateGroup(int id, String code, String name, String desc, int tId,
        Template t, int flags, int rank, int subrank, int labelTop, int labelLeft,
        int labelWidth, String labelWidthUnit, String labelAlign, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setTId(tId);
      setT(t);
      setFlags(flags);
      setRank(rank);
      setSubrank(subrank);
      setLabelTop(labelTop);
      setLabelLeft(labelLeft);
      setLabelWidth(labelWidth);
      setLabelWidthUnit(labelWidthUnit);
      setLabelAlign(labelAlign);
      setUserId(userId);
    }

    /**
     * Create a new template.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateGroup(ResultSet rs, boolean withTables) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setTId(rs.getInt(T_ID));
      setFlags(rs.getInt(FLAGS));
      setRank(rs.getInt(RANK));
      setSubrank(rs.getInt(SUBRANK));
      setLabelTop(rs.getInt(LABELTOP));
      setLabelLeft(rs.getInt(LABELLEFT));
      setLabelWidth(rs.getInt(LABELWIDTH));
      setLabelWidthUnit(rs.getString(LABELWIDTHUNIT));
      setLabelAlign(rs.getString(LABELALIGN));
      setUserId(rs.getInt(USER_ID));

      if (withTables)
        setT(new Template(rs, false));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getTId() {
      return tId;
    }

    public void setTId(int tId) {
      this.tId = tId;
    }

    /**
     * @return the t
     */
    public Template getT() {
      return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(Template t) {
      this.t = t;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
      return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
      this.flags = flags;
    }

    /**
     * @return the rank
     */
    public int getRank() {
      return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
      this.rank = rank;
    }

    /**
     * @return the subrank
     */
    public int getSubrank() {
      return subrank;
    }

    /**
     * @param subrank the subrank to set
     */
    public void setSubrank(int subrank) {
      this.subrank = subrank;
    }

    /**
     * @return the labelTop
     */
    public int getLabelTop() {
      return labelTop;
    }

    /**
     * @param labelTop the labelTop to set
     */
    public void setLabelTop(int labelTop) {
      this.labelTop = labelTop;
    }

    /**
     * @return the labelLeft
     */
    public int getLabelLeft() {
      return labelLeft;
    }

    /**
     * @param labelLeft the labelLeft to set
     */
    public void setLabelLeft(int labelLeft) {
      this.labelLeft = labelLeft;
    }

    /**
     * @return the labelWidth
     */
    public int getLabelWidth() {
      return labelWidth;
    }

    /**
     * @param labelWidth the labelWidth to set
     */
    public void setLabelWidth(int labelWidth) {
      this.labelWidth = labelWidth;
    }

    /**
     * @return the labelWidthUnit
     */
    public String getLabelWidthUnit() {
      return labelWidthUnit;
    }

    /**
     * @param labelWidthUnit the labelWidthUnit to set
     */
    public void setLabelWidthUnit(String labelWidthUnit) {
      this.labelWidthUnit = labelWidthUnit;
    }

    public String getLabelAlign() {
      return labelAlign;
    }

    public void setLabelAlign(String labelAlign) {
      this.labelAlign = labelAlign;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static TemplateGroup load(Connection c, int id) throws SQLException {
      TemplateGroup result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new TemplateGroup(rs, false);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public TemplateGroup save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + T_ID + ", " + FLAGS + ", "
            + RANK + ", " + SUBRANK + ", " + LABELTOP + ", " + LABELLEFT + ", "
            + LABELWIDTH + ", " + LABELWIDTHUNIT + ", " + LABELALIGN + ", "
            + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getTId()
          + "," + getFlags()
          + "," + getRank()
          + "," + getSubrank()
          + "," + (getLabelTop() > 0 ? getLabelTop() : "null")
          + "," + (getLabelLeft() > 0 ? getLabelLeft() : "null")
          + "," + (getLabelWidth() > 0 ? getLabelLeft() : "null")
          + "," + (getLabelWidthUnit() != null ? "'" + getLabelWidthUnit() + "'" : "null")
          + "," + (getLabelAlign()  != null ? "'" + getLabelAlign() + "'" : "null")
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + T_ID + "=" + getTId() + ", "
            + FLAGS + "=" + getFlags() + ", "
            + RANK + "=" + getRank() + ", "
            + SUBRANK + "=" + getSubrank() + ", "
            + LABELTOP + "=" + (getLabelTop() > 0 ? ""+getLabelTop() : "null") + ", "
            + LABELLEFT + "=" + (getLabelLeft() > 0 ? ""+getLabelLeft() : "null") + ", "
            + LABELWIDTH + "=" + (getLabelWidth() > 0 ? ""+getLabelWidth() : "null") + ", "
            + LABELWIDTHUNIT + "=" + (getLabelWidthUnit() != null ? "'" + getLabelWidthUnit() + "'": "null") + ", "
            + LABELALIGN + "=" + (getLabelAlign() != null ? "'" + getLabelAlign() + "'": "null") + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<TemplateGroup> loadTemplateGroups(Connection c) throws SQLException {
      ArrayList<TemplateGroup> result = new ArrayList<TemplateGroup>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + ", " + Template.TABLE
          + " WHERE " + T_ID + "=" + Template.ID;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateGroup(rs, true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static ArrayList<TemplateGroup> loadTemplateGroups(Connection c,
        int tId) throws SQLException {
      ArrayList<TemplateGroup> result = new ArrayList<TemplateGroup>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + ", " + Template.TABLE
          + " WHERE " + T_ID + "=" + tId
          + " AND " + T_ID + "=" + Template.ID
          + " ORDER BY " + RANK + ", " + SUBRANK;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateGroup(rs, true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a template attribute.
   */
  public static class TemplateAttribute {
    public static final String OBJECT_CODE = "TA";
    public static final String TABLE = "template_attributes";

    public static final String ID = "ta_id";
    public static final String CODE = "ta_code";
    public static final String NAME = "ta_name";
    public static final String DESC = "ta_desc";
    public static final String TG_ID = "ta_tg_id";
    public static final String OA_ID = "ta_oa_id";
    public static final String FLAGS = "ta_flags";
    public static final String STYLE = "ta_style";
    public static final String TABINDEX = "ta_tabindex";
    public static final String DEF = "ta_default";
    public static final String LENGTH = "ta_length";
    public static final String LABELTOP = "ta_label_top";
    public static final String LABELLEFT = "ta_label_left";
    public static final String LABELWIDTH = "ta_label_width";
    public static final String LABELWIDTHUNIT = "ta_label_width_unit";
    public static final String LABELALIGN = "ta_label_align";
    public static final String TOP = "ta_top";
    public static final String LEFT = "ta_left";
    public static final String WIDTH = "ta_width";
    public static final String WIDTHUNIT = "ta_width_unit";
    public static final String ALIGN = "ta_align";
    public static final String UNITTOP = "ta_unit_top";
    public static final String UNITLEFT = "ta_unit_left";
    public static final String UNITWIDTH = "ta_unit_width";
    public static final String UNITWIDTHUNIT = "ta_unit_width_unit";
    public static final String UNITALIGN = "ta_unit_align";
    public static final String SHARED1 = "ta_shared1";
    public static final String SHARED2 = "ta_shared2";
    public static final String SHARED3 = "ta_shared3";
    public static final String SHARED4 = "ta_shared4";
    public static final String SHARED5 = "ta_shared5";
    public static final String USER_ID = "ta_user_id";
    public static final String LASTUPDATEDAT = "ta_lastupdateat";

    public static final int FLAG_DESCENDANT = 4;
    public static final int FLAG_DERIVED = 5;

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the template attribute.
     */
    private String code;

    /**
     * The name of the template attribute.
     */
    private String name;

    /**
     * The description of the template attribute.
     */
    private String desc;

    /**
     * The template group to which the template attribute belongs.
     */
    private int tgId;
    private TemplateGroup tg;

    /**
     * The object attribute which the template attribute represents.
     */
    private int oaId;
    private ObjectAttribute oa;

    /**
     * The flags of the template attribute.
     * 1st bit ... attribute label's visibility
     * 2nd bit ... attribute's visibility
     * 3rd bit ... attribute unit's visibility
     */
    private int flags;

    /**
     * The style of the template attribute.
     */
    private int style;

    /**
     * The tabindex of the template attribute.
     */
    private int tabIndex;

    /**
     * The default value of the template attribute.
     */
    private String def;

    /**
     * The length of the template attribute.
     */
    private int length;

    /**
     * The position's top of the template attribute's label.
     */
    private int labelTop;

    /**
     * The position's left of the template attribute's label.
     */
    private int labelLeft;

    /**
     * The width of the template attribute's label.
     */
    private int labelWidth;

    /**
     * The unit of the width of the template attribute's label.
     */
    private String labelWidthUnit;

    /**
     * The align of the template attribute's label.
     */
    private String labelAlign;

    /**
     * The position's top of the template group's label.
     */
    private int top;

    /**
     * The position's left of the template attribute.
     */
    private int left;

    /**
     * The width of the template attribute.
     */
    private int width;

    /**
     * The unit of the width of the template attribute.
     */
    private String widthUnit;

    /**
     * The align of the template attribute.
     */
    private String align;

    /**
     * The position's top of the template attribute's unit.
     */
    private int unitTop;

    /**
     * The position's left of the template attribute's unit.
     */
    private int unitLeft;

    /**
     * The width of the template attribute's unit.
     */
    private int unitWidth;

    /**
     * The unit of the width of the template attribute's unit.
     */
    private String unitWidthUnit;

    /**
     * The align of the template attribute's unit.
     */
    private String unitAlign;

    /**
     * Shared values are used for specific styles (like dynamic combos, tables, ...)
     */
    private int shared1;
    private int shared2;
    private int shared3;
    private int shared4;
    private int shared5;

    /**
     * The key of the author created this template.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new template attribute.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param tgId
     * @param tg
     * @param oaId
     * @param oa
     * @param flags
     * @param style
     * @param tabIndex
     * @param def
     * @param length
     * @param labelTop
     * @param labelLeft
     * @param labelWidth
     * @param labelWidthUnit
     * @param labelAlign
     * @param top
     * @param left
     * @param width
     * @param widthUnit
     * @param align
     * @param unitTop
     * @param unitLeft
     * @param unitWidth
     * @param unitWidthUnit
     * @param unitAlign
     * @param shared1
     * @param shared2
     * @param shared3
     * @param shared4
     * @param shared5
     * @param userId
     *          the author who created this template
     */
    public TemplateAttribute(int id, String code, String name, String desc,
        int tgId, TemplateGroup tg, int oaId, ObjectAttribute oa, int flags,
        int style, int tabIndex, String def, int length, int labelTop,
        int labelLeft, int labelWidth, String labelWidthUnit, String labelAlign,
        int top, int left, int width, String widthUnit, String align, int unitTop,
        int unitLeft, int unitWidth, String unitWidthUnit, String unitAlign,
        int shared1, int shared2, int shared3, int shared4, int shared5, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setTgId(tgId);
      setTg(tg);
      setOaId(oaId);
      setOa(oa);
      setFlags(flags);
      setStyle(style);
      setTabIndex(tabIndex);
      setDef(def);
      setLength(length);
      setLabelTop(labelTop);
      setLabelLeft(labelLeft);
      setLabelWidth(labelWidth);
      setLabelWidthUnit(labelWidthUnit);
      setLabelAlign(labelAlign);
      setTop(top);
      setLeft(left);
      setWidth(width);
      setWidthUnit(widthUnit);
      setAlign(align);
      setUnitTop(unitTop);
      setUnitLeft(unitLeft);
      setUnitWidth(unitWidth);
      setUnitWidthUnit(unitWidthUnit);
      setUnitAlign(unitAlign);
      setShared1(shared1);
      setShared2(shared2);
      setShared3(shared3);
      setShared4(shared4);
      setShared5(shared5);
      setUserId(userId);
    }

    /**
     * Create a new template.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateAttribute(ResultSet rs, boolean withTablesDown,
        boolean withTablesUp) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setTgId(rs.getInt(TG_ID));
      setOaId(rs.getInt(OA_ID));
      setFlags(rs.getInt(FLAGS));
      setStyle(rs.getInt(STYLE));
      setTabIndex(rs.getInt(TABINDEX));
      setLength(rs.getInt(LENGTH));
      setDef(rs.getString(DEF));
      setLabelTop(rs.getInt(LABELTOP));
      setLabelLeft(rs.getInt(LABELLEFT));
      setLabelWidth(rs.getInt(LABELWIDTH));
      setLabelWidthUnit(rs.getString(LABELWIDTHUNIT));
      setLabelAlign(rs.getString(LABELALIGN));
      setTop(rs.getInt(TOP));
      setLeft(rs.getInt(LEFT));
      setWidth(rs.getInt(WIDTH));
      setWidthUnit(rs.getString(WIDTHUNIT));
      setAlign(rs.getString(ALIGN));
      setUnitTop(rs.getInt(UNITTOP));
      setUnitLeft(rs.getInt(UNITLEFT));
      setUnitWidth(rs.getInt(UNITWIDTH));
      setUnitWidthUnit(rs.getString(UNITWIDTHUNIT));
      setUnitAlign(rs.getString(UNITALIGN));
      setShared1(rs.getInt(SHARED1));
      setShared2(rs.getInt(SHARED2));
      setShared3(rs.getInt(SHARED3));
      setShared4(rs.getInt(SHARED4));
      setShared5(rs.getInt(SHARED5));
      setUserId(rs.getInt(USER_ID));

      if (withTablesDown)
        setOa(new ObjectAttribute(rs));
      if (withTablesUp)
        setTg(new TemplateGroup(rs, true));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getTgId() {
      return tgId;
    }

    public void setTgId(int tgId) {
      this.tgId = tgId;
    }

    public TemplateGroup getTg() {
      return tg;
    }

    public void setTg(TemplateGroup tg) {
      this.tg = tg;
    }

    public int getOaId() {
      return oaId;
    }

    public void setOaId(int oaId) {
      this.oaId = oaId;
    }

    public ObjectAttribute getOa() {
      return oa;
    }

    public void setOa(ObjectAttribute oa) {
      this.oa = oa;
    }

    public int getFlags() {
      return flags;
    }

    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getStyle() {
      return style;
    }

    public void setStyle(int style) {
      this.style = style;
    }

    public int getTabIndex() {
      return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
      this.tabIndex = tabIndex;
    }

    public String getDef() {
      return def;
    }

    public void setDef(String def) {
      this.def = def;
    }

    public int getLength() {
      return length;
    }

    public void setLength(int length) {
      this.length = length;
    }

    public int getLabelTop() {
      return labelTop;
    }

    public void setLabelTop(int labelTop) {
      this.labelTop = labelTop;
    }

    public int getLabelLeft() {
      return labelLeft;
    }

    public void setLabelLeft(int labelLeft) {
      this.labelLeft = labelLeft;
    }

    public int getLabelWidth() {
      return labelWidth;
    }

    public void setLabelWidth(int labelWidth) {
      this.labelWidth = labelWidth;
    }

    public String getLabelWidthUnit() {
      return labelWidthUnit;
    }

    public void setLabelWidthUnit(String labelWidthUnit) {
      this.labelWidthUnit = labelWidthUnit;
    }

    public String getLabelAlign() {
      return labelAlign;
    }

    public void setLabelAlign(String labelAlign) {
      this.labelAlign = labelAlign;
    }

    public int getTop() {
      return top;
    }

    public void setTop(int top) {
      this.top = top;
    }

    public int getLeft() {
      return left;
    }

    public void setLeft(int left) {
      this.left = left;
    }

    public int getWidth() {
      return width;
    }

    public void setWidth(int width) {
      this.width = width;
    }

    public String getWidthUnit() {
      return widthUnit;
    }

    public void setWidthUnit(String widthUnit) {
      this.widthUnit = widthUnit;
    }

    public String getAlign() {
      return align;
    }

    public void setAlign(String align) {
      this.align = align;
    }

    public int getUnitTop() {
      return unitTop;
    }

    public void setUnitTop(int unitTop) {
      this.unitTop = unitTop;
    }

    public int getUnitLeft() {
      return unitLeft;
    }

    public void setUnitLeft(int unitLeft) {
      this.unitLeft = unitLeft;
    }

    public int getUnitWidth() {
      return unitWidth;
    }

    public void setUnitWidth(int unitWidth) {
      this.unitWidth = unitWidth;
    }

    public String getUnitWidthUnit() {
      return unitWidthUnit;
    }

    public void setUnitWidthUnit(String unitWidthUnit) {
      this.unitWidthUnit = unitWidthUnit;
    }

    public String getUnitAlign() {
      return unitAlign;
    }

    public void setUnitAlign(String unitAlign) {
      this.unitAlign = unitAlign;
    }

    public int getShared1() {
      return shared1;
    }

    public void setShared1(int shared1) {
      this.shared1 = shared1;
    }

    public int getShared2() {
      return shared2;
    }

    public void setShared2(int shared2) {
      this.shared2 = shared2;
    }

    public int getShared3() {
      return shared3;
    }

    public void setShared3(int shared3) {
      this.shared3 = shared3;
    }

    public int getShared4() {
      return shared4;
    }

    public void setShared4(int shared4) {
      this.shared4 = shared4;
    }

    public int getShared5() {
      return shared5;
    }

    public void setShared5(int shared5) {
      this.shared5 = shared5;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public TemplateAttribute save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + TG_ID + ", " + OA_ID + ", "
            + FLAGS + ", " + STYLE + ", " + TABINDEX + ", " + DEF + ", " + LENGTH + ", "
            + LABELTOP + ", " + LABELLEFT + ", " + LABELWIDTH + ", "
            + LABELWIDTHUNIT + ", " + LABELALIGN + ", "
            + TOP + ", " + LEFT + ", " + WIDTH + ", "
            + WIDTHUNIT + ", " + ALIGN + ", "
            + UNITTOP + ", " + UNITLEFT + ", " + UNITWIDTH + ", "
            + UNITWIDTHUNIT + ", " + UNITALIGN + ", "
            + SHARED1 + ", " + SHARED2 + ", " + SHARED3 + ", " + SHARED4 + ", "
            + SHARED5 + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getTgId()
          + "," + (getOaId() == 0 ? "null" : ""+getOaId())
          + "," + getFlags()
          + "," + getStyle()
          + "," + getTabIndex()
          + "," + (getDef() != null ? "'" + ServerUtils.mySQLFilter(getDef()) + "'" : "null")
          + "," + getLength()
          + "," + (getLabelTop() > 0 ? getLabelTop() : "null")
          + "," + (getLabelLeft() > 0 ? getLabelLeft() : "null")
          + "," + (getLabelWidth() > 0 ? getLabelWidth() : "null")
          + "," + (getLabelWidthUnit() != null ? "'" + getLabelWidthUnit() + "'" : "null")
          + "," + (getLabelAlign()  != null ? "'" + getLabelAlign() + "'" : "null")
          + "," + (getTop() > 0 ? getTop() : "null")
          + "," + (getLeft() > 0 ? getLeft() : "null")
          + "," + (getWidth() > 0 ? getWidth() : "null")
          + "," + (getWidthUnit() != null ? "'" + getWidthUnit() + "'" : "null")
          + "," + (getAlign()  != null ? "'" + getAlign() + "'" : "null")
          + "," + (getUnitTop() > 0 ? getUnitTop() : "null")
          + "," + (getUnitLeft() > 0 ? getUnitLeft() : "null")
          + "," + (getUnitWidth() > 0 ? getUnitLeft() : "null")
          + "," + (getUnitWidthUnit() != null ? "'" + getUnitWidthUnit() + "'" : "null")
          + "," + (getUnitAlign()  != null ? "'" + getUnitAlign() + "'" : "null")
          + "," + (getShared1() > 0 ? getShared1() : "null")
          + "," + (getShared2() > 0 ? getShared2() : "null")
          + "," + (getShared3() > 0 ? getShared3() : "null")
          + "," + (getShared4() > 0 ? getShared4() : "null")
          + "," + (getShared5() > 0 ? getShared5() : "null")
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + TG_ID + "=" + getTgId() + ", "
            + OA_ID + "=" + (getOaId() == 0 ? "null, " : getOaId() + ", ")
            + FLAGS + "=" + getFlags() + ", "
            + STYLE + "=" + getStyle() + ", "
            + TABINDEX + "=" + getTabIndex() + ", "
            + DEF + "=" + (getDef() != null ? "'" + ServerUtils.mySQLFilter(getDef()) + "'" : "null") + ", "
            + LENGTH + "=" + getLength() + ", "
            + LABELTOP + "=" + (getLabelTop() > 0 ? ""+getLabelTop() : "null") + ", "
            + LABELLEFT + "=" + (getLabelLeft() > 0 ? ""+getLabelLeft() : "null") + ", "
            + LABELWIDTH + "=" + (getLabelWidth() > 0 ? ""+getLabelWidth() : "null") + ", "
            + LABELWIDTHUNIT + "=" + (getLabelWidthUnit() != null ? "'" + getLabelWidthUnit() + "'": "null") + ", "
            + LABELALIGN + "=" + (getLabelAlign() != null ? "'" + getLabelAlign() + "'": "null") + ", "
            + TOP + "=" + (getTop() > 0 ? ""+getTop() : "null") + ", "
            + LEFT + "=" + (getLeft() > 0 ? ""+getLeft() : "null") + ", "
            + WIDTH + "=" + (getWidth() > 0 ? ""+getWidth() : "null") + ", "
            + WIDTHUNIT + "=" + (getWidthUnit() != null ? "'" + getWidthUnit() + "'": "null") + ", "
            + ALIGN + "=" + (getAlign() != null ? "'" + getAlign() + "'": "null") + ", "
            + UNITTOP + "=" + (getUnitTop() > 0 ? ""+getUnitTop() : "null") + ", "
            + UNITLEFT + "=" + (getUnitLeft() > 0 ? ""+getUnitLeft() : "null") + ", "
            + UNITWIDTH + "=" + (getUnitWidth() > 0 ? ""+getUnitWidth() : "null") + ", "
            + UNITWIDTHUNIT + "=" + (getUnitWidthUnit() != null ? "'" + getUnitWidthUnit() + "'": "null") + ", "
            + UNITALIGN + "=" + (getUnitAlign() != null ? "'" + getUnitAlign() + "'": "null") + ", "
            + SHARED1 + "=" + (getShared1() > 0 ? ""+getShared1() : "null") + ", "
            + SHARED2 + "=" + (getShared2() > 0 ? ""+getShared2() : "null") + ", "
            + SHARED3 + "=" + (getShared3() > 0 ? ""+getShared3() : "null") + ", "
            + SHARED4 + "=" + (getShared4() > 0 ? ""+getShared4() : "null") + ", "
            + SHARED5 + "=" + (getShared5() > 0 ? ""+getShared5() : "null") + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<TemplateAttribute> loadTemplateAttributes(Connection c) throws SQLException {
      ArrayList<TemplateAttribute> result = new ArrayList<TemplateAttribute>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TemplateGroup.TABLE + ", "
          + Template.TABLE + ", " + TABLE
          + " left outer join " + ObjectAttribute.TABLE + " on ("
          + OA_ID + "=" + ObjectAttribute.ID + ") "
          + " left outer join " + ObjectType.TABLE + " on ("
          + ObjectAttribute.OT_ID + "=" + ObjectType.ID + ") "
          + " left outer join " + Unit.TABLE + " on ("
          + ObjectAttribute.UNIT_ID + "=" + Unit.ID + ") "
          + " left outer join " + ValueType.TABLE + " on ("
          + ObjectAttribute.TYPE_ID + "=" + ValueType.ID + ")"
          + " WHERE " + TG_ID + "=" + TemplateGroup.ID
          + " AND " + TemplateGroup.T_ID + "=" + Template.ID;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateAttribute(rs, true, true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static ArrayList<TemplateAttribute> loadTemplateAttributes(Connection c, int tId) throws SQLException {
      ArrayList<TemplateAttribute> result = new ArrayList<TemplateAttribute>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TemplateGroup.TABLE + ", "
          + Template.TABLE + ", " + TABLE
          + " left outer join " + ObjectAttribute.TABLE + " on ("
          + OA_ID + "=" + ObjectAttribute.ID + ") "
          + " left outer join " + ObjectType.TABLE + " on ("
          + ObjectAttribute.OT_ID + "=" + ObjectType.ID + ") "
          + " left outer join " + Unit.TABLE + " on ("
          + ObjectAttribute.UNIT_ID + "=" + Unit.ID + ") "
          + " left outer join " + ValueType.TABLE + " on ("
          + ObjectAttribute.TYPE_ID + "=" + ValueType.ID + ")"
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + TG_ID + "=" + TemplateGroup.ID
          + " AND " + TemplateGroup.T_ID + "=" + Template.ID
          + " ORDER BY " + TemplateGroup.RANK + ", " + TemplateGroup.SUBRANK
          + ", " + TABINDEX;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateAttribute(rs, true, true));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing a template tree.
   */
  public static class TemplateTree {
    public static final String OBJECT_CODE = "TT";
    public static final String TABLE = "template_trees";

    public static final String ID = "tt_id";
    public static final String CODE = "tt_code";
    public static final String NAME = "tt_name";
    public static final String DESC = "tt_desc";
    public static final String T_ID = "tt_t_id";
    public static final String FLAGS = "tt_flags";
    public static final String RANK = "tt_rank";
    public static final String USER_ID = "tt_user_id";
    public static final String LASTUPDATEDAT = "tt_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the template tree.
     */
    private String code;

    /**
     * The name of the template tree.
     */
    private String name;

    /**
     * The description of the template tree.
     */
    private String desc;

    /**
     * The template to which the template tree belongs.
     */
    private int tId;

    /**
     * The flags of the template group.
     */
    private int flags;

    /**
     * The rank of the template tree within a template.
     */
    private int rank;

    /**
     * The key of the author created this template tree.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new template.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param tId
     * @param flags
     * @param rank
     * @param userId
     *          the author who created this template tree
     */
    public TemplateTree(int id, String code, String name, String desc, int tId,
        int flags, int rank, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setTId(tId);
      setFlags(flags);
      setRank(rank);
      setUserId(userId);
    }

    /**
     * Create a new template tree.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateTree(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setTId(rs.getInt(T_ID));
      setFlags(rs.getInt(FLAGS));
      setRank(rs.getInt(RANK));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getTId() {
      return tId;
    }

    public void setTId(int tId) {
      this.tId = tId;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
      return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static TemplateTree load(Connection c, int id) throws SQLException {
      TemplateTree result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new TemplateTree(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public TemplateTree save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + T_ID + ", " + FLAGS + ", "
            + RANK + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getTId()
          + "," + getFlags()
          + "," + getRank()
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + T_ID + "=" + getTId() + ", "
            + FLAGS + "=" + getFlags() + ", "
            + RANK + "=" + getRank() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<TemplateTree> loadTemplateTrees(Connection c, int tId) throws SQLException {
      ArrayList<TemplateTree> result = new ArrayList<TemplateTree>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
          + " WHERE " + T_ID + "=" + tId;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateTree(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }


  /**
   * An ORM object representing a template tree item.
   */
  public static class TemplateTreeItem {
    public static final String TABLE = "template_tree_items";

    public static final String ID = "tti_id";
    public static final String TT_ID = "tti_tt_id";
    public static final String TA_ID = "tti_ta_id";
    public static final String RANK = "tti_rank";
    public static final String SUBSETOF = "tti_subsetof";

    /**
     * The key of the template tree item.
     */
    private int id;

    /**
     * The key of the template tree.
     */
    private int ttId;
    private TemplateTree tt;

    /**
     * The key of the template attribute.
     */
    private int taId;
    private TemplateAttribute ta;

    /**
     * The rank of the template tree item.
     */
    private int rank;

    /**
     * The id of template tree item which the TemplateTreeItem is subset of
     */
    private int subsetOf;


    /**
     * Create a new application template.
     *
     * @param id
     * @param ttId
     * @param taId
     * @param rank
     * @param subsetOf
     */
    public TemplateTreeItem(int id, int ttId, int taId, int rank, int subsetOf) {
      setId(id);
      setTtId(ttId);
      setTaId(taId);
      setRank(rank);
      setSubsetOf(subsetOf);
    }

    /**
     * Create a new template tree item.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateTreeItem(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setTtId(rs.getInt(TT_ID));
      setTt(new TemplateTree(rs));
      setTaId(rs.getInt(TA_ID));
      setTa(new TemplateAttribute(rs, true, false));
      setRank(rs.getInt(RANK));
      setSubsetOf(rs.getInt(SUBSETOF));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getTtId() {
      return ttId;
    }

    public void setTtId(int ttId) {
      this.ttId = ttId;
    }

    public TemplateTree getTt() {
      return tt;
    }

    public void setTt(TemplateTree tt) {
      this.tt = tt;
    }

    public int getTaId() {
      return taId;
    }

    public void setTaId(int taId) {
      this.taId = taId;
    }

    public TemplateAttribute getTa() {
      return ta;
    }

    public void setTa(TemplateAttribute ta) {
      this.ta = ta;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

    public int getSubsetOf() {
      return subsetOf;
    }

    public void setSubsetOf(int subsetOf) {
      this.subsetOf = subsetOf;
    }

    public TemplateTreeItem save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      stmt.execute(
        "INSERT INTO " + TABLE + " ("
          + TT_ID + ", " + TA_ID + ", " + RANK + ", " + SUBSETOF + ")"
          + " VALUES (" + getTtId() + "," + getTaId() + "," + getRank() + "," + getSubsetOf() + ")");
      stmt.close();
      return this;
    }

    public static void deleteTemplateTreeItems(Connection c, int ttId) throws SQLException {
      Statement stmt = c.createStatement();
      String query = "DELETE FROM " + TABLE + " WHERE " + TT_ID + "=" + ttId;
      stmt.execute(query);
      stmt.close();
    }

    public static ArrayList<TemplateTreeItem> loadTemplateTreeItems(Connection c, int ttId)
        throws SQLException {
      ArrayList<TemplateTreeItem> result = new ArrayList<TemplateTreeItem>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + ", " + TemplateTree.TABLE
          + ", " + TemplateAttribute.TABLE + ", " + ObjectType.TABLE + ", "
          + ObjectAttribute.TABLE + " left outer join " + Unit.TABLE
          + " on (" + ObjectAttribute.UNIT_ID + "=" + Unit.ID + "), " + ValueType.TABLE
          + " WHERE " + TT_ID + "=" + ttId
          + " AND " + TT_ID + "=" + TemplateTree.ID
          + " AND " + TA_ID + "=" + TemplateAttribute.ID
          + " AND " + TemplateAttribute.OA_ID + "=" + ObjectAttribute.ID
          + " AND " + ObjectAttribute.TYPE_ID + "=" + ValueType.ID
          + " AND " + ObjectAttribute.OT_ID + "=" + ObjectType.ID
          + " ORDER BY " + RANK;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateTreeItem(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }
  }

  /**
   * An ORM object representing a template list.
   */
  public static class TemplateList {
    public static final String OBJECT_CODE = "TL";
    public static final String TABLE = "template_lists";

    public static final String ID = "tl_id";
    public static final String CODE = "tl_code";
    public static final String NAME = "tl_name";
    public static final String DESC = "tl_desc";
    public static final String T_ID = "tl_t_id";
    public static final String FLAGS = "tl_flags";
    public static final String RANK = "tl_rank";
    public static final String USER_ID = "tl_user_id";
    public static final String LASTUPDATEDAT = "tl_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the template list.
     */
    private String code;

    /**
     * The name of the template list.
     */
    private String name;

    /**
     * The description of the template list.
     */
    private String desc;

    /**
     * The template to which the template list belongs.
     */
    private int tId;

    /**
     * The flags of the template list.
     */
    private int flags;

    /**
     * The rank of the template list within a template.
     */
    private int rank;

    /**
     * The key of the author created this template list.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new template.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param tId
     * @param flags
     * @param rank
     * @param userId
     *          the author who created this template tree
     */
    public TemplateList(int id, String code, String name, String desc, int tId,
        int flags, int rank, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setTId(tId);
      setFlags(flags);
      setRank(rank);
      setUserId(userId);
    }

    /**
     * Create a new template list.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateList(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setTId(rs.getInt(T_ID));
      setFlags(rs.getInt(FLAGS));
      setRank(rs.getInt(RANK));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getTId() {
      return tId;
    }

    public void setTId(int tId) {
      this.tId = tId;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
      return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static TemplateList load(Connection c, int id) throws SQLException {
      TemplateList result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new TemplateList(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public TemplateList save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        setCode(getNewCode(c));
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + T_ID + ", " + FLAGS + ", "
            + RANK + ", " + USER_ID + ")"
          + " VALUES ('" + getCode() + "'"
          + ",'" + ServerUtils.mySQLFilter(getName()) + "'"
          + "," + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null")
          + "," + getTId()
          + "," + getFlags()
          + "," + getRank()
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + NAME + "='" + ServerUtils.mySQLFilter(getName()) + "', "
            + DESC + "=" + (getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ")
            + T_ID + "=" + getTId() + ", "
            + FLAGS + "=" + getFlags() + ", "
            + RANK + "=" + getRank() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<TemplateList> loadTemplateLists(Connection c, int tId)
        throws SQLException {
      ArrayList<TemplateList> result = new ArrayList<TemplateList>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
          + " WHERE " + T_ID + "=" + tId;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateList(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }


  /**
   * An ORM object representing a template list item.
   */
  public static class TemplateListItem {
    public static final String TABLE = "template_list_items";

    public static final String ID = "tli_id";
    public static final String TL_ID = "tli_tl_id";
    public static final String TA_ID = "tli_ta_id";
    public static final String RANK = "tli_rank";

    /**
     * The key of the template list item.
     */
    private int id;

    /**
     * The key of the template list.
     */
    private int tlId;
    private TemplateList tl;

    /**
     * The key of the template attribute.
     */
    private int taId;
    private TemplateAttribute ta;

    /**
     * The rank of the template list item.
     */
    private int rank;

    /**
     * Create a new template list item.
     *
     * @param tlId
     * @param taId
     * @param rank
     *          the author who created this application template
     */
    public TemplateListItem(int tlId, int taId, int rank) {
      setTlId(tlId);
      setTaId(taId);
      setRank(rank);
    }

    /**
     * Create a new template list item.
     *
     * @param rs
     * @throws SQLException
     */
    public TemplateListItem(ResultSet rs) throws SQLException {
      setTlId(rs.getInt(TL_ID));
      setTl(new TemplateList(rs));
      setTaId(rs.getInt(TA_ID));
      setTa(new TemplateAttribute(rs, true, false));
      setRank(rs.getInt(RANK));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getTlId() {
      return tlId;
    }

    public void setTlId(int tlId) {
      this.tlId = tlId;
    }

    public TemplateList getTl() {
      return tl;
    }

    public void setTl(TemplateList tl) {
      this.tl = tl;
    }

    public int getTaId() {
      return taId;
    }

    public void setTaId(int taId) {
      this.taId = taId;
    }

    public TemplateAttribute getTa() {
      return ta;
    }

    public void setTa(TemplateAttribute ta) {
      this.ta = ta;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

    public TemplateListItem save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      stmt.execute(
        "INSERT INTO " + TABLE + " ("
          + TL_ID + ", " + TA_ID + ", " + RANK + ")"
          + " VALUES (" + getTlId() + "," + getTaId() + "," + getRank() + ")");
      stmt.close();
      return this;
    }

    public static void deleteTemplateListItems(Connection c, int tlId) throws SQLException {
      Statement stmt = c.createStatement();
      String query = "DELETE FROM " + TABLE
          + " WHERE " + TL_ID + "=" + tlId;
      stmt.execute(query);
      stmt.close();
    }

    public static ArrayList<TemplateListItem> loadTemplateListItems(Connection c, int tlId)
        throws SQLException {
      ArrayList<TemplateListItem> result = new ArrayList<TemplateListItem>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + ", " + TemplateList.TABLE
          + ", " + TemplateAttribute.TABLE + ", " + ObjectType.TABLE + ", "
          + ObjectAttribute.TABLE + " left outer join " + Unit.TABLE
          + " on (" + ObjectAttribute.UNIT_ID + "=" + Unit.ID + "), " + ValueType.TABLE
          + " WHERE " + TL_ID + "=" + tlId
          + " AND " + TL_ID + "=" + TemplateList.ID
          + " AND " + TA_ID + "=" + TemplateAttribute.ID
          + " AND " + TemplateAttribute.OA_ID + "=" + ObjectAttribute.ID
          + " AND " + ObjectAttribute.TYPE_ID + "=" + ValueType.ID
          + " AND " + ObjectAttribute.OT_ID + "=" + ObjectType.ID
          + " ORDER BY " + RANK;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new TemplateListItem(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }
  }

  /**
   * An ORM object representing an application.
   */
  public static class Application {
    public static final String OBJECT_CODE = "AP";
    public static final String TABLE = "applications";

    public static final String ID = "app_id";
    public static final String CODE = "app_code";
    public static final String NAME = "app_name";
    public static final String DESC = "app_desc";
    public static final String CATEGORY = "app_category";
    public static final String FLAGS = "app_flags";
    public static final String USER_ID = "app_user_id";
    public static final String LASTUPDATEDAT = "app_lastupdateat";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The code of the application.
     */
    private String code;

    /**
     * The name of the application.
     */
    private String name;

    /**
     * The description of the application.
     */
    private String desc;

    /**
     * The category of the application.
     */
    private String category;

    /**
     * The flags of the application.
     */
    private int flags;

    /**
     * The key of the author created this application.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new application.
     *
     * @param id
     * @param code
     * @param name
     * @param desc
     * @param flags
     * @param userId
     *          the author who created this application
     */
    public Application(int id, String code, String name, String desc,
        String category, int flags, int userId) {
      setId(id);
      setCode(code);
      setName(name);
      setDesc(desc);
      setCategory(category);
      setFlags(flags);
      setUserId(userId);
    }

    /**
     * Create a new application.
     *
     * @param rs
     * @throws SQLException
     */
    public Application(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setCode(rs.getString(CODE));
      setName(rs.getString(NAME));
      setDesc(rs.getString(DESC));
      setCategory(rs.getString(CATEGORY));
      setFlags(rs.getInt(FLAGS));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public String getCategory() {
      return category;
    }

    public void setCategory(String category) {
      this.category = category;
    }

    public int getFlags() {
      return flags;
    }

    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static Application load(Connection c, int id) throws SQLException {
      Application result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new Application(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public Application save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      StringBuilder query = new StringBuilder(BUFFER_SIZE);
      if (this.getId() == 0) {
        // insert new record
        setCode(getNewCode(c));
        query.append("INSERT INTO " + TABLE + " ("
            + CODE + ", " + NAME + ", " + DESC + ", " + CATEGORY + ", "
            + FLAGS + ", " + USER_ID + ")");
        query.append(" VALUES (");
        query.append("'").append(getCode()).append("'");
        query.append(",'").append(ServerUtils.mySQLFilter(getName())).append("'");
        query.append(",").append(getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "'" : "null");
        query.append(",").append(getCategory() != null ? "'" + ServerUtils.mySQLFilter(getCategory()) + "'" : "null");
        query.append(",").append(getFlags());
        query.append(",").append(getUserId()).append(")");
        log.info(TABLE + ": " + query.toString());
        stmt.execute(query.toString());
        setId(getIdentity(stmt));
      } else {
        // update an existing record
        query.append("UPDATE " + TABLE + " SET ");
        query.append(NAME + "='").append(ServerUtils.mySQLFilter(getName())).append("', ");
        query.append(DESC + "=").append(getDesc() != null ? "'" + ServerUtils.mySQLFilter(getDesc()) + "', " : "null, ");
        query.append(CATEGORY + "=").append(getCategory() != null ? "'" + ServerUtils.mySQLFilter(getCategory()) + "', " : "null, ");
        query.append(FLAGS + "=").append(getFlags()).append(", ");
        query.append(USER_ID + "=").append(getUserId()).append(", ");
        query.append(LASTUPDATEDAT + "= now()");
        query.append(" WHERE " + ID + "=").append(getId());
        log.info(TABLE + ": " + query.toString());
        stmt.executeUpdate(query.toString());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<Application> loadApplications(Connection c) throws SQLException {
      ArrayList<Application> result = new ArrayList<Application>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + " ORDER BY " + NAME ;
      //log.info(TABLE + ": " + query);
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new Application(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static String getNewCode(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      return StoreDB.getNewCode(stmt, TABLE, OBJECT_CODE, ID, CODE);
    }
  }

  /**
   * An ORM object representing an application template.
   */
  public static class ApplicationTemplate {
    public static final String TABLE = "application_templates";

    public static final String APP_ID = "appt_app_id";
    public static final String T_ID = "appt_t_id";
    public static final String FLAGS = "appt_flags";
    public static final String PARENT_MENU = "appt_parent_menu";
    public static final String RANK = "appt_rank";
    public static final String USER_ID = "appt_user_id";
    public static final String LASTUPDATEDAT = "appt_lastupdateat";

    /**
     * The key of the application.
     */
    private int appId;
    private Application app;

    /**
     * The key of the template.
     */
    private int tId;
    private Template t;

    /**
     * The flags of the application.
     */
    private int flags;

    /**
     * The key of the parent menu template.
     */
    private int parentMenuId;

    /**
     * The rank of the template within menu.
     */
    private int rank;

    /**
     * The key of the author created this application.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new application template.
     *
     * @param appId
     * @param tId
     * @param flags
     * @param parentMenuId
     * @param rank
     * @param userId
     *          the author who created this application template
     */
    public ApplicationTemplate(int appId, int tId, int flags, int parentMenuId,
        int rank, int userId) {
      setAppId(appId);
      setTId(tId);
      setFlags(flags);
      setParentMenuId(parentMenuId);
      setRank(rank);
      setUserId(userId);
    }

    /**
     * Create a new application template.
     *
     * @param rs
     * @throws SQLException
     */
    public ApplicationTemplate(ResultSet rs) throws SQLException {
      setAppId(rs.getInt(APP_ID));
      setApp(new Application(rs));
      setTId(rs.getInt(T_ID));
      setT(new Template(rs, true));
      setFlags(rs.getInt(FLAGS));
      setParentMenuId(rs.getInt(PARENT_MENU));
      setRank(rs.getInt(RANK));
      setUserId(rs.getInt(USER_ID));
    }

    public int getAppId() {
      return appId;
    }

    public void setAppId(int appId) {
      this.appId = appId;
    }

    public Application getApp() {
      return app;
    }

    public void setApp(Application app) {
      this.app = app;
    }

    public int getTId() {
      return tId;
    }

    public void setTId(int tId) {
      this.tId = tId;
    }

    public Template getT() {
      return t;
    }

    public void setT(Template t) {
      this.t = t;
    }

    public int getParentMenuId() {
      return parentMenuId;
    }

    public void setParentMenuId(int parentMenuId) {
      this.parentMenuId = parentMenuId;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

    public int getFlags() {
      return flags;
    }

    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static ApplicationTemplate load(Connection c, int appId, int tId) throws SQLException {
      ApplicationTemplate result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + APP_ID + "=" + appId + " AND " + T_ID + "=" + tId;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new ApplicationTemplate(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public ApplicationTemplate save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert or update new record
      stmt.execute(
        "INSERT INTO " + TABLE + " ("
          + APP_ID + ", " + T_ID + ", " + FLAGS + ", "
          + (getParentMenuId() > 0 ? PARENT_MENU + ", " : "")
          + RANK + ", "
          + USER_ID + ")"
          + " VALUES (" + getAppId() + "," + getTId() + "," + getFlags() + ","
          + (getParentMenuId() > 0 ? getParentMenuId() + ", " : "")
          + getRank() + ", "
          + getUserId() + ") "
          + "ON DUPLICATE KEY UPDATE "
          + FLAGS + "=" + getFlags() + ","
          + PARENT_MENU + "=" + (getParentMenuId() > 0 ? getParentMenuId() : "null") + ","
          + RANK + "=" + getRank() + ","
          + USER_ID + "=" + getUserId()
          );
      stmt.close();
      return this;
    }

    public static void deleteTemplates(Connection c, int appId) throws SQLException {
      Statement stmt = c.createStatement();
      String query = "DELETE FROM " + TABLE
          + " WHERE " + APP_ID + "=" + appId;
      stmt.execute(query);
      stmt.close();
    }

    public static ArrayList<ApplicationTemplate> loadAppTemplatesByApp(
        Connection c, int appId) throws SQLException {
      ArrayList<ApplicationTemplate> result = new ArrayList<ApplicationTemplate>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM "
          + Template.TABLE + " left outer join "
          + TABLE + " on (" + T_ID + "=" + Template.ID + ") left outer join "
          + Application.TABLE + " on (" + APP_ID + "=" + Application.ID + ") left outer join "
          + ObjectAttribute.TABLE + " on (" + Template.OA_ID + "="
          + ObjectAttribute.ID + ") left outer join " + ObjectType.TABLE
          + " on (" + Template.OT_ID + "=" + ObjectType.ID
          + ") left outer join " + ValueType.TABLE
            + " on (" + ObjectAttribute.TYPE_ID + "=" + ValueType.ID
          + ") left outer join " + Unit.TABLE
            + " on (" + ObjectAttribute.UNIT_ID + "=" + Unit.ID + ")"
          + " WHERE " + APP_ID + (appId == 0 ? " is null" : "=" + appId)
          + " ORDER BY " + PARENT_MENU + ", " + RANK;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ApplicationTemplate(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static ArrayList<ApplicationTemplate> loadAppTemplatesByTemplate(
        Connection c, int tId) throws SQLException {
      ArrayList<ApplicationTemplate> result = new ArrayList<ApplicationTemplate>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + ", "
          + Application.TABLE + ", " + Template.TABLE
          + " left outer join " + ObjectAttribute.TABLE
          + " on (" + Template.OA_ID + "=" + ObjectAttribute.ID
          + ") left outer join " + ObjectType.TABLE
          + " on (" + Template.OT_ID + "=" + ObjectType.ID
          + ") left outer join " + ValueType.TABLE
            + " on (" + ObjectAttribute.TYPE_ID + "=" + ValueType.ID
          + ") left outer join " + Unit.TABLE
            + " on (" + ObjectAttribute.UNIT_ID + "=" + Unit.ID + ")"
          + " WHERE " + T_ID + "=" + tId
          + " AND " + APP_ID + "=" + Application.ID
          + " AND " + T_ID + "=" + Template.ID
          + " ORDER BY " + Application.NAME;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ApplicationTemplate(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }
  }


  /**
   * An ORM object representing an application user.
   */
  public static class ApplicationUser {
    public static final String TABLE = "application_users";

    public static final String ID = "appu_id";
    public static final String APP_ID = "appu_app_id";
    public static final String APPUSER_ID = "appu_appuser_id";
    public static final String FLAGS = "appu_flags";
    public static final String TOP = "appu_top";
    public static final String LEFT = "appu_left";
    public static final String WIDTH = "appu_width";
    public static final String HEIGHT = "appu_height";
    public static final String SUBSCRIBEDAT = "appu_subscribedat";
    public static final String USER_ID = "appu_user_id";
    public static final String LASTUPDATEDAT = "appu_lastupdateat";


    /**
     * The key of the application's user.
     */
    private int id;

    /**
     * The key of the application.
     */
    private int appId;

    /**
     * The key of the user.
     */
    private int appUserId;

    /**
     * The flags of the application.
     */
    private int flags;

    /**
     * The top position of the application widget.
     */
    private int top;

    /**
     * The left position of the application widget.
     */
    private int left;

    /**
     * The width of the application widget.
     *
     */
    private int width;

    /**
     * The height of the application widget
     *
     */
    private int height;

    /**
     * The key of the author created this application.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();

    /**
     * The date of the first time this object was persisted.
     */
    private Date subscribedat = new Date();


    /**
     * Create a new application user.
     *
     * @param id
     * @param appId
     * @param appUserId
     * @param flags
     * @param top
     * @param left
     * @param width
     * @param height
     * @param userId
     *          the author who created this application template
     */
    public ApplicationUser(int id, int appId, int appUserId, int flags,
        int top, int left, int width, int height, int userId) {
      setId(id);
      setAppId(appId);
      setAppUserId(appUserId);
      setFlags(flags);
      setTop(top);
      setLeft(left);
      setWidth(width);
      setHeight(height);
      setUserId(userId);
    }

    /**
     * Create a new application user.
     *
     * @param rs
     * @throws SQLException
     */
    public ApplicationUser(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setAppId(rs.getInt(APP_ID));
      setAppUserId(rs.getInt(APPUSER_ID));
      setFlags(rs.getInt(FLAGS));
      setTop(rs.getInt(TOP));
      setLeft(rs.getInt(LEFT));
      setWidth(rs.getInt(WIDTH));
      setHeight(rs.getInt(HEIGHT));
      setUserId(rs.getInt(USER_ID));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getAppId() {
      return appId;
    }

    public void setAppId(int appId) {
      this.appId = appId;
    }

    public int getAppUserId() {
      return appUserId;
    }

    public void setAppUserId(int appUserId) {
      this.appUserId = appUserId;
    }

    public int getFlags() {
      return flags;
    }

    public void setFlags(int flags) {
      this.flags = flags;
    }

    public int getTop() {
      return top;
    }

    public void setTop(int top) {
      this.top = top;
    }

    public int getLeft() {
      return left;
    }

    public void setLeft(int left) {
      this.left = left;
    }

    public int getWidth() {
      return width;
    }

    public void setWidth(int width) {
      this.width = width;
    }

    public int getHeight() {
      return height;
    }

    public void setHeight(int height) {
      this.height = height;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public Date getSubscribedAt() {
      return subscribedat;
    }

    public void setSuscribedAt(Date subscribedat) {
      this.subscribedat = subscribedat;
    }

    public static ApplicationUser load(Connection c, int id) throws SQLException {
      ApplicationUser result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new ApplicationUser(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public ApplicationUser save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (this.getId() == 0) {
        stmt.execute(
          "INSERT INTO " + TABLE + " ("
            + APP_ID + ", " + APPUSER_ID + ", " + FLAGS + ", " + TOP + ", "
            + LEFT + ", " + WIDTH + ", " + HEIGHT + ", "
            + SUBSCRIBEDAT + ", " + USER_ID + ")"
          + " VALUES (" + getAppId()
          + "," + getAppUserId()
          + "," + getFlags()
          + "," + (getTop() == 0 ? "null" : ""+getTop())
          + "," + (getLeft() == 0 ? "null" : ""+getLeft())
          + "," + (getWidth() == 0 ? "null" : ""+getWidth())
          + "," + (getHeight() == 0 ? "null" : ""+getHeight())
          + ", now() "
          + "," + getUserId() + ")");
        setId(getIdentity(stmt));
      }
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + FLAGS + "=" + getFlags() + ", "
            + TOP + "=" + (getTop() == 0 ? "null, " : getTop() + ", ")
            + LEFT + "=" + (getLeft() == 0 ? "null, " : getLeft() + ", ")
            + WIDTH + "=" + (getWidth() == 0 ? "null, " : getWidth() + ", ")
            + HEIGHT + "=" + (getHeight() == 0 ? "null, " : getHeight() + ", ")
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static ArrayList<ApplicationUser> loadApplicationUsers(Connection c, int appId)
        throws SQLException {
      ArrayList<ApplicationUser> result = new ArrayList<ApplicationUser>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
          + " WHERE " + APP_ID + "=" + appId;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ApplicationUser(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static ArrayList<ApplicationUser> loadUserApplications(Connection c, int userId)
        throws SQLException {
      ArrayList<ApplicationUser> result = new ArrayList<ApplicationUser>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
          + " WHERE " + APPUSER_ID + "=" + userId;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.add(new ApplicationUser(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }
  }

  /**
   * An ORM object representing an object.
   */
  public static class AObject {
    public static final String TABLE = "aobjects";
    public static final String TABLE_PARENTS = "aobject_parents";

    public static final String ID = "ao_id";
    public static final String OT_ID = "ao_ot_id";
    public static final String USER_ID = "ao_user_id";
    public static final String LASTUPDATEDAT = "ao_lastupdateat";

    public static final String P_AO_ID = "pa_ao_id";
    public static final String P_OT_ID = "pa_ot_id";
    public static final String LEVEL = "pa_level";

    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The key of the object type.
     */
    private int otId;

    /**
     * The level in object types hierarchy.
     */
    private int level;

    /**
     * The value of given attribute.
     */
    private String leaf;

    /**
     * The key of the author created this object.
     */
    private int userId;
    private AppUser user;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new object.
     *
     * @param id
     * @param otId
     * @param level
     * @param userId
     *          the author who created this object
     */
    public AObject(int id, int otId, int level, String leaf, int userId) {
      setId(id);
      setOtId(otId);
      setLevel(level);
      setLeaf(leaf);
      setUserId(userId);
    }

    /**
     * Create a new object.
     *
     * @param rs
     * @throws SQLException
     */
    public AObject(ResultSet rs, boolean withLevel, boolean withUser) throws SQLException {
      setId(rs.getInt(ID));
      setOtId(rs.getInt(OT_ID));
      if (withLevel) setLevel(rs.getInt(LEVEL));
      setUserId(rs.getInt(USER_ID));
      if (withUser) setUser(new AppUser(rs));
      if (rs.getString(AValue.VALUE_STRING) != null)
        setLeaf(rs.getString(AValue.VALUE_STRING));
      else if (rs.getString(AValue.VALUE_NUMBER) != null)
        setLeaf(""+rs.getDouble(AValue.VALUE_NUMBER));
      else if (rs.getString(AValue.VALUE_DATE) != null)
        setLeaf(""+rs.getDate(AValue.VALUE_DATE));
      setLastUpdatedAt(rs.getTimestamp(LASTUPDATEDAT));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getOtId() {
      return otId;
    }

    public void setOtId(int otId) {
      this.otId = otId;
    }

    public int getLevel() {
      return level;
    }

    public void setLevel(int level) {
      this.level = level;
    }

    public String getLeaf() {
      return leaf;
    }

    public void setLeaf(String leaf) {
      this.leaf = leaf;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public AppUser getUser() {
      return user;
    }

    public void setUser(AppUser user) {
      this.user = user;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static AObject load(Connection c, int id) throws SQLException {
      AObject result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
          + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new AObject(rs, false, false);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public AObject save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      // insert new record
      if (getId() == 0) {
        stmt.execute(
          "INSERT INTO " + TABLE + " (" + OT_ID + ", " + USER_ID + ")"
          + " VALUES (" + getOtId() + ", " + getUserId() + ")");
        setId(getIdentity(stmt));
      }

      /*
      // update an existing record
      else {
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + OT_ID + "=" + getOtId() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }*/
      stmt.close();
      return this;
    }

    public static int delete(Connection c, int otId, int objectId, int userId)
        throws SQLException {
      Statement stmt = c.createStatement();
      int result = stmt.executeUpdate(
          "DELETE FROM " + TABLE + " WHERE " + ID + "=" + objectId);
      stmt.execute("CALL log_delete(" + otId + ", " + objectId + ", "
          + LOG_OBJECT_DELETE + "," + userId +")");
      stmt.close();
      return result;
    }

    public static List<AObject> loadObjects(Connection c, int langId, int tId,
        List<TreeLevel> path, int oaId) throws SQLException {
      List<AObject> result = new ArrayList<AObject>();

      Statement stmt = c.createStatement();

      String[] hlp = getTablesAndWheres(path);
      String query = "SELECT * FROM " + AValue.TABLE + " av left outer join "
          + AValue.TABLE_CHAR + " avc on (avc." + AValue.AVC_AV_ID + "= av." + AValue.ID + ") left outer join "
          + AValue.TABLE_DATE + " avd on (avd." + AValue.AVD_AV_ID + "= av." + AValue.ID + ") left outer join "
          + AValue.TABLE_NUMBER + " avn on (avn." + AValue.AVN_AV_ID + "= av." + AValue.ID + "), "
          + AppUser.TABLE + ", " + Template.TABLE + ", " + TABLE + ", " + TABLE_PARENTS + hlp[0]
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + P_OT_ID + "=" + Template.OT_ID
          + " AND " + P_AO_ID + "=" + ID
          + " AND " + USER_ID + "=" + AppUser.ID
          + " AND av." + AValue.AO_ID + "=" + ID
          + " AND av." + AValue.OA_ID + "=" + oaId
          + hlp[1]
          + " ORDER BY avc." + AValue.AVC_AV_ID + ", avc." + AValue.AVC_LANG_ID;
      log.info(query);
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        AObject defObject = new AObject(rs, true, true);
        while (rs.next()) {
          AObject object = new AObject(rs, true, true);
          if (object.getId() == defObject.getId()) {
            if (rs.getInt(AValue.AVC_LANG_ID) == langId)
              defObject = object;
          } else {
            result.add(defObject);
            defObject = object;
          }
        }
        result.add(defObject);
      }
      rs.close();
      stmt.close();
      Collections.sort(result, new AObjectComparator());
      return result;
    }

    public static HashMap<Template, Integer> loadSearchObjectCounts(Connection c,
        int appId, String searchString) throws SQLException {
      HashMap<Template, Integer> result = new LinkedHashMap<Template, Integer>();
      Statement stmt = c.createStatement();

      String query = "SELECT counts.c, t.* FROM " + Template.TABLE + " t, "
          + " (SELECT COUNT(distinct " + ID + ") c, "  + Template.ID
          + " FROM " + TABLE + ", " + Template.TABLE + ", " + ApplicationTemplate.TABLE
          + (searchString.length() > 0 ? ", " + AValue.TABLE + ", " + AValue.TABLE_CHAR : "")
          + " WHERE " + OT_ID + "=" + Template.OT_ID
          + " AND " + Template.ID + "=" + ApplicationTemplate.T_ID
          + " AND " + ApplicationTemplate.APP_ID + "=" + appId
          + (searchString.length() > 0 ? " AND " + ID + "=" + AValue.AO_ID
              + " AND " + AValue.ID + "=" + AValue.AVC_AV_ID
              + " AND " + AValue.VALUE_STRING + " like '" + searchString + "%'" : "")
          + " GROUP BY " + Template.ID + ") counts "
          + " WHERE t." + Template.ID + "= counts." + Template.ID
          + " ORDER BY " + Template.NAME;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.put(new Template(rs, false), rs.getInt("c"));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static Map<AObject, List<AValue>> loadSearchObjects(Connection c,
        int langId, String searchString, int tlId, int from, int perPage)
            throws SQLException {
      Statement stmt = c.createStatement();

      // query could return less then "perPage" records if it hits an object
      // more than x times on average
      int x = 4;

      String query = "SELECT distinct ao_id, ao_ot_id, ao_user_id, "
          + " ao_lastupdateat, ta_flags, tli_rank, main_val.av_id, main_val.av_ao_id, "
          + " main_val.av_oa_id, main_val.av_rank, main_val.av_lastupdateat, "
          + " main_char.avc_value, main_text.avt_value, main_text.avt_lang_id, "
          + " CASE WHEN main_ref.avr_av_id is NULL THEN main_char.avc_lang_id ELSE ref_char.avc_lang_id END as avc_lang_id, "
          + " avd_value, avn_value, main_ref.avr_value, main_val.av_user_id, "
          + " ref.av_ao_id as ao, ref.av_oa_id as oa, ref_char.avc_value as ao_leaf "
          + " FROM (SELECT distinct * FROM (SELECT ao_id, ao_ot_id, "
          + " ao_user_id, ao_lastupdateat, ta_oa_id, ta_shared2, ta_flags, tli_rank "
          + " FROM " + Template.TABLE + ", " + TemplateList.TABLE + ", "
          + TemplateListItem.TABLE + "," + TemplateAttribute.TABLE + ", " + TABLE
          + (searchString.length() > 0 ? ", " + AValue.TABLE + " search, "
          + AValue.TABLE_CHAR + " search_value " : "")
          + " WHERE " + TemplateList.ID + "=" + tlId
          + " AND " + Template.ID + "=" + TemplateList.T_ID
          + " AND " + OT_ID + "=" + Template.OT_ID
          + " AND " + TemplateList.ID + "=" + TemplateListItem.TL_ID
          + " AND " + TemplateListItem.TA_ID + "=" + TemplateAttribute.ID
          + (searchString.length() > 0 ? " AND " + ID + "= search." + AValue.AO_ID
              + " AND search." + AValue.ID + " = search_value." + AValue.AVC_AV_ID
              + " AND search_value." + AValue.VALUE_STRING + " like '" + searchString + "%'" : "")
          + " ORDER BY " + ID + ", " + TemplateListItem.RANK
          + " LIMIT " + from + ", " + perPage*x + ") raw LIMIT 0, " + perPage
          + ") objs left outer join "
          + AValue.TABLE + " main_val on  (main_val." + AValue.AO_ID + "=" + ID
          + " AND main_val." + AValue.OA_ID + "=" + TemplateAttribute.OA_ID + ") left outer join "
          + AValue.TABLE_CHAR + " main_char on (main_char." + AValue.AVC_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_DATE + " on (" + AValue.AVD_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_NUMBER + " on (" + AValue.AVN_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_REF + " main_ref on (main_ref." + AValue.AVR_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_TEXT + " main_text on (main_text." + AValue.AVT_AV_ID + " = main_val." + AValue.ID + ") "
          + " left outer join " + AValue.TABLE + " ref on (main_ref." + AValue.VALUE_REF + " = ref." + AValue.AO_ID
          + " AND " + TemplateAttribute.SHARED2 + " = ref." + AValue.OA_ID + ") left outer join "
          + AValue.TABLE_CHAR + " ref_char on (ref_char." + AValue.AVC_AV_ID + " = ref." + AValue.ID + ")"
          + " ORDER BY " + ID + ", " + TemplateListItem.RANK + ", "
          + AValue.RANK  + ", main_char." + AValue.AVC_LANG_ID + ", ref_char." + AValue.AVC_LANG_ID;
      ResultSet rs = stmt.executeQuery(query);
      Map<AObject, List<AValue>> result = getRows(rs, langId);
      rs.close();
      stmt.close();
      return result;
    }

    public static int loadRelatedObjectCounts(Connection c,
        int objId, int rel) throws SQLException {
      int result = 0;
      Statement stmt = c.createStatement();

      String query = "SELECT COUNT(*) "
          + " FROM " + Relation.TABLE
          + " WHERE " + Relation.OREL + "=" + rel
          + " AND " + Relation.OBJ1 + "=" + objId;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = rs.getInt(1);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static Map<AObject, List<AValue>> loadRelatedObjects(Connection c,
        int langId, int objId, int rel, int tlId, int from, int perPage)
            throws SQLException {
      Statement stmt = c.createStatement();

      String query = "SELECT ao_id, ao_ot_id, ao_user_id, ao_lastupdateat, "
      		+ " ta_flags, tli_rank, main_val.av_id, main_val.av_ao_id, "
      		+ " main_val.av_oa_id, main_val.av_rank, main_val.av_lastupdateat, "
      		+ " main_char.avc_value, main_text.avt_value, main_text.avt_lang_id, "
          + " CASE WHEN main_ref.avr_av_id is NULL THEN main_char.avc_lang_id ELSE ref_char.avc_lang_id END as avc_lang_id, "
      		+ " avd_value, avn_value, main_ref.avr_value, "
      		+ " main_val.av_user_id, ref.av_ao_id as ao, ref.av_oa_id as oa, "
      		+ " ref_char.avc_value as ao_leaf "
          + " FROM (SELECT ao_id, ao_ot_id, ao_user_id, "
          + " ao_lastupdateat, ta_oa_id, ta_shared2, ta_flags, tli_rank FROM "
          + Template.TABLE + "," + TemplateList.TABLE + "," + TemplateListItem.TABLE + ","
          + TemplateAttribute.TABLE + "," + TABLE + "," + Relation.TABLE
          + " WHERE " + TemplateList.ID + "=" + tlId
          + " AND " + Template.ID + "=" + TemplateList.T_ID
          + " AND " + OT_ID + "=" + Template.OT_ID
          + " AND " + TemplateList.ID + "=" + TemplateListItem.TL_ID
          + " AND " + TemplateListItem.TA_ID + "=" + TemplateAttribute.ID
          + " AND " + Relation.OREL + "=" + rel
          + " AND " + Relation.OBJ1 + "=" + objId
          + " AND " + Relation.OBJ2 + "=" + ID
          + " ORDER BY " + ID + ", " + TemplateListItem.RANK
          + " LIMIT " + from + ", " + perPage
          + ") objs left outer join "
          + AValue.TABLE + " main_val on  (" + AValue.AO_ID + "=" + ID
          + " AND " + AValue.OA_ID + "=" + TemplateAttribute.OA_ID + ") left outer join "
          + AValue.TABLE_CHAR + " main_char on (main_char." + AValue.AVC_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_DATE + " on (" + AValue.AVD_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_NUMBER + " on (" + AValue.AVN_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_REF + " main_ref on (main_ref." + AValue.AVR_AV_ID + " = main_val." + AValue.ID + ") left outer join "
          + AValue.TABLE_TEXT + " main_text on (main_text." + AValue.AVT_AV_ID + " = main_val." + AValue.ID + ") "
          + " left outer join " + AValue.TABLE + " ref on (main_ref." + AValue.VALUE_REF + " = ref." + AValue.AO_ID
          + " AND " + TemplateAttribute.SHARED2 + " = ref." + AValue.OA_ID + ") left outer join "
          + AValue.TABLE_CHAR + " ref_char on (ref_char." + AValue.AVC_AV_ID + " = ref." + AValue.ID + ")"
          + " ORDER BY " + ID + ", " + TemplateListItem.RANK + ", "
          + AValue.RANK + ", main_char." + AValue.AVC_LANG_ID + ", ref_char." + AValue.AVC_LANG_ID;
      ResultSet rs = stmt.executeQuery(query);
      Map<AObject, List<AValue>> result = getRows(rs, langId);
      rs.close();
      stmt.close();
      return result;
    }

    public static AObject find(Connection c, int tId, ArrayList<AValue> filter)
        throws SQLException {
      AObject result = null;

      Statement stmt = c.createStatement();

      String[] hlp = getTablesAndWheresForFilter(filter);

      String query = "SELECT * FROM " + TABLE + ", " + TABLE_PARENTS + ", "
          + Template.TABLE + hlp[0]
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + P_OT_ID + "=" + Template.OT_ID
          + " AND " + ID + "=" + P_AO_ID
          + hlp[1];
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) result = new AObject(rs, false, false);
      if (rs.next()) result = new AObject(0,0,0,null,0);

      rs.close();
      stmt.close();
      return result;
    }
  }

  /**
   * An ORM object representing an object tree level.
   */
  public static class TreeLevel {

    /**
     * The id of the tree level.
     */
    private int id;

    /**
     * The template attribute.
     */
    private TemplateAttribute ta;

    /**
     * The string value.
     */
    private String valueString;

    /**
     * The date value.
     */
    private Date valueDate;

    /**
     * The number value.
     */
    private Double valueDouble;

    /**
     * The reference value.
     */
    private int valueRef;

    /**
     * The id of tree level which the TreeLevel is subset of
     */
    private int subsetOf;


    /**
     * Create a new tree level item.
     *
     * @param ta
     */
    public TreeLevel(int id, TemplateAttribute ta) {
      this.id = id;
      this.ta = ta;
    }

    public int getId() {
      return id;
    }

    public TemplateAttribute getTa() {
      return ta;
    }

    public String getValueString() {
      return valueString;
    }

    public void setValueString(String valueString) {
      this.valueString = valueString;
    }

    public Date getValueDate() {
      return valueDate;
    }

    public void setValueDate(Date valueDate) {
      this.valueDate = valueDate;
    }

    public Double getValueDouble() {
      return valueDouble;
    }

    public void setValueDouble(Double valueDouble) {
      this.valueDouble = valueDouble;
    }

    public int getValueRef() {
      return valueRef;
    }

    public void setValueRef(int valueRef) {
      this.valueRef = valueRef;
    }

    public int getSubsetOf() {
      return subsetOf;
    }

    public void setSubsetOf(int subsetOf) {
      this.subsetOf = subsetOf;
    }

    public static List<TreeLevel> loadTreeLevelString(Connection c, int langId, List<AValue> filter, int tId,
        List<TreeLevel> path, TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      List<TreeLevel> result = new ArrayList<TreeLevel>();

      Statement stmt = c.createStatement();
      String[] hlp = getTablesAndWheres(path);
      String[] hlpFilter =  getTablesAndWheresForFilter(filter);

      String avc = "avc." + AValue.VALUE_STRING;
      boolean isDerived = ta.getDef() != null &&
          (ta.getFlags() & (1<<TemplateAttribute.FLAG_DERIVED)) > 0;
      if (isDerived) {
        avc = String.format(ta.getDef(), avc) + " as " + AValue.VALUE_STRING;
      }

      String query = "SELECT distinct av." + AValue.OA_ID + ", " + avc
          + " FROM " + Template.TABLE +  ", " + AObject.TABLE_PARENTS
          + " left outer join " + AValue.TABLE + " av on (av." + AValue.AO_ID
            + "=" + AObject.P_AO_ID + " AND av." + AValue.OA_ID + "=" + ta.getOaId() + ") "
          + " left outer join " + AValue.TABLE_CHAR + " avc on (avc."
            + AValue.AVC_AV_ID + "= av." + AValue.ID + ") "
          + hlp[0]
          + hlpFilter[0]
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + AObject.P_OT_ID + "=" + Template.OT_ID
          + " AND " + AValue.AVC_LANG_ID + "=" + langId
          + hlp[1]
          + hlpFilter[1]
          + " ORDER BY avc." + AValue.VALUE_STRING;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        TreeLevel tl = new TreeLevel(tti.getId(), ta);
        tl.setValueString(rs.getString(AValue.VALUE_STRING));
        result.add(tl);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static List<TreeLevel> loadTreeLevelDate(Connection c, List<AValue> filter, int tId, List<TreeLevel> path,
        TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      List<TreeLevel> result = new ArrayList<TreeLevel>();

      Statement stmt = c.createStatement();
      String[] hlp = getTablesAndWheres(path);
      String[] hlpFilter =  getTablesAndWheresForFilter(filter);

      String avd = "avd." + AValue.VALUE_DATE;
      boolean isDerived = ta.getDef() != null &&
          (ta.getFlags() & (1<<TemplateAttribute.FLAG_DERIVED)) > 0;
      if (isDerived) {
        avd = String.format(ta.getDef(), avd) + " as " + AValue.VALUE_DATE;
      }

      String query = "SELECT distinct av." + AValue.OA_ID + ", " + avd
          + " FROM " + Template.TABLE +  ", " + AObject.TABLE_PARENTS
          + " left outer join " + AValue.TABLE + " av on (av." + AValue.AO_ID + "=" + AObject.P_AO_ID
            + " AND av." + AValue.OA_ID + "=" + ta.getOaId() + ") "
          + " left outer join " + AValue.TABLE_DATE + " avd on (avd." + AValue.AVD_AV_ID + "= av." + AValue.ID + ") "
          + hlp[0]
          + hlpFilter[0]
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + AObject.P_OT_ID + "=" + Template.OT_ID
          + hlp[1]
          + hlpFilter[1]
          + " ORDER BY avd." + AValue.VALUE_DATE;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        TreeLevel tl = new TreeLevel(tti.getId(), ta);
        tl.setValueString(rs.getString(AValue.VALUE_DATE));
        result.add(tl);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static List<TreeLevel> loadTreeLevelNumber(Connection c, List<AValue> filter, int tId, List<TreeLevel> path,
        TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      List<TreeLevel> result = new ArrayList<TreeLevel>();

      Statement stmt = c.createStatement();
      String[] hlp = getTablesAndWheres(path);
      String[] hlpFilter =  getTablesAndWheresForFilter(filter);
      String query = "SELECT distinct av." + AValue.OA_ID + ", avn." + AValue.VALUE_NUMBER
          + " FROM " + Template.TABLE +  ", " + AObject.TABLE_PARENTS
          + " left outer join " + AValue.TABLE + " av on (av." + AValue.AO_ID + "=" + AObject.P_AO_ID
            + " AND av." + AValue.OA_ID + "=" + ta.getOaId() + ") "
          + " left outer join " + AValue.TABLE_NUMBER + " avn on (avn."
            + AValue.AVC_AV_ID + "= av." + AValue.ID + ") "
          + hlp[0]
          + hlpFilter[0]
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + AObject.P_OT_ID + "=" + Template.OT_ID
          + hlp[1]
          + hlpFilter[1]
          + " ORDER BY avn." + AValue.VALUE_NUMBER;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        TreeLevel tl = new TreeLevel(tti.getId(), ta);
        tl.setValueDouble(rs.getDouble(AValue.VALUE_NUMBER));
        result.add(tl);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static List<TreeLevel> loadTreeLevelRef(Connection c, int langId, List<AValue> filter, int tId,
        List<TreeLevel> path, TemplateTreeItem tti, TemplateAttribute ta) throws SQLException {
      List<TreeLevel> result = new ArrayList<TreeLevel>();

      Statement stmt = c.createStatement();
      String[] hlp = getTablesAndWheres(path);
      String[] hlpFilter =  getTablesAndWheresForFilter(filter);
//      String query = "SELECT distinct av." + AValue.OA_ID
//          + ", avr." + AValue.VALUE_REF + ", avcref." + AValue.AVC_AV_ID
//          + ", avcref." + AValue.AVC_LANG_ID + ", avcref." + AValue.VALUE_STRING
//          + " FROM " + Template.TABLE +  ", " + AObject.TABLE_PARENTS
//          + " left outer join " + AValue.TABLE + " av on (av." + AValue.AO_ID + "=" + AObject.P_AO_ID
//            + " AND av." + AValue.OA_ID + "=" + oaId + ") "
//          + " left outer join " + AValue.TABLE_REF + " avr on (avr." + AValue.AVR_AV_ID + "= av." + AValue.ID + ") "
//          + " left outer join " + AValue.TABLE + " avref on (avr." + AValue.VALUE_REF + "= avref." + AValue.AO_ID
//            + " AND avref." + AValue.OA_ID + "=" + refoaId + ") "
//          + " left outer join " + AValue.TABLE_CHAR + " avcref on (avcref." + AValue.AVC_AV_ID + "= avref." + AValue.ID + ") "
//          + hlp[0]
//          + hlpfilter[0]
//          + " WHERE " + Template.ID + "=" + tId
//          + " AND " + AObject.P_OT_ID + "=" + Template.OT_ID
//          + hlp[1]
//          + hlpfilter[1]
//          + " ORDER BY avcref." + AValue.AVC_AV_ID + ", avcref."
//          + AValue.AVC_LANG_ID + ", avcref." + AValue.VALUE_STRING;
      String query = "SELECT distinct "
          + Relation.OBJ2 + ", avcref." + AValue.AVC_AV_ID
          + ", avcref." + AValue.AVC_LANG_ID + ", avcref." + AValue.VALUE_STRING
          + " FROM " + Template.TABLE +  ", " + AObject.TABLE_PARENTS
          + " left outer join " + Relation.TABLE + " on (" + AObject.P_AO_ID
            + "=" + Relation.OBJ1 + " AND " + Relation.OREL + "=" + ta.getOa().getShared2() + ") "
          + " left outer join " + AValue.TABLE + " av on (av." + AValue.AO_ID
            + "=" + Relation.OBJ2 + " AND av." + AValue.OA_ID + "=" + ta.getShared2() + ") "
          + " left outer join " + AValue.TABLE_CHAR + " avcref on (avcref."
            + AValue.AVC_AV_ID + "= av." + AValue.ID + ") "
          + hlp[0]
          + hlpFilter[0]
          + " WHERE " + Template.ID + "=" + tId
          + " AND " + AObject.P_OT_ID + "=" + Template.OT_ID
          + hlp[1]
          + hlpFilter[1]
          + " ORDER BY avcref." + AValue.AVC_AV_ID + ", avcref."
          + AValue.AVC_LANG_ID + ", avcref." + AValue.VALUE_STRING;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        int defId = rs.getInt(AValue.AVC_AV_ID);
        String defString = rs.getString(AValue.VALUE_STRING);
        int defRef = rs.getInt(Relation.OBJ2);
        while (rs.next()) {
          if (rs.getInt(AValue.AVC_AV_ID) == defId) {
            if (rs.getInt(AValue.AVC_LANG_ID) == langId) {
              defId = rs.getInt(AValue.AVC_AV_ID);
              defString = rs.getString(AValue.VALUE_STRING);
              defRef = rs.getInt(Relation.OBJ2);
            }
          } else {
            TreeLevel tl = new TreeLevel(tti.getId(), ta);
            tl.setValueRef(defRef);
            tl.setValueString(defString);
            tl.setSubsetOf(tti.getSubsetOf());
            result.add(tl);
            defId = rs.getInt(AValue.AVC_AV_ID);
            defString = rs.getString(AValue.VALUE_STRING);
            defRef = rs.getInt(Relation.OBJ2);
          }
        }
        TreeLevel tl = new TreeLevel(tti.getId(), ta);
        tl.setValueRef(defRef);
        tl.setValueString(defString);
        tl.setSubsetOf(tti.getSubsetOf());
        result.add(tl);
      }
      rs.close();
      stmt.close();
      Collections.sort(result, new TreeLevelComparator());
      return result;
    }
  }

  /**
   * An ORM object representing a value.
   */
  public static class AValue {
    public static final String TABLE = "avalues";
    public static final String TABLE_CHAR = "avalues_char";
    public static final String TABLE_DATE = "avalues_date";
    public static final String TABLE_NUMBER = "avalues_number";
    public static final String TABLE_REF = "avalues_ref";
    public static final String TABLE_TEXT = "avalues_text";

    public static final String ID = "av_id";
    public static final String AO_ID = "av_ao_id";
    public static final String OA_ID = "av_oa_id";
    public static final String RANK = "av_rank";
    public static final String USER_ID = "av_user_id";
    public static final String LASTUPDATEDAT = "av_lastupdateat";

    public static final String AVC_AV_ID = "avc_av_id";
    public static final String VALUE_STRING = "avc_value";
    public static final String AVC_LANG_ID = "avc_lang_id";
    public static final String AVT_AV_ID = "avt_av_id";
    public static final String VALUE_TEXT = "avt_value";
    public static final String AVT_LANG_ID = "avt_lang_id";
    public static final String AVD_AV_ID = "avd_av_id";
    public static final String VALUE_DATE= "avd_value";
    public static final String AVN_AV_ID = "avn_av_id";
    public static final String VALUE_NUMBER = "avn_value";
    public static final String AVR_AV_ID = "avr_av_id";
    public static final String VALUE_REF = "avr_value";


    /**
     * An auto-generated primary key for this object. This key will be a child
     * key of the owning surface's key.
     */
    private int id;

    /**
     * The key of the object.
     */
    private int oId;

    /**
     * The key of the object attribute.
     */
    private int oaId;

    /**
     * The rank of the value (row in table).
     */
    private int rank;

    /**
     * The string value.
     */
    private String valueString;
    private int langId;

    /**
     * The date value.
     */
    private Date valueDate;
    private Date valueTimestamp;

    /**
     * The number value.
     */
    private Double valueDouble;

    /**
     * The reference value.
     */
    private int valueRef;

    /**
     * The key of the author created this object.
     */
    private int userId;

    /**
     * The date of the last time this object was persisted.
     */
    private Date lastupdatedat = new Date();


    /**
     * Create a new value.
     *
     * @param id
     * @param oId
     * @param oaId
     * @param rank
     * @param valueString
     * @param valueDate
     * @param valueDouble
     * @param valueRef
     * @param userId
     *          the author who created this object
     */
    public AValue(int id, int oId, int oaId, int rank, String valueString,
        int langId, Date valueDate, Date valueTimestamp, Double valueDouble,
        int valueRef, int userId) {
      setId(id);
      setOId(oId);
      setOaId(oaId);
      setRank(rank);
      setValueString(valueString);
      setLangId(langId);
      setValueDate(valueDate);
      setValueTimestamp(valueTimestamp);
      setValueDouble(valueDouble);
      setValueRef(valueRef);
      setUserId(userId);
    }

    /**
     * Create a new object.
     *
     * @param rs
     * @throws SQLException
     */
    public AValue(ResultSet rs) throws SQLException {
      setId(rs.getInt(ID));
      setOId(rs.getInt(AO_ID));
      setOaId(rs.getInt(OA_ID));
      setRank(rs.getInt(RANK));
      String sstring = rs.getString(VALUE_STRING);
      if (sstring != null) {
        setValueString(rs.getString(VALUE_STRING));
        setLangId(rs.getInt(AVC_LANG_ID));
      } else {
        setValueString(rs.getString(VALUE_TEXT));
        setLangId(rs.getInt(AVT_LANG_ID));
      }
      setValueDate(rs.getDate(VALUE_DATE));
      setValueTimestamp(rs.getTimestamp(VALUE_DATE));
      String sdouble = rs.getString(VALUE_NUMBER);
      if (sdouble != null)
        setValueDouble(rs.getDouble(VALUE_NUMBER));
      else
        setValueDouble(null);
      setValueRef(rs.getInt(VALUE_REF));
      setUserId(rs.getInt(USER_ID));
      setLastUpdatedAt(rs.getTimestamp(LASTUPDATEDAT));
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getOId() {
      return oId;
    }

    public void setOId(int oId) {
      this.oId = oId;
    }

    public int getOaId() {
      return oaId;
    }

    public void setOaId(int oaId) {
      this.oaId = oaId;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

    public String getValueString() {
      return valueString;
    }

    public void setValueString(String valueString) {
      this.valueString = valueString;
    }

    public int getLangId() {
      return langId;
    }

    public void setLangId(int langId) {
      this.langId = langId;
    }

    public Date getValueDate() {
      return valueDate;
    }

    public void setValueDate(Date valueDate) {
      this.valueDate = valueDate;
    }

    public Date getValueTimestamp() {
      return valueTimestamp;
    }

    public void setValueTimestamp(Date valueTimestamp) {
      this.valueTimestamp = valueTimestamp;
    }

    public Double getValueDouble() {
      return valueDouble;
    }

    public void setValueDouble(Double valueDouble) {
      this.valueDouble = valueDouble;
    }

    public int getValueRef() {
      return valueRef;
    }

    public void setValueRef(int valueRef) {
      this.valueRef = valueRef;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public Date getLastUpdatedAt() {
      return lastupdatedat;
    }

    public void setLastUpdatedAt(Date lastupdatedat) {
      this.lastupdatedat = lastupdatedat;
    }

    public static AValue load(Connection c, int id) throws SQLException {
      AValue result = null;

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE
        + " WHERE " + ID + "=" + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        result = new AValue(rs);
      }
      rs.close();
      stmt.close();
      return result;
    }

    public AValue save(Connection c) throws SQLException {
      Statement stmt = c.createStatement();
      if ((getValueString() == null || getValueString().length() == 0)
          && getValueDate() == null && getValueDouble() == null
          && getValueRef() == 0) {
        if (getId() > 0 ) {
          // delete empty record
          // delete avalues_ref to invoke triggers
          stmt.execute("DELETE FROM " + TABLE_REF
              + " WHERE " + AVR_AV_ID + "=" + getId());
          stmt.execute("DELETE FROM " + TABLE
              + " WHERE " + ID + "=" + getId());
          stmt.execute("CALL clean_multivalues(" + getOId() + ", "
              + getRank() + ")");
          stmt.execute("CALL log_delete(" + getOaId() + ", " + getOId() + ", "
              + LOG_VALUE_DELETE + "," + getUserId() +")");
          setId(0);
        } else return this;
      } else {
        if (this.getId() == 0) {
          // find value if one was created by share_values routine
          StringBuilder query = new StringBuilder(BUFFER_SIZE);
          query.append("SELECT " + ID + " FROM " + TABLE
              + " WHERE " + AO_ID + "=");
          query.append(getOId());
          query.append(" AND " + OA_ID + "=");
          query.append(getOaId());
          query.append(" AND " + RANK + "=");
          query.append(getRank());
          ResultSet rs = stmt.executeQuery(query.toString());
          if (rs.next())
            setId(rs.getInt(ID));
          else {
            // insert new record
            stmt.execute(
              "INSERT INTO " + TABLE + " (" + AO_ID + ", " + OA_ID + ", "
                  + RANK + ", " + USER_ID + ")"
                  + " VALUES ("
                  + getOId() + ", "
                  + getOaId() + ", "
                  + getRank() + ", "
                  + getUserId() + ")");
            setId(getIdentity(stmt));
          }
        }

        log.info("ao: " + getOId() + "  oa: " + getOaId() + "  rank: " + getRank());
        if (getValueString() != null && getValueString().length() > 0) {
          StringBuilder query = new StringBuilder(BUFFER_SIZE);
          query.append("SELECT " + ValueType.TYPE + " FROM " + ValueType.TABLE
              + ", " + ObjectAttribute.TABLE
              + " WHERE " + ObjectAttribute.TYPE_ID + "=" + ValueType.ID
              + " AND " + ObjectAttribute.ID + "=");
          query.append(getOaId());
          log.info(query.toString());
          ResultSet rs = stmt.executeQuery(query.toString());
          if (rs.next())
            switch (rs.getInt(ValueType.TYPE)) {
            case ValueType.VT_STRING:
              stmt.execute(
                  "INSERT INTO " + TABLE_CHAR + " (" + AVC_AV_ID + ", "
                      + VALUE_STRING + ", "  + AVC_LANG_ID + ")"
                      + " VALUES ("
                      + getId() + ", "
                      + "'" + ServerUtils.mySQLFilter(getValueString()) + "', "
                      + getLangId() + ")"
                      + " ON DUPLICATE KEY UPDATE "
                      + VALUE_STRING + "='" + ServerUtils.mySQLFilter(getValueString()) + "'");
              break;
            case ValueType.VT_TEXT:
              stmt.execute(
                  "INSERT INTO " + TABLE_TEXT + " (" + AVT_AV_ID + ", "
                      + VALUE_TEXT + ", "  + AVT_LANG_ID + ")"
                      + " VALUES ("
                      + getId() + ", "
                      + "'" + ServerUtils.mySQLFilter(getValueString()) + "', "
                      + getLangId() + ")"
                      + " ON DUPLICATE KEY UPDATE "
                      + VALUE_TEXT + "='" + ServerUtils.mySQLFilter(getValueString()) + "'");
              break;
            default:
              break;
            }
        } else if (getValueDate() != null)
          stmt.execute(
              "INSERT INTO " + TABLE_DATE + " (" + AVD_AV_ID + ", "
                  + VALUE_DATE + ")"
                  + " VALUES ("
                  + getId() + ", "
                  + "'" + ServerUtils.MYSQLDATEFORMAT.format(getValueDate()) + "')"
                  + " ON DUPLICATE KEY UPDATE "
                  + VALUE_DATE + "='" + ServerUtils.MYSQLDATEFORMAT.format(getValueDate()) + "'");
        else if (getValueDouble() != null)
          stmt.execute(
              "INSERT INTO " + TABLE_NUMBER + " (" + AVN_AV_ID + ", "
                  + VALUE_NUMBER + ")"
                  + " VALUES ("
                  + getId() + ", "
                  + getValueDouble() + ")"
                  + " ON DUPLICATE KEY UPDATE "
                  + VALUE_NUMBER + "=" + getValueDouble());
        else if (getValueRef() > 0) {
          stmt.execute(
              "INSERT INTO " + TABLE_REF + " (" + AVR_AV_ID + ", "
                  + VALUE_REF + ")"
                  + " VALUES ("
                  + getId() + ", "
                  + getValueRef() + ")"
                  + " ON DUPLICATE KEY UPDATE "
                  + VALUE_REF + "=" + getValueRef());
          stmt.execute("CALL share_values(" + getOId() + ", "
              + getValueRef() + ", " + getUserId() + ")");
        }
        // update an existing record
        stmt.executeUpdate(
          "UPDATE " + TABLE + " SET "
            + RANK + "=" + getRank() + ", "
            + USER_ID + "=" + getUserId() + ", "
            + LASTUPDATEDAT + "= now()"
          + " WHERE " + ID + "=" + getId());
      }
      stmt.close();
      return this;
    }

    public static HashMap<String, AValue> loadSingleValues(Connection c, int oId)
        throws SQLException {
      HashMap<String, AValue> result = new HashMap<String, AValue>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + " left outer join "
          + TABLE_CHAR + " on (" + AVC_AV_ID + "=" + ID + ") left outer join "
          + TABLE_TEXT + " on (" + AVT_AV_ID + "=" + ID + ") left outer join "
          + TABLE_DATE + " on (" + AVD_AV_ID + "=" + ID + ") left outer join "
          + TABLE_NUMBER + " on (" + AVN_AV_ID + "=" + ID + ") left outer join "
          + TABLE_REF + " on (" + AVR_AV_ID + "=" + ID + ")"
          + " WHERE " + AO_ID + "=" + oId;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        result.put(rs.getInt(OA_ID)+"_"+rs.getInt(RANK), new AValue(rs));
      }
      rs.close();
      stmt.close();
      return result;
    }

    public static Map<Integer, List<AValue>> loadValues(Connection c, int oId) throws SQLException {
      Map<Integer, List<AValue>> result = new HashMap<Integer, List<AValue>>();

      Statement stmt = c.createStatement();

      String query = "SELECT * FROM " + TABLE + " left outer join "
          + TABLE_CHAR + " on (" + AVC_AV_ID + "=" + ID + ") left outer join "
          + TABLE_DATE + " on (" + AVD_AV_ID + "=" + ID + ") left outer join "
          + TABLE_NUMBER + " on (" + AVN_AV_ID + "=" + ID + ") left outer join "
          + TABLE_REF + " on (" + AVR_AV_ID + "=" + ID + ") left outer join "
          + TABLE_TEXT + " on (" + AVT_AV_ID + "=" + ID + ")"
          + " WHERE " + AO_ID + "=" + oId
          + " ORDER BY " + OA_ID + ", " + RANK + ", " + AVC_LANG_ID;
      log.info(TABLE + ": " + query);
      ResultSet rs = stmt.executeQuery(query);
      int oaId = 0;
      List<AValue> values = null;
      while (rs.next()) {
        if (oaId != rs.getInt(OA_ID)) {
          if (oaId > 0)
            result.put(oaId, values);
          values = new ArrayList<AValue>();
          oaId = rs.getInt(OA_ID);
        }
        values.add(new AValue(rs));
      }
      result.put(oaId, values);
      rs.close();
      stmt.close();
      return result;
    }

  }

  /**
   * An ORM object representing a relation.
   */
  public static class Relation {
    public static final String TABLE = "relations";

    public static final String ID = "ar_id";
    public static final String OBJ1 = "ar_ao_id1";
    public static final String OBJ2 = "ar_ao_id2";
    public static final String OREL = "ar_or_id";
    public static final String USER_ID = "ar_user_id";
  }

  /**
   * An ORM object representing a import tables.
   */
  public static class Import {
    public static final String TABLE_HEADER = "import_headers";
    public static final String TABLE_ROW = "import_rows";

    public static final String ID = "ih_id";
    public static final String APP_ID = "ih_app_id";
    public static final String T_ID = "ih_t_id";
    public static final String FILENAME = "ih_filename";
    public static final String USER_ID = "ih_user_id";

    public static final String ROW_ID = "ir_id";
    public static final String ROW_HEADER = "ir_ih_id";
    public static final String ROW_NUMBER = "ir_number";
    public static final String ROW_COLUMN = "ir_column";

    public static int saveHeader(Connection c, String userId, String appId, String tId,
        String filename) throws SQLException {
      Statement stmt = c.createStatement();
      stmt.execute(
          "INSERT INTO " + TABLE_HEADER + " (" + APP_ID + ", " + T_ID + ", "
              + FILENAME + ", " + USER_ID + ")"
              + " VALUES ("
              + appId + ", "
              + tId + ", '"
              + filename + "', "
              + userId + ")");
      return getIdentity(stmt);
    }

    public static int saveRow(Connection c, int headerId, int rowNumber, String row[])
        throws SQLException {
      Statement stmt = c.createStatement();

      String[] hlp = getColumnsAndValues(row);

      stmt.execute(
          "INSERT INTO " + TABLE_ROW + " (" + ROW_HEADER + ", " + ROW_NUMBER
              + hlp[0] + ")"
              + " VALUES ("
              + headerId + ", "
              + rowNumber
              + hlp[1] + ")");
      return getIdentity(stmt);
    }

    private static String[] getColumnsAndValues(String row[]) {
      String[] result = new String[2];
      result[0] = "";
      result[1] = "";
      int length = (row != null && row.length <= 100 ? row.length : 100);
      for (int i = 0; i < length; i++) {
        result[0] = result[0] + ", " + ROW_COLUMN + i;
        if (row != null)
          result[1] = result[1] + ", '" + ServerUtils.mySQLFilter(row[i]) + "'";
      }

      return result;
    }

    public static List<Integer> importObjects(Connection c, int userId,
        Application app, Template t, String filename,
        Map<Integer, TemplateAttribute> map,
        Map<Integer, TemplateAttribute> keys, boolean onlyUpdate)
            throws SQLException {
      List<Integer> result = new ArrayList<Integer>();
      Statement stmt = c.createStatement();
      Statement stmtDel = c.createStatement();
      String[] hlp = getColumnsAndValues(null);
      ResultSet rs = stmt.executeQuery("SELECT " + ID + ", " + ROW_NUMBER + hlp[0]
          + " FROM " + TABLE_HEADER + ", " + TABLE_ROW
          + " WHERE " + USER_ID + "=" + userId + " AND " + APP_ID + "=" + app.getId()
          + " AND " + T_ID + "=" + t.getId() + " AND " + FILENAME + "='" + filename
          + "' AND " + ID + "=" + ROW_HEADER);
      int count = 0;
      boolean wrongKey = false;
      while (rs.next()) {
        int headerId = rs.getInt(ID);
        ArrayList<AValue> filter = new ArrayList<AValue>();
        for (Integer field : keys.keySet()) {
          String svalue = rs.getString(ROW_COLUMN+field);
          if (svalue.length() == 0) {
            wrongKey = true;
            break;
          }
          TemplateAttribute ta = keys.get(field);
          AValue value = new AValue(0,0,ta.getOaId(),0,null,0,null,null,null,0,
              userId);
          setValue(c, value, ta, svalue, t.getId(), filter);
          filter.add(value);
        }

        if (wrongKey) {
          wrongKey = false;
          continue;
        }

        Map<String, AValue> dbvalues = new HashMap<String, AValue>();
        AObject ao = AObject.find(c, t.getId(), filter);
        // no match found - insert new object
        if (ao == null) {
          if (onlyUpdate) continue;
          ao = new AObject(0, t.getOtId(), 0, null, userId);
        // more than one match found - no update
        } else if (ao.getId() == 0) {
          log.warn("More than one match found for " + rs.getString(ROW_COLUMN+"0"));
          continue;
        // exactly one match found - update object
        } else
          dbvalues = AValue.loadSingleValues(c, ao.getId());
        ao.save(c);

        int taId = 0;
        int rank = 0;
        for (Integer field : map.keySet()) {
          String svalue = rs.getString(ROW_COLUMN+field);
          if (svalue.length() > 0) {
            TemplateAttribute ta = map.get(field);
            if (ta.getId() == taId) rank++;
            else {
              rank = 0;
              taId = ta.getId();
            }

            AValue value = dbvalues.get(ta.getOaId()+"_"+rank);
            if (value == null)
              value = new AValue(0,ao.getId(),ta.getOaId(),rank,null,0,
                  null,null, null,0,userId);

            setValue(c, value, ta, svalue, t.getId(), dbvalues.values());
            value.save(c);
            dbvalues.put(ta.getOaId()+"_"+rank, value);
          }
        }
        stmtDel.execute("delete from " + TABLE_ROW
            + " WHERE " + ROW_HEADER + "=" + headerId
            + " AND " + ROW_NUMBER + "=" + rs.getInt(ROW_NUMBER));
        count++;
        result.add(ao.getId());
        if (count%50 == 0) break;
      }
      rs.close();
      if (count == 0)
        stmtDel.execute("delete from " + TABLE_HEADER
            + " WHERE " + USER_ID + "=" + userId + " AND " + APP_ID + "=" + app.getId()
            + " AND " + T_ID + "=" + t.getId() + " AND " + FILENAME + "='" + filename + "'");
      stmtDel.close();
      stmt.close();
      return result;
    }

    private static void setValue(Connection c, AValue value, TemplateAttribute ta, String field,
        int tId, Collection<AValue> avalues) throws SQLException {
      if (field.length() == 0) return;
      switch (ta.getOa().getVt().getType()) {
        case ValueType.VT_INT:
          value.setValueDouble(new Double(field));
          break;

        case ValueType.VT_REAL:
          value.setValueDouble(new Double(field));
          break;

        case ValueType.VT_STRING:
          value.setValueString(field);
          break;

        case ValueType.VT_DATE:
          Date date;
          try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(field);
            value.setValueDate(date);
          } catch (ParseException e) {
            log.warn("Wrong date format: "+field, e);
          }
          break;

        case ValueType.VT_DATETIME:
          Date datetime;
          try {
            datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(field);
            value.setValueDate(datetime);
          } catch (ParseException e) {
            log.warn("Wrong date format: "+field, e);
          }
          break;

        case ValueType.VT_REF:
          ArrayList<AValue> filterRef = new ArrayList<AValue>();
          Map<Integer, TemplateAttribute> catrs = getCommonAttributes(c, tId, ta.getShared1());
          for (AValue av : avalues) {
            TemplateAttribute taref = catrs.get(av.getOaId());
            if (taref != null) {
              AValue valueRef = new AValue(0, 0, taref.getOaId(), 0,
                  av.getValueString(), av.getLangId(), av.getValueDate(),
                  av.getValueTimestamp(), av.getValueDouble(), av.getValueRef(),
                  0);
              filterRef.add(valueRef);
            }
          }
          AValue valueRef = new AValue(0,0,ta.getShared2(),0,null,0,null,null,
              null,0,0);
          valueRef.setValueString(field);
          filterRef.add(valueRef);
          AObject ao = AObject.find(c, ta.getShared1(), filterRef);
          if (ao != null && ao.getId() > 0)
            value.setValueRef(ao.getId());
          break;
      }
    }
  }

  public static Map<Integer, TemplateAttribute> getCommonAttributes(Connection c, int t1, int t2)
       throws SQLException {
    Map<Integer, TemplateAttribute> result = new HashMap<Integer, TemplateAttribute>();

    Statement stmt = c.createStatement();

    String query = "SELECT oa1." + ObjectAttribute.ID + " as oaId, ta2.*, tg2.*, oa2.*, vt.* "
        + " FROM " + TemplateGroup.TABLE + " tg1, " + TemplateAttribute.TABLE + " ta1, " + ObjectAttribute.TABLE + " oa1, "
        + TemplateGroup.TABLE + " tg2, " + TemplateAttribute.TABLE + " ta2, " + ObjectAttribute.TABLE + "  oa2, "
        + ValueType.TABLE + " vt"
        + " WHERE tg1." + TemplateGroup.T_ID + " = " + t1
        + " AND tg1." + TemplateGroup.ID + " = ta1." + TemplateAttribute.TG_ID
        + " AND ta1." + TemplateAttribute.OA_ID + " = oa1." + ObjectAttribute.ID
        + " AND tg2." + TemplateGroup.T_ID + " = " + t2
        + " AND tg2." + TemplateGroup.ID + " = ta2." + TemplateAttribute.TG_ID
        + " AND ta2." + TemplateAttribute.OA_ID + " = oa2." + ObjectAttribute.ID
        + " AND oa1." + ObjectAttribute.TYPE_ID + " = oa2." + ObjectAttribute.TYPE_ID
        + " AND oa1." + ObjectAttribute.TYPE_ID + " = vt." + ValueType.ID
        + " AND vt." + ValueType.FLAGS + " & (1 << " + ValueType.FLAG_SHAREABLE + ") > 0";

    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      result.put(rs.getInt("oaId"), new TemplateAttribute(rs, false, false));
    }
    rs.close();
    stmt.close();

    return result;
  }

  /** holds the Connection to the Database */
  //private static Connection connection;

  /**
   * Create a new database Store.
   *
   */
  public StoreDB() {
    //connection = getConnection();
  }

  /**
   * Starts a data store session and returns an Api object to use.
   *
   * @return      <code>Api</code>
   */
  public Api getApi() {
    return new Api();
  }

  /**
   * Connect to database <BR>
   *
   * @return successful of connect <BR>
   */
//  public static Connection getConnection() {
//    try {
//      if (connection == null || !connection.isInvalid(10)) {
//        DriverManager.registerDriver(new AppEngineDriver());
//
//        String url = "jdbc:google:rdbms://appsres:appsres/appsresource";
//        String user = "appsres";
//        String password = "appsres";
//        connection = DriverManager.getConnection(url, user, password);
//        connection.setAutoCommit(false);
//      }
//    }
//    catch (SQLException ex) {
//      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
//    }
//
//    return connection;
//  }

  /**
   * get last identity <BR>
   * <BR>
   *
   * @return identity <BR>
   */
  public static int getIdentity(Statement stmt) throws SQLException {
    String query = "select LAST_INSERT_ID()";
    ResultSet rs = stmt.executeQuery(query);
    rs.next();
    int identity = rs.getInt(1);
    rs.close();
    return identity;
  }

  /**
   * get code <BR>
   * <BR>
   *
   * @return code <BR>
   */
  public static String getNewCode(Statement stmt, String table, String code,
      String idColumn, String codeColumn) throws SQLException {
    String result;
    String query = "SELECT CAST(substring("
        + codeColumn + ", " + (1+code.length()+CODE_SEPARATOR.length()) + ", "
        + (code.length()+ID_OFFSET+CODE_SEPARATOR.length())
        + ") as UNSIGNED) FROM " + table
        + " WHERE " + idColumn + " = (SELECT MAX(" + idColumn + ") FROM " + table
        + ")";
    ResultSet rs = stmt.executeQuery(query);
    if (rs.next())
      result = code + CODE_SEPARATOR
          + String.format(String.format("%%0%dd", ID_OFFSET), (rs.getInt(1)+1));
    else
      result = code + CODE_SEPARATOR
          + String.format(String.format("%%0%dd", ID_OFFSET), 1);
    rs.close();
    return result;
  }

  public static String[] getTablesAndWheres(List<TreeLevel> path) {
    String[] result = new String[2];

    List<TreeLevel> adjustedPath = adjustPath(path);
    result[0] = "";
    result[1] = "";
    for (int i = 0; i < adjustedPath.size(); i++) {
      TreeLevel level = adjustedPath.get(i);
      boolean isDerived = level.getTa().getDef() != null &&
          (level.getTa().getFlags() & (1<<TemplateAttribute.FLAG_DERIVED)) > 0;

      if (level.getValueString() == null && level.getValueDouble() == null
          && level.getValueDate() == null && level.getValueRef() == 0) {
        result[0] = " left outer join " + AValue.TABLE + " av" + i
            + " on (av" + i + "." + AValue.AO_ID + "=" + AObject.P_AO_ID
            + " AND av" + i + "." + AValue.OA_ID + "=" + level.getTa().getOaId() + ") "
            + result[0];
        result[1] = " AND av" + i + "." + AValue.ID + " is null " + result[1];
      } else {
        String derived;
        result[1] = result[1]
            + " AND av" + i + "." + AValue.AO_ID + "=" + AObject.P_AO_ID
            + " AND av" + i + "." + AValue.OA_ID + "="  + level.getTa().getOaId();
        switch (level.getTa().getOa().getVt().getType()) {
          case ValueType.VT_INT:
          case ValueType.VT_REAL:
            result[0] = result[0] + ", " + AValue.TABLE + " av" + i + ", " + AValue.TABLE_DATE + " avn" + i;
            result[1] = result[1]
                + " AND avn" + i + "." + AValue.AVN_AV_ID + "= av" + i + "." + AValue.ID
                + " AND avn" + i + "." + AValue.VALUE_NUMBER + "=" + level.getValueDouble();
            break;
          case ValueType.VT_STRING:
            result[0] = result[0] + ", " + AValue.TABLE + " av" + i + ", " + AValue.TABLE_CHAR + " avc" + i;

            derived = "avc" + i + "." + AValue.VALUE_STRING;
            if (isDerived) {
              derived = String.format(level.getTa().getDef(), derived);
            }
            result[1] = result[1]
                + " AND avc" + i + "." + AValue.AVC_AV_ID + "= av" + i + "." + AValue.ID
                + " AND " + derived + "='" + ServerUtils.mySQLFilter(level.getValueString()) + "'";
            break;
          case ValueType.VT_DATETIME:
          case ValueType.VT_DATE:
            result[0] = result[0] + ", " + AValue.TABLE + " av" + i + ", " + AValue.TABLE_DATE + " avd" + i;

            derived = "avd" + i + "." + AValue.VALUE_DATE;
            if (isDerived) {
              derived = String.format(level.getTa().getDef(), derived);
            }

            result[1] = result[1]
                + " AND avd" + i + "." + AValue.AVD_AV_ID + "= av" + i + "." + AValue.ID
                + " AND " + derived + "='" + (isDerived ? level.getValueString() : level.getValueDate()) + "'";
            break;
          case ValueType.VT_REF:
            result[0] = result[0] + ", " + AValue.TABLE + " av" + i + ", " + AValue.TABLE_REF + " avr" + i;
            result[1] = result[1]
                + " AND avr" + i + "." + AValue.AVR_AV_ID + "= av" + i + "." + AValue.ID
                + " AND avr" + i + "." + AValue.VALUE_REF + "=" + level.getValueRef();
            break;
        }
      }
    }
    return result;
  }

  public static String[] getTablesAndWheresForFilter(List<AValue> filter) {
    String[] result = new String[2];

    result[0] = "";
    result[1] = "";
    if (filter != null) {
      for (int i = FILTER_OFFSET; i < filter.size()+FILTER_OFFSET; i++) {
        AValue value = filter.get(i-FILTER_OFFSET);
        result[0] = result[0] + ", " + AValue.TABLE + " av" + i;
        result[1] = result[1] +  " AND av" + i + "." + AValue.AO_ID + "=" + AObject.P_AO_ID
            + " AND av" + i + "." + AValue.OA_ID + "=" + value.getOaId();
        if (value.getValueRef() > 0) {
          result[0] = result[0] + ", " + AValue.TABLE_REF + " avr" + i;
          result[1] = result[1] + " AND avr" + i + "." + AValue.AVR_AV_ID + "= av" + i + "." + AValue.ID
            + " AND avr" + i + "." + AValue.VALUE_REF + "=" + value.getValueRef();
        }
        else if (value.getValueString() != null) {
          result[0] = result[0] + ", " + AValue.TABLE_CHAR + " avc" + i;
          result[1] = result[1] + " AND avc" + i + "." + AValue.AVC_AV_ID + "= av" + i + "." + AValue.ID
              + " AND avc" + i + "." + AValue.VALUE_STRING + "='" + ServerUtils.mySQLFilter(value.getValueString()) + "'";
        }
        else if (value.getValueDate() != null) {
          result[0] = result[0] + ", " + AValue.TABLE_DATE + " avd" + i;
          result[1] = result[1] + " AND avd" + i + "." + AValue.AVD_AV_ID + "= av" + i + "." + AValue.ID
            + " AND avd" + i + "." + AValue.VALUE_DATE + "='" + value.getValueDate() + "'";
        }
        else if (value.getValueDouble() != null) {
          result[0] = result[0] + ", " + AValue.TABLE_NUMBER + " avn" + i;
          result[1] = result[1] + " AND avn" + i + "." + AValue.AVN_AV_ID + "= av" + i + "." + AValue.ID
            + " AND avn" + i + "." + AValue.VALUE_NUMBER + "=" + value.getValueDouble();
        }
        else {
          result[0] = result[0] + " left outer join " + AValue.TABLE + " av" + i
              + " on (av" + i + "." + AValue.AO_ID + "=" + AObject.P_AO_ID
              + " AND av" + i + "." + AValue.OA_ID + "=" + value.getOaId() + ") ";
          result[1] = result[1] + " AND av" + i + "." + AValue.ID + " is null";
        }
      }
    }
    return result;
  }

  private static List<TreeLevel> adjustPath(List<TreeLevel> path) {
    Map<Integer, TreeLevel> map = new HashMap<Integer, TreeLevel>();
    for (TreeLevel tl : path) {
      map.put(tl.getId(), tl);
      if (tl.getSubsetOf() > 0) {
        map.remove(tl.getSubsetOf());
      }
    }
    return new ArrayList(map.values());
  }

  private static Map<AObject, List<AValue>> getRows(ResultSet rs, int langId) throws SQLException {
    Map<AObject, List<AValue>> result = new LinkedHashMap<AObject, List<AValue>>();

    List<AValue> values = null;
    AObject aobject = null;
    AValue valuePrev = null;
    boolean isDescendingPrev = false;
    while (rs.next()) {
      if (aobject == null || rs.getInt(AObject.ID) != aobject.getId()) {
        if (values != null) {
          if (isDescendingPrev)
            values.add(valuePrev);
          result.put(aobject, values);
        }
        values = new ArrayList<AValue>();
        aobject = new AObject(rs, false, false);
        isDescendingPrev = false;
        valuePrev = null;
      }
      AValue value = new AValue(rs);
      boolean isDescending = (rs.getInt("ta_flags") & (1<<TemplateAttribute.FLAG_DESCENDANT)) > 0;
      if (value.getId() > 0) {
        if (rs.getString("ao_leaf") != null) {
          value.setValueString(rs.getString("ao_leaf"));
          value.setLangId(rs.getInt(AValue.AVC_LANG_ID));
        }

        if (valuePrev != null
            && valuePrev.getOaId() == value.getOaId()
            && valuePrev.getRank() == value.getRank()
            && value.getLangId() != langId)
          value = valuePrev;

        if (isDescendingPrev) {
          if (valuePrev != null && valuePrev.getOaId() != value.getOaId())
            values.add(valuePrev);
        } else if (!isDescending) {
          if (valuePrev == null || valuePrev.getOaId() != value.getOaId())
            values.add(value);
          else if (valuePrev.getOaId() == value.getOaId()
              && valuePrev.getRank() == value.getRank()
              && value.getLangId() == langId) {
            values.remove(values.size()-1);
            values.add(value);
          }
        }
      } else
        values.add(value);
      valuePrev = value;
      isDescendingPrev = isDescending;
    }
    if (isDescendingPrev)
      values.add(valuePrev);
    result.put(aobject, values);

    return result;
  }
}
