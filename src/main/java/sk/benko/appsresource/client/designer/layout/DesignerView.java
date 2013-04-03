package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.Track;
import sk.benko.appsresource.client.designer.*;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the UI associated with the designer.
 *
 */
public class DesignerView extends FlowPanel {

  private final DesignerModel designerModel;

  private ApplicationDialog applicationDialog;
  private TemplateDialog templateDialog;
  private TemplateGroupDialog templateGroupDialog;
  private TemplateAttributeDialog templateAttributeDialog;
  private TemplateRelationDialog templateRelationDialog;
  private ObjectTypeDialog objectTypeDialog;
  private ObjectAttributeDialog objectAttributeDialog;
  private ObjectRelationDialog objectRelationDialog;
  private ValueTypeDialog valueTypeDialog;
  private UnitDialog unitDialog;

  /**
   * @param designerModel
   *          the model to which the UI will bind itself
   */
  public DesignerView(final DesignerModel designerModel) {
    getElement().setId(CSSConstants.CSS_APPLICATION);
    this.designerModel = designerModel;
    
    add(new DesignerHeaderView());
    add(new DesignerActionView(this));

    Track.track("designer");
  }

  public DesignerModel getDesignerModel() {
    return designerModel;
  }

  @Override
  public void onUnload() {
    if (applicationDialog != null) {
      designerModel.removeDataObserver(applicationDialog);
      designerModel.removeTemplateObserver(applicationDialog);
    }
    if (templateDialog != null) {
      designerModel.removeObjectTypeObserver(templateDialog);
      designerModel.removeObjectAttributeObserver(templateDialog);
      designerModel.removeTemplateAttributeObserver(templateDialog);
      designerModel.removeDataObserver(templateDialog);
    }
    if (templateGroupDialog != null) {
      designerModel.removeDataObserver(templateGroupDialog);
    }
    if (templateAttributeDialog != null) {
      designerModel.removeTemplateGroupObserver(templateAttributeDialog);
      designerModel.removeObjectAttributeObserver(templateAttributeDialog);
    }
    if (templateRelationDialog != null) {
      designerModel.removeDataObserver(templateRelationDialog);
      designerModel.removeObjectRelationObserver(templateRelationDialog);
    }
    if (objectTypeDialog != null) {
      designerModel.removeObjectTypeObserver(objectTypeDialog);
    }
    if (objectAttributeDialog != null) {
      designerModel.removeObjectTypeObserver(objectAttributeDialog);
      designerModel.removeValueTypeObserver(objectAttributeDialog);
      designerModel.removeUnitObserver(objectAttributeDialog);
      designerModel.removeObjectRelationObserver(objectAttributeDialog);
    }
    if (objectRelationDialog != null) {
      designerModel.removeObjectTypeObserver(objectRelationDialog);
      designerModel.removeObjectRelationObserver(objectRelationDialog);
    }
  }

  public ApplicationDialog getApplicationDialog() {
    if (applicationDialog == null) {
      applicationDialog = new ApplicationDialog(this);

      designerModel.addDataObserver(applicationDialog);
      designerModel.addTemplateObserver(applicationDialog);
    }
    return applicationDialog;
  }

  public TemplateDialog getTemplateDialog() {
    if (templateDialog == null) {
      templateDialog = new TemplateDialog(this);

      designerModel.addObjectTypeObserver(templateDialog);
      designerModel.addObjectAttributeObserver(templateDialog);
      designerModel.addTemplateAttributeObserver(templateDialog);
      designerModel.addDataObserver(templateDialog);
    }
    return templateDialog;
  }

  public TemplateGroupDialog getTemplateGroupDialog() {
    if (templateGroupDialog == null) {
      templateGroupDialog = new TemplateGroupDialog(this);

      designerModel.addDataObserver(templateGroupDialog);
    }
    return templateGroupDialog;
  }

  public TemplateAttributeDialog getTemplateAttributeDialog() {
    if (templateAttributeDialog == null) {
      templateAttributeDialog = new TemplateAttributeDialog(this);

      designerModel.addTemplateGroupObserver(templateAttributeDialog);
      designerModel.addObjectAttributeObserver(templateAttributeDialog);
    }
    return templateAttributeDialog;
  }

  public TemplateRelationDialog getTemplateRelationDialog() {
    if (templateRelationDialog == null) {
      templateRelationDialog = new TemplateRelationDialog(this);

      designerModel.addDataObserver(templateRelationDialog);
      designerModel.addObjectRelationObserver(templateRelationDialog);
    }
    return templateRelationDialog;
  }

  public ObjectTypeDialog getObjectTypeDialog() {
    if (objectTypeDialog == null) {
      objectTypeDialog = new ObjectTypeDialog(this);

      designerModel.addObjectTypeObserver(objectTypeDialog);
    }
    return objectTypeDialog;
  }

  public ObjectAttributeDialog getObjectAttributeDialog() {
    if (objectAttributeDialog == null) {
      objectAttributeDialog = new ObjectAttributeDialog(this);

      designerModel.addObjectTypeObserver(objectAttributeDialog);
      designerModel.addValueTypeObserver(objectAttributeDialog);
      designerModel.addUnitObserver(objectAttributeDialog);
      designerModel.addObjectRelationObserver(objectAttributeDialog);
    }
    return objectAttributeDialog;
  }

  public ObjectRelationDialog getObjectRelationDialog() {
    if (objectRelationDialog == null) {
      objectRelationDialog = new ObjectRelationDialog(this);

      designerModel.addObjectTypeObserver(objectRelationDialog);
      designerModel.addObjectRelationObserver(objectRelationDialog);
    }
    return objectRelationDialog;
  }

  public ValueTypeDialog getValueTypeDialog() {
    if (valueTypeDialog == null) {
      valueTypeDialog = new ValueTypeDialog(this);
    }
    return valueTypeDialog;
  }

  public UnitDialog getUnitDialog() {
    if (unitDialog == null) {
      unitDialog = new UnitDialog(this);
    }
    return unitDialog;
  }
}
