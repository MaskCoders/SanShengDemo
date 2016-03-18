package hstt.update;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.os.StatFs;

import hstt.core.Config;

public class FileManager {


  /**
   * 是否安装了SD卡
   *
   * @return
   */
  public static boolean existSDCard() {
    if (android.os.Environment.getExternalStorageState().equals(
        android.os.Environment.MEDIA_MOUNTED)) {
      return true;
    } else
      return false;
  }


  /**
   * 得到SD卡的根目录
   *
   * @return
   */
  public static File getSDDirectory() {
    return Environment.getExternalStorageDirectory();
  }


  /**
   * 得到SD卡可用字节数
   *
   * @return 字节数
   */
  public static long getSDFreeSize() {
    //取得SD卡文件路径
    File path = Environment.getExternalStorageDirectory();
    StatFs stat = new StatFs(path.getPath());
//		      return stat.getAvailableBytes();
    //这个必须使用这个不建议使用的方法，否则真机会报 java.lang.NoSuchMethodError: android.os.StatFs.getAvailableBytes 错误
    long blockSize = stat.getBlockSize();
    return stat.getAvailableBlocks() * blockSize;

  }


  /**
   * 得到机身可用空间 单位字节
   *
   * @return 可用字节
   */
  public static long getRomAvailableSize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
//				return stat.getAvailableBytes();
    long blockSize = stat.getBlockSize();
    return stat.getAvailableBlocks() * blockSize;
  }


  /**
   * 得到SD卡可以用空间
   *
   * @return
   */
  public static long getSDCardAvailableSize() {
    File path = getSDDirectory();//取得sdcard文件路径
    StatFs stat = new StatFs(path.getPath());
//			 return stat.getAvailableBytes();
    long blockSize = stat.getBlockSize();
    return stat.getAvailableBlocks() * blockSize;
  }


  /**
   * 得到可用空间，
   * 如果有SD卡，则返回sd卡的可用空间
   * 如果没有SD卡，则返回内部存储空间
   *
   * @return
   */
  public static long getAvailableSize() {
    if (existSDCard()) {
      return getSDCardAvailableSize();
    }
    return getRomAvailableSize();
  }

  /**
   * 根据目录名得到文件
   * 如果设备没有SD卡，则返回机身存储位置
   *
   * @param folderpath
   * @return
   */

  public static File getSaveDirectcory(String folderpath) {
    // 当没有SD卡时的处理，直接存储到手机内存，存储缓存文件要小点

    if (existSDCard()) {
      File file = getSDDirectory();
      File file2 = new File(file.getPath() + File.separator + folderpath);
      return file2;
    } else {
      File file = new File(File.separator + folderpath);
      return file;
    }

  }


  public static File getHiborSaveDirectcory(String folderpath) {
    // 当没有SD卡时的处理，直接存储到手机内存，存储缓存文件要小点

    return getSaveDirectcory(Config.ROOT + File.separator + folderpath);

  }

  /**
   * 得到hibor 存储的根目录
   *
   * @return
   */
  public static File getHiborSaveRootDirectrory() {
    return getSaveDirectcory(Config.ROOT);
  }

  public static File getHiborSaveFile(String folderpath, String fileName) {
    File filep = getHiborSaveDirectcory(folderpath);
    if (!filep.exists()) {
      filep.mkdirs();
    }

    File file = new File(filep.getPath() + File.separator + fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        file = null;
        e.printStackTrace();
      }
    }
    return file;

  }


  public static String getHiborSaveFilePath(String folderpath, String fileName) {
    File filep = getHiborSaveDirectcory(folderpath);
    if (!filep.exists()) {
      filep.mkdirs();
    }

    File file = new File(filep.getPath() + File.separator + fileName);
//			if (!file.exists()) {
//				try {
//					file.createNewFile();
//				} catch (IOException e) {
//					file = null;
//					e.printStackTrace();
//				}	
//			}
    return file.getPath();

  }


  /**
   * 根据文件夹和文件名
   *
   * @param folderpath
   * @param fileName
   * @return
   */
  public static File getSaveFilePath(String folderpath, String fileName) {
    File filep = getSaveDirectcory(folderpath);
    if (!filep.exists()) {
      filep.mkdirs();
    }

    File file = new File(filep.getPath() + File.separator + fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        file = null;
        e.printStackTrace();
      }
    }
    return file;
  }


  /**
   * 删除文件夹
   *
   * @param file
   */
  public static void delete(File file) {
    if (file.isFile()) {
      file.delete();
      return;
    }

    if (file.isDirectory()) {
      File[] childFiles = file.listFiles();
      if (childFiles == null || childFiles.length == 0) {
        file.delete();
        return;
      }

      for (int i = 0; i < childFiles.length; i++) {
        delete(childFiles[i]);
      }
      file.delete();
    }
  }


}
