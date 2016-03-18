package hstt.proto;

import hstt.proto.mp07.Mp07;
import hstt.proto.upgw.UpGw;

import java.util.HashMap;

/**
 * Created by nboy on 2016-03-11.
 */
public class ProtoFactory {

  //创建实例类
  public static IProto Create(ProtoType type) {
   HashMap<ProtoType, IProto> protos = new HashMap<ProtoType, IProto>();

    if (!protos.containsKey(type)) {
      switch (type) {
        case UpGw:
          protos.put(type, new UpGw());
          break;
        case Mp07:
          protos.put(type, new Mp07());
          break;
      }
    }
    return protos.get(type);
  }

}
