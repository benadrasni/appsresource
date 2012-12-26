package sk.benko.appsresource.server;

import java.text.SimpleDateFormat;

public class ServerUtils {
  
  /**
   * Creates new date formatter with format string initialized to MySQL
   * date format.
   * 
   * @return The newly created date formatter.
   */
  public static SimpleDateFormat MYSQLDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  /**
   * Helper function for filtering user input strings for special characters
   * that cannot be inserted into mysql database directly in strings.
   */
  public static String mySQLFilter(String s) {
    if (s == null)
      s = "";
    StringBuffer filtered = new StringBuffer(s.length());
    char c;
    for (int i = 0; i < s.length(); i++) {
      c = s.charAt(i);
      switch (c) {
      case '\'':
        filtered.append("''");
        break;
      //        case '"': filtered.append("\\\""); break;
      case '\\':
        filtered.append("\\\\");
        break;
      default:
        filtered.append(c);
      }
    }
    return filtered.toString();
  }
}
