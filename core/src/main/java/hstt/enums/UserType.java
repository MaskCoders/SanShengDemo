package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户大类 Created by nboy on 2016-03-17.
 */
public class UserType extends EnumBase {

  public static final int NoUse = 0;
  public static final int Large = 1;
  public static final int Middle = 2;
  public static final int LowThreePh = 3;
  public static final int LowSngPh = 4;
  public static final int Resident = 5;
  public static final int ResTrans = 6;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(NoUse, "无需配置"));
    lst.add(new EnumBase(Large, "大型专变用户"));
    lst.add(new EnumBase(Middle, "中小型专变用户"));
    lst.add(new EnumBase(LowThreePh, "低压三相一般工商业用户"));
    lst.add(new EnumBase(LowSngPh, "低压单相一般工商业用户"));
    lst.add(new EnumBase(Resident, "居民用户"));
    lst.add(new EnumBase(ResTrans, "公变考核计量点"));
  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }
}