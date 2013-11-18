package sk.benko.appsresource.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.benko.appsresource.client.model.AppUser;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.DbService;
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateList;
import sk.benko.appsresource.client.model.TemplateListItem;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.TemplateTree;
import sk.benko.appsresource.client.model.TemplateTreeItem;
import sk.benko.appsresource.client.model.Unit;
import sk.benko.appsresource.client.model.ValueType;
import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectRelationResult;

import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * The server-side RPC endpoint for {@link DbService}.
 *
 *
 */
@SuppressWarnings("serial")
public class DbServiceImpl extends ServiceImpl implements DbService {
  private static final Logger log = Logger.getLogger(DbServiceImpl.class.getName());
  
  /**
   * A reference to the data store.
   */
  private final StoreDB store = new StoreDB();

  /**
   * A reference to a cache service.
   */
  private final Cache cache = new Cache(MemcacheServiceFactory.getMemcacheService());
  private final CacheApplication cacheApp = new CacheApplication(MemcacheServiceFactory.getMemcacheService());

  /*
   * Object Type
   */
  
  private static ArrayList<ObjectType> toClientObjectTypes(
      ArrayList<StoreDB.ObjectType> objectTypes) {
    final ArrayList<ObjectType> clients = new ArrayList<ObjectType>();
    for (StoreDB.ObjectType ot : objectTypes) {
      clients.add(Utils.toClientObjectType(ot));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateObjectTypeResult createObjectType(final ObjectType objectType, 
      final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.ObjectType ot = new StoreDB.ObjectType(objectType.getId(), 
          objectType.getCode(), objectType.getName(), objectType.getDesc(), 
          objectType.getParentId(), 
          Utils.toServerObjectType(objectType.getParent()), author.getId());
      api.saveObjectType(ot);
      final CreateOrUpdateObjectTypeResult result = 
          new CreateOrUpdateObjectTypeResult(ot.getId(), ot.getCode(), 
          ot.getLastUpdatedAt());
      api.commit();
      
      cache.deleteObjectTypes();
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<ObjectType> getObjectTypes() {
    final StoreDB.Api api = store.getApi();
    try {
      final List<ObjectType> fromCache = cache.getObjectTypes();
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putObjectTypes(
          toClientObjectTypes(api.getObjectTypes()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<ObjectType>();
  }

  /*
   * Object Attribute
   */
  
  private static ArrayList<ObjectAttribute> toClientObjectAttributes(
      ArrayList<StoreDB.ObjectAttribute> objectAttributes) {
    final ArrayList<ObjectAttribute> clients = new ArrayList<ObjectAttribute>();
    for (StoreDB.ObjectAttribute oa : objectAttributes) {
      clients.add(Utils.toClientObjectAttribute(oa));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateObjectAttributeResult createObjectAttribute(
      final ObjectAttribute oa, final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.ObjectAttribute objattr = new StoreDB.ObjectAttribute(oa.getId(), 
          oa.getCode(), oa.getName(), oa.getDesc(), oa.getOtId(), null, oa.getVtId(), 
          null, oa.getUnitId(), null, oa.getShared1(), oa.getShared2(), 
          oa.getShared3(), oa.getShared4(), oa.getShared5(), author.getId());
      api.saveObjectAttribute(objattr);
      final CreateOrUpdateObjectAttributeResult result = 
          new CreateOrUpdateObjectAttributeResult(objattr.getId(), 
              objattr.getCode(), objattr.getLastUpdatedAt());
      api.commit();
      
      cache.deleteObjectAttributes(oa.getOtId());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<ObjectAttribute> getObjectAttributes(int otId) {
    final StoreDB.Api api = store.getApi();
    try {
      final List<ObjectAttribute> fromCache = cache.getObjectAttributes(otId);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putObjectAttributes(otId,
          toClientObjectAttributes(api.getObjectAttributes(otId)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<ObjectAttribute>();  
  }

  /*
   * Object Relation
   */
  
  private static ArrayList<ObjectRelation> toClientObjectRelations(
      ArrayList<StoreDB.ObjectRelation> objectRelations) {
    final ArrayList<ObjectRelation> clients = new ArrayList<ObjectRelation>();
    for (StoreDB.ObjectRelation or : objectRelations) {
      clients.add(Utils.toClientObjectRelation(or));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateObjectRelationResult createObjectRelation(final ObjectRelation or,
      final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.ObjectRelation objrel = new StoreDB.ObjectRelation(or.getId(), 
          or.getCode(), or.getName(), or.getDesc(), or.getOt1Id(), 
          Utils.toServerObjectType(or.getOt1()), or.getOt2Id(), 
          Utils.toServerObjectType(or.getOt2()), or.getType(), or.getOrId(), 
          Utils.toServerObjectRelation(or.getOr(), true), author.getId());
      api.saveObjectRelation(objrel);
      final CreateOrUpdateObjectRelationResult result = 
          new CreateOrUpdateObjectRelationResult(objrel.getId(), 
              objrel.getCode(), objrel.getLastUpdatedAt());
      api.commit();
      
      cache.deleteObjectRelations(or.getOt1Id());
      cache.deleteObjectRelations(or.getOt2Id());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<ObjectRelation> getObjectRelations(int otId) {
    final StoreDB.Api api = store.getApi();
    try {
      final List<ObjectRelation> fromCache = cache.getObjectRelations(otId);
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putObjectRelations(otId,
          toClientObjectRelations(api.getObjectRelations(otId)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<ObjectRelation>();  
  }

  /*
   * Value Type
   */

  private static ArrayList<ValueType> toClientValueTypes(
      ArrayList<StoreDB.ValueType> valueTypes) {
    final ArrayList<ValueType> clients = new ArrayList<ValueType>();
    for (StoreDB.ValueType vt : valueTypes) {
      clients.add(Utils.toClientValueType(vt));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateValueTypeResult createValueType(ValueType valueType,
      AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.ValueType vt = new StoreDB.ValueType(valueType.getId(), 
          valueType.getCode(), valueType.getName(), valueType.getDesc(),
          valueType.getType(), valueType.getFlags(), author.getId());
      api.saveValueType(vt);
      final CreateOrUpdateValueTypeResult result = new CreateOrUpdateValueTypeResult(
          vt.getId(), vt.getCode(), vt.getLastUpdatedAt());
      api.commit();
      
      cache.deleteValueTypes();
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<ValueType> getValueTypes() {
    final StoreDB.Api api = store.getApi();
    try {
      final List<ValueType> fromCache = cache.getValueTypes();
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putValueTypes(toClientValueTypes(api.getValueTypes()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<ValueType>();
  }

  /*
   * Unit
   */
  
  private static ArrayList<Unit> toClientUnits(
      ArrayList<StoreDB.Unit> units) {
    final ArrayList<Unit> clients = new ArrayList<Unit>();
    for (StoreDB.Unit n : units) {
      clients.add(new Unit(n.getId(), n.getCode(), n.getName(), n.getDesc(),
          n.getSymbol(), n.getType(), n.getConversion(), n.getUserId(), 
          n.getLastUpdatedAt()));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateUnitResult createUnit(Unit unit, AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.Unit u = new StoreDB.Unit(unit.getId(), unit.getCode(), 
          unit.getName(), unit.getDesc(), unit.getSymbol(), unit.getType(), 
          unit.getConversion(), author.getId());
      api.saveUnit(u);
      final CreateOrUpdateUnitResult result = new CreateOrUpdateUnitResult(
          u.getId(), u.getCode(), u.getLastUpdatedAt());
      api.commit();
      
      cache.deleteUnits();
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<Unit> getUnits() {
    final StoreDB.Api api = store.getApi();
    try {
      final List<Unit> fromCache = cache.getUnits();
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putUnits(toClientUnits(api.getUnits()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    return new ArrayList<Unit>();  
  }

  /*
   * Template
   */
  
  private static ArrayList<Template> toClientTemplates(
      ArrayList<StoreDB.Template> templates) {
    final ArrayList<Template> clients = new ArrayList<Template>();
    for (StoreDB.Template n : templates) {
      clients.add(new Template(n.getId(), n.getCode(), n.getName(),
          n.getDesc(), n.getOtId(), Utils.toClientObjectType(n.getOt()), 
          n.getOaId(), Utils.toClientObjectAttribute(n.getOa()), 
          n.getUserId(), n.getLastUpdatedAt()));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateTemplateResult createTemplate(final Template template, 
      final HashMap<TemplateTree,ArrayList<TemplateTreeItem>> trees, 
      final HashMap<TemplateList,ArrayList<TemplateListItem>> lists,
      final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.Template t = new StoreDB.Template(template.getId(), 
          template.getCode(), template.getName(), template.getDesc(), 
          template.getOtId(), null, template.getOaId(), null, author.getId());
      api.saveTemplate(t);
      if (template.getId() == 0)
        cacheApp.deleteApplicationTemplates(0);
      else {
        // invalidate cache for application which contains template
        ArrayList<StoreDB.ApplicationTemplate> appts = api.getAppTemplatesByTemplate(template.getId());
        for (StoreDB.ApplicationTemplate appt : appts)
          cacheApp.deleteApplicationTemplates(appt.getAppId());
      }
        
      cacheApp.deleteTemplateTrees(t.getId());
      for (TemplateTree tt : trees.keySet()) {
        StoreDB.TemplateTree stt = new StoreDB.TemplateTree(tt.getId(), 
            tt.getCode(), tt.getName(), tt.getDesc(), tt.getTId(), tt.getFlags(),
            tt.getRank(), author.getId()); 
        api.saveTemplateTree(stt);
        api.deleteTemplateTreeItems(stt);
        cacheApp.deleteTemplateTreeItems(tt);
        ArrayList<TemplateTreeItem> ttis = trees.get(tt);
        for (int i = 0; i < ttis.size(); i++) {
          TemplateTreeItem tti = ttis.get(i);
          StoreDB.TemplateTreeItem stti = new StoreDB.TemplateTreeItem(tti.getId(),
              stt.getId(), tti.getTaId(), i, tti.getSubsetOf());
          api.saveTemplateTreeItem(stti);
        }
      }

      cacheApp.deleteTemplateLists(t.getId());
      for (TemplateList tl : lists.keySet()) {
        StoreDB.TemplateList stl = new StoreDB.TemplateList(tl.getId(), 
            tl.getCode(), tl.getName(), tl.getDesc(), tl.getTId(), 
            tl.getFlags(), tl.getRank(), author.getId()); 
        api.saveTemplateList(stl);
        api.deleteTemplateListItems(stl);
        cacheApp.deleteTemplateListItems(tl);
        ArrayList<TemplateListItem> tlis = lists.get(tl);
        for (int i = 0; i < tlis.size(); i++) {
          TemplateListItem tli = tlis.get(i);
          StoreDB.TemplateListItem stli = new StoreDB.TemplateListItem(
              stl.getId(), tli.getTaId(), i);
          api.saveTemplateListItem(stli);
        }
      }

      final CreateOrUpdateTemplateResult result = 
          new CreateOrUpdateTemplateResult(t.getId(), t.getCode(), 
          t.getLastUpdatedAt());
      api.commit();
      
      cache.deleteTemplates();
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<Template> getTemplates() {
    final StoreDB.Api api = store.getApi();
    try {
      final List<Template> fromCache = cache.getTemplates();
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplates(
          toClientTemplates(api.getTemplates()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } 
    
    return new ArrayList<Template>();  
  }

  /*
   * Template Relation
   */

  private static ArrayList<TemplateRelation> toClientTemplateRelations(
      ArrayList<StoreDB.TemplateRelation> templateRelations) {
    final ArrayList<TemplateRelation> clients = new ArrayList<TemplateRelation>();
    for (StoreDB.TemplateRelation tr : templateRelations) {
      clients.add(Utils.toClientTemplateRelation(tr));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateTemplateRelationResult createTemplateRelation(
      final TemplateRelation templateRelation, final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.TemplateRelation tr = new StoreDB.TemplateRelation(templateRelation.getId(), 
          templateRelation.getCode(), templateRelation.getName(), templateRelation.getDesc(), 
          templateRelation.getT1Id(), null, templateRelation.getT2Id(), null, 
          templateRelation.getOrId(), null, templateRelation.getFlags(), 
          templateRelation.getRank(), templateRelation.getSubrank(), author.getId());
      api.saveTemplateRelation(tr);
      final CreateOrUpdateTemplateRelationResult result = 
          new CreateOrUpdateTemplateRelationResult(tr.getId(), tr.getCode(), 
          tr.getLastUpdatedAt());
      api.commit();
      
      cache.deleteTemplateRelations();
      cacheApp.deleteTemplateRelations(templateRelation.getT1Id());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<TemplateRelation> getTemplateRelations() {
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateRelation> fromCache = cache.getTemplateRelations();
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateRelations(
          toClientTemplateRelations(api.getTemplateRelations()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<TemplateRelation>();  
  }

  
  /*
   * Template Group
   */
  
  private static ArrayList<TemplateGroup> toClientTemplateGroups(
      ArrayList<StoreDB.TemplateGroup> templateGroups) {
    final ArrayList<TemplateGroup> clients = new ArrayList<TemplateGroup>();
    for (StoreDB.TemplateGroup tg : templateGroups) {
      clients.add(Utils.toClientTemplateGroup(tg));
    }
    return clients;
  }

  @Override
  public CreateOrUpdateTemplateGroupResult createTemplateGroup(
      final TemplateGroup templateGroup, final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.TemplateGroup tg = new StoreDB.TemplateGroup(templateGroup.getId(), 
          templateGroup.getCode(), templateGroup.getName(), templateGroup.getDesc(), 
          templateGroup.getTId(), null, templateGroup.getFlags(), templateGroup.getRank(), 
          templateGroup.getSubRank(), templateGroup.getLabelTop(),
          templateGroup.getLabelLeft(), templateGroup.getLabelWidth(), 
          templateGroup.getLabelWidthUnit(), templateGroup.getLabelAlign(), author.getId());
      api.saveTemplateGroup(tg);
      final CreateOrUpdateTemplateGroupResult result = 
          new CreateOrUpdateTemplateGroupResult(tg.getId(), tg.getCode(), 
          tg.getLastUpdatedAt());
      api.commit();
      
      cache.deleteTemplateGroups(tg.getTId());
      cacheApp.deleteTemplateGroups(templateGroup.getTId());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public List<TemplateGroup> getTemplateGroups(Template template) {
    final StoreDB.Api api = store.getApi();
    try {
      final List<TemplateGroup> fromCache = cache.getTemplateGroups(template.getId());
      if (fromCache != null) {
        return fromCache;
      }

      return cache.putTemplateGroups(template.getId(),
          toClientTemplateGroups(api.getTemplateGroups(template.getId())));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    
    return new ArrayList<TemplateGroup>();  
  }
  
  /*
   * Template Attribute
   */

  public CreateOrUpdateTemplateAttributeResult createTemplateAttribute(
      final TemplateAttribute templateAttribute, final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.TemplateAttribute ta = new StoreDB.TemplateAttribute(templateAttribute.getId(), 
          templateAttribute.getCode(), templateAttribute.getName(), templateAttribute.getDesc(),
          templateAttribute.getTgId(), null, templateAttribute.getOaId(), null, 
          templateAttribute.getFlags(), 
          templateAttribute.getStyle(), templateAttribute.getTabIndex(), 
          templateAttribute.getDef(), templateAttribute.getLength(), 
          templateAttribute.getLabelTop(), templateAttribute.getLabelLeft(), 
          templateAttribute.getLabelWidth(), templateAttribute.getLabelWidthUnit(), 
          templateAttribute.getLabelAlign(), 
          templateAttribute.getTop(), templateAttribute.getLeft(), 
          templateAttribute.getWidth(), templateAttribute.getWidthUnit(), 
          templateAttribute.getAlign(), 
          templateAttribute.getUnitTop(), templateAttribute.getUnitLeft(), 
          templateAttribute.getUnitWidth(), templateAttribute.getUnitWidthUnit(), 
          templateAttribute.getUnitAlign(), 
          templateAttribute.getShared1(), templateAttribute.getShared2(),
          templateAttribute.getShared3(), templateAttribute.getShared4(),
          templateAttribute.getShared5(),
          author.getId());
      api.saveTemplateAttribute(ta);
      final CreateOrUpdateTemplateAttributeResult result = 
          new CreateOrUpdateTemplateAttributeResult(ta.getId(), ta.getCode(), 
          ta.getLastUpdatedAt());
      api.commit();
      
      cache.deleteTemplateAttributes();
      cacheApp.deleteTemplateAttributes(templateAttribute.getTg().getT());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  /*
   * Application
   */
  
  public CreateOrUpdateApplicationResult createOrUpdateApplication(
      final Application application, final ArrayList<ApplicationTemplate> appts,
      final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {
      
      final StoreDB.Application app = new StoreDB.Application(application.getId(), 
          application.getCode(), application.getName(), application.getDesc(), 
          application.getCategory(), application.getFlags(), author.getId());
      api.saveApplication(app);
      api.deleteApplicationTemplates(app);
      for (ApplicationTemplate appt : appts) {
        final StoreDB.ApplicationTemplate apptdb = new StoreDB.ApplicationTemplate(
            appt.getAppId(), appt.getTId(), appt.getFlags(), appt.getParentMenuId(),
            appt.getRank(), author.getId());
        api.saveApplicationTemplate(apptdb);
      }
      final CreateOrUpdateApplicationResult result = 
          new CreateOrUpdateApplicationResult(app.getId(), app.getCode(), app.getLastUpdatedAt());
      api.commit();
      
      cache.deleteApplications();
      cacheApp.deleteApplicationTemplates(application.getId());
      return result;
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

}
