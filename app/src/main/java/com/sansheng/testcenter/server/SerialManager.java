package com.sansheng.testcenter.server;

import android.content.Context;
import android.widget.Toast;
import com.sansheng.testcenter.tools.serial.SerialHelper;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by hua on 16-1-24.
 */
public class SerialManager {
    Context mContext;

    private void OpenComPort(SerialHelper ComPort){
        try
        {
            ComPort.open();
        } catch (SecurityException e) {
            ShowMessage("打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            ShowMessage("打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            ShowMessage("打开串口失败:参数错误!");
        }

    }
    private void SetLoopData(SerialHelper ComPort,String sLoopData){
            ComPort.setHexLoopData(sLoopData);
    }
    private void ShowMessage(String sMsg)
    {
        Toast.makeText(mContext, sMsg, Toast.LENGTH_SHORT).show();
    }
}
