package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 电能示值格式，正数小数位数 WeiShuId Created by nboy on 2016-03-17.
 */
public class WeiShu extends EnumBase {


  public static final int Ws_5_5_2 = 5;
  public static final int Ws_7_5_4 = 7;
  public static final int Ws_9_6_2 = 9;
  public static final int Ws13_7_2 = 13;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(Ws_5_5_2, "5位整数2位小数"));
    lst.add(new EnumBase(Ws_7_5_4, "5位整数4位小数"));
    lst.add(new EnumBase(Ws_9_6_2, "6位整数2位小数"));
    lst.add(new EnumBase(Ws13_7_2, "7位整数2位小数"));

  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }
}