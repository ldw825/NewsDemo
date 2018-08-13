package com.kent.newsdemo;

/**
 * author Kent
 * date 2018/7/23 023
 * version 1.0
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.kent.newsdemo.ui.BottomTab;
import com.kent.newsdemo.ui.MineFragment;
import com.kent.newsdemo.ui.NewsFragmengt;


public class MainActivity extends NetworkBaseActivity implements NetworkStateManager
        .OnNetworkChangeListener {

    private View mLoading;
    private BottomTab mNewsTab;
    private BottomTab mMineTab;
    private NewsFragmengt mNewsFragment;
    private MineFragment mMineFragment;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private long mLastBackPressTime;

    private View.OnClickListener mOnTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.isSelected()) {
                return;
            }
            selectTab((BottomTab) v, true);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (v == mNewsTab) {
                selectTab(mMineTab, false);
                ft.hide(mMineFragment);
                Fragment f = mFragmentManager.findFragmentByTag(NewsFragmengt.TAG);
                if (f == null) {
                    ft.add(R.id.container, mNewsFragment, NewsFragmengt.TAG);
                }
                ft.show(mNewsFragment);
            } else if (v == mMineTab) {
                selectTab(mNewsTab, false);
                ft.hide(mNewsFragment);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                }
                Fragment f = mFragmentManager.findFragmentByTag(MineFragment.TAG);
                if (f == null) {
                    ft.add(R.id.container, mMineFragment, MineFragment.TAG);
                }
                ft.show(mMineFragment);
            }
            ft.commit();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mLoading = findViewById(R.id.loading);
        mNewsTab = findViewById(R.id.tab_news);
        mMineTab = findViewById(R.id.tab_mine);

        mNewsTab.setOnClickListener(mOnTabClickListener);
        mMineTab.setOnClickListener(mOnTabClickListener);

        selectTab(mNewsTab, true);
        mNewsFragment = new NewsFragmengt();
        mFragmentManager.beginTransaction().add(R.id.container, mNewsFragment, NewsFragmengt.TAG)
                .commit();
    }

    private void selectTab(BottomTab tab, boolean select) {
        tab.select(select);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastBackPressTime > 2000) {
            Toast.makeText(this, R.string.double_press_tip, Toast.LENGTH_SHORT).show();
            mLastBackPressTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            mNewsFragment.setCanceled();
        }
    }

    @Override
    public void onNetworkChange(int newType) {
        if (newType == NetworkStateManager.TYPE_MOBILE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.network_dialog_title)
                    .setMessage(R.string.network_dialog_message)
                    .setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

}
