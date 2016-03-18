package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 信道类型  Created by nboy on 2016-03-17.
 */
public class ChannelType extends EnumBase {


  public static final int Gprs = 2;
  public static final int Ether = 5;
  public static final int RS232 = 6;
  public static final int RS485 = 7;
  public static final int Ird38K = 8;
  public static final int Plc = 9;//载波")]

  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new EnumBase(Gprs, "终端作客户端"));
    lst.add(new EnumBase(Ether, "终端作服务端"));
    lst.add(new EnumBase(RS232, "RS232"));
    lst.add(new EnumBase(RS485, "RS485"));
    lst.add(new EnumBase(Ird38K, "红外"));

  }

  @Override
  public List<IEnumField> getEfLst() {
    return lst;
  }
}