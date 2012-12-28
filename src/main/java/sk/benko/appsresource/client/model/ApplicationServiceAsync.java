package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.model.result.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The asynchronous interface for calls to {@link ApplicationService}.
 */
public interface ApplicationServiceAsync extends ServiceAsync {

  /**
   * @param langId
   * @param tId
   * @param path
   * @param ta
   * @param callback
   * @see ApplicationService#getTreeLevel(int, int, java.util.List, TemplateAttribute)
   */
  void getTreeLevel(int langId, int tId, List<TreeLevel> path, TemplateAttribute ta,
                    AsyncCallback<GetTreeLevelResult> callback);

  void getTreeLevel(int langId, int tIdSource, Map<Integer, List<AValue>> values, int tId,
                    List<TreeLevel> path, TemplateAttribute ta, AsyncCallback<GetTreeLevelResult> callback);

  /**
   * @param langId
   * @param tId
   * @param path
   * @param ta
   * @param callback
   * @see ApplicationService#getObjects(int, int, java.util.List, TemplateAttribute)
   */
  void getObjects(int langId, int tId, List<TreeLevel> path, TemplateAttribute ta,
                  AsyncCallback<GetObjectsResult> callback);

  /**
   * @param appId
   * @param searchString
   * @param callback
   * @see ApplicationService#getSearchObjectCounts(int, String)
   */
  void getSearchObjectCounts(int appId, String searchString, AsyncCallback<GetSearchCountsResult> callback);

  /**
   * @param langId
   * @param searchString
   * @param tlId
   * @param from
   * @param perPage
   * @param callback
   * @see ApplicationService#getSearchObjects(int, String, int, int, int)
   */
  void getSearchObjects(int langId, String searchString, int tlId, int from, int perPage,
                        AsyncCallback<GetSearchObjectsResult> callback);

  /**
   * @param objId
   * @param rel
   * @param callback
   * @see ApplicationService#getRelatedObjectCounts(int, int, Template)
   */
  void getRelatedObjectCounts(int objId, int rel, Template t, AsyncCallback<GetSearchCountsResult> callback);

  /**
   * @param langId
   * @param objId
   * @param rel
   * @param tlId
   * @param perPage
   * @param callback
   * @see ApplicationService#getRelatedObjects(int, int, int, int, int, int)
   */
  void getRelatedObjects(int langId, int objId, int rel, int tlId, int from, int perPage,
                         AsyncCallback<GetSearchObjectsResult> callback);

  /**
   * @param object
   * @param callback
   * @see ApplicationService#getValues(AObject)
   */
  void getValues(AObject object, AsyncCallback<GetValuesResult> callback);

  /**
   * @param object
   * @param values
   * @param callback
   * @see ApplicationService#createObject(AObject, java.util.List, AppUser)
   */
  void createObject(AObject object, List<AValue> values, AppUser author,
                    AsyncCallback<CreateOrUpdateObjectResult> callback);

  /**
   * @param object
   * @param callback
   * @see ApplicationService#deleteObject(AObject, AppUser)
   */
  void deleteObject(AObject object, AppUser author, AsyncCallback<String> callback);

  void updateValue(AValue value, AppUser author, AsyncCallback<UpdateValueResult> callback);

  void importObjects(Application app, Template t, String filename,
                     Map<Integer, TemplateAttribute> map,
                     Map<Integer, TemplateAttribute> keys,
                     boolean onlyUpdate, AppUser author,
                     AsyncCallback<CountObjectsResult> callback);

  void removeDuplicates(Application app, Template t,
                        Map<Integer, TemplateAttribute> keys, AppUser author,
                        AsyncCallback<CountObjectsResult> callback);

}
