package sk.benko.appsresource.shared.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 16.9.2013
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@JsonIgnoreProperties
public class ListResult {
  Map<Integer, Map<Integer, String>> results;


}
