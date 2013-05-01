package sk.benko.appsresource.server.rest;

import com.google.appengine.api.rdbms.AppEngineDriver;
import org.apache.log4j.Logger;
import sk.benko.appsresource.server.ServerUtils;
import sk.benko.appsresource.server.TreeLevelComparator;
import sk.benko.appsresource.shared.domain.CountRequest;
import sk.benko.appsresource.shared.domain.FilterAttribute;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 24.4.2013
 * Time: 17:56
 *
 * Class which provides database operations
 */
public class RestDB {

  public class Api {
    private static final String URL = "jdbc:google:rdbms://appsres:appsres2/appsresource";
    private static final String USER = "appsres";
    private static final String PASSWORD = "appsres";

    private Connection connection;

    private Api () {
      connection = getConnection();
    }

    private Connection getConnection() {
      try {
        DriverManager.registerDriver(new AppEngineDriver());

        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
      }
      catch (SQLException ex) {
        log.error(ex.getLocalizedMessage(), ex);
      }

      return connection;
    }

    public void commit() throws SQLException {
      connection.commit();
    }

    public void rollback() {
      try {
        connection.rollback();
      } catch (Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
      }
    }

    public void close() {
      try {
        connection.close();
      } catch (Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
      }
    }

    public Integer getCount(CountRequest countRequest) throws SQLException {
      Integer result = 0;

      return RestDB.getCount(connection, countRequest);
    }
  }

  private static final Logger log = Logger.getLogger(RestDB.class.getName());

  public static final String TABLE_PARENTS = "aobject_parents";
  public static final String TABLE_VALUE = "avalues";
  public static final String TABLE_VALUE_REF = "avalues_ref";

  public static final String PARENTS_OT_ID = "pa_ot_id";
  public static final String PARENTS_OA_ID = "pa_ao_id";
  public static final String VALUE_ID = "av_id";
  public static final String VALUE_AO_ID = "av_ao_id";
  public static final String VALUE_OA_ID = "av_oa_id";
  public static final String VALUE_REF_AVR_AV_ID = "avr_av_id";
  public static final String VALUE_REF_VALUE = "avr_value";

  /**
   * Starts a data store session and returns an Api object to use.
   *
   * @return      <code>Api</code>
   */
  public Api getApi() {
    return new Api();
  }

  private static Integer getCount(Connection c, CountRequest countRequest) throws SQLException {
    Integer result = 0;

    Statement stmt = c.createStatement();
    String[] hlpFilter =  getTablesAndWheresForFilter(countRequest.getFilterAttributes(), 0);

    String query = "SELECT COUNT(*) "
        + " FROM " + TABLE_PARENTS
        + hlpFilter[0]
        + " WHERE " + PARENTS_OT_ID + "=" + countRequest.getObjectTypeId()
        + hlpFilter[1];

    ResultSet rs = stmt.executeQuery(query);
    if (rs.next()) {
      result = rs.getInt(1);
    }
    rs.close();
    stmt.close();
    return result;
  }


  private static String[] getTablesAndWheresForFilter(List<FilterAttribute> filter, int filterOffset) {
    String[] result = new String[2];

    result[0] = "";
    result[1] = "";
    if (filter != null) {
      for (int i = filterOffset; i < filter.size()+filterOffset; i++) {
        FilterAttribute filterAttribute = filter.get(i-filterOffset);
        result[0] = result[0] + ", " + TABLE_VALUE + " av" + i;
        result[1] = result[1] +  " AND av" + i + "." + VALUE_AO_ID + "=" + PARENTS_OA_ID
            + " AND av" + i + "." + VALUE_OA_ID + "=" + filterAttribute.getAttributeId();

        result[0] = result[0] + ", " + TABLE_VALUE_REF + " avr" + i;
        result[1] = result[1] + " AND avr" + i + "." + VALUE_REF_AVR_AV_ID + "= av" + i + "." + VALUE_ID
            + " AND avr" + i + "." + VALUE_REF_VALUE + "=" + filterAttribute.getValueId();
      }
    }
    return result;
  }
}
