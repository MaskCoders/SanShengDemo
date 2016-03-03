package com.sansheng.testcenter.base;

import java.io.IOException;

/**
 * Created by hua on 3/3/16.
 */
public interface ConnInter {
    public void open() throws IOException;
    public void close();
    public void sendMessage(String hex);
    public void sendMessage(byte[] arr);
    public String getConnInfo();


}
