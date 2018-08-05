package com.kent.newsdemo;

import android.support.v7.app.AppCompatActivity;

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
