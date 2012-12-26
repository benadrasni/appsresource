package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationTemplate;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A widget to display object type in table row.
 *
 */
public class ApplicationTemplateRowView extends FlexTable  {
  private ApplicationTemplate appt;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ApplicationTemplateRowView(final ApplicationTemplate at, String style) {
    setStyleName(style);
    setAppt(at);
  }
  
  public void generateWidgetTree(final ApplicationModel model) {
    Label lblName = new Label(getAppt().getT().getName());
    setWidget(0, 0, lblName);
    if (getAppt().getT().getOtId() == 0)
      getCellFormatter().addStyleName(0, 0, ClientUtils.CSS_DISABLED);
    else
      lblName.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          model.setAppt(getAppt());
          ObjectView ov = new ObjectView(model);
          ov.initialize();
          Document.get().getElementById("appcontent").removeFromParent();
          final RootPanel root = RootPanel.get();
          root.add(ov);
        }
      });
  }

  public void generateWidgetDefTree() {
    Label lblName = new Label(getAppt().getT().getName());
    setWidget(0, 0, lblName);
    if (getAppt().getT().getOtId() > 0) {
      CheckBox cbPublicData = new CheckBox(Main.constants.applicationPublicData());
      cbPublicData.setValue(getAppt().isPublicData());
      setWidget(0, 1, cbPublicData);
      getCellFormatter().addStyleName(0, 1, "td-right");
    }
  }

  /**
   * @return the appt
   */
  public ApplicationTemplate getAppt() {
    return appt;
  }

  /**
   * @param appt the appt to set
   */
  public void setAppt(ApplicationTemplate appt) {
    this.appt = appt;
  }
}
