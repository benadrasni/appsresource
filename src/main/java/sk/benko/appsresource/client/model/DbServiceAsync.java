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
   * @see DbService#getObjectAttributes()
   * @param callback
   */
  void getObjectAttributes(int otId,
      AsyncCallback<DbService.GetObjectAttributesResult> callback);

  /**
   * @see DbService#getObjectRelations()
   * @param timestamp
   * @param callback
   */
  void getObjectRelations(int otId,
      AsyncCallback<GetObjectRelationsResult> callback);
  
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
   * @see DbService#getTemplates(String)
   * @param callback
   */
  void getTemplates(AsyncCallback<DbService.GetTemplatesResult> callback);

  /**
   * @see DbService#getTemplateRelations(String)
   * @param callback
   */
  void getTemplateRelations(
      AsyncCallback<DbService.GetTemplateRelationsResult> callback);

  /**
   * @see DbService#getTemplateGroups(String)
   * @param callback
   */
  void getTemplateGroups(Template template,
      AsyncCallback<DbService.GetTemplateGroupsResult> callback);

  /**
   * @see DbService#createObjectType(ObjectType)
   * @param objecType
   * @param callback
   */
  void createObjectType(ObjectType objectType, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateObjectTypeResult> callback);

  /**
   * @see DbService#createObjectAttribute(ObjectAttribute)
   * @param objecType
   * @param callback
   */
  void createObjectAttribute(ObjectAttribute objectAttribute, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateObjectAttributeResult> callback);

  /**
   * @see DbService#createObjectRelation(ObjectRelation)
   * @param objecType
   * @param callback
   */
  void createObjectRelation(ObjectRelation objectRelation, AppUser author,
      AsyncCallback<CreateOrUpdateObjectRelationResult> callback);

    /**
   * @see DbService#createValueType(ValueType)
   * @param valueType
   * @param callback
   */
  void createValueType(ValueType valueType, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateValueTypeResult> callback);

  /**
   * @see DbService#createUnit(Unit)
   * @param unit
   * @param callback
   */
  void createUnit(Unit unit, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateUnitResult> callback);
  
  /**
   * @see DbService#createTemplate(Template)
   * @param template
   * @param callback
   */
  void createTemplate(Template template, 
      HashMap<TemplateTree,ArrayList<TemplateTreeItem>> trees, 
      HashMap<TemplateList,ArrayList<TemplateListItem>> lists, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateResult> callback);

  /**
   * @see DbService#createTemplateRelation(TemplateRelation)
   * @param templateRelation
   * @param callback
   */
  void createTemplateRelation(TemplateRelation templateRelation, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateRelationResult> callback);

  /**
   * @see DbService#createTemplateGroup(TemplateGroup)
   * @param templateGroup
   * @param callback
   */
  void createTemplateGroup(TemplateGroup templateGroup, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateGroupResult> callback);

  /**
   * @see DbService#createTemplateAttribute(TemplateAttribute)
   * @param templateAttribute
   * @param callback
   */
  void createTemplateAttribute(TemplateAttribute templateAttribute, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateTemplateAttributeResult> callback);

  /**
   * @see DbService#createApplication(Application)
   * @param application
   * @param appts
   * @param callback
   */
  void createOrUpdateApplication(Application application, 
      ArrayList<ApplicationTemplate> appts, AppUser author,
      AsyncCallback<DbService.CreateOrUpdateApplicationResult> callback);

}
