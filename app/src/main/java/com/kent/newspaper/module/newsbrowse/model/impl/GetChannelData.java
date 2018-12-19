package com.kent.newspaper.module.newsbrowse.model.impl;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.kent.newspaper.module.newsbrowse.HttpHelper;
import com.kent.newspaper.module.newsbrowse.NewsContants;
import com.kent.newspaper.module.newsbrowse.model.abs.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class GetChannelData extends GetData<List<String>> {

    private static final String URL = NewsContants.CHANNEL_URL + "?" + NewsContants.PARAM_APP_KEY + "=" + NewsContants.APP_KEY;

    @Override
    public void doGetData() {
        new Thread(mRequest).start();
    }

    private Runnable mRequest = new Runnable() {
        @Override
        public void run() {
            try {
                String result = HttpHelper.sendGet(URL, "utf-8");
                List<String> channels = parseChannel(result);
                if (channels == null) {
                    notifyGetFailed("");
                } else {
                    notifyGetSuccess(channels);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Nullable
    private List<String> parseChannel(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<String> list = null;
        try {
            JSONObject jObj = new JSONObject(json);
            JSONArray jArray = jObj.getJSONArray("result");
            int size = jArray.length();
            if (size == 0) {
                return null;
            }
            list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(jArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
