package hstt.enums;

import java.util.List;

/**
 * Created by nboy on 2016-03-17.
 */
public class EnumBase implements IEnumField {
  public String txt;
  public int val;


  public EnumBase() {

  }

  public EnumBase(int val, String txt) {
    this.val = val;
    this.txt = txt;
    //lst.add(this);
  }

  protected List<IEnumField> getEfLst() {
    return null;
  }


  public int getPos(int val) {
    List<IEnumField> lst = getLst();
    for (int i = 0; i < lst.size(); i++) {
      if (lst.get(i).getVal() == val) return i;
    }
    return 0;
  }

  public List<IEnumField> getLst() {
    return getEfLst();
  }

  public String getTxt() {
    return txt;
  }

  public int getVal() {
    return val;
  }

}
