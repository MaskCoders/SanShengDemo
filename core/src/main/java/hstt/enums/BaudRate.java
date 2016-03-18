package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 波特率Created by nboy on 2016-03-17.
 */
public class BaudRate extends EnumBase {
  public static final int B0_Defualt = 0;
  public static final int B1_600 = 1;
  public static final int B2_1200 = 2;
  public static final int B3_2400 = 3;
  public static final int B4_4800 = 4;
  public static final int B5_7200 = 5;
  public static final int B6_9600 = 6;
  public static final int B7_19200 = 7;
  public static final int B8_115200 = 8;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {

    lst.add(new EnumBase(B0_Defualt, "表示无需设置"));
    lst.add(new EnumBase(B1_600, "600"));
    lst.add(new EnumBase(B2_1200, "1200"));
    lst.add(new EnumBase(B3_2400, "2400"));
    lst.add(new EnumBase(B4_4800, "4800"));
    lst.add(new EnumBase(B5_7200, "7200"));
    lst.add(new EnumBase(B6_9600, "9600"));
    lst.add(new EnumBase(B7_19200, "19200"));
    lst.add(new EnumBase(B8_115200, "115200"));

  }

  @Override
  protected List<IEnumField> getEfLst() {
    return lst;
  }

}
