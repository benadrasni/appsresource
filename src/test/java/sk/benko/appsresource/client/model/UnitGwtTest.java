package sk.benko.appsresource.client.model;

import com.google.gwt.junit.client.GWTTestCase;


import java.util.Date;

public class UnitGwtTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "sk.benko.appsresource.client.model.Unit";
  }

  public void testConstructors() {
    Unit unit = new Unit("metre", "m");
    assertTrue(unit.getName().equals("metre"));
    assertTrue(unit.getSymbol().equals("m"));

    Date date = new Date();
    unit = new Unit(1, "UN-0000001", "metre", "meter", "m", 2, (float) 2.0, 3, date);
    assertTrue(unit.getId() == 1);
    assertTrue(unit.getCode().equals("UN-0000001"));
    assertTrue(unit.getName().equals("metre"));
    assertTrue(unit.getDesc().equals("meter"));
    assertTrue(unit.getSymbol().equals("m"));
    assertTrue(unit.getType() == 2);
    assertTrue(unit.getConversion() == (float) 2.0);
    assertTrue(unit.getUserId() == 3);
    assertTrue(unit.getLastUpdatedAt() == date);
  }

  public void testUpdate() {
    Unit unit = new Unit("metre", "m");
    assertTrue(unit.getName().equals("metre"));
    assertTrue(unit.getSymbol().equals("m"));
    assertTrue(unit.getId() == 0);
    assertTrue(unit.getCode() == null);
    assertTrue(unit.getLastUpdatedAt() == null);

    Date date = new Date();
    unit.update(1, "UN-0000001", date);
    assertTrue(unit.getId() == 1);
    assertTrue(unit.getCode().equals("UN-0000001"));
    assertTrue(unit.getLastUpdatedAt() == date);
  }

}
