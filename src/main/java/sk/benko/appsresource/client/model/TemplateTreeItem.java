package sk.benko.appsresource.client.model;

import com.google.gwt.core.client.GWT;

import java.io.Serializable;

/**
 * A client side data object representing a template tree item.
 *
 */
@SuppressWarnings("serial")
public class TemplateTreeItem implements Serializable {

  public interface Observer {
    void onUpdate(TemplateTreeItem tti);
  }

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;

  /**
   * The template tree to which the item belongs.
   */
  private int ttId;
  private TemplateTree tt;

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
   * The id of template tree item which the TemplateTreeItem is subset of
   */
  private int subsetOf;

  /**
   * An observer to receive callbacks whenever this {@link TemplateTreeItem} is updated.
   */
  private transient Observer observer;

  /**
   * A constructor to be used on client-side only.
   *
   * @param ttId
   * @param taId
   */
  public TemplateTreeItem(int ttId, int taId) {
    assert GWT.isClient();
    this.ttId = ttId;
    this.taId = taId;
  }
  
  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param ttId
   * @param taId
   * @param rank
   */
  public TemplateTreeItem(int id, int ttId, TemplateTree tt, int taId, 
      TemplateAttribute ta, int rank, int subsetOf) {
    assert !GWT.isClient();
    this.id = id;
    this.ttId = ttId;
    this.tt = tt;
    this.taId = taId;
    this.ta = ta;
    this.rank = rank;
    this.subsetOf = subsetOf;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TemplateTreeItem() {
  }

  public int getId() {
    return id;
  }

  public int getTtId() {
    return ttId;
  }

  public void setTtId(int ttId) {
    this.ttId = ttId;
  }

  public TemplateTree getTt() {
    return tt;
  }

  public void setTt(TemplateTree tt) {
    this.tt = tt;
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

  public int getSubsetOf() {
    return subsetOf;
  }

  public void setSubsetOf(int subsetOf) {
    this.subsetOf = subsetOf;
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
