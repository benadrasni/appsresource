package sk.benko.appsresource.client;

public class DropDownObjectImpl implements DropDownObject {
  
  private int id;
  private String name;
  private Object userObject;

  public DropDownObjectImpl(int id, String name, Object userObject) {
    setId(id);
    setName(name);
    setUserObject(userObject);
  }

  public DropDownObjectImpl(int id, String name) {
    this(id, name, null);
  }
  
  /**
   * @return the id
   */
  public int getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the userObject
   */
  public Object getUserObject() {
    return userObject;
  }

  /**
   * @param userObject the userObject to set
   */
  public void setUserObject(Object userObject) {
    this.userObject = userObject;
  }
}
