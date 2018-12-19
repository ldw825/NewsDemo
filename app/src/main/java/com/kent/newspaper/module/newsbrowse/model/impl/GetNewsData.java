package com.kent.newspaper.module.newsbrowse.model.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.kent.newspaper.module.newsbrowse.HttpHelper;
import com.kent.newspaper.module.newsbrowse.NewsContants;
import com.kent.newspaper.module.newsbrowse.entity.NewsInfo;
import com.kent.newspaper.module.newsbrowse.entity.SingleNews;
import com.kent.newspaper.module.newsbrowse.model.abs.GetData;
import com.kent.newspaper.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class GetNewsData extends GetData<NewsInfo> {

    public static final int REQUEST_STEP_NUM = 40;

    private String mChannel;
    private int mCurrentNum;

    public GetNewsData(String channel) {
        mChannel = channel;
    }

    @Override
    public void doGetData() {
        GetTask task = new GetTask();
        new Thread(task).start();
        LogUtil.d("mChannel=" + mChannel);
    }

    private class GetTask implements Runnable {
        @Override
        public void run() {
            try {
                Map<String, String> map = new HashMap<>();
                map.put(NewsContants.PARAM_APP_KEY, NewsContants.APP_KEY);
                map.put(NewsContants.PARAM_NUM, String.valueOf(REQUEST_STEP_NUM));
                map.put(NewsContants.PARAM_START, String.valueOf(mCurrentNum));
                map.put(NewsContants.PARAM_CHANNEL, URLEncoder.encode(mChannel, "utf-8"));
                String result = HttpHelper.sendGet(appendUrl(NewsContants.NEWS_URL, map), "utf-8");
                NewsInfo newsInfo = parseNewsInfo(result);
                LogUtil.d("list=" + newsInfo.newsList);
                if (newsInfo == null) {
                    notifyGetFailed("");
                } else {
                    mCurrentNum += newsInfo.newsList.size();
                    notifyGetSuccess(newsInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private String appendUrl(String baseUrl, Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        result.append(baseUrl).append("?");
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            result.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

    @Nullable
    private NewsInfo parseNewsInfo(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            NewsInfo newsInfo = new NewsInfo();
            JSONObject obj = new JSONObject(json);
            JSONObject result = obj.getJSONObject("result");
            newsInfo.channel = result.getString("channel");
            newsInfo.num = Integer.parseInt(result.getString("num"));
            newsInfo.newsList = new ArrayList<>();
            JSONArray list = result.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                obj = list.getJSONObject(i);
                SingleNews news = new SingleNews();
                news.title = obj.getString("title");
                news.time = obj.getString("time");
                news.src = obj.getString("src");
                news.category = obj.getString("category");
                news.pic = obj.getString("pic");
                news.content = obj.getString("content");
                news.url = obj.getString("url");
                news.weburl = obj.getString("weburl");
                newsInfo.newsList.add(news);
            }
            return newsInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
