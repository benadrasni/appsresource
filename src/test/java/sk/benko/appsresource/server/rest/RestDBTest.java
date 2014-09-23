package sk.benko.appsresource.server.rest;

import junit.framework.TestCase;
import org.junit.Test;
import sk.benko.appsresource.shared.domain.FilterAttribute;
import sk.benko.appsresource.shared.domain.ListRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 20.11.2013
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class RestDBTest extends TestCase {

  private static final String URL = "jdbc:mysql://localhost:3306/appsresource";
  private static final String USER = "appsres";
  private static final String PASSWORD = "appsres";

  private final RestDB store = new RestDB();

  @Test
  public void testGetList() {

    List<FilterAttribute> filterAttributes =  new ArrayList<FilterAttribute>();
    List<Integer> attributes = new ArrayList<Integer>();
    attributes.add(49);
    attributes.add(391);
    ListRequest listRequest = new ListRequest(0, filterAttributes, attributes, 0, 10);
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
      Map<Integer, Map<Integer, String>> result = store.getList(con, listRequest);
      assertNotNull(result.get(189375));
      assertNotNull(result.get(189440));
      assertNotNull(result.get(189447));
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
