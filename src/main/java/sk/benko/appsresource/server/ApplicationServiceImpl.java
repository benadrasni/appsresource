package sk.benko.appsresource.server;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.result.*;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The server-side RPC endpoint for {@link ApplicationService}.
 */
@SuppressWarnings("serial")
public class ApplicationServiceImpl extends ServiceImpl implements ApplicationService {
  private static final Logger log = Logger.getLogger(ApplicationServiceImpl.class.getName());
  /**
   * A reference to a cache service.
   */
  private final CacheApplication cache = new CacheApplication(MemcacheServiceFactory
      .getMemcacheService());
  /**
   * A reference to the data store.
   */
  private final StoreDB store = new StoreDB();

  private static List<StoreDB.TreeLevel> toServerTreeLevels(List<TreeLevel> levels) {
    final List<StoreDB.TreeLevel> servers = new ArrayList<StoreDB.TreeLevel>();
    for (TreeLevel tl : levels) {
      StoreDB.TreeLevel stl = new StoreDB.TreeLevel(tl.getId(), Utils.toServerTemplateAttribute(tl.getTa()));
      stl.setValueString(tl.getValueString());
      stl.setValueDate(tl.getValueDate());
      stl.setValueDouble(tl.getValueDouble());
      stl.setValueRef(tl.getValueRef());
      stl.setSubsetOf(tl.getSubsetOf());
      servers.add(stl);
    }
    return servers;
  }

  private static List<TreeLevel> toClientTreeLevels(List<StoreDB.TreeLevel> levels) {
    final List<TreeLevel> servers = new ArrayList<TreeLevel>();
    for (StoreDB.TreeLevel tl : levels) {
      servers.add(new TreeLevel(tl.getId(), Utils.toClientTemplateAttribute(tl.getTa()),
          tl.getValueString(), tl.getValueDate(), tl.getValueDouble(), tl.getValueRef(), tl.getSubsetOf()));
    }
    return servers;
  }

  private static List<AObject> toClientObjects(List<StoreDB.AObject> objects) {
    final List<AObject> clients = new ArrayList<AObject>();
    for (StoreDB.AObject o : objects) {
      clients.add(Utils.toClientObject(o));
    }
    return clients;
  }

  private static Map<Integer, StoreDB.TemplateAttribute> toServerTemplateAttributes(
      Map<Integer, TemplateAttribute> tas) {
    final Map<Integer, StoreDB.TemplateAttribute> servers = new HashMap<Integer, StoreDB.TemplateAttribute>();
    for (Integer key : tas.keySet()) {
      TemplateAttribute ta = tas.get(key);

      servers.put(key, new StoreDB.TemplateAttribute(ta.getId(), ta.getCode(), ta.getName(),
          ta.getDesc(), ta.getTgId(), Utils.toServerTemplateGroup(ta.getTg()),
          ta.getOaId(), Utils.toServerObjectAttribute(ta.getOa()),
          ta.getFlags(), ta.getStyle(), ta.getTabIndex(), ta.getDef(),
          ta.getLength(), ta.getLabelTop(), ta.getLabelLeft(),
          ta.getLabelWidth(), ta.getLabelWidthUnit(), ta.getLabelAlign(),
          ta.getTop(), ta.getLeft(), ta.getWidth(),
          ta.getWidthUnit(), ta.getAlign(),
          ta.getUnitTop(), ta.getUnitLeft(), ta.getUnitWidth(),
          ta.getUnitWidthUnit(), ta.getUnitAlign(),
          ta.getShared1(), ta.getShared2(), ta.getShared3(), ta.getShared4(), ta.getShared5(),
          ta.getUserId()));
    }
    return servers;
  }

  /*
   * Tree level
   */
  @Override
  public List<TreeLevel> getTreeLevel(int langId, int tId, List<TreeLevel> path, TemplateTreeItem tti)
      throws AccessDeniedException {
    return getLevel(langId, tId, path, tti);
  }

  public List<TreeLevel> getTreeLevel(int langId, int tIdSource, Map<Integer, List<AValue>> values, int tId,
                                         List<TreeLevel> path, TemplateTreeItem tti)
      throws AccessDeniedException {
    return getLevel(langId, tIdSource, values, tId, path, tti);
  }

  private List<TreeLevel> getLevel(int langId, int tId, List<TreeLevel> path, TemplateTreeItem tti) {
    return getLevel(langId, 0, null, tId, path, tti);
  }

  private List<TreeLevel> getLevel(int langId, int tIdSource, Map<Integer, List<AValue>> values, int tId,
                                   List<TreeLevel> path, TemplateTreeItem tti) {
    List<TreeLevel> result = new ArrayList<TreeLevel>();
    final StoreDB.Api api = store.getApi();
    try {
      List<StoreDB.AValue> filter = null;
      if (tIdSource > 0) {
        // filtering choose tree
        Map<Integer, StoreDB.TemplateAttribute> cattrs =  api.getCommonAttributes(tIdSource, tId);
        filter = new ArrayList<StoreDB.AValue>();
        for (List<AValue> avs : values.values()) {

          if (avs != null && avs.size() > 0) {
            int i = 0;
            AValue value = avs.get(i);
            AValue defValue = value;
            while (i < avs.size()
                && value.getLangId() != langId) {
              value = ++i < avs.size() ? avs.get(i) : defValue;
            }

            StoreDB.TemplateAttribute taRef = cattrs.get(value.getOaId());
            if (taRef != null) {
              StoreDB.AValue dbValue = new StoreDB.AValue(0, 0, taRef.getOaId(), 0,
                  value.getValueString(), value.getLangId(), value.getValueDate(),
                  value.getValueTimestamp(), value.getValueDouble(), value.getValueRef(),
                  0);
              filter.add(dbValue);
            }
          }
        }
      }

      List<StoreDB.TreeLevel> tl;
      switch (tti.getTa().getOa().getVt().getType()) {
        case ValueType.VT_INT:
        case ValueType.VT_REAL:
          tl = api.getTreeLevelNumber(filter, tId, toServerTreeLevels(path),
              Utils.toServerTemplateTreeItem(tti), Utils.toServerTemplateAttribute(tti.getTa()));
          break;

        case ValueType.VT_STRING:
          tl = api.getTreeLevelString(langId, filter, tId, toServerTreeLevels(path),
              Utils.toServerTemplateTreeItem(tti), Utils.toServerTemplateAttribute(tti.getTa()));
          break;

        case ValueType.VT_DATETIME:
        case ValueType.VT_DATE:
          tl = api.getTreeLevelDate(filter, tId, toServerTreeLevels(path),
              Utils.toServerTemplateTreeItem(tti), Utils.toServerTemplateAttribute(tti.getTa()));
          break;

        case ValueType.VT_REF:
          tl = api.getTreeLevelRef(langId, filter, tId, toServerTreeLevels(path),
              Utils.toServerTemplateTreeItem(tti), Utils.toServerTemplateAttribute(tti.getTa()));
          break;

        default:
          tl = null;
          break;
      }
      return toClientTreeLevels(tl);
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public GetObjectsResult getObjects(int langId, int tId, List<TreeLevel> path, TemplateAttribute ta)
      throws AccessDeniedException {
    return new GetObjectsResult(path, getObjs(langId, tId, path, ta));
  }

  @Override
  public Map<Template, Integer> getSearchObjectCounts(int appId, String searchString)
      throws AccessDeniedException {
    return getSearchObjCounts(appId, searchString);
  }

  @Override
  public Map<Template, Integer> getRelatedObjectCounts(int objId, int rel, Template t)
      throws AccessDeniedException {
    return getRelObjCounts(objId, rel, t);
  }

  private List<AObject> getObjs(int langId, int tId, List<TreeLevel> path, TemplateAttribute ta) {
    List<AObject> result = new ArrayList<AObject>();
    final StoreDB.Api api = store.getApi();
    try {
      return toClientObjects(api.getObjects(langId, tId, toServerTreeLevels(path), ta.getOaId()));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public Map<AObject, List<AValue>> getSearchObjects(int langId, String searchString, int tlId, int from, int perPage) {
    Map<AObject, List<AValue>> result = new LinkedHashMap<AObject, List<AValue>>();
    final StoreDB.Api api = store.getApi();
    try {
      Map<StoreDB.AObject, List<StoreDB.AValue>> objValues =
          api.getSearchObjects(langId, searchString, tlId, from, perPage);
      for (StoreDB.AObject aObject : objValues.keySet()) {

        ArrayList<AValue> values = new ArrayList<AValue>();
        List<StoreDB.AValue> dbValues = objValues.get(aObject);
        if (dbValues != null)
          for (StoreDB.AValue dbValue : dbValues)
            values.add(Utils.toClientValue(dbValue));
        result.put(Utils.toClientObject(aObject), values);
      }
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  @Override
  public Map<AObject, List<AValue>> getRelatedObjects(int langId, int objId, int rel, int tlId, int from, int perPage) {
    Map<AObject, List<AValue>> result = new LinkedHashMap<AObject, List<AValue>>();
    final StoreDB.Api api = store.getApi();
    try {
      Map<StoreDB.AObject, List<StoreDB.AValue>> objValues =
          api.getRelatedObjects(langId, objId, rel, tlId, from, perPage);
      for (StoreDB.AObject aObject : objValues.keySet()) {

        List<AValue> values = new ArrayList<AValue>();
        List<StoreDB.AValue> dbValues = objValues.get(aObject);
        if (dbValues != null)
          for (StoreDB.AValue dbValue : dbValues)
            values.add(Utils.toClientValue(dbValue));
        result.put(Utils.toClientObject(aObject), values);
      }
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  private HashMap<Template, Integer> getSearchObjCounts(int appId, String searchString) {
    HashMap<Template, Integer> result = new LinkedHashMap<Template, Integer>();
    final StoreDB.Api api = store.getApi();
    try {
      HashMap<StoreDB.Template, Integer> counts =
          api.getSearchObjectCounts(appId, searchString);
      for (StoreDB.Template t : counts.keySet()) {
        result.put(Utils.toClientTemplate(t), counts.get(t));
      }
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  private HashMap<Template, Integer> getRelObjCounts(int objId, int rel, Template t) {
    HashMap<Template, Integer> result = new LinkedHashMap<Template, Integer>();
    final StoreDB.Api api = store.getApi();
    try {
      result.put(t, api.getRelatedObjectCounts(objId, rel));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  public CreateOrUpdateObjectResult createObject(final AObject object, final List<AValue> values, final AppUser author)
      throws AccessDeniedException {
    final StoreDB.Api api = store.getApi();
    try {

      final StoreDB.AObject ao = new StoreDB.AObject(object.getId(),
          object.getOtId(), object.getLevel(), object.getLeaf(), author.getId());
      api.saveObject(ao);
      for (AValue value : values) {
        final StoreDB.AValue avaluedb = new StoreDB.AValue(
            value.getId(), ao.getId(), value.getOaId(), value.getRank(),
            value.getValueString(), value.getLangId(), value.getValueDate(),
            value.getValueTimestamp(), value.getValueDouble(),
            value.getValueRef(), author.getId());
        api.saveValue(avaluedb);
      }
      api.commit();

      return new CreateOrUpdateObjectResult(ao.getId(), ao.getLastUpdatedAt(), getValues(Utils.toClientObject(ao)));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  public String deleteObject(final AObject object, final AppUser author) {
    final StoreDB.Api api = store.getApi();
    try {

      api.deleteObject(object.getOtId(), object.getId(), author.getId());
      api.commit();
      cache.deleteValues(object.getId());

      return "";
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public Map<Integer, Map<Integer, List<AValue>>> getValues(AObject object) {
    Map<Integer, Map<Integer, List<AValue>>> result = new HashMap<Integer, Map<Integer, List<AValue>>>();

    final StoreDB.Api api = store.getApi();
    try {
      Map<Integer, List<AValue>> values = cache.getValues(object.getId());
      if (values == null) {
        values = Utils.toClientValues(api.getValues(object.getId()));
        cache.putValues(object.getId(), values);
      }
      result.put(object.getId(), values);

      getSubValues(api, values, result);
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }

    return result;
  }

  private void getSubValues(StoreDB.Api api,
                            Map<Integer, List<AValue>> allValues,
                            Map<Integer, Map<Integer, List<AValue>>> result)
      throws SQLException {
    for (Integer oaId : allValues.keySet()) {
      List<AValue> values = allValues.get(oaId);

      if (values != null)
        for (AValue value : values)
          if (value.getValueRef() > 0 && result.get(value.getValueRef()) == null) {
            Map<Integer, List<AValue>> subValues = cache.getValues(value.getValueRef());
            if (subValues == null) {
              subValues = Utils.toClientValues(api.getValues(value.getValueRef()));
              cache.putValues(value.getValueRef(), subValues);
            }

            result.put(value.getValueRef(), subValues);
            getSubValues(api, subValues, result);
          }
    }
  }

  /*
   * Import objects
   */

  public UpdateValueResult updateValue(final AValue value, final AppUser author)
      throws AccessDeniedException {
    final StoreDB.Api api = store.getApi();
    try {

      final StoreDB.AValue avaluedb = new StoreDB.AValue(
          value.getId(), value.getOId(), value.getOaId(), value.getRank(),
          value.getValueString(), value.getLangId(), value.getValueDate(),
          value.getValueTimestamp(), value.getValueDouble(),
          value.getValueRef(), author.getId());
      api.saveValue(avaluedb);
      api.commit();
      cache.deleteValues(value.getOId());

      return new UpdateValueResult(avaluedb.getId(), avaluedb.getLastUpdatedAt(),
          Utils.toClientValue(avaluedb));
    } catch (SQLException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
    } finally {
      api.close();
    }
    return null;
  }

  @Override
  public Integer importObjects(Application app, Template t, String filename, Map<Integer, TemplateAttribute> map,
                               Map<Integer, TemplateAttribute> keys, boolean onlyUpdate, AppUser author) {
    final StoreDB.Api api = store.getApi();
    int count = 0;
    try {
      List<Integer> oIds = api.importObjects(author.getId(), Utils.toServerApplication(app),
          Utils.toServerTemplate(t), filename, toServerTemplateAttributes(map),
          toServerTemplateAttributes(keys), onlyUpdate);
      api.commit();
      count = oIds.size();
      for (Integer oId : oIds)
        cache.deleteValues(oId);
    } catch (SQLException ex) {
      api.rollback();
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    return count;
  }

  @Override
  public Integer removeDuplicates(Application app, Template t, Map<Integer, TemplateAttribute> keys, AppUser author) {
    final StoreDB.Api api = store.getApi();
    int count = 0;
    try {
      //count = api.removeDuplicates(author.getId(), Utils.toServerApplication(app), 
      //    Utils.toServerTemplate(t), toServerTemplateAttributes(keys));
      api.commit();
    } catch (SQLException ex) {
      api.rollback();
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    return count;
  }

}
