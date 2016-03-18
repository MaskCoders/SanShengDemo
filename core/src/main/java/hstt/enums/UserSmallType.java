package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户小类 Created by nboy on 2016-03-17.
 */
public class UserSmallType extends EnumBase {

  public static final int Default = 0;
  public static final int MeterSng = 1;
  public static final int MeterThr = 2;
  public static final int Type3 = 3;
  public static final int Type4 = 4;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(Default, "缺省值"));
    lst.add(new EnumBase(MeterSng, "单相表用户"));
    lst.add(new EnumBase(MeterThr, "三相表用户"));
    lst.add(new EnumBase(Type3, "备用3类"));
    lst.add(new EnumBase(Type4, "备用4类"));
  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }
}