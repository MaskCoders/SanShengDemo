package hstt.proto;

public interface IProto {
  ProtoType getProtoType();

  byte[][] SearchValid(byte[] pBuffer, int pFilledLen);//提取有效报文

  Object Parse(Object pObject, byte[] pBuffer);//报文解析

  byte[] Build(Object pObject);//根据任务对象组包

  boolean IsEndFrame(byte[] pBuffer);//判断是否有连续帧
}