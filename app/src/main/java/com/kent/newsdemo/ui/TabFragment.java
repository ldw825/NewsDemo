package com.kent.newsdemo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kent.newsdemo.Constants;
import com.kent.newsdemo.R;
import com.kent.newsdemo.model.NewsListAdapter;
import com.kent.newsdemo.model.abs.OnGetDataListener;
import com.kent.newsdemo.model.entity.NewsInfo;
import com.kent.newsdemo.model.entity.SingleNews;
import com.kent.newsdemo.model.impl.GetNewsData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author Kent
 * date 2018/7/25 025
 * version 1.0
 */
public class TabFragment extends Fragment implements OnGetDataListener<NewsInfo>,
        SwipeRefreshLayout.OnRefreshListener, NewsFragmengt.OnHideListener {

    private static final String KEY_CHANNEL = "channel";
    private static final String KEY_DATA = "data";

    private ListView mListView;
    //        private SwipeRefreshLayout mRefreshLayout;
    private PtrFrameLayout mLoadMoreLayout;
    private View mLoadMoreView;

    private String mChannel;
    private GetNewsData mGetNewsData;
    private ArrayList<SingleNews> mNewsList;
    private NewsListAdapter mListAdapter;
    private boolean mHasLoadedAllData;
    private boolean mIsLoading;
    private Handler mHandler = new Handler();
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mChannel = getArguments().getString(KEY_CHANNEL);
            mGetNewsData = new GetNewsData(mChannel);
            mGetNewsData.addListener(this);
            mGetNewsData.doGetData();
        } else {
            mChannel = savedInstanceState.getString(KEY_CHANNEL);
            mGetNewsData = new GetNewsData(mChannel);
            mGetNewsData.addListener(this);
            mNewsList = savedInstanceState.getParcelableArrayList(KEY_DATA);
            if (mNewsList == null || mNewsList.isEmpty()) {
                mGetNewsData.doGetData();
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        NewsInfo newsInfo = new NewsInfo();
                        newsInfo.channel = mChannel;
                        newsInfo.num = mNewsList.size();
                        newsInfo.newsList = (List<SingleNews>) mNewsList.clone();
                        mNewsList.clear();
                        mListAdapter = null;
                        onGetDataSuccess(newsInfo);
                    }
                });
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        mListView = view.findViewById(R.id.listview);
//        mRefreshLayout = view.findViewById(R.id.refresh);
//        mRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color
// .holo_green_light, android.R.color.holo_blue_light);
//        mRefreshLayout.setOnRefreshListener(this);
        mLoadMoreLayout = view.findViewById(R.id.load_more);
        mLoadMoreLayout.setMode(PtrFrameLayout.Mode.BOTH);
        mLoadMoreLayout.setLoadingMinTime(0);

        PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(getContext());
        mLoadMoreLayout.setHeaderView(defaultHeader);
//        mLoadMoreLayout.addPtrUIHandler(defaultHeader);
        PtrClassicDefaultFooter defaultFooter = new PtrClassicDefaultFooter(getContext());
        mLoadMoreLayout.setFooterView(defaultFooter);
        mLoadMoreLayout.addPtrUIHandler(defaultFooter);
        mLoadMoreLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (mHasLoadedAllData) {
                    showAllLoadedToast();
                    mLoadMoreLayout.refreshComplete();
                } else {
                    mIsLoading = true;
                    mGetNewsData.doGetData();
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLoadMoreLayout.refreshComplete();
            }
        });

//        if (mNewsList != null && !mNewsList.isEmpty()) {
//            NewsInfo newsInfo = new NewsInfo();
//            newsInfo.channel = mChannel;
//            newsInfo.num = mNewsList.size();
//            newsInfo.newsList = (List<SingleNews>) mNewsList.clone();
//            mNewsList.clear();
//            onGetDataSuccess(newsInfo);
//        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_CHANNEL, mChannel);
        outState.putParcelableArrayList(KEY_DATA, mNewsList);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListAdapter != null) {
            mListAdapter.hideTipViewWindow();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcast();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcast();
    }

    @Override
    public void onGetDataSuccess(NewsInfo data) {
        if (mNewsList == null) {
            mNewsList = new ArrayList<>();
        }
        mNewsList.addAll(data.newsList);
        Collections.sort(mNewsList);
        if (mListAdapter == null) {
            mListAdapter = new NewsListAdapter(getContext(), mNewsList, mChannel);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);
            mListView.setOnItemLongClickListener(mListAdapter);
        } else {
            mListAdapter.notifyDataSetChanged();
            if (mIsLoading) {
                mIsLoading = false;
                mLoadMoreLayout.refreshComplete();
                if (data.num < GetNewsData.REQUEST_STEP_NUM) {
                    mHasLoadedAllData = true;
                    showAllLoadedToast();
                } else {
                    showUpdatedToast(data.num);
                }
            }
        }
    }

    @Override
    public void onGetDataFailed(String reason) {
        if (mIsLoading) {
            mIsLoading = false;
            mLoadMoreLayout.refreshComplete();
        }
        Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
    }

    private void showAllLoadedToast() {
        Toast.makeText(getContext(), R.string.all_data_loaded, Toast.LENGTH_SHORT).show();
    }

    private void showUpdatedToast(int num) {
        String msg = getString(R.string.updated_data, num);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(false);
//            }
//        }, 1000);
    }

    @Override
    public void onHide() {
        if (mListAdapter != null) {
            mListAdapter.hideTipViewWindow();
        }
    }

    private void registerBroadcast() {
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Constants.ACTION_DOUBLE_CLICK_BOTTOM.equals(intent.getAction())) {
                        scrollListToTop();
                    }
                }
            };
        }
        IntentFilter filter = new IntentFilter(Constants.ACTION_DOUBLE_CLICK_BOTTOM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
    }

    private void unregisterBroadcast() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        }
    }

    private void scrollListToTop() {
        int firstVisiblePos = mListView.getFirstVisiblePosition();
        if (firstVisiblePos > 0) {
            mListView.smoothScrollToPosition(0);
            return;
        }
        View firstView = mListView.getChildAt(0);
        if (firstView != null) {
            int top = firstView.getTop();
            if (top < 0) {
                mListView.smoothScrollBy(top, 200);
            }
        }

    }

}
