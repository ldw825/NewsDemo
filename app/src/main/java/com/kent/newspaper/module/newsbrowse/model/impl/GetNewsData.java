package com.kent.newspaper.module.newsbrowse.model.impl;

import com.kent.newspaper.module.newsbrowse.NewsContants;
import com.kent.newspaper.module.newsbrowse.entity.NewsData;
import com.kent.newspaper.module.newsbrowse.entity.NewsInfo;
import com.kent.newspaper.module.newsbrowse.model.abs.GetData;
import com.kent.newspaper.module.newsbrowse.model.abs.GetDataApi;
import com.kent.newspaper.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Map<String, String> map = getCommonParams();
        map.put(NewsContants.PARAM_NUM, String.valueOf(REQUEST_STEP_NUM));
        map.put(NewsContants.PARAM_START, String.valueOf(mCurrentNum));
        try {
            map.put(NewsContants.PARAM_CHANNEL, URLEncoder.encode(mChannel, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        GetDataApi api = obtainGetDataApi();
        Call<NewsData> call = api.getNews(NewsContants.NEWS_PATH, map);
        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                NewsInfo newsInfo = response.body().getResult();
                LogUtil.d("list=" + newsInfo.getList());
                mCurrentNum += newsInfo.getList().size();
                notifyGetSuccess(newsInfo);
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                notifyGetFailed(t.getMessage());
            }
        });
        LogUtil.d("mChannel=" + mChannel);
    }

}
