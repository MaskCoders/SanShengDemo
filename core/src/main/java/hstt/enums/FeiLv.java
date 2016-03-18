package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *  费率个数 FeiLvId Created by nboy on 2016-03-17.
 */
public class FeiLv extends EnumBase {


  public static final int Feilv1 = 1;
  public static final int Feilv4 = 4;
  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(Feilv1, "单费率"));
    lst.add(new EnumBase(Feilv4, "4费率"));
  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }
}