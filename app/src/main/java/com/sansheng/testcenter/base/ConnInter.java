package com.sansheng.testcenter.base;

import hstt.data.ref;
import hstt.proto.mp07.TaskInterface;

import java.io.IOException;

/**
 * Created by hua on 3/3/16.
 */
public interface ConnInter {
    public void open() throws IOException;
    public void close();
    public void sendMessage(String hex);
    public void sendMessage(byte[] arr);
    public void sendMessage(TaskInterface task);
    void setAddress(ref<String> address);
    public String getConnInfo();


}
