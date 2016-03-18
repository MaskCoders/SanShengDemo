package hstt.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;

import android.os.Environment;
import android.util.Log;

import hstt.core.Config;

/**
 * Log���ߣ�����android.util.Log??tag�Զ���������??
 * customTagPrefix:className.methodName(Line:lineNumber),
 * customTagPrefixΪ��ʱֻ�����className.methodName(Line:lineNumber)??
 * http://blog.csdn.net/finddreams
 */
public class Logger {

  public static String customTagPrefix = "nb"; // �Զ���Tag��ǰ????������������
  private static boolean isSaveLog = true; // �Ƿ�ѱ�����־��SD����
  public static boolean allowD = true;
  /// mnt/sharedd/"; //
  /// Environment.getExternalStorageDirectory().getPath()
  // +
  // "/hstt/";
  // //
  // SD���еĸ�Ŀ¼
  private static final String PATH_LOG_INFO = Config.ROOT + "log/";

  static {
    Map<String, String> map = System.getenv();
    if (map.containsKey("SESSIONNAME")) {
      allowD = false;
      isSaveLog = false;
    }

  }

  private Logger() {
  }


  public static void d(String format, Object... args) {
    String content = String.format(format, args);
    StackTraceElement caller = getCallerStackTraceElement();
    String tag = generateTag(caller);
    if (allowD) {
      Log.e(tag, content);
      point(PATH_LOG_INFO, tag, content);
    } else {
      System.out.println(tag + "\t\t" + content);
    }
  }

  public static void d(String content, Throwable tr) {
    String ss = content + " Error:" + tr.getMessage();
    StackTraceElement caller = getCallerStackTraceElement();
    String tag = generateTag(caller);
    if (allowD) {
      Log.e(tag, content);
      point(PATH_LOG_INFO, tag, ss);
    } else {
      System.out.println(tag + "\t\t" + ss);
    }
  }


  private static String generateTag(StackTraceElement caller) {
    String tag = "%s.%s.%d"; // ռλ??
    String callerClazzName = caller.getClassName(); // ��ȡ����??
    callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
    tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber()); // �滻
    // tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix +
    // ":" + tag;
    return tag;
  }

  private static StackTraceElement getCallerStackTraceElement() {

    return Thread.currentThread().getStackTrace()[3];
  }

  public static void point(String path, String tag, String msg) {
    if (!isSaveLog) return;
    if (isSDAva()) {
      Date date = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
      dateFormat.applyPattern("yyyy-MM-dd");
      // path = path + dateFormat.format(date) + "/";
      // dateFormat.applyPattern("MM");
      // path += dateFormat.format(date) + "/";
      // dateFormat.applyPattern("dd");
      path += dateFormat.format(date) + ".log";
      dateFormat.applyPattern("HH:mm:ss.SSS");
      String time = dateFormat.format(date);
      File file = new File(path);
      if (!file.exists()) createDipPath(path);
      // Log.d(tag, path);
      BufferedWriter out = null;
      try {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
        out.write(time + " " + tag + " " + msg + "\r\n");
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (out != null) {
            out.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * �����ļ�·�� �ݹ鴴���ļ�
   *
   * @param file
   */
  public static void createDipPath(String file) {
    String parentFile = file.substring(0, file.lastIndexOf("/"));
    File file1 = new File(file);
    File parent = new File(parentFile);
    if (!file1.exists()) {
      parent.mkdirs();
      try {
        file1.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * A little trick to reuse a formatter in the same thread
   */
  private static class ReusableFormatter {

    private Formatter formatter;
    private StringBuilder builder;

    public ReusableFormatter() {
      builder = new StringBuilder();
      formatter = new Formatter(builder);
    }

    public String format(String msg, Object... args) {
      formatter.format(msg, args);
      String s = builder.toString();
      builder.setLength(0);
      return s;
    }
  }

  private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
    protected ReusableFormatter initialValue() {
      return new ReusableFormatter();
    }
  };

  public static String format(String msg, Object... args) {
    ReusableFormatter formatter = thread_local_formatter.get();
    return formatter.format(msg, args);
  }

  public static boolean isSDAva() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageDirectory().exists()) {
      return true;
    } else {
      return false;
    }
  }

}