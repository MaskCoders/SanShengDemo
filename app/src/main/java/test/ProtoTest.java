package test;

import hstt.data.DataFmt;
import hstt.data.DataItem;
import hstt.enums.BaudRate;
import hstt.enums.Proto;
import hstt.enums.IEnumField;
import hstt.data.ref;
import hstt.proto.IProto;
import hstt.proto.ProtoBase;
import hstt.proto.ProtoFactory;
import hstt.proto.ProtoType;
import hstt.proto.mp07.ControlCode;
import hstt.proto.mp07.MpTask;
import hstt.proto.upgw.AFN;
import hstt.proto.upgw.GwTask;
import hstt.proto.upgw.UpGw;
import hstt.util.DateUtil;
import hstt.util.Logger;
import hstt.util.Util;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by nboy on 2016-03-11.
 */
public class ProtoTest {
  public static final void main(String[] args) {
  System.out.println("---");
//    Logger.d("-----------------------");
////    TestEnumField();
//    TestMp07();
//    Logger.d("-----------");
  }

  public static final void TestEnumField() {
    List<IEnumField> lstProto = Proto.lst;// new Proto().getLst();
    List<IEnumField> lstBaudRate = BaudRate.lst;// new BaudRate().getLst();

    int pos1=  new Proto().getPos(Proto.Dlt97);
    int pos2=  new BaudRate().getPos(BaudRate.B8_115200);

  }

  public static final void TestDatUtil() {
    float fval = 1.1f;
    Object o = fval;
    DateUtil du = null;
    try {
      du = new DateUtil("2016-03-14 12:13:34");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date d1 = du.GetVal();
    o = d1;
    String ss = String.format("%s", o);
    Logger.d(ss);
  }

  public static final void TestUpGw() {
    //68 32 00 32 00 68 4A 00 00 01 00 02 0C 60 00 00 02 00 BB 16
    //68 4A 00 4A 00 68 88 00 00 01 00 02 0C 60 00 00 02 00 49 48 11 15 52 15 17 16
    UpGw p = new UpGw();
    ref<String> tt = new ref<String>("");
    p.IsXinTiaoPacket(null,tt);
    GwTask task = new GwTask("00000001", AFN.GetData1.val, 2, null, null);
    byte[] buffer = p.BuildPacket(task);
    Logger.d("发送组包：" + Util.ByteArrayToString(buffer));
    buffer = Util.StringToByteArray("11 22 68 4A 00 4A 00 68 88 00 00 01 00 02 0C 60 00 00 02 00 49 48 11 15 52 15 17 16 33 44");
    byte[][] validPackets = p.SearchValid(buffer, buffer.length);
    Logger.d("validPackets length= " + validPackets.length);
    Logger.d("validPackets[0] length= " + validPackets[0].length);
    Logger.d("返回数组：" + Util.ByteArrayToString(validPackets[0]));
    List<DataItem> results = p.ParsePacket(task, validPackets[0]);

    for (DataItem di : results) {
      Logger.d("解析结果：" + di);
    }
  }

  public static final void TestMp07() {
    byte[] buffer = new byte[20];
    ref<Integer> pos = new ref<Integer>(0);

    buffer = Util.StringToByteArray("68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5D 16");
    IProto mp = ProtoFactory.Create(ProtoType.Mp07);
    byte[][] validPackets = mp.SearchValid(buffer, buffer.length);
    Logger.d("validPackets length= " + validPackets.length);
    Logger.d("validPackets[0] length= " + validPackets[0].length);
    MpTask task = new MpTask("201000000002", ControlCode.Get, 0x0001FF00, null);
    if (validPackets[0].length > 0) {
      Logger.d("有效数据：" + Util.ByteArrayToString(validPackets[0], 0, validPackets[0].length));

      DataItem dataItem = (DataItem) mp.Parse(task, validPackets[0]);
      Logger.d("解析结果：" + dataItem.n + " " + dataItem.v);
    }
    buffer = mp.Build(task);
    Logger.d("下发组包：" + Util.ByteArrayToString(buffer, 0, buffer.length));
    //68 02 00 00 00 10 20 68 11 04 33 32 34 33 E3 16
  }


  public static final void TestGetData() {
    byte[] buffer = Util.StringToByteArray("34 13 12 15 43 16");
    ref<Integer> pos = new ref<Integer>(0);
    try {
      Object o = ProtoBase.GetData(buffer, pos, DataFmt.Data1);
      Logger.d("返回数据：" + o.toString());
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static final void TestSetData() {
    byte[] buffer = new byte[20];
    ref<Integer> pos = new ref<Integer>(0);
    try {
      ProtoBase.SetData(buffer, pos, "2016-03-14 12:13:34", DataFmt.Data1);
      Logger.d("返回数组：" + Util.ByteArrayToString(buffer));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
