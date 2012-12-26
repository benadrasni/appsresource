package sk.benko.appsresource.server;

import java.util.ArrayList;

import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.Unit;
import sk.benko.appsresource.client.model.ValueType;

import com.google.appengine.api.memcache.MemcacheService;

/**
 * A more typesafe wrapper around a {@link MemcacheService}.
 *
 */
public class Cache {

  /**
   * The memcache service to use for caching.
   */
  private static final String OBJECTTYPES_PREFIX = "OBJECTTYPES/";
  private static final String OBJECTATTRIBUTES_PREFIX = "OBJECTATTRIBUTES/";
  private static final String OBJECTRELATIONS_PREFIX = "OBJECTRELATIONS/";
  private static final String VALUETYPES_PREFIX = "VALUETYPES/";
  private static final String UNITS_PREFIX = "UNITS/";
  private static final String TEMPLATES_PREFIX = "TEMPLATES/";
  private static final String TEMPLATEGROUPS_PREFIX = "TEMPLATEGROUPS/";
  private static final String TEMPLATERELATIONS_PREFIX = "TEMPLATERELATIONS/";
  private static final String TEMPLATEATTRIBUTES_PREFIX = "TEMPLATEATTRIBUTES/";
  private static final String APPLICATIONS_PREFIX = "APPLICATIONS/";
  private static final String USERKEYS_PREFIX = "USERS/";
  private static final String OBJECTTYPESKEYS_PREFIX = "OBJECTTYPES/";

  private static String createApplicationsId(int userId) {
    return USERKEYS_PREFIX + userId;
  }

  private static String createTemplateGroupsId(int tId) {
    return TEMPLATEGROUPS_PREFIX + tId;
  }

  private static String createObjectAttributesId(int otId) {
    return OBJECTATTRIBUTES_PREFIX + otId;
  }

  private static String createObjectRelationsId(int otId) {
    return OBJECTRELATIONS_PREFIX + otId;
  }

  private static String createObjectsId(int otId) {
    return OBJECTTYPESKEYS_PREFIX + otId;
  }

  /**
   * The memcache service to use for caching.
   */
  private final MemcacheService memcache;

  /**
   * Creates a new cache.
   *
   * @param memcache
   *          the memcache service to use for caching objects
   */
  public Cache(MemcacheService memcache) {
    this.memcache = memcache;
  }

  /**
   * Attempts to fetch the collection of object types.
   *
   * @return a collection of object types if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<ObjectType> getObjectTypes() {
    try {
      return (ArrayList<ObjectType>) memcache.get(OBJECTTYPES_PREFIX);
    } catch (Exception e) {
      deleteObjectTypes();
      return null;
    }  
  }

  
  /**
   * Adds object types to the cache.
   *
   * @param objectTypes
   *          object types
   * @return <code>objectTypes</code>, for call chaining
   */
  public ArrayList<ObjectType> putObjectTypes(ArrayList<ObjectType> objectTypes) {
    memcache.put(OBJECTTYPES_PREFIX, objectTypes);
    return objectTypes;
  }
  
  /**
   * Deletes the collection of object types that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteObjectTypes() {
    memcache.delete(OBJECTTYPES_PREFIX);
  }

  /**
   * Attempts to fetch the collection of object attributes.
   *
   * @return a collection of object attributes if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<ObjectAttribute> getObjectAttributes(int otId) {
    try {
      return (ArrayList<ObjectAttribute>) memcache.get(createObjectAttributesId(otId));
    } catch (Exception e) {
      deleteObjectAttributes(otId);
      return null;
    }  
  }

  
  /**
   * Adds object types to the cache.
   *
   * @param objectTypes
   *          object types
   * @return <code>objectTypes</code>, for call chaining
   */
  public ArrayList<ObjectAttribute> putObjectAttributes(int otId, 
      ArrayList<ObjectAttribute> objectAttributes) {
    memcache.put(createObjectAttributesId(otId), objectAttributes);
    return objectAttributes;
  }
  
  /**
   * Deletes the collection of object types that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteObjectAttributes(int otId) {
    memcache.delete(createObjectAttributesId(otId));
  }

  /**
   * Attempts to fetch the collection of object relations.
   *
   * @param otId
   * 
   * @return a collection of object relations if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<ObjectRelation> getObjectRelations(int otId) {
    try {
      return (ArrayList<ObjectRelation>) memcache.get(createObjectRelationsId(otId));
    } catch (Exception e) {
      deleteObjectRelations(otId);
      return null;
    }  
  }

  
  /**
   * Adds object types to the cache.
   *
   * @param otId
   * @param objectRelations
   *          object relations
   *          
   * @return <code>objectRelations</code>, for call chaining
   */
  public ArrayList<ObjectRelation> putObjectRelations(int otId, ArrayList<ObjectRelation> objectRelation) {
    memcache.put(createObjectRelationsId(otId), objectRelation);
    return objectRelation;
  }
  
  /**
   * Deletes the collection of object relations that are cached. This is used
   * to invalidate cache entries.
   *
   * @param otId
   */
  public void deleteObjectRelations(int otId) {
    memcache.delete(createObjectRelationsId(otId));
  }

  /**
   * Attempts to fetch the collection of value types.
   *
   * @return a collection of value types if there is a cache entry, 
   *          <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<ValueType> getValueTypes() {
    try {
      return (ArrayList<ValueType>) memcache.get(VALUETYPES_PREFIX);
    } catch (Exception e) {
      deleteValueTypes();
      return null;
    }  
  }

  /**
   * Adds value types to the cache.
   *
   * @param valueTypes
   *          value types
   * @return <code>valueTypes</code>, for call chaining
   */
  public ArrayList<ValueType> putValueTypes(ArrayList<ValueType> valueTypes) {
    memcache.put(VALUETYPES_PREFIX, valueTypes);
    return valueTypes;
  }
  
  /**
   * Deletes the collection of value types that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteValueTypes() {
    memcache.delete(VALUETYPES_PREFIX);
  }
  
  /**
   * Attempts to fetch the collection of units.
   *
   * @return a collection of units if there is a cache entry, 
   *          <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<Unit> getUnits() {
    try {
      return (ArrayList<Unit>) memcache.get(UNITS_PREFIX);
    } catch (Exception e) {
      deleteUnits();
      return null;
    }  
  }

  /**
   * Adds units to the cache.
   *
   * @param units
   * @return <code>units</code>, for call chaining
   */
  public ArrayList<Unit> putUnits(ArrayList<Unit> units) {
    memcache.put(UNITS_PREFIX, units);
    return units;
  }

  /**
   * Deletes the collection of units that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteUnits() {
    memcache.delete(UNITS_PREFIX);
  }
  
  /**
   * Attempts to fetch the collection of templates.
   *
   * @return a collection of templates if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<Template> getTemplates() {
    try {
      return (ArrayList<Template>) memcache.get(TEMPLATES_PREFIX);
    } catch (Exception e) {
      deleteTemplates();
      return null;
    }  
  }

  /**
   * Adds object types to the cache.
   *
   * @param templates
   *          templates
   * @return <code>templates</code>, for call chaining
   */
  public ArrayList<Template> putTemplates(ArrayList<Template> templates) {
    memcache.put(TEMPLATES_PREFIX, templates);
    return templates;
  }
  
  /**
   * Deletes the collection of templates that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplates() {
    memcache.delete(TEMPLATES_PREFIX);
  }

  /**
   * Attempts to fetch the collection of template relations.
   *
   * @return a collection of template relations if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateRelation> getTemplateRelations() {
    try {
      return (ArrayList<TemplateRelation>) memcache.get(TEMPLATERELATIONS_PREFIX);
    } catch (Exception e) {
      deleteTemplateRelations();
      return null;
    }  
  }

  
  /**
   * Adds template relations to the cache.
   *
   * @param templateRelations
   *          template relations
   * @return <code>templateRelations</code>, for call chaining
   */
  public ArrayList<TemplateRelation> putTemplateRelations(
      ArrayList<TemplateRelation> templateRelations) {
    memcache.put(TEMPLATERELATIONS_PREFIX, templateRelations);
    return templateRelations;
  }
  
  /**
   * Deletes the collection of template relations that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplateRelations() {
    memcache.delete(TEMPLATERELATIONS_PREFIX);
  }

  /**
   * Attempts to fetch the collection of template groups.
   *
   * @return a collection of template groups if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateGroup> getTemplateGroups(int tId) {
    try {
      return (ArrayList<TemplateGroup>) memcache.get(createTemplateGroupsId(tId));
    } catch (Exception e) {
      deleteTemplateGroups(tId);
      return null;
    }  
  }

  
  /**
   * Adds template groups to the cache.
   *
   * @param templateGroups
   *          template groups
   * @return <code>templateGroups</code>, for call chaining
   */
  public ArrayList<TemplateGroup> putTemplateGroups(int tId,
      ArrayList<TemplateGroup> templateGroups) {
    memcache.put(createTemplateGroupsId(tId), templateGroups);
    return templateGroups;
  }
  
  /**
   * Deletes the collection of template groups that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplateGroups(int tId) {
    memcache.delete(createTemplateGroupsId(tId));
  }
  
  /**
   * Attempts to fetch the collection of template attributes.
   *
   * @return a collection of template attributes if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateAttribute> getTemplateAttributes() {
    try {
      return (ArrayList<TemplateAttribute>) memcache.get(TEMPLATEATTRIBUTES_PREFIX);
    } catch (Exception e) {
      deleteTemplateAttributes();
      return null;
    }  
  }

  
  /**
   * Adds template attributes to the cache.
   *
   * @param templateAttributes
   *          template attributes
   * @return <code>templateGroups</code>, for call chaining
   */
  public ArrayList<TemplateAttribute> putTemplateAttributes(
      ArrayList<TemplateAttribute> templateAttributes) {
    memcache.put(TEMPLATEATTRIBUTES_PREFIX, templateAttributes);
    return templateAttributes;
  }
  
  /**
   * Deletes the collection of template attributes that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplateAttributes() {
    memcache.delete(TEMPLATEATTRIBUTES_PREFIX);
  }
  
  /**
   * Attempts to fetch the collection of applications.
   *
   * @return a collection of applications if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<Application> getApplications() {
    try {
      return (ArrayList<Application>) memcache.get(APPLICATIONS_PREFIX);
    } catch (Exception e) {
      deleteApplications();
      return null;
    }  
  }

  
  /**
   * Adds applications to the cache.
   *
   * @param applications
   *          Applications
   * @return <code>templateGroups</code>, for call chaining
   */
  public ArrayList<Application> putApplications(
      ArrayList<Application> applications) {
    memcache.put(APPLICATIONS_PREFIX, applications);
    return applications;
  }
  
  /**
   * Deletes the collection of applications that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteApplications() {
    memcache.delete(APPLICATIONS_PREFIX);
  }
  
  /**
   * Attempts to fetch the collection of applications for given user.
   *
   * @return a collection of applications if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<ApplicationUser> getApplicationUsers(int userId) {
    try {
      return (ArrayList<ApplicationUser>) memcache.get(createApplicationsId(userId));
    } catch (Exception e) {
      deleteApplicationUsers(userId);
      return null;
    }  
  }

  /**
   * Adds user's applications to the cache.
   *
   * @param userApps
   *          application's users
   * @return <code>userApps</code>, for call chaining
   */
  public ArrayList<ApplicationUser> putApplicationUsers(int userId, 
      ArrayList<ApplicationUser> userApps) {
    memcache.put(createApplicationsId(userId), userApps);
    return userApps;
  }
  
  /**
   * Deletes the collection of user's application that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteApplicationUsers(int userId) {
    memcache.delete(createApplicationsId(userId));
  }
  
  /**
   * Attempts to fetch the collection of objects for given object type.
   *
   * @return a collection of objects if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<AObject> getObjects(int otId) {
    try {
      return (ArrayList<AObject>) memcache.get(createObjectsId(otId));
    } catch (Exception e) {
      deleteObjects(otId);
      return null;
    }  
  }

  /**
   * Adds objects to the cache.
   *
   * @param objects
   *          objects
   * @return <code>objects</code>, for call chaining
   */
  public ArrayList<AObject> putObjects(int otId, 
      ArrayList<AObject> objects) {
    memcache.put(createObjectsId(otId), objects);
    return objects;
  }
  
  /**
   * Deletes the collection of user's application that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteObjects(int otId) {
    memcache.delete(createObjectsId(otId));
  }
}
