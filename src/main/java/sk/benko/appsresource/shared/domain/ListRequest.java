package sk.benko.appsresource.shared.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 16.9.2013
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@JsonIgnoreProperties
public class ListRequest {

  private Integer langId;
  private List<FilterAttribute> filterAttributes;
  private List<Integer> attributes;
  private Integer from;
  private Integer number;

  public ListRequest() {}

  public ListRequest(int langId, List<FilterAttribute> filterAttributes,
                     List<Integer> attributes, int from, int number) {
    this.langId = langId;
    this.filterAttributes =  (filterAttributes != null) ? new ArrayList<FilterAttribute>(filterAttributes) : null;
    this.attributes =  (attributes != null) ? new ArrayList<Integer>(attributes) : null;
    this.from = from;
    this.number = number;
  }

  public Integer getLangId() {
    return langId;
  }

  public List<FilterAttribute> getFilterAttributes() {
    return filterAttributes;
  }

  public List<Integer> getAttributes() {
    return attributes;
  }

  public Integer getFrom() {
    return from;
  }

  public Integer getNumber() {
    return number;
  }

  public String getAttributesAsString() {
    StringBuilder result = new StringBuilder();
    for (Integer attribute : attributes) {
      result.append(","+attribute);
    }
    return result.toString().substring(1);
  }
}
