package hstt.proto.upgw;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hstt.data.ref;
import hstt.data.DataItem;
import hstt.data.DataFmt;
import hstt.proto.ProtoType;
import hstt.proto.ProtoBase;
import hstt.proto.ProtoException;
import hstt.proto.IUpProto;
import hstt.util.Util;

public class UpGw extends ProtoBase implements IUpProto {


  public ProtoType getProtoType() {
    return ProtoType.UpGw;
  }

  public List<DataItem> Parse(Object task, byte[] pBuffer) {
    return ParsePacket((GwTask) task, pBuffer);
  }

  public byte[] Build(Object task) {
    return BuildPacket((GwTask) task);
  }

  public final byte[] BuildPacket(GwTask pTask) throws ProtoException {
    return BuildPacket(pTask.CommAddr, AFN.forValue(pTask.Afn), pTask.Fn, pTask.Params, pTask.Das);
  }


  public final byte[][] SearchValid(byte[] pBuffer, int pFilledLen) {

    int cnt = 1;
    byte[][] buff = new byte[cnt][];
    if (pFilledLen < MinPacketLen) return buff;
    for (int fromIndex = 0; fromIndex <= pFilledLen - MinPacketLen; fromIndex++) {
      if (((pBuffer[fromIndex + ControlCodePos] & 0xFF) >> 7) == 1 && pBuffer[fromIndex + HeadChar1Pos] == PacketHeadChar && pBuffer[fromIndex + HeadChar2Pos] == PacketHeadChar) {
        int dataLen = ((pBuffer[fromIndex + DataLenLoPos] & 0xFF) >> 2);
        dataLen = dataLen + pBuffer[fromIndex + DataLenHiPos] * 64;
        int endIndex = fromIndex + dataLen + 7;
        if (pFilledLen > endIndex && pBuffer[endIndex] == PacketEndChar) {
          int crc = pBuffer[endIndex - 1];
          if (Util.SumCheck(pBuffer, fromIndex + ControlCodePos, fromIndex + ControlCodePos + dataLen - 1) == crc) {
            buff[cnt - 1] = Arrays.copyOfRange(pBuffer, fromIndex, endIndex + 1);
            cnt++;
          }
        }
      }
    }
    return buff;
  }

  //常量定义
  public static final int DaP0 = 0;
  public static final int DaPAll = 0xFFFF;
  public static final char AsciiEndChar = (char) 0;
  // public const char ParaAsciiEndChar = (char)' ';
  public static final int MergeMeterNumPerPacket = 10;
  // ——采用专用无线数传信道，长度L1不大于255；
  // ——采用网络传输，长度L1不大于16383。
  public static final int MaxDownPacketLen = 2048; // allocate first, then
  // check
  public static final int MinPacketLen = 20;
  public static final byte PacketHeadChar = 0x68;
  public static final byte PacketEndChar = 0x16;

  public static final int HeadChar1Pos = 0; // 命令头1的位置
  public static final int DataLenLoPos = 1; // 数据区长度L
  public static final int DataLenHiPos = 2; // 数据区长度H
  public static final int DataLenLoPos1 = 3; // 数据区长度L
  public static final int DataLenHiPos1 = 4; // 数据区长度H
  public static final int HeadChar2Pos = 5; // 命令头2的位置
  public static final int ControlCodePos = 6; // 控制域(命令方向与服务功能)
  public static final int CollectAddrPos = 7; // 终端地址字节1
  public static final int AFNPos = 12; // 功能码
  public static final int FrameSeqPos = 13; // /帧序列
  public static final int DataBeginPos = 14; // /信息点DA1

  public static final int FrameTypeMidOfMulti = 0;
  public static final int FrameTypeEndOfMulti = 1;
  public static final int FrameTypeBeginOfMulti = 2;
  public static final int FrameTypeSigle = 3;


  public static final int DateTimeObjectLen = 6;
  public static final String CollectAllMeterCode = "FFFFFFFFFFFF";

  public final byte[] BuildPacket(String pCommAddr, AFN pAfn, int pDt, DataItem[] pParaItems, int[] pDas) throws ProtoException {

    byte[] buffer = new byte[MaxDownPacketLen];

    ControlCode cc = ControlCode.GetData2;
    int sc = SeqCode.SimpleT0;
    Boolean pwd = false;
    switch (pAfn) // 这里仅考虑了一般情况，根据AFN固定设置cc sc
    {
      case Reset: // 复位命令
        cc = ControlCode.Reset;
        pwd = true;
        break;
      case SetPara: // 设置参数
        cc = ControlCode.GetData1;
        pwd = true;
        break;
      case Control: // 控制命令
        cc = ControlCode.GetData1;
        pwd = true;
        break;
      case Atteste: // 身份认证
        cc = ControlCode.GetData1;
        pwd = true;
        break;
      case GetConfig: // 请求终端配置
        cc = ControlCode.GetData2;
        break;
      case GetPara: // 查询参数
        cc = ControlCode.GetData2;
        break;
      case GetData1: // 请求1类数据
        cc = ControlCode.GetData1;
        sc = SeqCode.Con0T0;
        break;
      case GetData2: // 请求2类数据
        cc = ControlCode.GetData2;
        sc = SeqCode.Con0T0;
        break;
      case GetData3: // 请求3类数据
        cc = ControlCode.GetData3;
        sc = SeqCode.Con0T0;
        break;
      case File: // 文件传输
        cc = ControlCode.GetData1;
        pwd = true;
        break;
      case Trans: // 数据转发
        cc = ControlCode.GetData1;
        pwd = true;
        break;
    }
    try {
      SetPacketHeader(buffer, pCommAddr, cc, pAfn, sc);// 头部
      ref<Integer> pos = new ref<Integer>(DataBeginPos);
      SetParaData(buffer, pos, pDt, pDas, pParaItems);// 按测量点循环，按Fn的参数写入数据
      buffer = SetPacketFooter(buffer, pos, pwd);// 是否加密码，计算累加和，重新Resize缓冲区
    } catch (Exception e) {
      throw new ProtoException(e.getMessage());
    }
    return buffer;

  }

  public void SetPacketHeader(byte[] pBuffer, String pCollectAddr, ControlCode pControlCode, AFN pAFN, int pFrameSeq) {
    byte MSAFlag = 2;
    pBuffer[HeadChar1Pos] = PacketHeadChar;
    pBuffer[HeadChar2Pos] = PacketHeadChar;
    // 控制域
    pBuffer[ControlCodePos] = (byte) pControlCode.val;
    // 终端地址
    String collectAddr = Util.PadLeft(pCollectAddr, 8, '0');
    pBuffer[CollectAddrPos] = Byte.parseByte(collectAddr.substring(2, 4), 16);
    pBuffer[CollectAddrPos + 1] = Byte.parseByte(collectAddr.substring(0, 2), 16);

    pBuffer[CollectAddrPos + 2] = Byte.parseByte(collectAddr.substring(6, 8), 16);
    pBuffer[CollectAddrPos + 3] = Byte.parseByte(collectAddr.substring(4, 6), 16);

    pBuffer[CollectAddrPos + 4] = MSAFlag;
    // 应用层功能码
    pBuffer[AFNPos] = (byte) pAFN.val;
    // 帧序列
    pBuffer[FrameSeqPos] = (byte) pFrameSeq;

  }


  public final byte[] SetPacketFooter(byte[] pBuffer, ref<Integer> pos, Boolean pPassword) {
    final int PasswordObjectLen = 16;
    // 附加域(密码)，时间标签不处理了
    if (pPassword) {
      for (int i = 0; i < PasswordObjectLen; i++) {
        pBuffer[pos.v++] = 0;
      }
    }

    long l1 = (long) (pos.v - ControlCodePos) << 2;
    pBuffer[DataLenHiPos] = pBuffer[DataLenHiPos1] = Util.ByteOfLong(l1 + 2, 2);
    pBuffer[DataLenLoPos] = pBuffer[DataLenLoPos1] = Util.ByteOfLong(l1 + 2, 1);

    pBuffer[pos.v] = Util.SumCheck(pBuffer, ControlCodePos, pos.v);
    pBuffer[pos.v + 1] = PacketEndChar;

    int bufferLen = pos.v + 2;

    byte[] buffer = new byte[bufferLen];
    System.arraycopy(pBuffer, 0, buffer, 0, bufferLen);
    return buffer;
  }


  public final void SetParaData(byte[] pBuffer, ref<Integer> pos, int pDt, int[] pDas, DataItem[] pParaItems) throws ParseException {
    if (pDas == null) pDas = new int[]{0};//默认测量点0，操作集中器
    for (int da : pDas) // 测量点循环，一般用于读多个电表的数据
    {
      SetDataDa(pBuffer, pos, da);//设置Da DT
      SetDataDt(pBuffer, pos, pDt);
      if (pParaItems == null) return;
      for (DataItem dataItem : pParaItems)//遍历参数所有的数据项
      {
        if (dataItem.f == DataFmt.None) continue;// 格式为None不处理
        SetData(pBuffer, pos, dataItem.v, dataItem.f);
      }
    }
  }


  public boolean NeedConfirm(byte[] pBuffer) {
    if (pBuffer == null) {
      return false;
    }
    if (pBuffer.length < FrameSeqPos) {
      return false;
    }
    return (pBuffer[FrameSeqPos] & 0x10) > 0;
  }

  public final boolean IsUpPacket(byte[] pBuffer, ref<String> pCollectAddr, ref<AFN> pAfn) {
    if ((pBuffer[ControlCodePos] & 0xC0) == 0xC0) {
      pCollectAddr.v = GetCollectCommAddr(pBuffer);
      pAfn.v = AFN.forValue(pBuffer[AFNPos]);
      return true;
    }
    return false;
  }

  public final boolean IsEndFrame(byte[] pBuffer) {
    if (pBuffer == null) {
      return false;
    }

    if (pBuffer.length == MinPacketLen && pBuffer[0] == PacketHeadChar && pBuffer[8] == PacketEndChar) {
      return true;
    } else if (pBuffer.length < FrameSeqPos) {
      return false;
    }

    int fs = ((pBuffer[FrameSeqPos] & 0x0060) >> 5);
    if ((fs == (byte) FrameTypeBeginOfMulti) || fs == ((byte) FrameTypeMidOfMulti)) {
      return false;
    } else {
      return true;
    }
  }

  public final boolean IsXinTiaoPacket(byte[] pBuffer, ref<String> pCollectAddr) {
    boolean ret = false;
    //131009 事件时是E9 控制域，导致心跳报文解析失败
    if ((pBuffer[ControlCodePos] & (byte) ControlCode.XinTiao.val) > 0 && pBuffer[AFNPos] == (byte) AFN.XinTiao.val) {
      pCollectAddr.v = GetCollectCommAddr(pBuffer);
      ret = true;
    }
    return ret;
  }

  public final byte[] XinTiaoAnswer(String pCollectAddr, byte[] pValidPacket) {
    byte[] buffer = new byte[MaxDownPacketLen];
    ref<Integer> pos = new ref<Integer>(DataBeginPos);
    int collectSeqCode = pValidPacket[FrameSeqPos] & 0x0F;
    byte sc = (byte) (SeqCode.Con0T0 | collectSeqCode);
    SetPacketHeader(buffer, pCollectAddr, ControlCode.ReadYes, AFN.Answer, sc);// 头部
    SetDataDa(buffer, pos, 0);
    SetDataDt(buffer, pos, 1);
    return SetPacketFooter(buffer, pos, false);
  }

  // 解析
  public final List<DataItem> ParsePacket(GwTask pTask, byte[] pBuffer) {
    AFN Afn = AFN.forValue(pBuffer[AFNPos]);
    List<DataItem> results = new ArrayList<DataItem>();
    try {
      String commAddr = GetCollectCommAddr(pBuffer);
      switch (Afn) {
        case Answer:
          Get_Answer(pTask, pBuffer, results);
          break;
        case GetData1:

          Get_Data1(pTask, pBuffer, results);

          break;
      }
    } catch (ParseException e) {
      DataItem errRet = new DataItem("解析失败", e.getMessage());
      e.printStackTrace();
    }
    return results;
  }

  public String GetCollectCommAddr(byte[] pBuffer) {
    String commAddr = String.format("%02X", pBuffer[CollectAddrPos + 1]);
    commAddr += String.format("%02X", pBuffer[CollectAddrPos]);
    int t = pBuffer[CollectAddrPos + 3] * 256 + pBuffer[CollectAddrPos + 2];
    commAddr += String.format("%04X", t);
    return commAddr;
  }

  public final void Get_Answer(GwTask pTask, byte[] pBuffer, List<DataItem> pResults) {
    ref<Integer> pos = new ref<Integer>(DataBeginPos);
    int Da = GetDataDa(pBuffer, pos);
    int Dt = GetDataDt(pBuffer, pos);

    DataItem ret = new DataItem("处理结果", "");
    pResults.add(ret);
    switch (Dt) {
      case 1:
        ret.v = "全部确认";
        break;
      case 2:
        ret.v = "全部否认";
        break;
      case 3:
        ret.v = "逐个确认/否认";
        break;
      default:
        ret.v = "无效确认";
        break;
    }
  }

  public final void Get_Data1(GwTask pTask, byte[] pBuffer, List<DataItem> pResults) throws ParseException {
    ref<Integer> pos = new ref<Integer>(DataBeginPos);
    DataItem ret;
    StringBuilder sb = new StringBuilder();
    int Da, Dt;
    while (pBuffer.length - pos.v > 5) {
      Da = GetDataDa(pBuffer, pos);
      Dt = GetDataDt(pBuffer, pos);
      ret = new DataItem();
      pResults.add(ret);
      switch (Dt) {
        case 2: {
          ret.AddPara("终端日历时钟", GetData(pBuffer, pos, DataFmt.Data1));
        }
        break;
        case 25: {
//          ret.AddPara("抄表时间", GetData(pBuffer, ref readIndex, ParaFormat.Data15));
//
//          sb = new StringBuilder();
//          String.format()
//          sb.AppendFormat("总{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("A相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("B相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("C相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          ret.AddPara("当前有功功率(kW):", sb.ToString());
//
//          sb = new StringBuilder();
//          sb.AppendFormat("总{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("A相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("B相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("C相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          ret.AddPara("当前无功功率(kW):", sb.ToString());
//
//
//          sb = new StringBuilder();
//          sb.AppendFormat("总{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data5));
//          sb.AppendFormat("A相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data5));
//          sb.AppendFormat("B相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data5));
//          sb.AppendFormat("C相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data5));
//          ret.AddPara("当前功率因数(%):", sb.ToString());
//
//          sb = new StringBuilder();
//          sb.AppendFormat("A相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data7));
//          sb.AppendFormat("B相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data7));
//          sb.AppendFormat("C相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data7));
//          ret.AddPara("当前电压(V):", sb.ToString());
//
//          sb = new StringBuilder();
//          sb.AppendFormat("A相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data25));
//          sb.AppendFormat("B相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data25));
//          sb.AppendFormat("C相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data25));
//          sb.AppendFormat("零序{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data25));
//          ret.AddPara("当前电流(A):", sb.ToString());
//
//          sb = new StringBuilder();
//          sb.AppendFormat("总{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("A相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("B相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          sb.AppendFormat("C相{0},", GetData(pBuffer, ref readIndex, ParaFormat.Data9));
//          ret.AddPara("当前视在功率(kVA):", sb.ToString());
        }
        break;
      }
    }
  }


  public final void Get_Data2(GwTask pTask, byte[] pBuffer, List<DataItem> pResults) throws ParseException {
    ref<Integer> pos = new ref<Integer>(DataBeginPos);
    DataItem ret;
    StringBuilder sb = new StringBuilder();
    int Da, Dt;
    while (pBuffer.length - pos.v > 5) {
      Da = GetDataDa(pBuffer, pos);
      Dt = GetDataDt(pBuffer, pos);
      ret = new DataItem();
      pResults.add(ret);
      switch (Dt) {
        case 161:
        case 162:
        case 163:
        case 164: {
//          dataTypeId = Dt > 164 ? EDataType.Month : EDataType.Day;
//          curFormat = ParaFormat.None;
//          valueTime = Convert.ToDateTime(GetData(pBuffer, ref readIndex, Dt > 164 ? ParaFormat.Data21 : ParaFormat.Data20));
//          readTime = Convert.ToDateTime(GetData(pBuffer, ref readIndex, ParaFormat.Data15));
//          m = GetBinValue(pBuffer, ref readIndex, 1);
//          ret.AddPara("测量点", Da);
//          ret.AddPara("数据时标", valueTime);
//          ret.AddPara("抄表时间", readTime);
        }
        break;
      }
    }
  }
}
