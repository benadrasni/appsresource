package sk.benko.appsresource.client;

public class Track {

  /**
   * constructor - nothing to do
   */
  public Track() {
  }

  /**
   * track an event
   * 
   * @param historyToken
   */
  public static void track(String historyToken) {

    if (historyToken == null) {
      historyToken = "historyToken_null";
    }

    historyToken = "/appsresource/" + historyToken;

    trackGoogleAnalytics(historyToken);

  }

  /**
   * trigger google analytic native js - included in the build CHECK -
   * DemoGoogleAnalytics.gwt.xml for -> <script src="../ga.js"/>
   * 
   * http://code.google.com/intl/en-US/apis/analytics/docs/gaJS/gaJSApiEventTracking.html
   * 
   * @param historyToken
   */
  public static native void trackGoogleAnalytics(String historyToken) /*-{

    try {

      // setup tracking object with account
      var pageTracker = $wnd._gat._getTracker("UA-30298207-1");

      pageTracker._setRemoteServerMode();

      // turn on anchor observing
      pageTracker._setAllowAnchor(true);

      // send event to google server
      pageTracker._trackPageview(historyToken);

    } catch (err) {
      // debug
      // alert('FAILURE: to send in event to google analytics: ' + err);
    }

  }-*/;
}
