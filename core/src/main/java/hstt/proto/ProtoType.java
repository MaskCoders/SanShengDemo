package hstt.proto;

public enum ProtoType {
  None(0, "未实现"),
  UpGw(1, "国网规约"),
  Mp07(21, "DLT645-2007"),//Meter Potocol
  Mp97(21, "DLT645-1997"),
  Lcm(31, "左模块");//local communication modlue

  public String title;
  public int val;

  private ProtoType(int val, String title) {
    this.title = title;
    this.val = val;
  }
}