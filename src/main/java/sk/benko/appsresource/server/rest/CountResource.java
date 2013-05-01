package sk.benko.appsresource.server.rest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import sk.benko.appsresource.shared.domain.CountRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 18.4.2013
 * Time: 18:56
 * <p/>
 * Count service
 */
@Path("/count")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CountResource {

  private static final Logger logger = Logger.getLogger(CountResource.class.getName());
  /**
   * A reference to the data store.
   */
  private final RestDB store = new RestDB();

  @POST
  public Integer getCount(CountRequest countRequest) {
    final RestDB.Api api = store.getApi();
    Integer result = 0;

    try {
      result = api.getCount(countRequest);
      logger.info("Returning count..." + result);
    } catch (SQLException ex) {
      logger.error(ex.getLocalizedMessage(), ex);
    } finally {
      api.close();
    }
    return result;
  }
}
