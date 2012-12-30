package sk.benko.appsresource.server;

import sk.benko.appsresource.client.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

  public static ObjectType toClientObjectType(StoreDB.ObjectType ot) {
    return ot == null ? null : new ObjectType(ot.getId(), ot.getCode(),
        ot.getName(), ot.getDesc(), ot.getParentId(),
        toClientObjectType(ot.getParent()), ot.getUserId(),
        ot.getLastUpdatedAt());
  }

  public static StoreDB.ObjectType toServerObjectType(ObjectType ot) {
    return ot == null ? null : new StoreDB.ObjectType(ot.getId(), ot.getCode(),
        ot.getName(), ot.getDesc(), ot.getParentId(),
        toServerObjectType(ot.getParent()), ot.getUserId());
  }

  public static ValueType toClientValueType(StoreDB.ValueType vt) {
    return vt == null ? null : new ValueType(vt.getId(), vt.getCode(), vt.getName(), vt.getDesc(),
        vt.getType(), vt.getFlags(), vt.getUserId(), vt.getLastUpdatedAt());
  }

  public static StoreDB.ValueType toServerValueType(ValueType vt) {
    return new StoreDB.ValueType(vt.getId(), vt.getCode(), vt.getName(),
        vt.getDesc(), vt.getType(), vt.getFlags(), vt.getUserId());
  }

  public static Unit toClientUnit(StoreDB.Unit unit) {
    return unit == null ? null : new Unit(unit.getId(), unit.getCode(),
        unit.getName(), unit.getDesc(), unit.getSymbol(), unit.getType(),
        unit.getConversion(), unit.getUserId(), unit.getLastUpdatedAt());
  }

  public static StoreDB.Unit toServerUnit(Unit unit) {
    return unit == null ? null : new StoreDB.Unit(unit.getId(), unit.getCode(),
        unit.getName(), unit.getDesc(), unit.getSymbol(), unit.getType(),
        unit.getConversion(), unit.getUserId());
  }

  public static ObjectAttribute toClientObjectAttribute(
      StoreDB.ObjectAttribute oa) {
    return oa == null ? null : new ObjectAttribute(oa.getId(), oa.getCode(),
        oa.getName(), oa.getDesc(), oa.getOtId(), toClientObjectType(oa.getOt()),
        oa.getVtId(), toClientValueType(oa.getVt()), oa.getUnitId(),
        toClientUnit(oa.getUnit()), oa.getShared1(), oa.getShared2(),
        oa.getShared3(), oa.getShared4(), oa.getShared5(), oa.getUserId(),
        oa.getLastUpdatedAt());
  }

  public static StoreDB.ObjectAttribute toServerObjectAttribute(
      ObjectAttribute oa) {
    return new StoreDB.ObjectAttribute(oa.getId(), oa.getCode(), oa.getName(),
        oa.getDesc(), oa.getOtId(), toServerObjectType(oa.getOt()),
        oa.getVtId(), toServerValueType(oa.getVt()), oa.getUnitId(),
        toServerUnit(oa.getUnit()), oa.getShared1(), oa.getShared2(),
        oa.getShared3(), oa.getShared4(), oa.getShared5(), oa.getUserId());
  }

  public static ObjectRelation toClientObjectRelation(StoreDB.ObjectRelation or) {
    return or == null ? null : new ObjectRelation(or.getId(), or.getCode(), or.getName(),
        or.getDesc(), or.getOt1Id(), toClientObjectType(or.getOt1()),
        or.getOt2Id(), toClientObjectType(or.getOt2()), or.getType(),
        or.getOrId(), toClientObjectRelation(or.getOr()), or.getUserId(),
        or.getLastUpdatedAt());
  }

  public static StoreDB.ObjectRelation toServerObjectRelation(ObjectRelation or,
                                                              boolean withRel) {
    return or == null ? null : new StoreDB.ObjectRelation(or.getId(),
        or.getCode(), or.getName(), or.getDesc(), or.getOt1Id(),
        toServerObjectType(or.getOt1()),
        or.getOt2Id(), toServerObjectType(or.getOt2()), or.getType(),
        or.getOrId(), withRel ? toServerObjectRelation(or.getOr(), false) : null,
        or.getUserId());
  }

  public static Template toClientTemplate(StoreDB.Template t) {
    return t == null ? null : new Template(t.getId(), t.getCode(), t.getName(), t.getDesc(),
        t.getOtId(), toClientObjectType(t.getOt()), t.getOaId(),
        toClientObjectAttribute(t.getOa()), t.getUserId(),
        t.getLastUpdatedAt());
  }

  public static StoreDB.Template toServerTemplate(Template t) {
    return new StoreDB.Template(t.getId(), t.getCode(), t.getName(),
        t.getDesc(), t.getOtId(), null, t.getOaId(), null, t.getUserId());
  }

  public static TemplateRelation toClientTemplateRelation(
      StoreDB.TemplateRelation tr) {
    return tr == null ? null : new TemplateRelation(tr.getId(), tr.getCode(), tr.getName(),
        tr.getDesc(), tr.getT1Id(), toClientTemplate(tr.getT1()), tr.getT2Id(),
        toClientTemplate(tr.getT2()), tr.getOrId(),
        toClientObjectRelation(tr.getOr()), tr.getFlags(), tr.getRank(),
        tr.getSubrank(), tr.getUserId(), tr.getLastUpdatedAt());
  }

  public static TemplateGroup toClientTemplateGroup(StoreDB.TemplateGroup tg) {
    return tg == null ? null : new TemplateGroup(tg.getId(), tg.getCode(), tg.getName(),
        tg.getDesc(), tg.getTId(), toClientTemplate(tg.getT()), tg.getFlags(),
        tg.getRank(), tg.getSubrank(), tg.getLabelTop(), tg.getLabelLeft(),
        tg.getLabelWidth(), tg.getLabelWidthUnit(), tg.getLabelAlign(),
        tg.getUserId(), tg.getLastUpdatedAt());
  }

  public static StoreDB.TemplateGroup toServerTemplateGroup(TemplateGroup tg) {
    return tg == null ? null : new StoreDB.TemplateGroup(tg.getId(), tg.getCode(), tg.getName(),
        tg.getDesc(), tg.getTId(), toServerTemplate(tg.getT()), tg.getFlags(),
        tg.getRank(), tg.getSubRank(), tg.getLabelTop(), tg.getLabelLeft(),
        tg.getLabelWidth(), tg.getLabelWidthUnit(), tg.getLabelAlign(),
        tg.getUserId());
  }

  public static TemplateTree toClientTemplateTree(StoreDB.TemplateTree tt) {
    return new TemplateTree(tt.getId(), tt.getCode(), tt.getName(),
        tt.getDesc(), tt.getTId(), tt.getFlags(), tt.getRank(), tt.getUserId(),
        tt.getLastUpdatedAt());
  }

  public static TemplateList toClientTemplateList(StoreDB.TemplateList tl) {
    return new TemplateList(tl.getId(), tl.getCode(), tl.getName(),
        tl.getDesc(), tl.getTId(), tl.getFlags(), tl.getRank(), tl.getUserId(),
        tl.getLastUpdatedAt());
  }

  public static TemplateAttribute toClientTemplateAttribute(
      StoreDB.TemplateAttribute ta) {
    return ta == null ? null : new TemplateAttribute(ta.getId(), ta.getCode(),
        ta.getName(), ta.getDesc(), ta.getTgId(), toClientTemplateGroup(ta.getTg()),
        ta.getOaId(), toClientObjectAttribute(ta.getOa()), ta.getFlags(), ta.getStyle(),
        ta.getTabIndex(), ta.getDef(), ta.getLength(), ta.getLabelTop(),
        ta.getLabelLeft(), ta.getLabelWidth(), ta.getLabelWidthUnit(),
        ta.getLabelAlign(), ta.getTop(), ta.getLeft(), ta.getWidth(),
        ta.getWidthUnit(), ta.getAlign(), ta.getUnitTop(), ta.getUnitLeft(),
        ta.getUnitWidth(), ta.getUnitWidthUnit(), ta.getUnitAlign(),
        ta.getShared1(), ta.getShared2(), ta.getShared3(), ta.getShared4(),
        ta.getShared5(), ta.getUserId(), ta.getLastUpdatedAt());
  }

  public static StoreDB.TemplateAttribute toServerTemplateAttribute(TemplateAttribute ta) {
    return ta == null ? null : new StoreDB.TemplateAttribute(ta.getId(), ta.getCode(),
        ta.getName(), ta.getDesc(), ta.getTgId(), toServerTemplateGroup(ta.getTg()),
        ta.getOaId(), toServerObjectAttribute(ta.getOa()), ta.getFlags(), ta.getStyle(),
        ta.getTabIndex(), ta.getDef(), ta.getLength(), ta.getLabelTop(),
        ta.getLabelLeft(), ta.getLabelWidth(), ta.getLabelWidthUnit(),
        ta.getLabelAlign(), ta.getTop(), ta.getLeft(), ta.getWidth(),
        ta.getWidthUnit(), ta.getAlign(), ta.getUnitTop(), ta.getUnitLeft(),
        ta.getUnitWidth(), ta.getUnitWidthUnit(), ta.getUnitAlign(),
        ta.getShared1(), ta.getShared2(), ta.getShared3(), ta.getShared4(),
        ta.getShared5(), ta.getUserId());
  }

  public static Application toClientApplication(StoreDB.Application app) {
    return new Application(app.getId(), app.getCode(), app.getName(),
        app.getDesc(), app.getCategory(), app.getFlags(), app.getUserId(),
        app.getLastUpdatedAt());
  }

  public static StoreDB.Application toServerApplication(Application app) {
    return new StoreDB.Application(app.getId(), app.getCode(), app.getName(),
        app.getDesc(), app.getCategory(), app.getFlags(), app.getUserId());
  }

  public static AppUser toClientUser(StoreDB.AppUser user) {
    return user == null ? null : new AppUser(user.getId(), user.getEmail(),
        user.getEmail(), user.getFlags());
  }

  public static AObject toClientObject(StoreDB.AObject o) {
    return new AObject(o.getId(), o.getOtId(), o.getLevel(), o.getLeaf(),
        o.getUserId(), toClientUser(o.getUser()), o.getLastUpdatedAt());
  }

  public static AValue toClientValue(StoreDB.AValue v) {
    return new AValue(v.getId(), v.getOId(), v.getOaId(), v.getRank(),
        v.getValueString(), v.getLangId(), v.getValueDate(),
        v.getValueTimestamp(), v.getValueDouble(), v.getValueRef(),
        v.getUserId(), v.getLastUpdatedAt());
  }

  public static Map<Integer, List<AValue>> toClientValues(Map<Integer, List<StoreDB.AValue>> values) {
    final Map<Integer, List<AValue>> clients = new HashMap<Integer, List<AValue>>();
    for (Integer oaId : values.keySet()) {
      List<StoreDB.AValue> vals = values.get(oaId);
      List<AValue> clientVals = new ArrayList<AValue>();
      if (vals != null)
        for (StoreDB.AValue v : vals)
          clientVals.add(Utils.toClientValue(v));
      clients.put(oaId, clientVals);
    }
    return clients;
  }

}
