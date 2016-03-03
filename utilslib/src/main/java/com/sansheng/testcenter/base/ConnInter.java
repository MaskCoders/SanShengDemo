package com.sansheng.testcenter.base;

/**
 * Created by hua on 3/3/16.
 */
public interface ConnInter {
    public void open();
    public void close();
    public void sendMessage(String hex);
    public void sendMessage(byte[] arr[]);

}
