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
 * <p/>
 * Get attributes of object of given id
 */
@XmlRootElement
@JsonIgnoreProperties
public class DetailRequest {

  private Integer langId;
  private Integer objectId;

  public DetailRequest() {}

  public DetailRequest(int langId, int objectId) {
    this.langId = langId;
    this.objectId = objectId;
  }

  public Integer getLangId() {
    return langId;
  }

  public Integer getObjectId() {
    return objectId;
  }

  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();

    result.append("langId=" + langId + "; ");
    result.append("objectId=" + objectId);

    return result.toString();
  }
}
