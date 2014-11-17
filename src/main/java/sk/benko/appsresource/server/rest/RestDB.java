package sk.benko.appsresource.server.rest;

import com.google.appengine.api.rdbms.AppEngineDriver;
import org.apache.log4j.Logger;
import sk.benko.appsresource.server.StoreDB;
import sk.benko.appsresource.shared.domain.CountRequest;
import sk.benko.appsresource.shared.domain.DetailRequest;
import sk.benko.appsresource.shared.domain.FilterAttribute;
import sk.benko.appsresource.shared.domain.ListRequest;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 24.4.2013
 * Time: 17:56
 * <p/>
 * Class which provides database operations
 */
public class RestDB {

  public class Api {
    private static final String URL = "jdbc:google:rdbms://appsresource:appsresource/appsresource";
    private static final String USER = "appsres";
    private static final String PASSWORD = "appsres";

    private Connection connection;

    private Api() {
      connection = getConnection();
    }

    private Connection getConnection() {
      try {
        DriverManager.registerDriver(new AppEngineDriver());

        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
      } catch (SQLException ex) {
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
      return RestDB.getCount(connection, countRequest);
    }

    public Map<Integer, Map<String, List<String>>> getList(ListRequest listRequest) throws SQLException {
      return RestDB.getList(connection, listRequest);
    }

    public Map<Integer, Map<String, List<String>>> getDetail(DetailRequest detailRequest) throws SQLException {
      return RestDB.getDetail(connection, detailRequest);
    }

  }

  private static final Logger log = Logger.getLogger(RestDB.class.getName());

  public static final String TA_ID = "ta_id";
  public static final String TA_FLAGS = "ta_flags";

  /**
   * Starts a data store session and returns an Api object to use.
   *
   * @return <code>Api</code>
   */
  public Api getApi() {
    return new Api();
  }

  static Integer getCount(Connection c, CountRequest countRequest) throws SQLException {
    Integer result = 0;

    Statement stmt = c.createStatement();
    String[] hlpFilter = getTablesAndWheresForFilter(countRequest.getFilterAttributes(), 0, true);

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


  static Map<Integer, Map<String, List<String>>> getList(Connection c, ListRequest listRequest) throws SQLException {
    Statement stmt = c.createStatement();
    String[] hlpFilter = getTablesAndWheresForFilter(listRequest.getFilterAttributes(), 0, false);

    String query = "SELECT distinct ao_id, ao_ot_id, ao_user_id, ao_lastupdateat, "
            + " ta_id, ta_flags, main_val.av_id, main_val.av_ao_id, "
            + " main_val.av_oa_id, main_val.av_rank, main_val.av_lastupdateat, "
            + " main_char.avc_value, main_text.avt_value, main_text.avt_lang_id, "
            + " CASE WHEN main_ref.avr_av_id is NULL THEN main_char.avc_lang_id ELSE ref_char.avc_lang_id END as avc_lang_id, "
            + " avd_value, avn_value, main_ref.avr_value, main_val.av_user_id, "
            + " ref.av_ao_id as ao, ref.av_oa_id as oa, ref_char.avc_value as ao_leaf "
            + " FROM aobjects " + hlpFilter[0] + ", template_attributes left outer join "
            + " avalues main_val on (main_val.av_oa_id = ta_oa_id) left outer join "
            + " avalues_char main_char on (main_char.avc_av_id = main_val.av_id) left outer join "
            + " avalues_date on (avd_av_id = main_val.av_id) left outer join "
            + " avalues_number on (avn_av_id = main_val.av_id) left outer join "
            + " avalues_ref main_ref on (main_ref.avr_av_id = main_val.av_id) left outer join "
            + " avalues_text main_text on (main_text.avt_av_id = main_val.av_id) "
            + " left outer join avalues ref on (main_ref.avr_value = ref.av_ao_id "
            + " AND ta_shared2 = ref.av_oa_id) left outer join "
            + " avalues_char ref_char on (ref_char.avc_av_id = ref.av_id)"
            + " WHERE main_val.av_ao_id = ao_id AND ta_id in (" + listRequest.getAttributesAsString() + ") " + hlpFilter[1]
            + " ORDER BY main_val.av_ao_id, ta_id, av_rank, main_char.avc_lang_id, ref_char.avc_lang_id";

    ResultSet rs = stmt.executeQuery(query);
    Map<Integer, Map<String, List<String>>> result = getRows(rs, listRequest.getLangId());
    rs.close();
    stmt.close();
    return result;
  }

  static Map<Integer, Map<String, List<String>>> getDetail(Connection c, DetailRequest detailRequest) throws SQLException {
    Statement stmt = c.createStatement();

    String query = "SELECT distinct ao_id, ao_ot_id, ao_user_id, ao_lastupdateat, "
            + " ta_id, ta_flags, main_val.av_id, main_val.av_ao_id, "
            + " main_val.av_oa_id, main_val.av_rank, main_val.av_lastupdateat, "
            + " main_char.avc_value, main_text.avt_value, main_text.avt_lang_id, "
            + " CASE WHEN main_ref.avr_av_id is NULL THEN main_char.avc_lang_id ELSE ref_char.avc_lang_id END as avc_lang_id, "
            + " avd_value, avn_value, main_ref.avr_value, main_val.av_user_id, "
            + " ref.av_ao_id as ao, ref.av_oa_id as oa, ref_char.avc_value as ao_leaf "
            + " FROM aobjects, template_attributes left outer join "
            + " avalues main_val on (main_val.av_oa_id = ta_oa_id) left outer join "
            + " avalues_char main_char on (main_char.avc_av_id = main_val.av_id) left outer join "
            + " avalues_date on (avd_av_id = main_val.av_id) left outer join "
            + " avalues_number on (avn_av_id = main_val.av_id) left outer join "
            + " avalues_ref main_ref on (main_ref.avr_av_id = main_val.av_id) left outer join "
            + " avalues_text main_text on (main_text.avt_av_id = main_val.av_id) "
            + " left outer join avalues ref on (main_ref.avr_value = ref.av_ao_id "
            + " AND ta_shared2 = ref.av_oa_id) left outer join "
            + " avalues_char ref_char on (ref_char.avc_av_id = ref.av_id)"
            + " WHERE ao_id = " + detailRequest.getObjectId() + " AND main_val.av_ao_id = ao_id"
            + " ORDER BY main_val.av_ao_id, ta_id, av_rank, main_char.avc_lang_id, ref_char.avc_lang_id";

    ResultSet rs = stmt.executeQuery(query);
    Map<Integer, Map<String, List<String>>> result = getRows(rs, detailRequest.getLangId());
    rs.close();
    stmt.close();
    return result;
  }

  private static String[] getTablesAndWheresForFilter(List<FilterAttribute> filter, int filterOffset, boolean isCount) {
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
        wheres.append(".av_ao_id = " + (isCount ? "pa_" : "") + "ao_id AND av");
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

  private static Map<Integer, Map<String, List<String>>> getRows(ResultSet rs, int langId) throws SQLException {
    Map<Integer, Map<String, List<String>>> result = new LinkedHashMap<Integer, Map<String, List<String>>>();

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
      boolean isDescending = (rs.getInt(TA_FLAGS) & (1 << StoreDB.TemplateAttribute.FLAG_DESCENDANT)) > 0;
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
          if (valuePrev == null || valuePrev.getOaId() != value.getOaId()) {
            values.add(value);
          } else if (valuePrev.getOaId() == value.getOaId()
                  && valuePrev.getRank() == value.getRank()
                  && value.getLangId() == langId) {
            values.remove(values.size() - 1);
            values.add(value);
          } else if (valuePrev.getOaId() == value.getOaId()
                  && valuePrev.getRank() != value.getRank()) {
            values.add(value);
          }
        }
      } else
        values.add(value);
      valuePrev = value;
      isDescendingPrev = isDescending;
    }
    if (aObject != null) {
      if (isDescendingPrev)
        values.add(valuePrev);
      result.put(aObject.getId(), convert(values));
    }

    return result;
  }

  private static Map<String, List<String>> convert(List<StoreDB.AValue> values) {
    Map<String, List<String>> result = new HashMap<String, List<String>>();
    for (StoreDB.AValue value : values) {
      List<String> resultValues = new ArrayList<String>();
      if (value.getValueString() != null) {
        resultValues.add(value.getValueString());
      } else if (value.getValueDate() != null) {
        resultValues.add(value.getValueDate().toString());
      } else if (value.getValueDouble() != null) {
        resultValues.add(value.getValueDouble().toString());
      } else if (value.getValueTimestamp() != null) {
        resultValues.add(value.getValueTimestamp().toString());
      }
      resultValues.add("" + value.getValueRef());
      resultValues.add("" + value.getLangId());
      result.put(""+value.getUserId()+"_"+value.getRank(), resultValues);
    }

    return result;
  }
}
