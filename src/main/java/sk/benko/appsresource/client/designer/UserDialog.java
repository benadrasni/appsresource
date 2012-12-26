package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.model.UserModel;

/**
 * A generic widget to display designer dialog.
 *
 */
public abstract class UserDialog extends CommonDialog {
  private UserModel model;

  /**
   * 
   */
  public UserDialog(UserModel model) {
    setModel(model);
  }

  // getters and setters

  /**
   * @return the model
   */
  public UserModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(UserModel model) {
    this.model = model;
  }
}
