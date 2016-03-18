package hstt.util;

public class Util {
    public static void Test() {

        byte[] buffer = new byte[]{0x1, 2, 3, 0x12, 13, 14};
        String str = ByteArrayToString(buffer, 0, buffer.length);
        Logger.d("ByteArrayToString %s", str);
        str = "01 02 03 11 12 13 14";

        buffer = StringToByteArray(str);

        str = ByteArrayToString(buffer, 0, buffer.length);
        Logger.d("ByteArrayToString %s", str);

    }

    public static String ByteArrayToString(byte[] pArray, int pFromIndex, int pLen) {
        if (pArray == null || pArray.length < 1) return "";
        StringBuilder sb = new StringBuilder(pLen * 3);
        String stmp = "";
        for (int i = pFromIndex; i < pFromIndex + pLen; i++) {
            stmp = Integer.toHexString(pArray[i] & 0xFF).toUpperCase() + " ";
            sb.append((stmp.length() == 2) ? "0" + stmp : stmp);
        }
        return sb.toString().trim();
    }

    public static String ByteArrayToString(byte[] pArray) {
        return ByteArrayToString(pArray, 0, pArray.length);
    }

    public static byte[] StringToByteArray(String pStr) {

        if (pStr == null || pStr.equals("")) {
            pStr = "FF";
        }
        String[] str = pStr.trim().split("[ ]", -1);
        byte[] bcd = new byte[str.length];
        for (int i = 0; i < str.length; i++) {
            try {
                bcd[i] = Integer.valueOf(str[i], 16).byteValue();
            } catch (java.lang.Exception e) {
                bcd[i] = (byte) 0xFF;
            }

        }
        return bcd;
    }


    public static byte SumCheck(byte[] pBuffer, int pFromIndex, int pEndIndex) {
        byte sum = 0;
        for (int i = pFromIndex; i <= pEndIndex; i++) {
            sum = (byte) (sum + pBuffer[i]);
        }
        return sum;
    }

    public static byte ByteOfLong(long pLong, int pWhichByte) {
        if (pWhichByte <= 1) pWhichByte = 1;
        return (byte) ((pLong >> ((pWhichByte - 1) * 8)) & 0x00ff);
    }

    public static String PadLeft(String str, int n, char c) {
        return String.format("%1$" + n + "s", str).replace(' ', c);
    }

    public static String PadRight(String str, int n, char c) {
        return String.format("%1$-" + n + "s", str).replace(' ', c);
    }

    //------------------------------------------------------------------------------------
    //	This method replaces the .NET static string method 'IsNullOrEmpty'.
    //------------------------------------------------------------------------------------
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.equals("");
    }

    //------------------------------------------------------------------------------------
    //	This method replaces the .NET static string method 'Join' (2 parameter version).
    //------------------------------------------------------------------------------------
    public static String join(String separator, String[] stringarray) {
        if (stringarray == null)
            return null;
        else
            return join(separator, stringarray, 0, stringarray.length);
    }

    //------------------------------------------------------------------------------------
    //	This method replaces the .NET static string method 'Join' (4 parameter version).
    //------------------------------------------------------------------------------------
    public static String join(String separator, String[] stringarray, int startindex, int count) {
        String result = "";

        if (stringarray == null)
            return null;

        for (int index = startindex; index < stringarray.length && index - startindex < count; index++) {
            if (separator != null && index > startindex)
                result += separator;

            if (stringarray[index] != null)
                result += stringarray[index];
        }

        return result;
    }

    //------------------------------------------------------------------------------------
    //	This method replaces the .NET static string method 'TrimEnd'.
    //------------------------------------------------------------------------------------
    public static String trimEnd(String string, Character... charsToTrim) {
        if (string == null || charsToTrim == null)
            return string;

        int lengthToKeep = string.length();
        for (int index = string.length() - 1; index >= 0; index--) {
            boolean removeChar = false;
            if (charsToTrim.length == 0) {
                if (Character.isWhitespace(string.charAt(index))) {
                    lengthToKeep = index;
                    removeChar = true;
                }
            } else {
                for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
                    if (string.charAt(index) == charsToTrim[trimCharIndex]) {
                        lengthToKeep = index;
                        removeChar = true;
                        break;
                    }
                }
            }
            if (!removeChar)
                break;
        }
        return string.substring(0, lengthToKeep);
    }

    //------------------------------------------------------------------------------------
    //	This method replaces the .NET static string method 'TrimStart'.
    //------------------------------------------------------------------------------------
    public static String trimStart(String string, Character... charsToTrim) {
        if (string == null || charsToTrim == null)
            return string;

        int startingIndex = 0;
        for (int index = 0; index < string.length(); index++) {
            boolean removeChar = false;
            if (charsToTrim.length == 0) {
                if (Character.isWhitespace(string.charAt(index))) {
                    startingIndex = index + 1;
                    removeChar = true;
                }
            } else {
                for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
                    if (string.charAt(index) == charsToTrim[trimCharIndex]) {
                        startingIndex = index + 1;
                        removeChar = true;
                        break;
                    }
                }
            }
            if (!removeChar)
                break;
        }
        return string.substring(startingIndex);
    }

    //------------------------------------------------------------------------------------
    //	This method replaces the .NET static string method 'Trim' when arguments are used.
    //------------------------------------------------------------------------------------
    public static String trim(String string, Character... charsToTrim) {
        return trimEnd(trimStart(string, charsToTrim), charsToTrim);
    }

    //------------------------------------------------------------------------------------
    //	This method is used for string equality comparisons when the option
    //	'Use helper 'stringsEqual' method to handle null strings' is selected
    //	(The Java String 'equals' method can't be called on a null instance).
    //------------------------------------------------------------------------------------
    public static boolean stringsEqual(String s1, String s2) {
        if (s1 == null && s2 == null)
            return true;
        else
            return s1 != null && s1.equals(s2);
    }
}
