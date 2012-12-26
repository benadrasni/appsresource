package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.result.GetApplicationTemplatesResult;
import sk.benko.appsresource.client.model.result.GetTemplateAttributesResult;
import sk.benko.appsresource.client.model.result.GetTemplateListItemsResult;
import sk.benko.appsresource.client.model.result.GetTemplateListsResult;
import sk.benko.appsresource.client.model.result.GetTemplateRelationsResult;
import sk.benko.appsresource.client.model.result.GetTemplateTreeItemsResult;
import sk.benko.appsresource.client.model.result.GetTemplateTreesResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The RPC api available to the client. The asynchronous version that is used
 * directly by the client is {@link DbServiceAsync}.
 *
 */
@RemoteServiceRelativePath("service")
public interface Service extends RemoteService {

  /**
   * An exception that is thrown by the server whenever the current user is not
   * logged in, or if the RPC requests an operation that cannot be carried out
   * for the user.
   */
  @SuppressWarnings("serial")
  static class AccessDeniedException extends Exception {
  }
  
  /**
   * Get all application templates for the application. 
   *
   * @param app
   *          the application
   * @return
   * @throws AccessDeniedException
   */
  GetApplicationTemplatesResult getApplicationTemplates(Application app)
      throws AccessDeniedException;
  
  /**
   * Get all template trees for the current template.
   *
   * @param tId
   *          template id
   * @return
   * @throws AccessDeniedException
   */
  GetTemplateTreesResult getTemplateTrees(int tId)
      throws AccessDeniedException;
  
  /**
   * Get all template tree items for the template tree.
   *
   * @param tt
   *          template tree
   * @return
   * @throws AccessDeniedException
   */
  GetTemplateTreeItemsResult getTemplateTreeItems(TemplateTree tt)
      throws AccessDeniedException;
  
  /**
   * Get all template lists for the current template.
   *
   * @param tId
   *          template id
   * @return
   * @throws AccessDeniedException
   */
  GetTemplateListsResult getTemplateLists(int tId)
      throws AccessDeniedException;

  /**
   * Get all template list items for the template list.
   *
   * @param tl
   *          template list
   * @return
   * @throws AccessDeniedException
   */
  GetTemplateListItemsResult getTemplateListItems(TemplateList tl)
      throws AccessDeniedException;
  
  /**
   * Get all template attributes for the template.
   *
   * @param t
   *          template
   * @return
   * @throws AccessDeniedException
   */
  GetTemplateAttributesResult getTemplateAttributes(Template t)
      throws AccessDeniedException;
  
  /**
   * Get all template relations for given template.
   *
   * @param t
   *          a template
   * @return
   * @throws AccessDeniedException
   */
  GetTemplateRelationsResult getTemplateRelations(Template t)
      throws AccessDeniedException;

}
