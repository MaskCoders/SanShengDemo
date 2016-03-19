package com.sansheng.testcenter.base;

import hstt.data.ref;

import java.io.IOException;

/**
 * Created by hua on 3/3/16.
 */
public interface ConnInter {
    public void open() throws IOException;
    public void close();
    public void sendMessage(String hex,ref<String> addr);
    public void sendMessage(byte[] arr,ref<String> addr);
    public String getConnInfo();


}
