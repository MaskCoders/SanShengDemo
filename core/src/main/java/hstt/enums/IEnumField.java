package hstt.enums;

import java.util.List;

/**
 * Created by nboy on 2016-03-17.
 */
public interface IEnumField {
  int getVal();

  String getTxt();

  int getPos(int val);
  List<IEnumField> getLst();
}
