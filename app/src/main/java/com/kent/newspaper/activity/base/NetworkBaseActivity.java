package com.kent.newspaper.activity.base;

import android.support.v7.app.AppCompatActivity;

import com.kent.newspaper.NetworkStateManager;

/**
 * author Kent
 * date 2018/8/4 004
 * version 1.0
 */
public class NetworkBaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        NetworkStateManager.getsInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkStateManager.getsInstance().unregister(this);
    }

}
