package sk.benko.appsresource.client.model;

import java.io.Serializable;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a template tree item.
 *
 */
@SuppressWarnings("serial")
public class TemplateListItem implements Serializable {

  public interface Observer {
    void onUpdate(TemplateListItem tli);
  }

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;

  /**
   * The template list to which the item belongs.
   */
  private int tlId;
  private TemplateList tl;

  /**
   * The template attribute to which the item belongs.
   */
  private int taId;
  private TemplateAttribute ta;

  /**
   * The rank of the template tree.
   */
  private int rank;

  /**
   * An observer to receive callbacks whenever this {@link TemplateListItem} is updated.
   */
  private transient Observer observer;

  /**
   * A constructor to be used on client-side only.
   *
   * @param tlId
   * @param taId
   */
  public TemplateListItem(int tlId, int taId) {
    assert GWT.isClient();
    this.tlId = tlId;
    this.taId = taId;
  }
  
  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param tlId
   * @param tl
   * @param taId
   * @param ta
   * @param rank
   */
  public TemplateListItem(int id, int tlId, TemplateList tl, int taId, 
      TemplateAttribute ta, int rank) {
    assert !GWT.isClient();
    this.id = id;
    this.tlId = tlId;
    this.tl = tl;
    this.taId = taId;
    this.ta = ta;
    this.rank = rank;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TemplateListItem() {
  }

  public int getId() {
    return id;
  }

  public int getTlId() {
    return tlId;
  }

  public void setTlId(int tlId) {
    this.tlId = tlId;
  }

  public TemplateList getTl() {
    return tl;
  }

  public void setTl(TemplateList tl) {
    this.tl = tl;
  }

  public int getTaId() {
    return taId;
  }

  public void setTaId(int taId) {
    this.taId = taId;
  }

  public TemplateAttribute getTa() {
    return ta;
  }

  public void setTa(TemplateAttribute ta) {
    this.ta = ta;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  /**
   * Gets the observer that is receiving notification when 
   * the template tree item is modified.
   *
   * @return
   */
  public Observer getObserver() {
    return observer;
  }

  /**
   * Sets the observer that will receive notification when this objectType is
   * modified.
   *
   * @param observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

}
