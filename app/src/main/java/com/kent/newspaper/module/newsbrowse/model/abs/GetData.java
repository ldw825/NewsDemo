package com.kent.newspaper.module.newsbrowse.model.abs;

import android.os.Handler;
import android.os.Looper;

import com.kent.newspaper.module.newsbrowse.HttpHelper;
import com.kent.newspaper.module.newsbrowse.NewsContants;
import com.kent.newspaper.module.newsbrowse.model.GetDataApiHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public abstract class GetData<T> implements IGetData {

    private Set<OnGetDataListener<T>> mListeners;
    private Handler mHandler;
    protected volatile boolean mIsDone = true;

    @Override
    public void cancelGetData() {
        mIsDone = true;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void addListener(OnGetDataListener<T> listener) {
        if (mListeners == null) {
            mListeners = new HashSet<>();
        }
        mListeners.add(listener);
    }

    public void removeListener(OnGetDataListener<T> listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    protected void notifyGetSuccess(final T data) {
        if (mListeners != null) {
            final Runnable notify = new Runnable() {
                @Override
                public void run() {
                    for (OnGetDataListener listener : mListeners) {
                        listener.onGetDataSuccess(data);
                    }
                }
            };
            if (isOnMainThread()) {
                notify.run();
            } else {
                checkHandler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notify.run();
                    }
                });
            }
        }
    }

    protected void notifyGetFailed(final String reason) {
        if (mListeners != null) {
            final Runnable notify = new Runnable() {
                @Override
                public void run() {
                    for (OnGetDataListener listener : mListeners) {
                        listener.onGetDataFailed(reason);
                    }
                }
            };
            if (isOnMainThread()) {
                notify.run();
            } else {
                checkHandler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notify.run();
                    }
                });
            }
        }
    }

    private boolean isOnMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        return myLooper == mainLooper;
    }

    private void checkHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    protected Map<String, String> getCommonParams() {
        Map<String, String> map = new HashMap<>();
        map.put(NewsContants.PARAM_APP_KEY, NewsContants.APP_KEY);
        return map;
    }

    protected GetDataApi obtainGetDataApi() {
        GetDataApi api = GetDataApiHolder.getInstance().get();
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(NewsContants.BASE_URL)
                    .client(getHttpClient()).addConverterFactory(GsonConverterFactory.create()).build();
            api = retrofit.create(GetDataApi.class);
            GetDataApiHolder.getInstance().set(api);
        }
        return api;
    }

    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeaderInterceptor());
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    private class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("User-Agent", HttpHelper.USER_AGENT)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .build();
            return chain.proceed(request);
        }
    }

}
