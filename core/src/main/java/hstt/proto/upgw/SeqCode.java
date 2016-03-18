package hstt.proto.upgw;

public class SeqCode {
  public static int SimpleT0 = 0x70;//, "单帧无时间标签"),
  public static int Con0T0 = 0x60;//"单帧 不需确认，无时间标签Tp"),
  public static int SimpleT1 = 0xF0;//"单帧 表示在附加信息域中带有时间标签Tp");

}
