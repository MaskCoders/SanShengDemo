package hstt.proto.mp07;

/**
 * Created by nboy on 2016-03-11.
 */
public class MpTask {
  public String MeterAddr;
  public ControlCode ControlCode;
  public long Di;
  public byte[] Data;

  public MpTask() {

  }

  public MpTask(String meterAddr, ControlCode controlCode, long di, byte[] data) {
    MeterAddr = meterAddr;
    ControlCode = controlCode;
    Di = di;
    Data = data;
  }

}
