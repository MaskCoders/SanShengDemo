package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 通信端口号 CommPortId
 * Created by nboy on 2016-03-17.
 */
public class CommPort extends EnumBase {


  public static final int Plc = 31;
  public static final int Rs485_1 = 2;
  public static final int Rs485_2 = 3;
  public static final int Sampling = 1;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(Plc, "低压载波"));
    lst.add(new EnumBase(Rs485_1, "1# RS485"));
    lst.add(new EnumBase(Rs485_2, "2# RS485"));
    lst.add(new EnumBase(Sampling, "交采"));

  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }
}