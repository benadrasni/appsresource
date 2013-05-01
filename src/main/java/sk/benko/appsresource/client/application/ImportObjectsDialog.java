package sk.benko.appsresource.client.application;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.designer.WidthUnitListBox;
import sk.benko.appsresource.client.layout.ButtonView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TreeLevel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * A generic widget to display designer dialog.
 */
public class ImportObjectsDialog extends PopupPanel implements ApplicationModel.ObjectsObserver {

  private static int ROWS = 5;
  final AbsolutePanel main = new AbsolutePanel();
  final ButtonView bImport = new ButtonView(ClientUtils.CSS_BUTTON + " "
      + ClientUtils.CSS_DIALOG_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTONOK);
  final ButtonView bCancel = new ButtonView(ClientUtils.CSS_BUTTON + " "
      + ClientUtils.CSS_DIALOG_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTONCANCEL);
  ObjectView objectView;
  String filename;
  String importHeader;
  ChooseFileButton chooseButton = new ChooseFileButton();
  CustomProgress progress = new CustomProgress();
  UploadButton uploadButton = new UploadButton();
  SingleUploader uploader = new SingleUploader(FileInputType.CUSTOM.with(chooseButton),
      progress, uploadButton);
  HashMap<Integer, TemplateAttribute> tas = new HashMap<Integer, TemplateAttribute>();
  HorizontalPanel header = new HorizontalPanel();
  Label x = new Label(ClientUtils.CLOSE_CHAR);
  FlowPanel body = new FlowPanel();
  FlowPanel bodyContent = new FlowPanel();
  FlowPanel bodyBottom = new FlowPanel();
  FlowPanel bodyButtons = new FlowPanel();
  FlexTable table = new FlexTable();
  private int attributeCount = 0;
  private int importCount = 0;
  private CheckBox cbOnlyUpdate;
  private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
    public void onFinish(IUploader uploader) {
      if (uploader.getStatus() == Status.SUCCESS) {
        importHeader = uploader.getServerInfo().message;
        filename = uploader.getServerInfo().name;

        if (importHeader != null) {

          String[] columns = importHeader.split(",");
          attributeCount = columns.length;

          int offset = -3;
          for (int i = 0; i < attributeCount; i++) {
            if (i % ROWS == 0) {
              offset = offset + 3;
              table.setWidget(0, offset + 2, new Label(Main.constants.key()));
            }

            String column = columns[i];
            table.setWidget(i % ROWS + 1, offset, new Label(column));

            ListBox lbTa = new ListBox();
            lbTa.addItem("", "0");
            lbTa.setWidth("100%");

            if (tas != null)
              for (TemplateAttribute tai : tas.values()) {
                if (tai.getOaId() > 0) {
                  lbTa.addItem(tai.getName(), "" + tai.getId());
                  if (tai.getName().equals(column))
                    lbTa.setSelectedIndex(lbTa.getItemCount() - 1);
                }
              }
            table.setWidget(i % ROWS + 1, offset + 1, lbTa);

            CheckBox cbKey = new CheckBox();
            cbKey.addClickHandler(new ClickHandler() {

              @Override
              public void onClick(ClickEvent event) {
                if (isKeyChecked())
                  bImport.removeStyleDependentName(CSSConstants.SUFFIX_DISABLED);
                else
                  bImport.addStyleDependentName(CSSConstants.SUFFIX_DISABLED);

              }
            });
            table.setWidget(i % ROWS + 1, offset + 2, cbKey);

          }
          cbOnlyUpdate = new CheckBox(Main.constants.onlyupdate());
          bodyContent.add(cbOnlyUpdate);

          bodyContent.add(table);
        } else
          bodyContent.add(new Label(Main.messages.noValidRows()));
      }
    }
  };

  /**
   *
   */
  public ImportObjectsDialog(final ObjectView objectView) {
    setStyleName(ClientUtils.CSS_DIALOGBOX);
    this.objectView = objectView;

    // Enable glass background.
    setGlassStyleName(ClientUtils.CSS_DIALOGBOX_GLASS);
    setGlassEnabled(true);


    // Set the dialog box's header.
    header.setStyleName(ClientUtils.CSS_DIALOGBOX_HEADER);
    header.add(uploader);
    header.add(uploadButton);

    main.add(header);
    x.setStyleName(ClientUtils.CSS_DIALOGBOX_X);
    main.add(x);

    // Set the dialog box's body.
    int width = Window.getClientWidth() * 2 / 3;
    int height = Window.getClientHeight() * 2 / 3;
    body.setHeight(height + WidthUnitListBox.WIDTH_UNITPX);
    body.setWidth(width + WidthUnitListBox.WIDTH_UNITPX);
    body.setStyleName(ClientUtils.CSS_DIALOGBOX_BODY);

    bodyContent.setStyleName(ClientUtils.CSS_IMPORTDIALOGBOX_CONTENT);
    bodyBottom.setStyleName(ClientUtils.CSS_IMPORTDIALOGBOX_BOTTOM);
    bodyButtons.setStyleName(ClientUtils.CSS_IMPORTDIALOGBOX_BUTTONS);

    bodyButtons.add(bCancel);
    bodyButtons.add(bImport);
    bodyBottom.add(bodyButtons);

    body.add(bodyContent);
    body.add(bodyBottom);
    main.add(body);

    add(main);

    setPopupPositionAndShow(new PopupPanel.PositionCallback() {
      public void setPosition(int offsetWidth, int offsetHeight) {
        int left = (Window.getClientWidth() - offsetWidth) / 2;
        int top = (Window.getClientHeight() - offsetHeight) / 2;
        setPopupPosition(left, top);
      }
    });

    initializeImport();
    uploader.addOnFinishUploadHandler(onFinishUploaderHandler);
  }

  private void initializeImport() {
    progress.setFileName(Main.messages.noFileChosen());
    filename = "";
    importHeader = "";
    uploader.reset();
    uploader.setServletPath(uploader.getServletPath()
        + "?template=" + getModel().getAppt().getTId()
        + "&app=" + getModel().getAppt().getAppId()
        + "&user=" + getModel().getAppu().getUserId());

    x.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        ImportObjectsDialog.this.hide();
      }
    });

    // load template filterAttributes for actual template
    tas.clear();
    for (TemplateAttribute ta : getModel().getAttrsByTemplate()
        .get(getModel().getAppt().getTId())) {
      tas.put(ta.getId(), ta);
    }

    // buttons
    bCancel.addDomHandler(
        new

            ClickHandler() {
              public void onClick(ClickEvent event) {
                ImportObjectsDialog.this.hide();
              }
            }, ClickEvent.getType());
    bCancel.getElement().setInnerText(Main.constants.cancel());

    bImport.addDomHandler(
        new

            ClickHandler() {
              public void onClick(ClickEvent event) {
                bImport.addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
                Main.status.showTaskStatus(Main.constants.importing());
                importObjects();
                //ImportObjectsDialog.this.hide();
              }
            }, ClickEvent.getType());
    bImport.getElement().setInnerText(Main.constants.importFile());
    bImport.addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
  }

  private void importObjects() {
    HashMap<Integer, TemplateAttribute> map = new LinkedHashMap<Integer, TemplateAttribute>();
    HashMap<Integer, TemplateAttribute> keys = new HashMap<Integer, TemplateAttribute>();

    int offset = -3;
    for (int i = 0; i < attributeCount; i++) {
      if (i % ROWS == 0) offset = offset + 3;
      ListBox lb = (ListBox) table.getWidget(i % ROWS + 1, offset + 1);
      int taId = Integer.parseInt(lb.getValue(lb.getSelectedIndex()));
      if (taId > 0) {
        map.put(i, tas.get(taId));
        if (((CheckBox) table.getWidget(i % ROWS + 1, offset + 2)).getValue())
          keys.put(i, tas.get(taId));
      }
    }
    getModel().importObjects(
        getModel().getAppt().getApp(), getModel().getAppt().getT(),
        filename, map, keys, cbOnlyUpdate.getValue());
  }

  @Override
  public void onObjectsLoaded(TreeItem ti, List<AObject> objects) {
  }

  @Override
  public void onTreeLevelLoaded(TreeItem ti, TemplateAttribute ta, List<TreeLevel> items) {
  }

  @Override
  public void onObjectsImported(int count) {
    importCount += count;
    if (count == 0) {
      Main.status.hideTaskStatus();
      bodyContent.clear();
      bodyContent.add(new Label(Main.messages.objectsImported(importCount)));
    } else
      importObjects();
  }

  public void onLoad() {
    this.objectView.getModel().addDataObserver(this);
  }

  public void onUnload() {
    this.objectView.getModel().removeDataObserver(this);
  }

  @Override
  public void onObjectsRemoved(int count) {
  }

  private boolean isKeyChecked() {
    boolean result = false;
    int offset = -3;
    for (int i = 0; i < attributeCount; i++) {
      if (i % ROWS == 0) offset = offset + 3;
      result = result || ((CheckBox) table.getWidget(i % ROWS + 1, offset + 2)).getValue();
    }
    return result;
  }

  /**
   * @return the objectView
   */
  public ObjectView getObjectView() {
    return objectView;
  }

  /**
   * @param objectView the objectView to set
   */
  public void setObjectView(ObjectView objectView) {
    this.objectView = objectView;
  }

  /**
   * @return the applicationModel
   */
  public ApplicationModel getModel() {
    return getObjectView().getModel();
  }

  public class ChooseFileButton extends ButtonView implements HasClickHandlers {

    public ChooseFileButton() {
      super(ClientUtils.CSS_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTON);
      getElement().setInnerText(Main.constants.chooseFile());
    }
  }

  public class UploadButton extends ButtonView implements HasClickHandlers {

    public UploadButton() {
      super(ClientUtils.CSS_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTON);
      getElement().setInnerText(Main.constants.uploadFile());
    }
  }

  public class CustomProgress implements IUploadStatus {
    Status status;
    int percent = 0;
    Panel panel = new HorizontalPanel();
    Label fileNameLabel = new Label();
    Label statusLabel = new Label();

    {
      panel.add(fileNameLabel);
      panel.add(statusLabel);
      fileNameLabel.setStyleName(ClientUtils.CSS_FILENAME);
      //statusLabel.setStyleName("status");
    }

    @Override
    public void setProgress(int done, int total) {
    }

    @Override
    public HandlerRegistration addCancelHandler(UploadCancelHandler handler) {
      return null;
    }

    @Override
    public Status getStatus() {
      return status;
    }

    @Override
    public void setStatus(Status status) {
      this.status = status;
    }

    @Override
    public Widget getWidget() {
      return panel;
    }

    @Override
    public IUploadStatus newInstance() {
      return new CustomProgress();
    }

    @Override
    public void setCancelConfiguration(Set<CancelBehavior> config) {
    }

    @Override
    public void setError(String error) {
    }

    @Override
    public void setFileName(String name) {
      fileNameLabel.setText(name);
    }

    @Override
    public void setI18Constants(UploadStatusConstants strs) {
    }

    @Override
    public void setStatusChangedHandler(UploadStatusChangedHandler handler) {
    }

    @Override
    public void setVisible(boolean b) {
    }
  }
}
