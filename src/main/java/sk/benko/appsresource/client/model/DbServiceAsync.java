package sk.benko.appsresource.client.model;

import java.util.ArrayList;
import java.util.HashMap;

import sk.benko.appsresource.client.model.result.CreateOrUpdateObjectRelationResult;
import sk.benko.appsresource.client.model.result.GetObjectRelationsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asynchronous interface for calls to {@link DbService}.
 *
 *
 */
public interface DbServiceAsync extends ServiceAsync {

  /**
   * @see DbService#getObjectTypes()
   * @param callback
   */
  void getObjectTypes(AsyncCallback<DbService.GetObjectTypesResult> callback);

  /**
   * @see DbService#getObjectAttributes(int)
   * @param otId
   * @param callback
   */
  void getObjectAttributes(int otId, AsyncCallback<DbService.GetObjectAttributesResult> callback);

  /**
   * @see DbService#getObjectRelations(int)
   * @param otId
   * @param callback
   */
  void getObjectRelations(int otId, AsyncCallback<GetObjectRelationsResult> callback);
  
  /**
   * @see DbService#getValueTypes()
   * @param callback
   */
  void getValueTypes(AsyncCallback<DbService.GetValueTypesResult> callback);

  /**
   * @see DbService#getUnits()
   * @param callback
   */
  void getUnits(AsyncCallback<DbService.GetUnitsResult> callback);

  /**
   * @see DbService#getTemplates()
   * @param callback
   */
  void getTemplates(AsyncCallback<DbService.GetTemplatesResult> callback);

  /**
   * @see DbService#getTemplateRelations()
   * @param callback
   */
  void getTemplateRelations(
      AsyncCallback<DbService.GetTemplateRelationsResult> callback);

  /**
   * @see DbService#getTemplateGroups(Template)
   * @param template
   * @param callback
   */
  void getTemplateGroups(Template template, AsyncCallback<DbService.GetTemplateGroupsResult> callback);

  /**
   * @see DbService#createObjectType(ObjectType, AppUser)
   * @param objectType
   * @param author
   * @param callback
   */
  void createObjectType(ObjectType objectType, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateObjectTypeResult> callback);

  /**
   * @see DbService#createObjectAttribute(ObjectAttribute, AppUser)
   * @param objectAttribute
   * @param author
   * @param callback
   */
  void createObjectAttribute(ObjectAttribute objectAttribute, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateObjectAttributeResult> callback);

  /**
   * @see DbService#createObjectRelation(ObjectRelation, AppUser)
   * @param objectRelation
   * @param author
   * @param callback
   */
  void createObjectRelation(ObjectRelation objectRelation, AppUser author,
      AsyncCallback<CreateOrUpdateObjectRelationResult> callback);

    /**
   * @see DbService#createValueType(ValueType, AppUser)
   * @param valueType
   * @param author
   * @param callback
   */
  void createValueType(ValueType valueType, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateValueTypeResult> callback);

  /**
   * @see DbService#createUnit(Unit, AppUser)
   * @param unit
   * @param callback
   */
  void createUnit(Unit unit, AppUser author, AsyncCallback<DbService.CreateOrUpdateUnitResult> callback);
  
  /**
   * @see DbService#createTemplate(Template, java.util.HashMap, java.util.HashMap, AppUser)
   * @param template
   * @param trees
   * @param lists
   * @param author
   * @param callback
   */
  void createTemplate(Template template, 
      HashMap<TemplateTree,ArrayList<TemplateTreeItem>> trees, 
      HashMap<TemplateList,ArrayList<TemplateListItem>> lists, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateResult> callback);

  /**
   * @see DbService#createTemplateRelation(TemplateRelation, AppUser)
   * @param templateRelation
   * @param author
   * @param callback
   */
  void createTemplateRelation(TemplateRelation templateRelation, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateRelationResult> callback);

  /**
   * @see DbService#createTemplateGroup(TemplateGroup, AppUser)
   * @param templateGroup
   * @param callback
   */
  void createTemplateGroup(TemplateGroup templateGroup, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateGroupResult> callback);

  /**
   * @see DbService#createTemplateAttribute(TemplateAttribute, AppUser)
   * @param templateAttribute
   * @param author
   * @param callback
   */
  void createTemplateAttribute(TemplateAttribute templateAttribute, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateAttributeResult> callback);

  /**
   * @see DbService#createOrUpdateApplication(Application, java.util.ArrayList, AppUser)
   * @param application
   * @param appts
   * @param author
   * @param callback
   */
  void createOrUpdateApplication(Application application, 
      ArrayList<ApplicationTemplate> appts, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateApplicationResult> callback);

}
