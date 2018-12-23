package com.kent.newspaper.module.newsbrowse.model.impl;

import com.kent.newspaper.module.newsbrowse.NewsContants;
import com.kent.newspaper.module.newsbrowse.entity.ChanneData;
import com.kent.newspaper.module.newsbrowse.model.abs.GetData;
import com.kent.newspaper.module.newsbrowse.model.abs.GetDataApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class GetChannelData extends GetData<List<String>> {

    @Override
    public void doGetData() {
        GetDataApi api = obtainGetDataApi();
        Call<ChanneData> call = api.getChannel(NewsContants.CHANNEL_PATH, getCommonParams());
        call.enqueue(new Callback<ChanneData>() {
            @Override
            public void onResponse(Call<ChanneData> call, Response<ChanneData> response) {
                ChanneData info = response.body();
                if (info != null) {
                    notifyGetSuccess(info.getResult());
                }
            }

            @Override
            public void onFailure(Call<ChanneData> call, Throwable t) {
                notifyGetFailed(t.getMessage());
            }
        });
    }

}
