package hstt.proto.upgw;

public enum AFN {

  Answer(0x00, "确认∕否认"),
  Reset(0x01, "复位"),
  XinTiao(0x02, "链路接口检测"),
  Relay(0x03, "中继站命令"),
  SetPara(0x04, "设置参数"),
  Control(0x05, "控制命令"),
  Atteste(0x06, "身份认证及密钥协商"),
  GetConfig(0x09, "请求集中器配置"),
  GetPara(0x0A, "查询参数"),
  GetData1(0x0C, "实时数据"),
  GetData2(0x0D, "历史数据"),
  GetData3(0x0E, "事件数据"),
  File(0x0F, "文件传输"),
  Trans(0x10, "数据转发");


  public String title;
  public int val;

  private static java.util.HashMap<Integer, AFN> mappings;

  private synchronized static java.util.HashMap<Integer, AFN> getMappings() {
    if (mappings == null) {
      mappings = new java.util.HashMap<Integer, AFN>();
    }
    return mappings;
  }

  private AFN(int value, String title) {
    int val = value;
    AFN.getMappings().put(value, this);
  }


  public static AFN forValue(int value) {
    return getMappings().get(value);
  }
}
