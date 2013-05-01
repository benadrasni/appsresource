package sk.benko.appsresource.shared.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 18.4.2013
 * Time: 19:51
 *
 * Get count of objects of given type which match filter attributes
 */
@XmlRootElement
@JsonIgnoreProperties
public class CountRequest {

  public Integer objectTypeId;
  public List<FilterAttribute> filterAttributes;

  public CountRequest() {}

  public CountRequest(int objectTypeId, List<FilterAttribute> attributes) {
    this.objectTypeId = objectTypeId;
    this.filterAttributes =  (attributes != null) ? new ArrayList<FilterAttribute>(attributes) : null;
  }

  public Integer getObjectTypeId() {
    return objectTypeId;
  }

  public void setObjectTypeId(Integer objectTypeId) {
    this.objectTypeId = objectTypeId;
  }

  public List<FilterAttribute> getFilterAttributes() {
    return filterAttributes;
  }

  public void setFilterAttributes(List<FilterAttribute> filterAttributes) {
    this.filterAttributes = filterAttributes;
  }
}
