package sk.benko.appsresource.client.model;

import sk.benko.appsresource.client.model.result.*;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.List;

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
  List<ApplicationTemplate> getApplicationTemplates(Application app) throws AccessDeniedException;
  
  /**
   * Get all template trees for the current template.
   *
   * @param tId
   *          template id
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateTree> getTemplateTrees(int tId) throws AccessDeniedException;
  
  /**
   * Get all template tree items for the template tree.
   *
   * @param tt
   *          template tree
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateTreeItem> getTemplateTreeItems(TemplateTree tt)
      throws AccessDeniedException;
  
  /**
   * Get all template lists for the current template.
   *
   * @param tId
   *          template id
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateList> getTemplateLists(int tId) throws AccessDeniedException;

  /**
   * Get all template list items for the template list.
   *
   * @param tl
   *          template list
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateListItem> getTemplateListItems(TemplateList tl) throws AccessDeniedException;
  
  /**
   * Get all template attributes for the template.
   *
   * @param t
   *          template
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateAttribute> getTemplateAttributes(Template t) throws AccessDeniedException;
  
  /**
   * Get all template relations for given template.
   *
   * @param t
   *          a template
   * @return
   * @throws AccessDeniedException
   */
  List<TemplateRelation> getTemplateRelations(Template t) throws AccessDeniedException;

}
