package hstt.proto;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;

import hstt.data.DataFmt;
import hstt.data.ref;
import hstt.util.DateUtil;
import hstt.util.Logger;
import hstt.util.Util;

public class ProtoBase {


  public static void SetData(byte[] pBuffer, ref<Integer> pos, String paraValue, int pDataFmt) throws ParseException {

    switch (pDataFmt) {
      case DataFmt.Da:
        SetDataDa(pBuffer, pos, Integer.parseInt(paraValue));
        break;
      case DataFmt.Bcd1:
        SetBcdData(pBuffer, pos, paraValue, 2, 1, false);
        break;
      case DataFmt.Bcd2:
        SetBcdData(pBuffer, pos, paraValue, 4, 1, false);
        break;
      case DataFmt.Bcd3:
        SetBcdData(pBuffer, pos, paraValue, 6, 1, false);
        break;
      case DataFmt.BinN: {
        byte[] data = Util.StringToByteArray(paraValue);
        System.arraycopy(data, 0, pBuffer, pos.v, data.length);
        pos.v += data.length;
      }
      break;
      case DataFmt.Bin2: {
        int val = Integer.parseInt(paraValue);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 1);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 2);
      }
      break;
      case DataFmt.Bin3: {
        long val = Long.parseLong(paraValue);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 1);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 2);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 3);
      }
      break;
      case DataFmt.Bin4: {
        long val = Long.parseLong(paraValue);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 1);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 2);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 3);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 4);
      }
      break;
      case DataFmt.Bs8:
      case DataFmt.Bin1: {
        byte val = Byte.parseByte(paraValue);
        pBuffer[pos.v++] = val;
      }
      break;

      case DataFmt.Bs16: {
        int val = Integer.parseInt(paraValue);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 2); //注意这里不倒叙
        pBuffer[pos.v++] = Util.ByteOfLong(val, 1);
      }
      break;
      case DataFmt.Bs24: {
        int val = Integer.parseInt(paraValue);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 3); //注意这里不倒叙
        pBuffer[pos.v++] = Util.ByteOfLong(val, 2); //注意这里不倒叙
        pBuffer[pos.v++] = Util.ByteOfLong(val, 1);
      }
      break;
      case DataFmt.Bs32: {
        int val = Integer.parseInt(paraValue);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 4); //注意这里不倒叙
        pBuffer[pos.v++] = Util.ByteOfLong(val, 3);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 2);
        pBuffer[pos.v++] = Util.ByteOfLong(val, 1);
      }
      break;
      case DataFmt.Bs64: //注意这里用字符串二进制表示
      {
        String para = Util.PadRight(paraValue, 64, '0');
        int bitPos = 0;
        for (int b = 0; b < 8; b++) {
          for (int i = 0; i < 8; i++) {
            int bit = Byte.parseByte(para.substring(bitPos++, bitPos++ + 1)) << i;
            pBuffer[pos.v] = (byte) (pBuffer[pos.v] | bit);
          }
          pos.v++;
        }
      }
      break;


      case DataFmt.Data1: {
        DateUtil du = new DateUtil(paraValue);
        Logger.d(du.ToLongStr());
        pBuffer[pos.v++] = du.GetBcd(Calendar.SECOND);
        pBuffer[pos.v++] = du.GetBcd(Calendar.MINUTE);
        pBuffer[pos.v++] = du.GetBcd(Calendar.HOUR_OF_DAY);
        pBuffer[pos.v++] = du.GetBcd(Calendar.DATE);
        byte mm = (byte) ((du.Get(Calendar.MONTH) + 1) & 0x1f);
        byte ww = (byte) (du.GetWeek());
        ww = (byte) ((ww & 0x07) << 5);//D5～D7编码表示0～7，0：无效，1～7依次表示星期一至星期日。
        pBuffer[pos.v++] = (byte) (ww | mm);
        pBuffer[pos.v++] = du.GetBcd(Calendar.YEAR);
      }
      break;
      case DataFmt.Data5:
        SetBcdData(pBuffer, pos, paraValue, 2, 10, true);
        break;
      case DataFmt.Data6:
        SetBcdData(pBuffer, pos, paraValue, 2, 100, true);
        break;
      case DataFmt.Data7:
        SetBcdData(pBuffer, pos, paraValue, 2, 10, false);
        break;
      case DataFmt.Data8:
        SetBcdData(pBuffer, pos, paraValue, 2, 1, false);
        break;
      case DataFmt.Data12: {
        String val = Util.PadLeft(paraValue, 12, '0');
        pBuffer[pos.v++] = Byte.parseByte(val.substring(10, 12), 16);
        pBuffer[pos.v++] = Byte.parseByte(val.substring(8, 10), 16);
        pBuffer[pos.v++] = Byte.parseByte(val.substring(6, 8), 16);
        pBuffer[pos.v++] = Byte.parseByte(val.substring(4, 6), 16);
        pBuffer[pos.v++] = Byte.parseByte(val.substring(2, 4), 16);
        pBuffer[pos.v++] = Byte.parseByte(val.substring(0, 2), 16);
      }
      break;

      case DataFmt.Data15: {
        DateUtil du = new DateUtil(paraValue);
        pBuffer[pos.v++] = du.GetBcd(Calendar.MINUTE);
        pBuffer[pos.v++] = du.GetBcd(Calendar.HOUR_OF_DAY);
        pBuffer[pos.v++] = du.GetBcd(Calendar.DATE);
        pBuffer[pos.v++] = du.GetBcd(Calendar.MONTH);
        pBuffer[pos.v++] = du.GetBcd(Calendar.YEAR);
      }
      break;
      case DataFmt.Data18: {
        pBuffer[pos.v++] = Byte.parseByte(paraValue.substring(4, 6), 16);
        pBuffer[pos.v++] = Byte.parseByte(paraValue.substring(2, 4), 16);
        pBuffer[pos.v++] = Byte.parseByte(paraValue.substring(0, 2), 16);
      }
      break;
      case DataFmt.Data19: {

        DateUtil du = new DateUtil(paraValue);
        pBuffer[pos.v++] = du.GetBcd(Calendar.MINUTE);
        pBuffer[pos.v++] = du.GetBcd(Calendar.HOUR_OF_DAY);
      }
      break;
      case DataFmt.Data20: {
        DateUtil du = new DateUtil(paraValue);
        pBuffer[pos.v++] = du.GetBcd(Calendar.HOUR_OF_DAY);
        pBuffer[pos.v++] = du.GetBcd(Calendar.MONTH);
        pBuffer[pos.v++] = du.GetBcd(Calendar.YEAR);
      }
      break;
      case DataFmt.Data21: {
        DateUtil du = new DateUtil(paraValue);
        pBuffer[pos.v++] = du.GetBcd(Calendar.MONTH);
        pBuffer[pos.v++] = du.GetBcd(Calendar.YEAR);
      }
      break;
      case DataFmt.Data23:
        SetBcdData(pBuffer, pos, paraValue, 3, 10000, false);
        break;
      case DataFmt.Data24:
        SetBcdData(pBuffer, pos, paraValue, 2, 1, false);
        break;
      case DataFmt.Data25:
        SetBcdData(pBuffer, pos, paraValue, 3, 1000, true);
        break;
      case DataFmt.Data26:
        SetBcdData(pBuffer, pos, paraValue, 2, 1000, false);
        break;
      case DataFmt.Data27:
        SetBcdData(pBuffer, pos, paraValue, 4, 1, false);
        break;
      case DataFmt.ASCII:
      case DataFmt.ASCII12:
      case DataFmt.ASCII16:
      case DataFmt.ASCII32: {
        int len = 0;
        if (pDataFmt == DataFmt.ASCII12) len = 12;
        if (pDataFmt == DataFmt.ASCIIRev16) len = 16;
        if (pDataFmt == DataFmt.ASCII16) len = 16;
        if (pDataFmt == DataFmt.ASCII32) len = 32;
        if (pDataFmt == DataFmt.ASCIIRev32) len = 32;
        String data = paraValue;
        if (paraValue.length() > len) {
          data = paraValue.substring(0, len);
        }

        if (pDataFmt == DataFmt.ASCIIRev32 || pDataFmt == DataFmt.ASCIIRev16) {
          data = new StringBuilder(data).reverse().toString();
        }

        byte[] strBytes = new byte[0];
        try {
          strBytes = data.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        System.arraycopy(strBytes, 0, pBuffer, pos.v, strBytes.length);
        pos.v += data.length();
        if (len > 0) //补全
        {
          for (int i = 0; i < len - data.length(); i++) {
            pBuffer[pos.v++] = 0;
          }
        }

      }
      break;
    }
  }


  public static final void SetBcdData(byte[] pBuffer, ref<Integer> pos, String paraValue, int len, int mult, boolean canNeg) {
    //		long valorg = Convert.ToInt64(Convert.ToDecimal(paraValue) * mult);
    Float v = Float.parseFloat(paraValue);
    long valorg = (long) (v * mult);
    String val = Util.PadLeft(String.valueOf(Math.abs(valorg)), len * 2, '0');
    for (int i = 0; i < len; i++) {
      pBuffer[pos.v++] = Integer.valueOf(val.substring((len - i - 1) * 2, (len - i - 1) * 2 + 2), 16).byteValue();
    }
    if (canNeg && (valorg < 0)) {
      pBuffer[pos.v - 1] = (byte) (pBuffer[pos.v - 1] | 0x80);
    }
  }

  public static void SetDataDa(byte[] pBuffer, ref<Integer> pos, int pDA) {
    switch (pDA) {
      case 0:
        pBuffer[pos.v++] = 0;
        pBuffer[pos.v++] = 0;
        break;
      case 0xFFFF:
        pBuffer[pos.v++] = (byte) 0xFF;
        pBuffer[pos.v++] = (byte) 0xFF;
        break;
      default:
        int da = pDA;
        int da1 = 1;
        int da2 = 1;
        while (da2 < 255) {
          int daMax = (da2) * 8;
          if (daMax >= da) {
            da1 = da1 << (7 - (daMax - da));
            break;
          }
          da2++;
        }

        pBuffer[pos.v++] = (byte) da1;
        pBuffer[pos.v++] = (byte) da2;
        break;
    }
  }

  public static void SetDataDt(byte[] pBuffer, ref<Integer> pos, int pDT) {

    int fn = pDT;
    int dt1 = 1;
    int dt2 = 0;
    while (dt2 < 255) {
      int dtMax = (dt2 + 1) * 8;
      if (dtMax >= fn) {
        dt1 = dt1 << (7 - (dtMax - fn));
        break;
      }
      dt2++;
    }
    pBuffer[pos.v++] = (byte) dt1;
    pBuffer[pos.v++] = (byte) dt2;
  }


  //解码

  public static final Object GetData(byte[] pBuffer, ref<Integer> pos, int pFormat) throws ParseException {
    switch (pFormat) {
      case DataFmt.Bin1: {
        return pBuffer[pos.v++];
      }
      case DataFmt.Bin2: {
        int v = pBuffer[pos.v++];
        v += pBuffer[pos.v++] * 256;
        return v;
      }
      case DataFmt.Bin3: {
        int v = pBuffer[pos.v++];
        v += pBuffer[pos.v++] * 256;
        v += pBuffer[pos.v++] * 256 * 256;
        return v;
      }
      case DataFmt.Bin4: {
        long v = pBuffer[pos.v++];
        v += (long) pBuffer[pos.v++] * 256;
        v += (long) pBuffer[pos.v++] * 256 * 256;
        v += (long) pBuffer[pos.v++] * 256 * 256 * 256;
        return v;
      }

      case DataFmt.Bs8:
        return GetBsStr(pBuffer, pos, 1);
      case DataFmt.Bs16:
        return GetBsStr(pBuffer, pos, 2);
      case DataFmt.Bs24:
        return GetBsStr(pBuffer, pos, 3);
      case DataFmt.Bs32:
        return GetBsStr(pBuffer, pos, 4);
      case DataFmt.Bs64: //注意顺序是反的，比如Erc1 是 第一个字节的0位， 09 3A 00 80 31 11 02 00 Erc1 Erc4
        return GetBsStr(pBuffer, pos, 8);
      case DataFmt.Data1: {
        String[] weekStr = new String[]{"无效星期", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        int ss = GetBcdInt(pBuffer, pos, 1);
        int nn = GetBcdInt(pBuffer, pos, 1);
        int hh = GetBcdInt(pBuffer, pos, 1);
        int dd = GetBcdInt(pBuffer, pos, 1);
        int wwmm = pBuffer[pos.v++];
        int mm = Integer.parseInt(String.format("%02X", (wwmm & 0x1F)));
        int ww = (wwmm & 0x00e0) >> 5;
        int yy = GetBcdInt(pBuffer, pos, 1);
        String dtStr = String.format("20%1$s-%2$s-%3$s %4$s:%5$s:%6$s", yy, mm, dd, hh, nn, ss);
        DateUtil du = new DateUtil(dtStr);
        if (du.GetWeek() != ww) {
          throw new ProtoException("星期错误");
        }
        return du.ToLongStr();
      }
      case DataFmt.Data2: {
        GetBcdValue(pBuffer, pos, 2, 10, true);

        byte gs = pBuffer[pos.v + 1];
        int g = (gs & 0x10) >> 5;
        int s = (gs & 0x10) > 0 ? -1 : 1;
        g = 4 - g;//0-10000,1-1000....7-0.001
        pBuffer[pos.v + 1] = (byte) (gs & 0x0F);
        return GetBcdValue(pBuffer, pos, 2, 1, false) * s * Math.pow(10, g);
      }
      case DataFmt.Data3: {
        byte gs = pBuffer[pos.v + 3];
        int g = (gs & 0x40) > 0 ? 1000 : 1;
        int s = (gs & 0x10) > 0 ? -1 : 1;
        pBuffer[pos.v + 3] = (byte) (gs & 0x0F);
        return GetBcdValue(pBuffer, pos, 4, 1, false) * s * g;
      }
      case DataFmt.Data4:
        return GetBcdValue(pBuffer, pos, 1, 1, true);
      case DataFmt.Data5:
        return GetBcdValue(pBuffer, pos, 2, 10, true);
      case DataFmt.Data6:
        return GetBcdValue(pBuffer, pos, 2, 100, true);
      case DataFmt.Data7:
        return GetBcdValue(pBuffer, pos, 2, 10, false);
      case DataFmt.Data8:
        return GetBcdValue(pBuffer, pos, 2, 1, false);

      case DataFmt.Data9:
        return GetBcdValue(pBuffer, pos, 3, 10000, true);

      case DataFmt.Data10:
        return GetBcdValue(pBuffer, pos, 3, 1, false);

      case DataFmt.Data11:
        return GetBcdValue(pBuffer, pos, 4, 100, false);

      case DataFmt.Data12:
        return GetBcdValue(pBuffer, pos, 6, 1, false);

      case DataFmt.Data13:
        return GetBcdValue(pBuffer, pos, 4, 10000, false);

      case DataFmt.Data14:
        return GetBcdValue(pBuffer, pos, 5, 10000, false);
      case DataFmt.Data15: {
        String nn = GetBcdString(pBuffer, pos, 1);
        String hh = GetBcdString(pBuffer, pos, 1);
        String dd = GetBcdString(pBuffer, pos, 1);
        String mm = GetBcdString(pBuffer, pos, 1);
        String yy = GetBcdString(pBuffer, pos, 1);
        String dtStr = String.format("20%1$s-%2$s-%3$s %4$s:%5$s:00", yy, mm, dd, hh, nn);
        return new DateUtil(dtStr).toString();
      }
      case DataFmt.Data16: {
        String ss = GetBcdString(pBuffer, pos, 1);
        String nn = GetBcdString(pBuffer, pos, 1);
        String hh = GetBcdString(pBuffer, pos, 1);
        String dd = GetBcdString(pBuffer, pos, 1);
        return String.format("%1$s日 %2$s:%3$s:%4$s", dd, hh, nn, ss);
      }
      case DataFmt.Data17: {
        String nn = GetBcdString(pBuffer, pos, 1);
        String hh = GetBcdString(pBuffer, pos, 1);
        String dd = GetBcdString(pBuffer, pos, 1);
        String mm = GetBcdString(pBuffer, pos, 1);

        return String.format("%1$s-%2$s %3$s:%4$s", mm, dd, hh, nn);
      }
      case DataFmt.Data18: {
        String nn = GetBcdString(pBuffer, pos, 1);
        String hh = GetBcdString(pBuffer, pos, 1);
        String dd = GetBcdString(pBuffer, pos, 1);
        return String.format("%1$s %2$s:%3$s", dd, hh, nn);
      }
      case DataFmt.Data19: {
        String nn = GetBcdString(pBuffer, pos, 1);
        String hh = GetBcdString(pBuffer, pos, 1);
        return String.format("%1$s:%2$s", hh, nn);
      }
      case DataFmt.Data20: {
        String dd = GetBcdString(pBuffer, pos, 1);
        String mm = GetBcdString(pBuffer, pos, 1);
        String yy = GetBcdString(pBuffer, pos, 1);
        String dtStr = String.format("20%1$s-%2$s-%3$s 00:00:00", yy, mm, dd);
        return new DateUtil(dtStr).toString();
      }
      case DataFmt.Data21: {
        String mm = GetBcdString(pBuffer, pos, 1);
        String yy = GetBcdString(pBuffer, pos, 1);
        String dtStr = String.format("20%1$s-%2$s-01 00:00:00", yy, mm);
        return new DateUtil(dtStr).toString();
      }

      case DataFmt.Data22: {
        return GetBcdValue(pBuffer, pos, 1, 1, false);
      }
      case DataFmt.Data23: {
        return GetBcdValue(pBuffer, pos, 3, 10000, false);
      }
      case DataFmt.Data24: {

        String mm = GetBcdString(pBuffer, pos, 1);
        String yy = GetBcdString(pBuffer, pos, 1);
        return String.format("%1$s日%2$s时", yy, mm);
      }
      case DataFmt.Data25: {
        return GetBcdValue(pBuffer, pos, 3, 1000, true);
      }
      case DataFmt.Data26: {
        return GetBcdValue(pBuffer, pos, 2, 1000, false);
      }
      case DataFmt.Data27: {
        return GetBcdValue(pBuffer, pos, 4, 1, false);
      }

      case DataFmt.YYMMDDWW: {

        String dd = GetBcdString(pBuffer, pos, 1);
        String mm = GetBcdString(pBuffer, pos, 1);
        String yy = GetBcdString(pBuffer, pos, 1);
        String ww = GetBcdString(pBuffer, pos, 1);
        return String.format("20%1$s-%2$s-%3$s ", yy, mm, dd) + "星期" + ww;
      }
      case DataFmt.hhmmss: {

        String ss = GetBcdString(pBuffer, pos, 1);
        String mm = GetBcdString(pBuffer, pos, 1);
        String hh = GetBcdString(pBuffer, pos, 1);
        String ww = GetBcdString(pBuffer, pos, 1);
        return String.format("%1$s:%2$s:%3$s ", hh, mm, ss);
      }
    }
    throw new ProtoException("不支持数据类型" + pFormat);
  }

  public static final String GetBsStr(byte[] pBuffer, ref<Integer> pos, int pLen) {

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < pLen; i++) {
      byte b = pBuffer[pos.v++];
      for (int bit = 0; bit < 8; bit++) {
        sb.append(((b >> bit) & 0x01) > 0 ? "1" : "0");
      }
    }
    return sb.toString();
  }

  public static final float GetBcdValue(byte[] pBuffer, ref<Integer> pos, int pLen, int pDivisor, boolean pSigned) {
    int pOrg = pos.v;
    int sign = 1;
    if (pSigned && (pBuffer[pLen + pos.v - 1] & 0x80) > 0) sign = -1;
    try {
      StringBuilder sb = new StringBuilder(pLen * 2);
      for (int i = 0; i < pLen; i++) {
        byte value = pBuffer[i + pos.v];
        if (i == pLen - 1) value = (byte) (value & 0x007F);
        String valstr = String.format("%02X", value).toUpperCase();
        if ((valstr.equals("EE")) || (valstr.equals("FF"))) {
          pos.v = pOrg + pLen;
          return -1f;
        }
        sb.insert(0, valstr);
      }
      pos.v += pLen;
      return Float.parseFloat(sb.toString()) * sign / pDivisor;
    } catch (java.lang.Exception e) {
      pos.v = pOrg + pLen;
      return -1;
    }
  }

  public static final int GetBcdInt(byte[] pBuffer, ref<Integer> pos, int pLen) {
    return Integer.parseInt(GetBcdString(pBuffer, pos, pLen));
  }

  public static final String GetBcdString(byte[] pBuffer, ref<Integer> pos, int pLen) {
    String ret = "";
    for (int i = 0; i < pLen; i++) {
      ret = String.format("%02X", pBuffer[i + pos.v]) + ret;
    }
    pos.v += pLen;
    return ret;
  }

  public static final int GetBs8Int(String bs8, int pFromD, int pToD) {
    return Integer.parseInt(bs8.substring(7 - pToD, 7 - pToD + pToD - pFromD + 1), 2);
  }

  public static final int GetDataDt(byte[] pBuffer, ref<Integer> pos) {
    byte pDt1 = pBuffer[pos.v++];
    byte pDt2 = pBuffer[pos.v++];
    int fn = 0;
    int dt1 = pDt1;

    fn = pDt2 * 8;
    int c = 0;
    while (dt1 > 0) {
      c++;
      dt1 = dt1 >> 1;
    }
    fn += c;
    return fn;
  }

  public static final int GetDataDa(byte[] pBuffer, ref<Integer> pos) {

    byte pDa1 = pBuffer[pos.v++];
    byte pDa2 = pBuffer[pos.v++];
    if (pDa1 == 0 && pDa2 == 0) {
      return 0;
    }
    if (pDa1 == 0xFF && pDa2 == 0xFF) {
      return 0;
    }
    int da = 0;
    int da1 = pDa1;

    da = (pDa2 - 1) * 8;
    int c = 0;
    while (da1 > 0) {
      c++;
      da1 = da1 >> 1;
    }
    da += c;
    return da;
  }
}