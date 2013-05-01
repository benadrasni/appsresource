package sk.benko.appsresource.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateList;
import sk.benko.appsresource.client.model.TemplateListItem;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.TemplateTree;
import sk.benko.appsresource.client.model.TemplateTreeItem;

import com.google.appengine.api.memcache.MemcacheService;

/**
 * A more typesafe wrapper around a {@link MemcacheService}.
 *
 */
public class CacheApplication {

  /**
   * The memcache service to use for caching.
   */
  private static final String APPLICATION_TEMPLATE_PREFIX = "APPLICATIONTEMPLATE/";
  private static final String TEMPLATE_GROUP_PREFIX = "TEMPLATEGROUP/";
  private static final String TEMPLATE_RELATION_PREFIX = "TEMPLATERELATION/";
  private static final String TEMPLATE_TREE_PREFIX = "TEMPLATETREE/";
  private static final String TEMPLATE_LIST_PREFIX = "TEMPLATELIST/";
  private static final String TREE_ITEM_PREFIX = "TREEITEM/";
  private static final String LIST_ITEM_PREFIX = "LISTITEM/";
  private static final String TEMPLATE_PREFIX = "TEMPLATE/";
  private static final String OBJECT_PREFIX = "OBJECT/";

  private static String createApplicationTemplateId(int appId) {
    return APPLICATION_TEMPLATE_PREFIX + appId;
  }

  private static String createTemplateTreeId(int tId) {
    return TEMPLATE_TREE_PREFIX + tId;
  }

  private static String createTemplateListId(int tId) {
    return TEMPLATE_LIST_PREFIX + tId;
  }

  private static String createTemplateRelationId(int tId) {
    return TEMPLATE_RELATION_PREFIX + tId;
  }

  private static String createTemplateGroupId(int tId) {
    return TEMPLATE_GROUP_PREFIX + tId;
  }

  private static String createTreeItemId(int ttId) {
    return TREE_ITEM_PREFIX + ttId;
  }

  private static String createListItemId(int tlId) {
    return LIST_ITEM_PREFIX + tlId;
  }

  private static String createTemplateId(int tId) {
    return TEMPLATE_PREFIX + tId;
  }

  private static String createObjectId(int oId) {
    return OBJECT_PREFIX + oId;
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
  public CacheApplication(MemcacheService memcache) {
    this.memcache = memcache;
  }
  
  /**
   * Attempts to fetch the collection of templates for given application.
   *
   * @return a collection of application's templates if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<ApplicationTemplate> getApplicationTemplates(int appId) {
    try {
      return (ArrayList<ApplicationTemplate>) memcache.get(createApplicationTemplateId(appId));
    } catch (Exception e) {
      deleteApplicationTemplates(appId);
      return null;
    }  
  }

  /**
   * Adds application's templates to the cache.
   *
   * @param appTemplates
   *          application's templates
   * @return <code>templates</code>, for call chaining
   */
  public ArrayList<ApplicationTemplate> putApplicationTemplates(int appId, 
      ArrayList<ApplicationTemplate> appTemplates) {
    memcache.put(createApplicationTemplateId(appId), appTemplates);
    return appTemplates;
  }
  
  /**
   * Deletes the collection of application's templates that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteApplicationTemplates(int appId) {
    memcache.delete(createApplicationTemplateId(appId));
  }

  /**
   * Attempts to fetch the collection of template relations.
   *
   * @return a collection of template groups if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateRelation> getTemplateRelations(Template t) {
    try {
      return (ArrayList<TemplateRelation>) memcache.get(createTemplateRelationId(t.getId()));
    } catch (Exception e) {
      deleteTemplateRelations(t.getId());
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
  public ArrayList<TemplateRelation> putTemplateRelations(Template t,
      ArrayList<TemplateRelation> templateRelations) {
    memcache.put(createTemplateRelationId(t.getId()), templateRelations);
    return templateRelations;
  }
  
  /**
   * Deletes the collection of template relations that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplateRelations(int tId) {
    memcache.delete(createTemplateRelationId(tId));
  }

  /**
   * Attempts to fetch the collection of template groups.
   *
   * @return a collection of template groups if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateGroup> getTemplateGroups(Template t) {
    try {
      return (ArrayList<TemplateGroup>) memcache.get(createTemplateGroupId(t.getId()));
    } catch (Exception e) {
      deleteTemplateGroups(t.getId());
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
  public ArrayList<TemplateGroup> putTemplateGroups(Template t,
      ArrayList<TemplateGroup> templateGroups) {
    memcache.put(createTemplateGroupId(t.getId()), templateGroups);
    return templateGroups;
  }
  
  /**
   * Deletes the collection of template groups that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplateGroups(int tId) {
    memcache.delete(createTemplateGroupId(tId));
  }
  
  /**
   * Attempts to fetch the collection of template filterAttributes.
   *
   * @return a collection of template filterAttributes if there is a cache entry,
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateAttribute> getTemplateAttributes(Template t) {
    try {
      return (ArrayList<TemplateAttribute>) memcache.get(createTemplateId(t.getId()));
    } catch (Exception e) {
      deleteTemplateAttributes(t);
      return null;
    }  
  }

  
  /**
   * Adds template filterAttributes to the cache.
   *
   * @param templateAttributes
   *          template filterAttributes
   * @return <code>templateGroups</code>, for call chaining
   */
  public ArrayList<TemplateAttribute> putTemplateAttributes(Template t,
      ArrayList<TemplateAttribute> templateAttributes) {
    memcache.put(createTemplateId(t.getId()), templateAttributes);
    return templateAttributes;
  }
  
  /**
   * Deletes the collection of template filterAttributes that are cached. This is used
   * to invalidate cache entries.
   *
   */
  public void deleteTemplateAttributes(Template t) {
    memcache.delete(createTemplateId(t.getId()));
  }
  
  
  /**
   * Attempts to fetch the collection of template trees for given template.
   *
   * @return a collection of template's trees if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateTree> getTemplateTrees(int tId) {
    try {
      return (ArrayList<TemplateTree>) memcache.get(createTemplateTreeId(tId));
    } catch (Exception e) {
      deleteTemplateTrees(tId);
      return null;
    }  
  }

  /**
   * Adds template's trees to the cache.
   *
   * @param templateTrees
   *          template's trees
   * @return <code>templateTrees</code>, for call chaining
   */
  public ArrayList<TemplateTree> putTemplateTrees(int tId, 
      ArrayList<TemplateTree> templateTrees) {
    memcache.put(createTemplateTreeId(tId), templateTrees);
    return templateTrees;
  }
  
  /**
   * Deletes the collection of template's trees that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteTemplateTrees(int tId) {
    memcache.delete(createTemplateTreeId(tId));
  }

  /**
   * Attempts to fetch the collection of template tree items for given template tree.
   *
   * @return a collection of template tree's items if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateTreeItem> getTemplateTreeItems(TemplateTree tt) {
    try {
      return (ArrayList<TemplateTreeItem>) memcache.get(createTreeItemId(tt.getId()));
    } catch (Exception e) {
      deleteTemplateTreeItems(tt);
      return null;
    }  
  }

  /**
   * Adds template tree's items to the cache.
   *
   * @param tt      template's trees
   * @return        <code>templateTrees</code>, for call chaining
   */
  public ArrayList<TemplateTreeItem> putTemplateTreeItems(TemplateTree tt, 
      ArrayList<TemplateTreeItem> templateTreeItems) {
    memcache.put(createTreeItemId(tt.getId()), templateTreeItems);
    return templateTreeItems;
  }
  
  /**
   * Deletes the collection of template tree's items that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteTemplateTreeItems(TemplateTree tt) {
    memcache.delete(createTreeItemId(tt.getId()));
  }
  
  /**
   * Attempts to fetch the collection of template lists for given template.
   *
   * @return a collection of template's lists if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateList> getTemplateLists(int tId) {
    try {
      return (ArrayList<TemplateList>) memcache.get(createTemplateListId(tId));
    } catch (Exception e) {
      deleteTemplateLists(tId);
      return null;
    }  
  }

  /**
   * Adds template's lists to the cache.
   *
   * @param templateLists
   *          template's lists
   * @return <code>templateLists</code>, for call chaining
   */
  public ArrayList<TemplateList> putTemplateLists(int tId, 
      ArrayList<TemplateList> templateLists) {
    memcache.put(createTemplateListId(tId), templateLists);
    return templateLists;
  }
  
  /**
   * Deletes the collection of template's lists that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteTemplateLists(int tId) {
    memcache.delete(createTemplateListId(tId));
  }

  /**
   * Attempts to fetch the collection of template list items for given template tree.
   *
   * @return a collection of template list's items if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TemplateListItem> getTemplateListItems(TemplateList tl) {
    try {
      return (ArrayList<TemplateListItem>) memcache.get(createListItemId(tl.getId()));
    } catch (Exception e) {
      deleteTemplateListItems(tl);
      return null;
    }  
  }

  /**
   * Adds template list's items to the cache.
   *
   * @param tl      template's lists
   * @return        <code>templateLists</code>, for call chaining
   */
  public ArrayList<TemplateListItem> putTemplateListItems(TemplateList tl, 
      ArrayList<TemplateListItem> templateListItems) {
    memcache.put(createListItemId(tl.getId()), templateListItems);
    return templateListItems;
  }
  
  /**
   * Deletes the collection of template list's items that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteTemplateListItems(TemplateList tl) {
    memcache.delete(createListItemId(tl.getId()));
  }

  /**
   * Attempts to fetch the collection of values for given object.
   *
   * @return a collection of object's values if there is a cache entry, 
   *        <code>null</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public Map<Integer, List<AValue>> getValues(int oId) {
    try {
      return (Map<Integer, List<AValue>>) memcache.get(createObjectId(oId));
    } catch (Exception e) {
      deleteValues(oId);
      return null;
    }  
  }

  /**
   * Adds object's values to the cache.
   *
   * @param oId     object's id
   * @param values  object's values
   * @return <code>templateLists</code>, for call chaining
   */
  public Map<Integer, List<AValue>> putValues(int oId, Map<Integer, List<AValue>> values) {
    memcache.put(createObjectId(oId), values);
    return values;
  }
  
  /**
   * Deletes the collection of object's values that are cached. 
   * This is used to invalidate cache entries.
   *
   */
  public void deleteValues(int oId) {
    memcache.delete(createObjectId(oId));
  }
  
}
