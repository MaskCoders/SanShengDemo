package hstt.proto.upgw;

public enum ControlCode {
  Reset(0x41, "复位命令"), //
  GetUserData(0x44, "请求1级数据"), //0100 1010
  GetData1(0x4A, "请求1级数据"), //0100 1010
  GetData2(0x4B, "请求2级数据"), //0100 1011
  GetData3(0x4B, "求2级数据"), //0100 1011 请
  WriteParam(0x4B, ""),
  WriteTime(0x4C, ""),
  ReadYes(0x30, "确认帧"), //0011 0000 下行  认可
  ReadNo(0x45, "否认"),


  XinTiao(0xC9, "链路测试"), //1100 1001 上行 请求∕响应帧 链路测试
  ReturnYes(0x81, "确认回答"), // 10000001 确认回答 成功 对主站下发的设置参数、终端动作的确认，也用于设置电表参数多帧情况，对主站下发报文的确认
  ReturnNo(0x85, "否认回答"), // 10000101 否认回答 失败 主站下发设置参数的内容非法、密码错误等，也用于设置电表参数多帧时对主站下发报文的否认，终端动作、实时抄表的否认。
  ReturnData(0x88, "返回数据"), // 10001000 带数据的响应帧 返回数据
  ReturnParam(0x8A, "返回参数"), // 10001010 带参数的响应帧 返回参数
  ReturnTime(0x8C, "数据即时钟"); // 10001100 带参数数据的响应帧 返回终端时钟 参数数据即时钟
  public String title;
  public int val;

  private ControlCode(int val, String title) {
    this.title = title;
    this.val = val;
  }

}
