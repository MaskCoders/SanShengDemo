package hstt.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件记录  定义，
 * flag0  0 表示无启动标志，1单字节，2双字节
 * flag1 flag2 事件标志
  * Created by nboy on 2016-03-17.
      */
  public class Erc implements IEnumField {
    public int val;
    public String txt;
    public int flag0;
  public String flag1;
  public String flag2;


  public static List<IEnumField> lst = new ArrayList<IEnumField>();

  static {
    lst.add(new Erc(1, "数据初始化和版本变更", 0, "数据区初始化,版本变更", ""));
    lst.add(new Erc(2, "参数丢失", 0, "终端参数丢失,测量点参数丢失", ""));
    lst.add(new Erc(3, "参数变更", 0, "", ""));
    lst.add(new Erc(4, "状态量变位", 0, "", ""));
    lst.add(new Erc(5, "遥控跳闸", 0, "", ""));
    lst.add(new Erc(6, "功控跳闸", 0, "", ""));
    lst.add(new Erc(7, "电控跳闸", 0, "", ""));
    lst.add(new Erc(8, "电能表参数变更", 20, "费率时段,编程时间,抄表日,脉冲常数,互感器倍率,最大需量清零", ""));
    lst.add(new Erc(9, "电流回路异常", 2, "A相,B相,C相", " ,短路,开路,反向"));
    lst.add(new Erc(10, "电压回路异常", 2, "A相,B相,C相", " ,断相,失压, "));
    lst.add(new Erc(11, "相序异常", 2, "", ""));
    lst.add(new Erc(12, "电能表时间超差", 2, "", ""));
    lst.add(new Erc(13, "电表故障信息", 2, "编程次数或最大需量清零,断相次数,断相次数,断相次数,电池欠压", ""));
    lst.add(new Erc(14, "终端停/上电", 0, "", ""));
    lst.add(new Erc(15, "谐波越限告警", 2, "A相,B相,C相", "电压,,电流,"));
    lst.add(new Erc(16, "直流模拟量越限", 4, "越上限,越下限", ""));
    lst.add(new Erc(17, "不平衡度越限", 2, "电压越限,电流越限", ""));
    lst.add(new Erc(18, "电容器投切自锁", 2, "过压,装置故障,执行回路故障", ""));
    lst.add(new Erc(19, "购电参数设置", 11, "", ""));
    lst.add(new Erc(20, "消息认证错误", 0, "", ""));
    lst.add(new Erc(21, "终端故障", 0, "", ""));
    lst.add(new Erc(22, "有功总电能量差动越限", 1, "", ""));
    lst.add(new Erc(23, "电控告警", 10, "", ""));
    lst.add(new Erc(24, "电压越限", 2, "A相,B相,C相", " ,越上上限,越下下限, "));
    lst.add(new Erc(25, "电流越限", 2, "A相,B相,C相", " ,越上上限,越下下限, "));
    lst.add(new Erc(26, "视在功率越限", 2, "", " ,越上上限,越上限, "));
    lst.add(new Erc(27, "电能表示度下降", 2, "", ""));
    lst.add(new Erc(28, "电能量超差", 2, "", ""));
    lst.add(new Erc(29, "电能表飞走", 2, "", ""));
    lst.add(new Erc(30, "电能表停走", 2, "", ""));
    lst.add(new Erc(31, "终端485抄表失败", 2, "", ""));
    lst.add(new Erc(32, "通信流量超限", 0, "", ""));
    lst.add(new Erc(33, "电表状态字变位", 2, "", ""));
    lst.add(new Erc(34, "CT异常", 2, "A相,B相,C相", ",一次侧短路,二次侧短路,二次侧开路"));
    lst.add(new Erc(35, "发现未知电表事件", 50, "", ""));
    lst.add(new Erc(36, "控制输出回路开关接入状态量变位", 0, "", ""));
    lst.add(new Erc(37, "电能表开表盖", 2, "", ""));
    lst.add(new Erc(38, "电能表开端钮盒", 2, "", ""));
    lst.add(new Erc(39, "补抄失败", 2, "", ""));
    lst.add(new Erc(40, "磁场异常", 2, "", ""));
    lst.add(new Erc(41, "对时", 20, "", ""));
    lst.add(new Erc(42, "电能表主动上报事件", 0, "", ""));
    lst.add(new Erc(43, "ERC43 备用", 20, "", ""));
    lst.add(new Erc(44, "ERC44 备用", 20, "", ""));
    lst.add(new Erc(45, "终端电池失效", 0, "", ""));


  }

  public Erc() {

  }

  public Erc(int val, String txt, int flag0, String flag1, String flag2) {
    this.val = val;
    this.txt = txt;
    this.flag0 = flag0;
    this.flag1 = flag1;
    this.flag2 = flag2;
  }


  public int getPos(int val) {
    List<IEnumField> lst = getLst();
    for (int i = 0; i < lst.size(); i++) {
      if (lst.get(i).getVal() == val) return i;
    }
    return 0;
  }

  public List<IEnumField> getLst() {
    return lst;
  }

  public String getTxt() {
    return txt;
  }

  public int getVal() {
    return val;
  }


}