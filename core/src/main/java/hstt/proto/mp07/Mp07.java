package hstt.proto.mp07;

import hstt.data.DataFmt;
import hstt.data.DataItem;
import hstt.proto.IProto;
import hstt.proto.ProtoBase;
import hstt.proto.ProtoException;
import hstt.util.Logger;
import hstt.util.Util;
import hstt.data.ref;

import java.text.ParseException;
import java.util.*;

import hstt.proto.ProtoType;

public class Mp07 extends ProtoBase implements IProto {


  //接口实现
  public ProtoType getProtoType() {
    return ProtoType.Mp07;
  }

  public DataItem Parse(Object task, byte[] pBuffer) {

    return ParsePacket((MpTask) task, pBuffer);
  }

  public byte[] Build(Object task) {
    return BuildPacket((MpTask) task);
  }

  public boolean IsEndFrame(byte[] pBuffer) {
    return true;
  }

  public final byte[][] SearchValid(byte[] pBuffer, int pFilledLen) {
    int cnt = 1;
    byte[][] buff = new byte[cnt][];

    int fromIndex = 0;
    if (pFilledLen < MinPacketLen) return buff;
    for (fromIndex = 0; fromIndex <= pFilledLen - MinPacketLen; fromIndex++) {
      //帧探测，方向判断，2个HeadChar判断
      if (((pBuffer[fromIndex + ControlCodePos] & 0xFF) >> 7) == 1 && pBuffer[fromIndex + HeadChar1Pos] == PacketHeadChar && pBuffer[fromIndex + HeadChar2Pos] == PacketHeadChar) {

        int packetLen = pBuffer[fromIndex + DataLenPos] + FixLen;
        int endIndex = fromIndex + packetLen - 1;//末尾的索引 对应PacketEndChar=0x16
        if (pFilledLen > endIndex && pBuffer[endIndex] == PacketEndChar) {
          int crc = pBuffer[endIndex - 1];
          if (Util.SumCheck(pBuffer, fromIndex, endIndex - 2) == crc) {
            buff[cnt - 1] = Arrays.copyOfRange(pBuffer, fromIndex, endIndex + 1);
            cnt++;
          }
        }
      }

    }
    return buff;
  }

  //常量定义，初始化

  public static final int FixLen = 12;//报文固定长度部分，头、尾，地址，校验
  public static final int MaxPacketLen = 100;
  public static final int MinPacketLen = 12;
  public static final int PacketHeadChar = 0x68;
  public static final int PacketEndChar = 0x16;

  public static final int HeadChar1Pos = 0;                               //命令头1的位置
  public static final int Addr1Pos = 1;                            //地址字节1
  public static final int HeadChar2Pos = 7;           //命令头2的位置
  public static final int ControlCodePos = 8;                               //控制域(命令方向与服务功能)
  public static final int DataLenPos = 9;
  public static final int DataBeginPos = 10;                         //信息点DA


  public static HashMap<String, DataItem> DiDict = new HashMap<String, DataItem>();

  static {
    Logger.d("初始化");
    for (int i = 0; i <= 0x3E; i++) {
      DiDict.put("050600" + String.format("%02X", i), new DataItem("上" + (i + 1) + "次日冻结时间", DataFmt.Data15, ""));
    }
    for (int i = 0; i <= 0x3E; i++) {
      DiDict.put("0x050600" + String.format("%02X", i), new DataItem("上" + (i + 1) + "日冻结正向有功", DataFmt.Data11, "kWh"));
    }


    DiDict.put("0001FF00", new DataItem("正向有功电能", DataFmt.Data11, "kWh", new String[]{"总", "尖", "峰", "平", "谷"}));
    DiDict.put("0201FF00", new DataItem("三相电压", DataFmt.Data7, "V", new String[]{"A相电压", "B相电压", "C相电压"}));

    DiDict.put("04000101", new DataItem("日期", DataFmt.Date3, ""));
    DiDict.put("04000102", new DataItem("时间", DataFmt.hhmmss, ""));


    DiDict.put("00900200", new DataItem("剩余金额", DataFmt.Data11, "元"));


    List<DataItem> items = new ArrayList<DataItem>();
    items.add(new DataItem("A相", DataFmt.Data10, "V"));
    items.add(new DataItem("", DataFmt.Data10, "次"));
    items.add(new DataItem("B相", DataFmt.Data10, "V"));
    items.add(new DataItem("", DataFmt.Data10, "次"));
    items.add(new DataItem("C相", DataFmt.Data10, "V"));
    items.add(new DataItem("", DataFmt.Data10, "次"));
    DiDict.put("03010000", new DataItem("失压情况", items));

    DiDict.put("03300D00", new DataItem("开盖次数", DataFmt.Data10, "次"));
    DiDict.put("1D000001", new DataItem("跳闸次数", DataFmt.Data10, "次"));

    DiDict.put("05060100", new DataItem("总", DataFmt.Data11, "kWh"));
    DiDict.put("05060101", new DataItem("尖", DataFmt.Data11, "kWh"));
    DiDict.put("05060102", new DataItem("峰", DataFmt.Data11, "kWh"));
    DiDict.put("05060103", new DataItem("平", DataFmt.Data11, "kWh"));
    DiDict.put("05060104", new DataItem("谷", DataFmt.Data11, "kWh"));
    DiDict.put("050601FF", new DataItem("日冻结正向有功", new String[]{"05060100", "05060101", "05060102", "05060103", "05060104"}));
    DiDict.put("05060200", new DataItem("总", DataFmt.Data11, "kWh"));
    DiDict.put("05060201", new DataItem("尖", DataFmt.Data11, "kWh"));
    DiDict.put("05060202", new DataItem("峰", DataFmt.Data11, "kWh"));
    DiDict.put("05060203", new DataItem("平", DataFmt.Data11, "kWh"));
    DiDict.put("05060204", new DataItem("谷", DataFmt.Data11, "kWh"));
    DiDict.put("050602FF", new DataItem("日冻结反向有功", new String[]{"05060200", "05060201", "05060202", "05060203", "05060204"}));
    DiDict.put("05060300", new DataItem("总", DataFmt.Data11, "kvarh"));
    DiDict.put("05060301", new DataItem("尖", DataFmt.Data11, "kvarh"));
    DiDict.put("05060302", new DataItem("峰", DataFmt.Data11, "kvarh"));
    DiDict.put("05060303", new DataItem("平", DataFmt.Data11, "kvarh"));
    DiDict.put("05060304", new DataItem("谷", DataFmt.Data11, "kvarh"));
    DiDict.put("050603FF", new DataItem("日冻结组合无功1", new String[]{"05060300", "05060301", "05060302", "05060303", "05060304"}));
    DiDict.put("05060400", new DataItem("总", DataFmt.Data11, "kvarh"));
    DiDict.put("05060401", new DataItem("尖", DataFmt.Data11, "kvarh"));
    DiDict.put("05060402", new DataItem("峰", DataFmt.Data11, "kvarh"));
    DiDict.put("05060403", new DataItem("平", DataFmt.Data11, "kvarh"));
    DiDict.put("05060404", new DataItem("谷", DataFmt.Data11, "kvarh"));
    DiDict.put("050604FF", new DataItem("日冻结组合无功2", new String[]{"05060400", "05060401", "05060402", "05060403", "05060404"}));
    DiDict.put("00010001", new DataItem("总", DataFmt.Data11, "kWh"));
    DiDict.put("00010101", new DataItem("尖", DataFmt.Data11, "kWh"));
    DiDict.put("00010201", new DataItem("峰", DataFmt.Data11, "kWh"));
    DiDict.put("00010301", new DataItem("平", DataFmt.Data11, "kWh"));
    DiDict.put("00010401", new DataItem("谷", DataFmt.Data11, "kWh"));
    DiDict.put("0001FF01", new DataItem("月冻结正向有功", new String[]{"00010001", "00010101", "00010201", "00010301", "00010401"}));
    DiDict.put("00020001", new DataItem("总", DataFmt.Data11, "kWh"));
    DiDict.put("00020101", new DataItem("尖", DataFmt.Data11, "kWh"));
    DiDict.put("00020201", new DataItem("峰", DataFmt.Data11, "kWh"));
    DiDict.put("00020301", new DataItem("平", DataFmt.Data11, "kWh"));
    DiDict.put("00020401", new DataItem("谷", DataFmt.Data11, "kWh"));
    DiDict.put("0002FF01", new DataItem("月冻结反向有功", new String[]{"00020001", "00020101", "00020201", "00020301", "00020401"}));
    DiDict.put("00030001", new DataItem("总", DataFmt.Data11, "kvarh"));
    DiDict.put("00030101", new DataItem("尖", DataFmt.Data11, "kvarh"));
    DiDict.put("00030201", new DataItem("峰", DataFmt.Data11, "kvarh"));
    DiDict.put("00030301", new DataItem("平", DataFmt.Data11, "kvarh"));
    DiDict.put("00030401", new DataItem("谷", DataFmt.Data11, "kvarh"));
    DiDict.put("0003FF01", new DataItem("月冻结组合无功1", new String[]{"00030001", "00030101", "00030201", "00030301", "00030401"}));
    DiDict.put("00040001", new DataItem("总", DataFmt.Data11, "kvarh"));
    DiDict.put("00040101", new DataItem("尖", DataFmt.Data11, "kvarh"));
    DiDict.put("00040201", new DataItem("峰", DataFmt.Data11, "kvarh"));
    DiDict.put("00040301", new DataItem("平", DataFmt.Data11, "kvarh"));
    DiDict.put("00040401", new DataItem("谷", DataFmt.Data11, "kvarh"));
    DiDict.put("0004FF01", new DataItem("月冻结组合无功2", new String[]{"00040001", "00040101", "00040201", "00040301", "00040401"}));
    DiDict.put("00010000", new DataItem("正向有功总电能", DataFmt.Data11, "kWh"));
    DiDict.put("00020000", new DataItem("反向有功总电能", DataFmt.Data11, "kWh"));
    DiDict.put("00030000", new DataItem("组合无功1总电能", DataFmt.Data11, "kvarh"));
    DiDict.put("00040000", new DataItem("组合无功2总电能", DataFmt.Data11, "kvarh"));
    //DiDict.put("0001FF00", new DataItem("正向有功", DataFmt.Data11, "kWh"));
    DiDict.put("0002FF00", new DataItem("反向有功", DataFmt.Data11, "kWh"));
    DiDict.put("0003FF00", new DataItem("组合无功1", DataFmt.Data11, "kvarh"));
    DiDict.put("0004FF00", new DataItem("组合无功2", DataFmt.Data11, "kvarh"));
    DiDict.put("00050000", new DataItem("第一象限无功总电能", DataFmt.Data11, "kvarh"));
    DiDict.put("00060000", new DataItem("第二象限无功总电能", DataFmt.Data11, "kvarh"));
    DiDict.put("00070000", new DataItem("第三象限无功总电能", DataFmt.Data11, "kvarh"));
    DiDict.put("00080000", new DataItem("第四象限无功总电能", DataFmt.Data11, "kvarh"));
    DiDict.put("02010100", new DataItem("A相", DataFmt.Data7, "V"));
    DiDict.put("02010200", new DataItem("B相", DataFmt.Data7, "V"));
    DiDict.put("02010300", new DataItem("C相", DataFmt.Data7, "V"));
    DiDict.put("0201FF00", new DataItem("电压", new String[]{"02010100", "02010200", "02010300"}));
    DiDict.put("02020100", new DataItem("A相", DataFmt.Data25, "A"));
    DiDict.put("02020200", new DataItem("B相", DataFmt.Data25, "A"));
    DiDict.put("02020300", new DataItem("C相", DataFmt.Data25, "A"));
    DiDict.put("0202FF00", new DataItem("电流", new String[]{"02020100", "02020200", "02020300"}));
    DiDict.put("02030000", new DataItem("总", DataFmt.Data9, "kW"));
    DiDict.put("02030100", new DataItem("A相", DataFmt.Data9, "kW"));
    DiDict.put("02030200", new DataItem("B相", DataFmt.Data9, "kW"));
    DiDict.put("02030300", new DataItem("C相", DataFmt.Data9, "kW"));
    DiDict.put("0203FF00", new DataItem("有功功率", new String[]{"02030000", "02030100", "02030200", "02030300"}));
    DiDict.put("02040000", new DataItem("总", DataFmt.Data25, "kvar"));
    DiDict.put("02040100", new DataItem("A相", DataFmt.Data25, "kvar"));
    DiDict.put("02040200", new DataItem("B相", DataFmt.Data25, "kvar"));
    DiDict.put("02040300", new DataItem("C相", DataFmt.Data25, "kvar"));
    DiDict.put("0204FF00", new DataItem("无功功率", new String[]{"02040000", "02040100", "02040200", "02040300"}));
    DiDict.put("02060000", new DataItem("总", DataFmt.Data5, ""));
    DiDict.put("02060100", new DataItem("A相", DataFmt.Data5, ""));
    DiDict.put("02060200", new DataItem("B相", DataFmt.Data5, ""));
    DiDict.put("02060300", new DataItem("C相", DataFmt.Data5, ""));
    DiDict.put("0206FF00", new DataItem("功率因数", new String[]{"02060000", "02060100", "02060200", "02060300"}));
    DiDict.put("04000101", new DataItem("日期及星期", DataFmt.YYMMDDWW, ""));
    DiDict.put("04000102", new DataItem("时间", DataFmt.hhmmss, ""));
    DiDict.put("04000501", new DataItem("电表运行状态字1", DataFmt.Bs16, ""));
    DiDict.put("04000502", new DataItem("电表运行状态字2", DataFmt.Bs16, ""));
    DiDict.put("04000503", new DataItem("电表运行状态字3", DataFmt.Bs16, ""));
  }

  public final byte[] BuildPacket(MpTask task) {
    try {
      String meterAddr = Util.PadLeft(task.MeterAddr, 12, '0');
      byte[] buffer = new byte[MaxPacketLen];
      buffer[HeadChar1Pos] = PacketHeadChar;
      buffer[HeadChar2Pos] = PacketHeadChar;
      //控制域
      buffer[ControlCodePos] = (byte) task.ControlCode.getValue();
      //地址
      int addrLen = 6;
      for (int i = 0; i < 6; i++) {
        String aa = meterAddr.substring((addrLen - i - 1) * 2, (addrLen - i) * 2);
        buffer[Addr1Pos + i] = Integer.valueOf(aa, 16).byteValue();
      }

      //应用层功能码
      byte pos = DataBeginPos;
      if (task.Di >= 0) {
        buffer[pos++] = Util.ByteOfLong(task.Di, 1);
        buffer[pos++] = Util.ByteOfLong(task.Di, 2);
        buffer[pos++] = Util.ByteOfLong(task.Di, 3);
        buffer[pos++] = Util.ByteOfLong(task.Di, 4);
      }
      if (task.Data != null && task.Data.length > 0) {
        for (int i = 0; i < task.Data.length; i++) {
          buffer[pos++] = task.Data[i];
        }
      }
      for (int i = DataBeginPos; i < pos; i++) {
        buffer[i] = (byte) (0x33 + buffer[i]); //加密
      }
      buffer[DataLenPos] = (byte) (pos - DataBeginPos);
      buffer[pos] = Util.SumCheck(buffer, 0, pos);
      buffer[pos + 1] = PacketEndChar;
      buffer = Arrays.copyOfRange(buffer, 0, pos + 2);

      return buffer;
    } catch (java.lang.Exception e) {
      return null;
    }

  }


  /**
   * 解码数据部分
   *
   * @param pBuffer
   */
  public static final void Decode(byte[] pBuffer) {
    for (int i = DataBeginPos; i < DataBeginPos + pBuffer[DataLenPos]; i++) {
      pBuffer[i] = (byte) (pBuffer[i] - 0x33);//解密
    }
  }

  public DataItem ParsePacket(MpTask task, byte[] pBuffer) {

    if (pBuffer == null) throw new ProtoException("数据为空");
    List<DataItem> dataItems = new ArrayList<DataItem>();
    Decode(pBuffer);
    Logger.d("解码数据：" + Util.ByteArrayToString(pBuffer));

    ref<Integer> pos = new ref<Integer>(DataBeginPos);
    ControlCode ctrlCode = Valid(pBuffer);
    DataItem ret = new DataItem();
    try {

      switch (ctrlCode) {
        case Set:
          break;
        case Get:
          String di = GetBcdString(pBuffer, pos, 4);
          long responseDi = Long.parseLong(di, 16);
          if (responseDi != task.Di) throw new ProtoException("数据项错误：" + di);
          if (!DiDict.containsKey(di)) throw new ProtoException("数据项不支持：" + di);
          DataItem diItem = DiDict.get(di);
          ret.n = diItem.n;
          switch (diItem.f) {
            default:
              ret.v = GetData(pBuffer, pos, diItem.f).toString();
              break;
            case DataFmt.Block:
              String value = "";
              int childCount = diItem.Childs.size();
              for (int i = 0; i < childCount; i++) {
                value += GetData(pBuffer, pos, diItem.Childs.get(i).f).toString();
                if (i < childCount - 1) value += ", ";
              }
              ret.v = value;
              break;
            case DataFmt.SubList:
              for (DataItem item : diItem.Childs) {
                ret.v += item.n + "" + GetData(pBuffer, pos, item.f).toString() + item.v + ",";//v保存单位信息
              }
              ret.v = Util.trimEnd(ret.v, ',');
              break;
          }
          break;
        case GetAddr: {
          ret.n = "电表地址";
          String meterAddr = "";
          pos.v = Addr1Pos;
          String meterAddr1 = GetBcdString(pBuffer, pos, 6);
          if (meterAddr1 != "AAAAAAAAAAAA") meterAddr = meterAddr1;
          int dataLen = pBuffer[DataLenPos];
          if (dataLen != 6) throw new ProtoException("长度错误");
          pos.v = DataBeginPos;
          String meterAddr2 = GetBcdString(pBuffer, pos, 6);
          if (meterAddr2 != "AAAAAAAAAAAA") meterAddr = meterAddr2;
          ret.v = meterAddr;
        }
        break;
        default:
          ret = new DataItem("解析错误", "规约不支持");
          break;
      }
    } catch (ParseException pe) {
      ret.n = "解析失败";
      ret.v = pe.getMessage();
    }
    return ret;
  }


  public final void Valid(byte[] pBuffer, ControlCode pControlCode) {
    int ctrlCode = pBuffer[ControlCodePos];
    int fn = ctrlCode & 0x1F;
    if ((ctrlCode & 0x40) > 0) {
      throw new ProtoException("异常应答");
    }
    if (fn != pControlCode.getValue()) {
      throw new ProtoException("功能码 " + fn + " 不符");
    }
  }

  public static final String[] ErrList1 = {"其他错误", "无请求数据", "密码错/未授权", "通信速率不能更改", "年时区数超", "日时段数超", "费率数超", "保留"};
  public static final String[] ErrList2 = {"其它错误", "重复充值", "ESAM验证失败", "身份认证失败", "客户编号不匹配", "充值次数错误", "购电超囤积", "保留"};

  public static ControlCode Valid(byte[] pBuffer) {

    int ctrlCode = pBuffer[ControlCodePos];
    int fn = ctrlCode & 0x1F;
    if ((ctrlCode & 0x40) > 0) {
      String errInfo = "";
      int errflg = pBuffer[ControlCodePos + 1];
      int errCode = pBuffer[ControlCodePos + 2];
      for (int bit = 0; bit < 8; bit++) {
        if (((errCode >> bit) & 0x01) > 0) {

          if (errflg == 1) {
            errInfo += ErrList1[bit] + " ";
          }
          if (errflg == 2) {
            errInfo += ErrList2[bit] + " ";
          }
        }
      }
      throw new ProtoException("异常应答 " + errCode + " " + errInfo);
    }
    return ControlCode.forValue(fn);
  }
}