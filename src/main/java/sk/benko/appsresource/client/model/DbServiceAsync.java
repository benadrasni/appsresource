package sk.benko.appsresource.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectRelationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The asynchronous interface for calls to {@link DbService}.
 */
public interface DbServiceAsync extends ServiceAsync {

  /**
   * @param callback
   * @see DbService#getObjectTypes()
   */
  void getObjectTypes(AsyncCallback<List<ObjectType>> callback);

  /**
   * @param otId
   * @param callback
   * @see DbService#getObjectAttributes(int)
   */
  void getObjectAttributes(int otId, AsyncCallback<List<ObjectAttribute>> callback);

  /**
   * @param otId
   * @param callback
   * @see DbService#getObjectRelations(int)
   */
  void getObjectRelations(int otId, AsyncCallback<List<ObjectRelation>> callback);

  /**
   * @param callback
   * @see DbService#getValueTypes()
   */
  void getValueTypes(AsyncCallback<List<ValueType>> callback);

  /**
   * @param callback
   * @see DbService#getUnits()
   */
  void getUnits(AsyncCallback<List<Unit>> callback);

  /**
   * @param callback
   * @see DbService#getTemplates()
   */
  void getTemplates(AsyncCallback<List<Template>> callback);

  /**
   * @param callback
   * @see DbService#getTemplateRelations()
   */
  void getTemplateRelations(AsyncCallback<List<TemplateRelation>> callback);

  /**
   * @param template
   * @param callback
   * @see DbService#getTemplateGroups(Template)
   */
  void getTemplateGroups(Template template, AsyncCallback<List<TemplateGroup>> callback);

  /**
   * @param objectType
   * @param author
   * @param callback
   * @see DbService#createObjectType(ObjectType, AppUser)
   */
  void createObjectType(ObjectType objectType, AppUser author,
                        AsyncCallback<DbService.CreateOrUpdateObjectTypeResult> callback);

  /**
   * @param objectAttribute
   * @param author
   * @param callback
   * @see DbService#createObjectAttribute(ObjectAttribute, AppUser)
   */
  void createObjectAttribute(ObjectAttribute objectAttribute, AppUser author,
                             AsyncCallback<DbService.CreateOrUpdateObjectAttributeResult> callback);

  /**
   * @param objectRelation
   * @param author
   * @param callback
   * @see DbService#createObjectRelation(ObjectRelation, AppUser)
   */
  void createObjectRelation(ObjectRelation objectRelation, AppUser author,
                            AsyncCallback<CreateOrUpdateObjectRelationResult> callback);

  /**
   * @param valueType
   * @param author
   * @param callback
   * @see DbService#createValueType(ValueType, AppUser)
   */
  void createValueType(ValueType valueType, AppUser author,
                       AsyncCallback<DbService.CreateOrUpdateValueTypeResult> callback);

  /**
   * @param unit
   * @param callback
   * @see DbService#createUnit(Unit, AppUser)
   */
  void createUnit(Unit unit, AppUser author, AsyncCallback<DbService.CreateOrUpdateUnitResult> callback);

  /**
   * @param template
   * @param trees
   * @param lists
   * @param author
   * @param callback
   * @see DbService#createTemplate(Template, java.util.HashMap, java.util.HashMap, AppUser)
   */
  void createTemplate(Template template,
                      HashMap<TemplateTree, ArrayList<TemplateTreeItem>> trees,
                      HashMap<TemplateList, ArrayList<TemplateListItem>> lists, AppUser author,
                      AsyncCallback<DbService.CreateOrUpdateTemplateResult> callback);

  /**
   * @param templateRelation
   * @param author
   * @param callback
   * @see DbService#createTemplateRelation(TemplateRelation, AppUser)
   */
  void createTemplateRelation(TemplateRelation templateRelation, AppUser author,
                              AsyncCallback<DbService.CreateOrUpdateTemplateRelationResult> callback);

  /**
   * @param templateGroup
   * @param callback
   * @see DbService#createTemplateGroup(TemplateGroup, AppUser)
   */
  void createTemplateGroup(TemplateGroup templateGroup, AppUser author,
                           AsyncCallback<DbService.CreateOrUpdateTemplateGroupResult> callback);

  /**
   * @param templateAttribute
   * @param author
   * @param callback
   * @see DbService#createTemplateAttribute(TemplateAttribute, AppUser)
   */
  void createTemplateAttribute(TemplateAttribute templateAttribute, AppUser author,
                               AsyncCallback<DbService.CreateOrUpdateTemplateAttributeResult> callback);

  /**
   * @param application
   * @param appts
   * @param author
   * @param callback
   * @see DbService#createOrUpdateApplication(Application, java.util.ArrayList, AppUser)
   */
  void createOrUpdateApplication(Application application,
                                 ArrayList<ApplicationTemplate> appts, AppUser author,
                                 AsyncCallback<DbService.CreateOrUpdateApplicationResult> callback);

}
