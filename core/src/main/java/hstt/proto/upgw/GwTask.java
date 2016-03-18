package hstt.proto.upgw;

import hstt.data.DataItem;

/**
 * Created by nboy on 2016-03-10.
 */
public class GwTask {
  public String CommAddr;
  public int Afn;
  public int Fn;
  public int[] Das;
  public DataItem[] Params;

  public GwTask(String pCommAddr, int pAfn, int pFn, DataItem[] pParams, int[] pDas) {
    CommAddr = pCommAddr;
    Afn = pAfn;
    Fn = pFn;
    Params = pParams;
    Das = pDas;
  }
}
