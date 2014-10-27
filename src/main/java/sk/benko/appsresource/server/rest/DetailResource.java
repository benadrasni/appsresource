package sk.benko.appsresource.server.rest;

import org.apache.log4j.Logger;
import sk.benko.appsresource.shared.domain.DetailRequest;
import sk.benko.appsresource.shared.domain.ListRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 16.9.2013
 * Time: 19:09
 * <p/>
 * List service
 */
@Path("/detail")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DetailResource {

  private static final Logger logger = Logger.getLogger(DetailResource.class.getName());
  /**
   * A reference to the data store.
   */
  private final RestDB store = new RestDB();

  @POST
  public Map<Integer, Map<String, List<String>>> getDetail(DetailRequest detailRequest) {
    logger.info("Getting request..." + detailRequest);
    final RestDB.Api api = store.getApi();
    Map<Integer, Map<String, List<String>>> result = new HashMap<Integer, Map<String, List<String>>>();

    try {
      result = api.getDetail(detailRequest);
      logger.info("Returning details..." + result);
    } catch (SQLException ex) {
      logger.error(ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    return result;
  }
}
