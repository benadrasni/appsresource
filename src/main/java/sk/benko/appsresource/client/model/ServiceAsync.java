package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.result.GetApplicationTemplatesResult;
import sk.benko.appsresource.client.model.result.GetTemplateAttributesResult;
import sk.benko.appsresource.client.model.result.GetTemplateListItemsResult;
import sk.benko.appsresource.client.model.result.GetTemplateListsResult;
import sk.benko.appsresource.client.model.result.GetTemplateRelationsResult;
import sk.benko.appsresource.client.model.result.GetTemplateTreeItemsResult;
import sk.benko.appsresource.client.model.result.GetTemplateTreesResult;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The asynchronous interface for calls to {@link Service}.
 *
 *
 */
public interface ServiceAsync {

  /**
   * @see Service#getApplicationTemplates(Application)
   * @param app
   * @param callback
   */
  void getApplicationTemplates(Application app,
      AsyncCallback<GetApplicationTemplatesResult> callback);
  
  /**
   * @see ApplicationService#getTemplateRelations(Template)
   * @param timestamp
   * @param callback
   */
  void getTemplateRelations(Template t,
      AsyncCallback<GetTemplateRelationsResult> callback);
  
  /**
   * @see Service#getTemplateTrees(Template)
   * @param t
   * @param callback
   */
  void getTemplateTrees(int tId,
      AsyncCallback<GetTemplateTreesResult> callback);
  
  /**
   * @see Service#getTemplateTreeItems(TemplateTree)
   * @param tt
   * @param callback
   */
  void getTemplateTreeItems(TemplateTree tt,
      AsyncCallback<GetTemplateTreeItemsResult> callback);

  /**
   * @see Service#getTemplateLists(Template)
   * @param t
   * @param callback
   */
  void getTemplateLists(int tId,
      AsyncCallback<GetTemplateListsResult> callback);

  /**
   * @see Service#getTemplateListItems(TemplateList)
   * @param tl
   * @param callback
   */
  void getTemplateListItems(TemplateList tl,
      AsyncCallback<GetTemplateListItemsResult> callback);
  
  /**
   * @see Service#getTemplateAttributes(Template)
   * @param t
   * @param callback
   */
  void getTemplateAttributes(Template t,
      AsyncCallback<GetTemplateAttributesResult> callback);
}
