package sk.benko.appsresource.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.Service;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateList;
import sk.benko.appsresource.client.model.TemplateListItem;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.TemplateTree;
import sk.benko.appsresource.client.model.TemplateTreeItem;
import sk.benko.appsresource.client.model.result.GetApplicationTemplatesResult;
import sk.benko.appsresource.client.model.result.GetTemplateAttributesResult;
import sk.benko.appsresource.client.model.result.GetTemplateListItemsResult;
import sk.benko.appsresource.client.model.result.GetTemplateListsResult;
import sk.benko.appsresource.client.model.result.GetTemplateRelationsResult;
import sk.benko.appsresource.client.model.result.GetTemplateTreeItemsResult;
import sk.benko.appsresource.client.model.result.GetTemplateTreesResult;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side RPC endpoint for {@link Service}.
 *
 *
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {
  private static final Logger log = Logger.getLogger(ServiceImpl.class.getName());

  /**
   * A reference to a cache service.
   */
  private final CacheApplication cache = new CacheApplication(MemcacheServiceFactory
      .getMemcacheService());
  private final CacheApplication cacheApp = new CacheApplication(
      MemcacheServiceFactory.getMemcacheService());

  /**
   * A reference to the data store.
   */
  private final StoreDB store = new StoreDB();
  

  /*
   * Application Template
   */
  
  private static ArrayList<ApplicationTemplate> toClientApplicationTemplates(
      ArrayList<StoreDB.ApplicationTemplate> applicationTemplates) {
    final ArrayList<ApplicationTemplate> clients = new ArrayList<ApplicationTemplate>();
    for (StoreDB.ApplicationTemplate n : applicationTemplates) {
      clients.add(new ApplicationTemplate(n.getAppId(), Utils.toClientApplication(n.getApp()), n.getTId(), 
          Utils.toClientTemplate(n.getT()), n.getFlags(), n.getParentMenuId(), n.getRank(), 
          n.getUserId(), n.getLastUpdatedAt()));
    }
    return clients;
  }

  public GetApplicationTemplatesResult getApplicationTemplates(Application app)
      throws AccessDeniedException {
    return new GetApplicationTemplatesResult(getATs(app));
  }
  

  private ArrayList<ApplicationTemplate> getATs(Application app) {
    ArrayList<ApplicationTemplate> result = new ArrayList<ApplicationTemplate>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<ApplicationTemplate> fromCache = cacheApp.getApplicationTemplates(app.getId());
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<ApplicationTemplate> applicationTemplates = 
          cacheApp.putApplicationTemplates(app.getId(), 
              toClientApplicationTemplates(api.getAppTemplatesByApp(app.getId())));
      return applicationTemplates;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return result;  
  }  

  /*
   * Template Tree
   */
  
  private static ArrayList<TemplateTree> toClientTemplateTrees(
      ArrayList<StoreDB.TemplateTree> templateTrees) {
    final ArrayList<TemplateTree> clients = new ArrayList<TemplateTree>();
    for (StoreDB.TemplateTree tt : templateTrees) {
      clients.add(new TemplateTree(tt.getId(), tt.getCode(), tt.getName(), 
          tt.getDesc(), tt.getTId(), tt.getFlags(), tt.getRank(), tt.getUserId(), 
          tt.getLastUpdatedAt()));
    }
    return clients;
  }

  public GetTemplateTreesResult getTemplateTrees(int tId)
      throws AccessDeniedException {
    return new GetTemplateTreesResult(getTts(tId));
  }
  
  private ArrayList<TemplateTree> getTts(int tId) {
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<TemplateTree> fromCache = cache.getTemplateTrees(tId);
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<TemplateTree> templateTrees = cache.putTemplateTrees(tId, 
          toClientTemplateTrees(api.getTemplateTrees(tId)));
      return templateTrees;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<TemplateTree>();  
  }  
  
  /*
   * Template Tree Item
   */
  
  private static ArrayList<TemplateTreeItem> toClientTemplateTreeItems(
      ArrayList<StoreDB.TemplateTreeItem> templateTreeItems) {
    final ArrayList<TemplateTreeItem> clients = new ArrayList<TemplateTreeItem>();
    for (StoreDB.TemplateTreeItem tt : templateTreeItems) {
      clients.add(new TemplateTreeItem(tt.getId(), tt.getTtId(), 
          Utils.toClientTemplateTree(tt.getTt()), tt.getTaId(), 
          Utils.toClientTemplateAttribute(tt.getTa()), tt.getRank()));
    }
    return clients;
  }

  public GetTemplateTreeItemsResult getTemplateTreeItems(TemplateTree tt)
      throws AccessDeniedException {
    return new GetTemplateTreeItemsResult(getTtis(tt));
  }
  
  private ArrayList<TemplateTreeItem> getTtis(TemplateTree tt) {
    ArrayList<TemplateTreeItem> result = new ArrayList<TemplateTreeItem>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<TemplateTreeItem> fromCache = cache.getTemplateTreeItems(tt);
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<TemplateTreeItem> templateTreeItems = cache.putTemplateTreeItems(tt, 
          toClientTemplateTreeItems(api.getTemplateTreeItems(tt.getId())));
      return templateTreeItems;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return result;  
  }  

  /*
   * Template List
   */
  
  private static ArrayList<TemplateList> toClientTemplateLists(
      ArrayList<StoreDB.TemplateList> templateTrees) {
    final ArrayList<TemplateList> clients = new ArrayList<TemplateList>();
    for (StoreDB.TemplateList tl : templateTrees) {
      clients.add(new TemplateList(tl.getId(), tl.getCode(), tl.getName(), 
          tl.getDesc(), tl.getTId(), tl.getFlags(), tl.getRank(), tl.getUserId(), 
          tl.getLastUpdatedAt()));
    }
    return clients;
  }

  public GetTemplateListsResult getTemplateLists(int tId)
      throws AccessDeniedException {
    return new GetTemplateListsResult(getTls(tId));
  }
  
  private ArrayList<TemplateList> getTls(int tId) {
    ArrayList<TemplateList> result = new ArrayList<TemplateList>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<TemplateList> fromCache = cache.getTemplateLists(tId);
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<TemplateList> templateTrees = cache.putTemplateLists(tId, 
          toClientTemplateLists(api.getTemplateLists(tId)));
      return templateTrees;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return result;  
  }  
  
  /*
   * Template List Item
   */
  
  private static ArrayList<TemplateListItem> toClientTemplateListItems(
      ArrayList<StoreDB.TemplateListItem> templateTreeItems) {
    final ArrayList<TemplateListItem> clients = new ArrayList<TemplateListItem>();
    for (StoreDB.TemplateListItem tli : templateTreeItems) {
      clients.add(new TemplateListItem(tli.getId(), tli.getTlId(), 
          Utils.toClientTemplateList(tli.getTl()), tli.getTaId(), 
          Utils.toClientTemplateAttribute(tli.getTa()), tli.getRank()));
    }
    return clients;
  }

  public GetTemplateListItemsResult getTemplateListItems(TemplateList tl)
      throws AccessDeniedException {
    return new GetTemplateListItemsResult(getTlis(tl));
  }
  
  private ArrayList<TemplateListItem> getTlis(TemplateList tl) {
    ArrayList<TemplateListItem> result = new ArrayList<TemplateListItem>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<TemplateListItem> fromCache = cache.getTemplateListItems(tl);
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<TemplateListItem> templateListItems = cache.putTemplateListItems(tl, 
          toClientTemplateListItems(api.getTemplateListItems(tl.getId())));
      return templateListItems;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return result;  
  }  

  /*
   * Template Attribute
   */
  
  @Override
  public GetTemplateAttributesResult getTemplateAttributes(Template t)
      throws AccessDeniedException {
    return new GetTemplateAttributesResult(getTAs(t));
  }  
  
  private static ArrayList<TemplateAttribute> toClientTemplateAttributes(
      ArrayList<StoreDB.TemplateAttribute> templateAttributes) {
    final ArrayList<TemplateAttribute> clients = new ArrayList<TemplateAttribute>();
    for (StoreDB.TemplateAttribute ta : templateAttributes) {
      clients.add(Utils.toClientTemplateAttribute(ta));
    }
    return clients;
  }

  private ArrayList<TemplateAttribute> getTAs(Template t) {
    ArrayList<TemplateAttribute> result = new ArrayList<TemplateAttribute>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<TemplateAttribute> fromCache = cache.getTemplateAttributes(t);
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<TemplateAttribute> templateAttributes = cache.putTemplateAttributes(
          t, toClientTemplateAttributes(api.getTemplateAttributes(t.getId())));
      return templateAttributes;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return result;  
  }

  /*
   * Template Relation
   */

  @Override
  public GetTemplateRelationsResult getTemplateRelations(Template t)
      throws AccessDeniedException {
    return new GetTemplateRelationsResult(getTrs(t));
  }

  private static ArrayList<TemplateRelation> toClientTemplateRelations(
      ArrayList<StoreDB.TemplateRelation> templateRelations) {
    final ArrayList<TemplateRelation> clients = new ArrayList<TemplateRelation>();
    for (StoreDB.TemplateRelation tr : templateRelations) {
      clients.add(Utils.toClientTemplateRelation(tr));
    }
    return clients;
  }

  private ArrayList<TemplateRelation> getTrs(Template t) {
    ArrayList<TemplateRelation> result = new ArrayList<TemplateRelation>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<TemplateRelation> fromCache = cache.getTemplateRelations(t);
      if (fromCache != null) {
        return fromCache;
      }

      final ArrayList<TemplateRelation> templateRelations = cache.putTemplateRelations(t, 
          toClientTemplateRelations(api.getTemplateRelations(t.getId())));
      return templateRelations;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return result;  
  } 
  

  
}
