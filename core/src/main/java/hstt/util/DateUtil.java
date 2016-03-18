package hstt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nboy on 2016-03-14.
 */
public class DateUtil {

  private Date val = null;
  private Calendar cal = Calendar.getInstance();
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式

  static {
  }

  public DateUtil() {

  }

  public DateUtil(String pDateTime) throws ParseException {

    val = sdf.parse(pDateTime);
    cal.setTime(val);
    cal.setFirstDayOfWeek(Calendar.MONDAY);
  }

  public Date GetVal() {
    return val;
  }

  public int Get(int field) {
    return cal.get(field);
  }


  public int GetWeek() {
    int ww = cal.get(Calendar.DAY_OF_WEEK)-1;
    if (ww == 0) ww = 7;
    return ww;
  }


  public byte GetBcd(int field) {
    if (field == Calendar.MONTH) return Byte.parseByte(String.valueOf(cal.get(field) + 1), 16);
    if (field == Calendar.YEAR) return Byte.parseByte(String.valueOf(cal.get(field) % 100), 16);
    return Byte.parseByte(String.valueOf(cal.get(field)), 16);
  }

  public String ToLongStr() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(val);
  }

  @Override
  public String toString() {
    return sdf.format(val);
  }
}
