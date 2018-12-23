package com.kent.newspaper.module.newsbrowse.model.abs;

import com.kent.newspaper.module.newsbrowse.entity.ChanneData;
import com.kent.newspaper.module.newsbrowse.entity.NewsData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author Kent
 * @version 1.0
 * @date 2018/12/22
 */
public interface GetDataApi {

    @GET("{path}")
    Call<ChanneData> getChannel(@Path("path") String path, @QueryMap(encoded = true) Map<String, String> params);

    @GET("{path}")
    Call<NewsData> getNews(@Path("path") String path, @QueryMap(encoded = true) Map<String, String> params);

}
