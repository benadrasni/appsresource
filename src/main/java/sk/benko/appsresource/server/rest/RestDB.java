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

  public static final String TABLE_PARENTS = "aobject_parents";
  public static final String TABLE_VALUE = "avalues";
  public static final String TABLE_VALUE_CHAR = "avalues_char";
  public static final String TABLE_VALUE_DATE = "avalues_date";
  public static final String TABLE_VALUE_NUMBER = "avalues_number";
  public static final String TABLE_VALUE_TEXT = "avalues_text";
  public static final String TABLE_VALUE_REF = "avalues_ref";
  public static final String TABLE_TEMPLATE_ATTRIBUTE = "template_attributes";

  public static final String PARENTS_OT_ID = "pa_ot_id";
  public static final String PARENTS_AO_ID = "pa_ao_id";
  public static final String VALUE_ID = "av_id";
  public static final String VALUE_AO_ID = "av_ao_id";
  public static final String VALUE_OA_ID = "av_oa_id";
  public static final String VALUE_RANK = "av_rank";
  public static final String VALUE_CHAR_AV_ID = "avc_av_id";
  public static final String VALUE_CHAR_LANG_ID = "avc_lang_id";
  public static final String VALUE_CHAR_VALUE = "avc_value";
  public static final String VALUE_DATE_AV_ID = "avd_av_id";
  public static final String VALUE_DATE_VALUE = "avd_value";
  public static final String VALUE_NUMBER_AV_ID = "avn_av_id";
  public static final String VALUE_NUMBER_VALUE = "avn_value";
  public static final String VALUE_TEXT_AV_ID = "avt_av_id";
  public static final String VALUE_TEXT_VALUE = "avt_value";
  public static final String VALUE_REF_AV_ID = "avr_av_id";
  public static final String VALUE_REF_VALUE = "avr_value";
  public static final String TA_ID = "ta_id";
  public static final String TA_OA_ID = "ta_oa_id";
  public static final String TA_SHARED2 = "ta_shared2";
  public static final String TA_FLAGS = "ta_flags";

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


  private static Map<Integer, Map<Integer, String>> getList(Connection c, ListRequest listRequest)
      throws SQLException {
    Statement stmt = c.createStatement();
    String[] hlpFilter =  getTablesAndWheresForFilter(listRequest.getFilterAttributes(), 0);

    String query = "SELECT distinct ao_id, ao_ot_id, ao_user_id, ao_lastupdateat, "
        + " ta_id, ta_flags, main_val.av_id, main_val.av_ao_id, "
        + " main_val.av_oa_id, main_val.av_rank, main_val.av_lastupdateat, "
        + " main_char.avc_value, main_text.avt_value, main_text.avt_lang_id, "
        + " CASE WHEN main_ref.avr_av_id is NULL THEN main_char.avc_lang_id ELSE ref_char.avc_lang_id END as avc_lang_id, "
        + " avd_value, avn_value, main_ref.avr_value, main_val.av_user_id, "
        + " ref.av_ao_id as ao, ref.av_oa_id as oa, ref_char.avc_value as ao_leaf "
        + " FROM " + TABLE_TEMPLATE_ATTRIBUTE + hlpFilter[0] + " left outer join "
        + TABLE_VALUE + " main_val on (main_val." + VALUE_OA_ID + " = " + TA_OA_ID + ") left outer join "
        + TABLE_VALUE_CHAR + " main_char on (main_char." + VALUE_CHAR_AV_ID + " = main_val." + VALUE_ID + ") left outer join "
        + TABLE_VALUE_DATE + " on (" + VALUE_DATE_AV_ID + " = main_val." + VALUE_ID + ") left outer join "
        + TABLE_VALUE_NUMBER + " on (" + VALUE_NUMBER_AV_ID + " = main_val." + VALUE_ID + ") left outer join "
        + TABLE_VALUE_REF + " main_ref on (main_ref." + VALUE_REF_AV_ID + " = main_val." + VALUE_ID + ") left outer join "
        + TABLE_VALUE_TEXT + " main_text on (main_text." + VALUE_TEXT_AV_ID + " = main_val." + VALUE_ID + ") "
        + " left outer join " + TABLE_VALUE + " ref on (main_ref." + VALUE_REF_VALUE + " = ref." + VALUE_AO_ID
        + " AND " + TA_SHARED2 + " = ref." + VALUE_OA_ID + ") left outer join "
        + TABLE_VALUE_CHAR + " ref_char on (ref_char." + VALUE_CHAR_AV_ID + " = ref." + VALUE_ID + ")"
        + " WHERE " + TA_ID + " in (" + listRequest.getAttributesAsString() + ") " + hlpFilter[1]
        + " ORDER BY main_val." + VALUE_AO_ID + ", " + VALUE_RANK
        + ", main_char." + VALUE_CHAR_LANG_ID + ", ref_char." + VALUE_CHAR_LANG_ID;

    ResultSet rs = stmt.executeQuery(query);
    Map<Integer, Map<Integer, String>> result = getRows(rs, listRequest.getLangId());
    if (rs.next()) {

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
      StringBuffer tables = new StringBuffer();
      StringBuffer wheres = new StringBuffer();

      int i = filterOffset;
      for (FilterAttribute filterAttribute : filter) {
        tables.append(", " + TABLE_VALUE + " av");
        tables.append(i);
        wheres.append(" AND av");
        wheres.append(i);
        wheres.append("." + VALUE_AO_ID + "=" + PARENTS_AO_ID + " AND av");
        wheres.append(i);
        wheres.append("." + VALUE_OA_ID + "=");
        wheres.append(filterAttribute.getAttributeId());

        tables.append(", " + TABLE_VALUE_REF + " avr");
        tables.append(i);
        wheres.append(" AND avr");
        wheres.append(i);
        wheres.append("." + VALUE_REF_AV_ID + "= av");
        wheres.append(i);
        wheres.append("." + VALUE_ID + " AND avr");
        wheres.append(i);
        wheres.append("." + VALUE_REF_VALUE + "=");
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
    StoreDB.AObject aobject = null;
    StoreDB.AValue valuePrev = null;
    boolean isDescendingPrev = false;
    while (rs.next()) {
      if (aobject == null || rs.getInt(StoreDB.AObject.ID) != aobject.getId()) {
        if (values != null) {
          if (isDescendingPrev)
            values.add(valuePrev);
          result.put(aobject.getId(), convert(values));
        }
        values = new ArrayList<StoreDB.AValue>();
        aobject = new StoreDB.AObject(rs, false, false);
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
    result.put(aobject.getId(), convert(values));

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
