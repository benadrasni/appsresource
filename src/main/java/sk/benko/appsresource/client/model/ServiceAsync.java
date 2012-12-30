package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.result.*;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;


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
  void getApplicationTemplates(Application app, AsyncCallback<List<ApplicationTemplate>> callback);
  
  /**
   * @see ApplicationService#getTemplateRelations(Template)
   * @param t
   * @param callback
   */
  void getTemplateRelations(Template t, AsyncCallback<List<TemplateRelation>> callback);
  
  /**
   * @see Service#getTemplateTrees(int)
   * @param tId
   * @param callback
   */
  void getTemplateTrees(int tId, AsyncCallback<List<TemplateTree>> callback);
  
  /**
   * @see Service#getTemplateTreeItems(TemplateTree)
   * @param tt
   * @param callback
   */
  void getTemplateTreeItems(TemplateTree tt, AsyncCallback<List<TemplateTreeItem>> callback);

  /**
   * @see Service#getTemplateLists(int)
   * @param tId
   * @param callback
   */
  void getTemplateLists(int tId, AsyncCallback<List<TemplateList>> callback);

  /**
   * @see Service#getTemplateListItems(TemplateList)
   * @param tl
   * @param callback
   */
  void getTemplateListItems(TemplateList tl, AsyncCallback<List<TemplateListItem>> callback);
  
  /**
   * @see Service#getTemplateAttributes(Template)
   * @param t
   * @param callback
   */
  void getTemplateAttributes(Template t, AsyncCallback<List<TemplateAttribute>> callback);
}
