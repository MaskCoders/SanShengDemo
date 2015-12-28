package com.sansheng.testcenter.tools;

/**
 * Created by hua on 15-12-26.
 */
public class ProtocolUtils {
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    //byte to 16
    public static String byte2hex( byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length()<2){
            hex="0"+hex;
        }
        return hex;
    }

    /**
     * 16 to bytes
     * @param hexString
     * @return
     */
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
    //byte 2 bin
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
    // 10 to 16
    public static String get2HexFromInt(int num){
        String o = Integer.toHexString(num);
        o = ("0000"+o);
        return o.substring(o.length()-4);
    }
        //2 转 16
            public static String binaryString2hexString(String bString)
            {
                if (bString == null || bString.equals("") || bString.length() % 8 != 0)
                    return null;
                StringBuffer tmp = new StringBuffer();
                int iTmp = 0;
                for (int i = 0; i < bString.length(); i += 4)
                {
                    iTmp = 0;
                    for (int j = 0; j < 4; j++)
                    {
                        iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
                    }
                    tmp.append(Integer.toHexString(iTmp));
                }
                return tmp.toString();
            }
        //16转2
        public static String hexString2binaryString(String hexString)
        {
            if (hexString == null || hexString.length() % 2 != 0)
                return null;
            String bString = "", tmp;
            for (int i = 0; i < hexString.length(); i++)
            {
                tmp = "0000"
                        + Integer.toBinaryString(Integer.parseInt(hexString
                        .substring(i, i + 1), 16));
                bString += tmp.substring(tmp.length() - 4);
            }
            return bString;
        }
    public static  String printByte(byte[] data){
//        System.out.println("-----------start");
//        printHexString(ba);
//        System.out.println("\n-----------end");
        StringBuffer sbbyte = new StringBuffer();
        StringBuffer hexbyte_withspace = new StringBuffer();
        StringBuffer hexbyte = new StringBuffer();
        for(int i=0;i<data.length;i++){
            sbbyte.append(data[i]).append(" ");
            hexbyte.append(byte2hex(data[i]));
            hexbyte_withspace.append(byte2hex(data[i])).append(" ");
        }
        System.out.println("byte:=> "+sbbyte.toString());
        System.out.println("hexbyte:=> "+hexbyte.toString());
        System.out.println("hexbyte_withspace:=> "+hexbyte_withspace.toString());
        return hexbyte.toString();
    }
}
