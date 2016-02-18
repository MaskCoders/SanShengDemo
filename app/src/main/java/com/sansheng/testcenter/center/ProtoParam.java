package com.sansheng.testcenter.center;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sunshaogang on 2/18/16.
 * 参数类，用来保存发送指令时传入的参数
 */
public class ProtoParam {

    public String title;
    public String value;
    public int type;

    public ProtoParam(JSONObject object) throws JSONException {
        this.title = object.getString("n");
        this.value = object.getString("v");
        this.type = object.getInt("f");
    }

    public ProtoParam(String title, String value, int type) {
        this.title = title;
        this.value = value;
        this.type = type;
    }

    public void restoreFromJson(JSONObject object) throws JSONException {
        this.title = object.getString("n");
        this.value = object.getString("v");
        this.type = object.getInt("f");
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("n", title);
            object.put("v", value);
            object.put("f", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
