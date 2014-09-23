package sk.benko.appsresource.server.rest;

import com.google.appengine.api.rdbms.AppEngineDriver;
import org.apache.log4j.Logger;
import sk.benko.appsresource.server.StoreDB;
import sk.benko.appsresource.shared.domain.CountRequest;
import sk.benko.appsresource.shared.domain.FilterAttribute;
import sk.benko.appsresource.shared.domain.ListRequest;

import java.sql.*;
import java.util.*;

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

    public Map<Integer, Map<Integer, String>> getList(ListRequest listRequest) throws SQLException {
      return RestDB.getList(connection, listRequest);
    }

    public Integer getCount(CountRequest countRequest) throws SQLException {
      return RestDB.getCount(connection, countRequest);
    }
  }

  private static final Logger log = Logger.getLogger(RestDB.class.getName());

  public static final String TA_ID = "ta_id";
  public static final String TA_FLAGS = "ta_flags";

  /**
   * Starts a data store session and returns an Api object to use.
   *
   * @return      <code>Api</code>
   */
  public Api getApi() {
    return new Api();
  }

  static Integer getCount(Connection c, CountRequest countRequest) throws SQLException {
    Integer result = 0;

    Statement stmt = c.createStatement();
    String[] hlpFilter =  getTablesAndWheresForFilter(countRequest.getFilterAttributes(), 0);

    String query = "SELECT COUNT(*) "
        + " FROM aobject_parents " + hlpFilter[0]
        + " WHERE pa_ot_id = " + countRequest.getObjectTypeId() + hlpFilter[1];

    ResultSet rs = stmt.executeQuery(query);
    if (rs.next()) {
      result = rs.getInt(1);
    }
    rs.close();
    stmt.close();
    return result;
  }


  static Map<Integer, Map<Integer, String>> getList(Connection c, ListRequest listRequest) throws SQLException {
    Statement stmt = c.createStatement();
    String[] hlpFilter =  getTablesAndWheresForFilter(listRequest.getFilterAttributes(), 0);

    String query = "SELECT distinct ao_id, ao_ot_id, ao_user_id, ao_lastupdateat, "
        + " ta_id, ta_flags, main_val.av_id, main_val.av_ao_id, "
        + " main_val.av_oa_id, main_val.av_rank, main_val.av_lastupdateat, "
        + " main_char.avc_value, main_text.avt_value, main_text.avt_lang_id, "
        + " CASE WHEN main_ref.avr_av_id is NULL THEN main_char.avc_lang_id ELSE ref_char.avc_lang_id END as avc_lang_id, "
        + " avd_value, avn_value, main_ref.avr_value, main_val.av_user_id, "
        + " ref.av_ao_id as ao, ref.av_oa_id as oa, ref_char.avc_value as ao_leaf "
        + " FROM aobjects, template_attributes " + hlpFilter[0] + " left outer join "
        + " avalues main_val on (main_val.av_oa_id = ta_oa_id) left outer join "
        + " avalues_char main_char on (main_char.avc_av_id = main_val.av_id) left outer join "
        + " avalues_date on (avc_av_id = main_val.av_id) left outer join "
        + " avalues_number on (avn_av_id = main_val.av_id) left outer join "
        + " avalues_ref main_ref on (main_ref.avr_av_id = main_val.av_id) left outer join "
        + " avalues_text main_text on (main_text.avt_av_id = main_val.av_id) "
        + " left outer join avalues ref on (main_ref.avr_value = ref.av_ao_id "
        + " AND ta_shared2 = ref.av_oa_id) left outer join "
        + " avalues_char ref_char on (ref_char.avc_av_id = ref.av_id)"
        + " WHERE main_val.av_ao_id = ao_id AND ta_id in (" + listRequest.getAttributesAsString() + ") " + hlpFilter[1]
        + " ORDER BY main_val.av_ao_id, av_rank, main_char.avc_lang_id, ref_char.avc_lang_id";

    ResultSet rs = stmt.executeQuery(query);
    Map<Integer, Map<Integer, String>> result = getRows(rs, listRequest.getLangId());
    rs.close();
    stmt.close();
    return result;
  }

  private static String[] getTablesAndWheresForFilter(List<FilterAttribute> filter, int filterOffset) {
    String[] result = new String[2];

    result[0] = "";
    result[1] = "";
    if (filter != null) {
      StringBuffer tables = new StringBuffer();
      StringBuffer wheres = new StringBuffer();

      int i = filterOffset;
      for (FilterAttribute filterAttribute : filter) {
        tables.append(", avalues av");
        tables.append(i);
        wheres.append(" AND av");
        wheres.append(i);
        wheres.append(".av_ao_id = pa_ao_id AND av");
        wheres.append(i);
        wheres.append(".av_oa_id = ");
        wheres.append(filterAttribute.getAttributeId());

        tables.append(", avalues_ref avr");
        tables.append(i);
        wheres.append(" AND avr");
        wheres.append(i);
        wheres.append(".avr_av_id = av");
        wheres.append(i);
        wheres.append(".av_id AND avr");
        wheres.append(i);
        wheres.append(".avr_value = ");
        wheres.append(filterAttribute.getValueId());

        i++;
      }
      result[0] = tables.toString();
      result[1] = wheres.toString();
    }
    return result;
  }

  private static Map<Integer, Map<Integer, String>> getRows(ResultSet rs, int langId) throws SQLException {
    Map<Integer, Map<Integer, String>> result = new LinkedHashMap<Integer, Map<Integer, String>>();

    List<StoreDB.AValue> values = null;
    StoreDB.AObject aObject = null;
    StoreDB.AValue valuePrev = null;
    boolean isDescendingPrev = false;
    while (rs.next()) {
      if (aObject == null || rs.getInt(StoreDB.AObject.ID) != aObject.getId()) {
        if (values != null) {
          if (isDescendingPrev)
            values.add(valuePrev);
          result.put(aObject.getId(), convert(values));
        }
        values = new ArrayList<StoreDB.AValue>();
        aObject = new StoreDB.AObject(rs, false, false);
        isDescendingPrev = false;
        valuePrev = null;
      }
      StoreDB.AValue value = new StoreDB.AValue(rs);
      // nasty hack
      value.setUserId(rs.getInt(TA_ID));
      boolean isDescending = (rs.getInt(TA_FLAGS) & (1<< StoreDB.TemplateAttribute.FLAG_DESCENDANT)) > 0;
      if (value.getId() > 0) {
        if (rs.getString("ao_leaf") != null) {
          value.setValueString(rs.getString("ao_leaf"));
          value.setLangId(rs.getInt(StoreDB.AValue.AVC_LANG_ID));
        }

        if (valuePrev != null
            && valuePrev.getOaId() == value.getOaId()
            && valuePrev.getRank() == value.getRank()
            && value.getLangId() != langId)
          value = valuePrev;

        if (isDescendingPrev) {
          if (valuePrev != null && valuePrev.getOaId() != value.getOaId())
            values.add(valuePrev);
        } else if (!isDescending) {
          if (valuePrev == null || valuePrev.getOaId() != value.getOaId())
            values.add(value);
          else if (valuePrev.getOaId() == value.getOaId()
              && valuePrev.getRank() == value.getRank()
              && value.getLangId() == langId) {
            values.remove(values.size()-1);
            values.add(value);
          }
        }
      } else
        values.add(value);
      valuePrev = value;
      isDescendingPrev = isDescending;
    }
    if (isDescendingPrev)
      values.add(valuePrev);
    result.put(aObject.getId(), convert(values));

    return result;
  }

  private static Map<Integer, String> convert(List<StoreDB.AValue> values) {
    Map<Integer, String> result = new HashMap<Integer, String>();
    for(StoreDB.AValue value : values) {
      if (value.getValueString() != null) {
        result.put(value.getUserId(), value.getValueString());
      } else if (value.getValueDate() != null) {
        result.put(value.getUserId(), value.getValueDate().toString());
      } else if (value.getValueDouble() != null) {
        result.put(value.getUserId(), value.getValueDouble().toString());
      } else if (value.getValueTimestamp() != null) {
        result.put(value.getUserId(), value.getValueTimestamp().toString());
      }
    }

    return result;
  }
}
