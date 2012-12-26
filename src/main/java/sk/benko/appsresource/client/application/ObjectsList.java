package sk.benko.appsresource.client.application;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;

import java.util.ArrayList;
import java.util.List;

public class ObjectsList extends FlowPanel implements
    Model.TemplateListObserver,
    Model.TemplateListItemObserver,
    ChangeHandler {

  protected final static int PER_PAGE1 = 1;
  protected final static int PER_PAGE2 = 10;
  protected final static int PER_PAGE3 = 20;
  protected final static int PER_PAGE4 = 50;
  protected final static int PER_PAGE5 = 100;
  protected final static int PER_PAGE_ALL = 0;
  protected ApplicationModel model;
  protected Template t;
  protected int count;
  protected int from;
  protected int columns;
  FlowPanel toolbar;
  FlowPanel header;
  FlowPanel list;
  NavigationBox nb;
  Label lblCount;
  private DropDownBox ddbTemplate;
  private DropDownBox ddbPerPage;

  public ObjectsList(ApplicationModel model, Template t, int count) {
    this.model = model;
    this.t = t;
    this.count = count;
    this.from = 0;
    this.addDomHandler(this, ChangeEvent.getType());

    getElement().setId("" + this.t.getId());
    setStyleName(ClientUtils.CSS_OBJECTLIST);

    toolbar = new FlowPanel();
    header = new FlowPanel();
    list = new FlowPanel();

    nb = new NavigationBox();
    lblCount = new Label();

    add(toolbar);
    add(header);
    add(list);

    fillToolbar();

    TemplateListLoader tll = new TemplateListLoader(toString(), getModel(), this.t.getId());
    tll.start();
  }

  private void fillToolbar() {
    FlexTable toolbarTable = new FlexTable();
    toolbarTable.setStyleName(ClientUtils.CSS_OBJECTLIST_TOOLBAR);

    Label lblTemplate = new Label(t.getName());
    lblTemplate.setStyleName(ClientUtils.CSS_DROPDOWN_HEAD);
    toolbarTable.setWidget(0, 0, lblTemplate);
    toolbarTable.getCellFormatter().setWidth(0, 0, "150px");

    toolbarTable.setWidget(0, 1, getDdbTemplate());
    toolbarTable.getCellFormatter().setWidth(0, 1, "100px");

    nb.setStartClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        from = 0;
        loadObjects();
      }
    });
    nb.setBackwardClickHandler(new
                                   ClickHandler() {
                                     @Override
                                     public void onClick(ClickEvent event) {
                                       if (from > 0) {
                                         from = (from - getPerPage() > 0 ? from - getPerPage() : 0);
                                         loadObjects();
                                       }
                                     }
                                   });
    nb.setForwardClickHandler(new
                                  ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                      if (from + getPerPage() < count) {
                                        from += getPerPage();
                                        loadObjects();
                                      }
                                    }
                                  });
    nb.setEndClickHandler(new
                              ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                  from = count - count % getPerPage();
                                  if (from == count) from = count - getPerPage();
                                  loadObjects();
                                }
                              });
    toolbarTable.setWidget(0, 3, nb);
    toolbarTable.getCellFormatter().setWidth(0, 3, "200px");

    lblCount.setStyleName(ClientUtils.CSS_DROPDOWN_HEAD);
    toolbarTable.setWidget(0, 4, lblCount);
    toolbarTable.getCellFormatter().setWidth(0, 4, "250px");

    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(PER_PAGE1, "" + PER_PAGE1));
    DropDownObject selection = new DropDownObjectImpl(PER_PAGE2, "" + PER_PAGE2);
    items.add(selection);
    items.add(new DropDownObjectImpl(PER_PAGE3, "" + PER_PAGE3));
    items.add(new DropDownObjectImpl(PER_PAGE4, "" + PER_PAGE4));
    items.add(new DropDownObjectImpl(PER_PAGE5, "" + PER_PAGE5));
    getDdbPerPage().setItems(items);
    getDdbPerPage().setSelection(selection);
    toolbarTable.setWidget(0, 5, getDdbPerPage());
    toolbarTable.getCellFormatter().setWidth(0, 5, "60px");

    toolbar.add(toolbarTable);
  }

  protected void loadObjects() {
    lblCount.setText((this.from + 1) + " - " + (this.from + getPerPage() >= count ?
        count : this.from + getPerPage()) + Main.constants.listOutOf() + this.count);
    load();
  }

  protected void load() {
  }

  @Override
  public void onTemplateListsLoaded(String ID, int tId, List<TemplateList> tls) {
    if (!toString().equals(ID)) return;
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (TemplateList tl : tls) {
      items.add(new DropDownObjectImpl(tl.getId(), tl.getName(), tl));
    }
    getDdbTemplate().setItems(items);
    getDdbTemplate().setVisible(tls.size() > 1);

    TemplateListItemLoader tliLoader = new TemplateListItemLoader(getModel(),
        (TemplateList) getDdbTemplate().getSelection().getUserObject());
    tliLoader.start();
  }

  @Override
  public void onTemplateListItemsLoaded(TemplateList tl, List<TemplateListItem> tlItems) {
    if (getDdbTemplate().getSelection() == null
        || tl.getId() != getDdbTemplate().getSelection().getId())
      return;
    FlexTable headerTable = new FlexTable();
    headerTable.setStyleName("content-header");
    header.clear();
    for (int i = 0; i < tlItems.size(); i++) {
      TemplateListItem tli = tlItems.get(i);
      headerTable.setWidget(0, i, new Label(tli.getTa().getName()));
      if (tli.getTa().getOa().getVt().getType() == ValueType.VT_REAL
          || tli.getTa().getOa().getVt().getType() == ValueType.VT_INT)
        headerTable.getWidget(0, i).addStyleName(ClientUtils.CSS_ALIGN_RIGHT);
    }
    header.add(headerTable);
    columns = tlItems.size();

    if (tlItems.size() > 0) loadObjects();
  }

  @Override
  public void onChange(ChangeEvent event) {
    loadObjects();
  }

  public void onLoad() {
    getModel().addDataObserver(this);
  }

  public void onUnload() {
    getModel().removeDataObserver(this);
  }

  /* Getters and Setters */

  public ApplicationModel getModel() {
    return this.model;
  }

  /**
   * Getter for property 'ddbTemplate'.
   *
   * @return Value for property 'ddbTemplate'.
   */
  protected DropDownBox getDdbTemplate() {
    if (ddbTemplate == null) {
      ddbTemplate = new DropDownBox(this, Main.constants.listView(),
          CSSConstants.SUFFIX_LIST);
      ddbTemplate.setVisible(false);
    }
    return ddbTemplate;
  }

  /**
   * Getter for property 'ddbPerPage'.
   *
   * @return Value for property 'ddbPerPage'.
   */
  protected DropDownBox getDdbPerPage() {
    if (ddbPerPage == null) {
      ddbPerPage = new DropDownBox(this, Main.constants.listPerPage(),
          CSSConstants.SUFFIX_LIST);
    }
    return ddbPerPage;
  }

  public int getPerPage() {
    return getDdbPerPage().getSelection().getId();
  }

  public int getFrom() {
    return from;
  }
}
