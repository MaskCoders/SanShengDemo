package com.sansheng.testcenter.collection;

import com.sansheng.testcenter.demo.util.MeterUtilies;

import java.util.Random;

/**
 * Created by sunshaogang on 12/25/15.
 */
public class CollectionUtilies {

    public static final String BAIDU_APP_KEY = "wFtG3DzxxFRqinI8SRxy9lIy";
    private static Random sRandom = new Random(System.currentTimeMillis());
    public static final int START_CAMERA = 502;
    public static final int RANDOM_FILE_NAME_LENGTH = 16;

    public static String generateRandomString(int len) {
        StringBuffer sb = new StringBuffer();
        sb.append("_sansheng_");
        final String seeds = "0123456789abcdefghijklmnopqrstuv";
        for (int i = 0; i < len; i++) {
            int value = sRandom.nextInt() & 31;
            char c = seeds.charAt(value);
            sb.append(c);
        }
        sb.append("_");
        sb.append(MeterUtilies.getCurrentTimeString(System.currentTimeMillis()));
        sb.append("");
        return sb.toString();
    }
}
