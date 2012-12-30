package sk.benko.appsresource.server;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.benko.appsresource.client.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The server-side RPC endpoint for {@link Service}.
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

  private static ArrayList<TemplateAttribute> toClientTemplateAttributes(
      ArrayList<StoreDB.TemplateAttribute> templateAttributes) {
    final ArrayList<TemplateAttribute> clients = new ArrayList<TemplateAttribute>();
    for (StoreDB.TemplateAttribute ta : templateAttributes) {
      clients.add(Utils.toClientTemplateAttribute(ta));
    }
    return clients;
  }

  private static ArrayList<TemplateRelation> toClientTemplateRelations(
      ArrayList<StoreDB.TemplateRelation> templateRelations) {
    final ArrayList<TemplateRelation> clients = new ArrayList<TemplateRelation>();
    for (StoreDB.TemplateRelation tr : templateRelations) {
      clients.add(Utils.toClientTemplateRelation(tr));
    }
    return clients;
  }

  @Override
  public ArrayList<ApplicationTemplate> getApplicationTemplates(Application app) {
    ArrayList<ApplicationTemplate> result = new ArrayList<ApplicationTemplate>();
    final StoreDB.Api api = store.getApi();
    try {
      final ArrayList<ApplicationTemplate> fromCache = cacheApp.getApplicationTemplates(app.getId());
      if (fromCache != null) {
        return fromCache;
      }

      return cacheApp.putApplicationTemplates(app.getId(),
          toClientApplicationTemplates(api.getAppTemplatesByApp(app.getId())));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public List<TemplateTree> getTemplateTrees(int tId) {
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateTree> fromCache = cache.getTemplateTrees(tId);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateTrees(tId, toClientTemplateTrees(api.getTemplateTrees(tId)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return new ArrayList<TemplateTree>();
  }

  @Override
  public List<TemplateTreeItem> getTemplateTreeItems(TemplateTree tt) {
    ArrayList<TemplateTreeItem> result = new ArrayList<TemplateTreeItem>();
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateTreeItem> fromCache = cache.getTemplateTreeItems(tt);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateTreeItems(tt,
          toClientTemplateTreeItems(api.getTemplateTreeItems(tt.getId())));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public List<TemplateList> getTemplateLists(int tId) {
    List<TemplateList> result = new ArrayList<TemplateList>();
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateList> fromCache = cache.getTemplateLists(tId);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateLists(tId, toClientTemplateLists(api.getTemplateLists(tId)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public List<TemplateListItem> getTemplateListItems(TemplateList tl) {
    ArrayList<TemplateListItem> result = new ArrayList<TemplateListItem>();
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateListItem> fromCache = cache.getTemplateListItems(tl);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateListItems(tl, toClientTemplateListItems(api.getTemplateListItems(tl.getId())));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public List<TemplateAttribute> getTemplateAttributes(Template t) {
    ArrayList<TemplateAttribute> result = new ArrayList<TemplateAttribute>();
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateAttribute> fromCache = cache.getTemplateAttributes(t);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateAttributes(
          t, toClientTemplateAttributes(api.getTemplateAttributes(t.getId())));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public List<TemplateRelation> getTemplateRelations(Template t) {
    ArrayList<TemplateRelation> result = new ArrayList<TemplateRelation>();
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateRelation> fromCache = cache.getTemplateRelations(t);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateRelations(t,
          toClientTemplateRelations(api.getTemplateRelations(t.getId())));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }
}
