package sk.benko.appsresource.server.rest;

import org.apache.log4j.Logger;
import sk.benko.appsresource.shared.domain.CountRequest;
import sk.benko.appsresource.shared.domain.ListRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 16.9.2013
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
@Path("/list")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListResource {

  private static final Logger logger = Logger.getLogger(CountResource.class.getName());
  /**
   * A reference to the data store.
   */
  private final RestDB store = new RestDB();

  @POST
  public Map<Integer, Map<Integer, String>> getList(ListRequest listRequest) {
    final RestDB.Api api = store.getApi();
    Map<Integer, Map<Integer, String>> result = new HashMap<Integer, Map<Integer, String>>();

    try {
      result = api.getList(listRequest);
      logger.info("Returning list..." + result);
    } catch (SQLException ex) {
      logger.error(ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    return result;
  }
}