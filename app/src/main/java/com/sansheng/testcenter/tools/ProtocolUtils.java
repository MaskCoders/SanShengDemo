package com.sansheng.testcenter.tools;

/**
 * Created by hua on 15-12-26.
 */
public class ProtocolUtils {
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String byte2hex( byte b) {
        return Integer.toHexString(b & 0xFF);
    }
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    public static String get2HexFromInt(int num){
        String o = Integer.toHexString(num);
        o = ("0000"+o);
        return o;
    }
}
