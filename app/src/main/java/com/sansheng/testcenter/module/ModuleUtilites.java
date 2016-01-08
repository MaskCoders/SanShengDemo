package com.sansheng.testcenter.module;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 1/7/16.
 */
public class ModuleUtilites {
    public static String listToJson(ArrayList<String> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        JSONArray array = new JSONArray();
        for (String uri : list) {
            array.put(uri);
        }
        return array.toString();
    }

    public static ArrayList<String> jsonToList(String json) {
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<String>();
        }
        ArrayList<String> a = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                a.add(array.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a;
    }

    public static HashMap<Integer, String> jsonToMapForMeterTest(String json, String[] allItems) {
        if (TextUtils.isEmpty(json)) {
            return new HashMap<Integer, String>();
        }
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                int index = Integer.valueOf(array.get(i).toString());
                map.put(index, allItems[index]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
