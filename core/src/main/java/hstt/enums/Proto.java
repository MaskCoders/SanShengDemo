package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nboy on 2016-03-16.电表规约
 */
public class Proto extends EnumBase {

  public static final int NoUse = 0;
  public static final int Dlt97 = 1;
  public static final int Sampling = 2;
  public static final int Dlt07 = 30;
  public static final int Plc = 31;
  public static final int Standby  = 255;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(NoUse, "无需抄表"));
    lst.add(new EnumBase(Dlt97, "645-1997"));
    lst.add(new EnumBase(Sampling, "交流采样"));
    lst.add(new EnumBase(Dlt07, "645-2007"));
    lst.add(new EnumBase(Plc, "窄带低压载波"));
    lst.add(new EnumBase(Standby, "备用"));

  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }

}
