package hstt.proto.mp07;

/**
 * Created by nboy on 2016-03-03.
 */
public enum ControlCode {

  Authen(0x03), //认证
  Control(0x1C), //控制
  Get(0x11),
  GetAddr(0x13),
  GetResponse(0x91),
  Set(0x14),
  SetResponse(0x94);

  private int intValue;
  private static java.util.HashMap<Integer, ControlCode> mappings;

  private synchronized static java.util.HashMap<Integer, ControlCode> getMappings() {
    if (mappings == null) {
      mappings = new java.util.HashMap<Integer, ControlCode>();
    }
    return mappings;
  }

  private ControlCode(int value) {
    intValue = value;
    ControlCode.getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static ControlCode forValue(int value) {
    return getMappings().get(value);
  }
}
