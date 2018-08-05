package com.kent.newsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.kent.newsdemo.util.LogUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * author Kent
 * date 2018/8/4 004
 * version 1.0
 */
public class NetworkStateManager extends BroadcastReceiver {

    public static final int TYPE_NONE = -1;
    public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;

    private static volatile NetworkStateManager sInstance;
    private int mCurrentNetType = TYPE_NONE;
    private Set<OnNetworkChangeListener> mListeners = new HashSet<>();
    private int mRegisterNum;
    private boolean mIsRegistered;

    public interface OnNetworkChangeListener {
        void onNetworkChange(int newType);
    }

    public static NetworkStateManager getsInstance() {
        if (sInstance == null) {
            synchronized (NetworkStateManager.class) {
                if (sInstance == null) {
                    sInstance = new NetworkStateManager();
                }
            }
        }
        return sInstance;
    }

    public void register(Context context) {
        if (context instanceof OnNetworkChangeListener) {
            mListeners.add(((OnNetworkChangeListener) context));
        }
        if (mRegisterNum == 0) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.getApplicationContext().registerReceiver(this, filter);
            mIsRegistered = true;
        }
        mRegisterNum++;
    }

    public void unregister(Context context) {
        if (context instanceof OnNetworkChangeListener) {
            mListeners.remove(((OnNetworkChangeListener) context));
        }
        mRegisterNum--;
        if (mRegisterNum == 0) {
            try {
                context.getApplicationContext().unregisterReceiver(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sInstance = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int lastType = mCurrentNetType;
            mCurrentNetType = getNetworkType(context);
            int msg = -1;
            switch (mCurrentNetType) {
                case TYPE_MOBILE:
                    if (lastType != mCurrentNetType) {
                        msg = R.string.network_msg_mobile;
                    }
                    break;
                case TYPE_WIFI:
                    if (lastType != mCurrentNetType) {
                        msg = R.string.network_msg_wifi;
                    }
                    break;
                case TYPE_NONE:
                    msg = R.string.network_msg_none;
                    break;
                default:
                    break;
            }
            if (msg != -1) {
                if (mIsRegistered) {
                    mIsRegistered = false;
                    LogUtil.d("no handle for new register");
                    return;
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                for (OnNetworkChangeListener listener : mListeners) {
                    listener.onNetworkChange(mCurrentNetType);
                }
            }
        }
    }

    public int getmCurrentNetType() {
        return mCurrentNetType;
    }

    private int getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return info.getType();
        }
        return TYPE_NONE;
    }

}
