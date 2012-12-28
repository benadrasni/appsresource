package sk.benko.appsresource.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.model.result.CountObjectsResult;
import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectResult;
import sk.benko.appsresource.client.model.result.GetObjectsResult;
import sk.benko.appsresource.client.model.result.GetSearchCountsResult;
import sk.benko.appsresource.client.model.result.GetSearchObjectsResult;
import sk.benko.appsresource.client.model.result.GetTreeLevelResult;
import sk.benko.appsresource.client.model.result.GetValuesResult;
import sk.benko.appsresource.client.model.result.UpdateValueResult;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The RPC api available to the client. The asynchronous version that is used
 * directly by the client is {@link ApplicationServiceAsync}.
 *
 */
@RemoteServiceRelativePath("appservice")
public interface ApplicationService extends Service {

  /**
   * Get all objects for given template and leaf attribute.
   *
   * @param langId
   *          a language id
   * @param tId
   *          a Template id
   * @param path
   *          values which represent previous tree levels
   * @param ta
   *          a Template Attribute
   * @return
   * @throws AccessDeniedException
   */
  GetTreeLevelResult getTreeLevel(int langId, int tId, List<TreeLevel> path, TemplateAttribute ta)
      throws AccessDeniedException;

  GetTreeLevelResult getTreeLevel(int langId, int tIdSource, Map<Integer, List<AValue>> values,
      int tId, List<TreeLevel> path, TemplateAttribute ta)  throws AccessDeniedException;
  
  /**
   * Get all objects for given template and leaf attribute.
   *
   * @param langId
   *          a language id
   * @param tId
   *          a Template id
   * @param ta
   *          a Template Attribute
   * @return
   * @throws AccessDeniedException
   */
  GetObjectsResult getObjects(int langId, int tId, List<TreeLevel> path, TemplateAttribute ta)
      throws AccessDeniedException;

  /**
   * Get counts of found objects by type.
   *
   * @param searchString
   * @return
   * @throws AccessDeniedException
   */
  GetSearchCountsResult getSearchObjectCounts(int appId, String searchString) 
      throws AccessDeniedException;

  /**
   * Get selected objects with attribute(s) which start with given string.
   *
   * @param langId
   * @param searchString
   * @param tlId
   * @param from
   * @param perPage
   * @return
   * @throws AccessDeniedException
   */
  GetSearchObjectsResult getSearchObjects(int langId, String searchString, 
      int tlId, int from, int perPage) throws AccessDeniedException;
  
  /**
   * Get counts of related objects by type.
   *
   * @param objId
   * @param rel
   * @return
   * @throws AccessDeniedException
   */
  GetSearchCountsResult getRelatedObjectCounts(int objId, int rel, Template t) 
      throws AccessDeniedException;

  /**
   * Get all objects for given template and leaf attribute.
   *
   * @param langId
   * @param objId
   * @param rel
   * @param tlId
   * @param perPage
   * @return
   * @throws AccessDeniedException
   */
  GetSearchObjectsResult getRelatedObjects(int langId, int objId, int rel, 
      int tlId, int from, int perPage) throws AccessDeniedException;
  
  /**
   * Get all values for given object.
   *
   * @param object
   * @return
   * @throws AccessDeniedException
   */
  GetValuesResult getValues(AObject object) throws AccessDeniedException;
  
  CreateOrUpdateObjectResult createObject(AObject object, List<AValue> values, AppUser author)
      throws AccessDeniedException;

  UpdateValueResult updateValue(AValue value, AppUser author) 
      throws AccessDeniedException;

  String deleteObject(AObject object, AppUser author); 

  CountObjectsResult importObjects(Application app, Template t, String filename,
      Map<Integer, TemplateAttribute> map,
      Map<Integer, TemplateAttribute> keys, boolean onlyUpdate,
      AppUser author);

  CountObjectsResult removeDuplicates(Application app, Template t,
      Map<Integer, TemplateAttribute> keys, AppUser author);
}
