package hstt.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 数据项 用于表示规约中的参数和数据 n是名称，v数据，f是数据格式或者数据转换的类型指示
 */
public class DataItem implements Serializable {
  public String n = "";
  public String v = "";
  public int f = DataFmt.None;
  public List<DataItem> Childs = null;

  public DataItem() {

  }

  public void AddPara(String pName, Object pValue) {
    n = pName;
    v = pValue.toString();
  }

  public DataItem(String pName, String pValue) {
    n = pName;
    v = pValue;
  }

  public DataItem(String pName, int pFormat, String pValue) {
    n = pName;
    v = pValue;
    f = pFormat;
  }

  //块数据，单位，格式相同 单位使用v字段存储
  public DataItem(String Title, int pFormat, String pUnit, String[] Subs) {
    this.n = Title;
    this.f = DataFmt.Block;
    this.Childs = new ArrayList<DataItem>();
    for (String item : Subs) {
      this.Childs.add(new DataItem(item, pFormat, pUnit));
    }
  }

  public DataItem(String Title, String[] Subs) {
    this.n = Title;
    this.f = DataFmt.Block;
    this.Childs = new ArrayList<DataItem>();
    for (String item : Subs) {
      this.Childs.add(new DataItem(item, DataFmt.None, ""));
    }
  }

  //子数据
  public DataItem(String Title, List<DataItem> Items) {
    this.n = Title;
    this.f = DataFmt.SubList;
    this.Childs = Items;
  }

  @Override
  public String toString() {
    return String.format("%s %s %d", n, v, f);
  }
}
