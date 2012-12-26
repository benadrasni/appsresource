package sk.benko.appsresource.client.model.result;

import com.google.gwt.core.client.GWT;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.TreeLevel;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulates a response from {@link sk.benko.appsresource.client.model.ApplicationService#getObjects(int, int,
 * java.util.List, sk.benko.appsresource.client.model.TemplateAttribute)}.
 */
@SuppressWarnings("serial")
public class GetObjectsResult implements Serializable {
  private List<TreeLevel> path;
  private List<AObject> objects;

  /**
   * Constructs a new result. This constructor can only be invoked on the server.
   *
   * @param path    the path within a tree
   * @param objects the list of objects to return
   */
  public GetObjectsResult(List<TreeLevel> path, List<AObject> objects) {
    assert !GWT.isClient();
    this.path = path;
    this.objects = objects;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetObjectsResult() {
  }

  /**
   * Returns the objects that were returned by the server. This can be zero-length, but will not be null.
   *
   * @return the objects
   */
  public List<AObject> getObjects() {
    return objects;
  }

  /**
   * Returns the items that were returned by the server. This can be zero-length, but will not be null.
   *
   * @return path
   */
  public List<TreeLevel> getTreePath() {
    return path;
  }
}

