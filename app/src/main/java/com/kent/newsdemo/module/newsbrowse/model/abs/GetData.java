package com.kent.newsdemo.module.newsbrowse.model.abs;

import android.os.Handler;
import android.os.Looper;

import java.util.HashSet;
import java.util.Set;

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

}
