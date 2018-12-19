package com.kent.newspaper.module.newsbrowse.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kent.newspaper.R;
import com.kent.newspaper.module.newsbrowse.NewsDataCache;
import com.kent.newspaper.module.newsbrowse.model.GetChannelModel;
import com.kent.newspaper.module.newsbrowse.presenter.GetChannelContract;
import com.kent.newspaper.module.newsbrowse.presenter.GetChannelPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * author Kent
 * date 2018/7/26 026
 * version 1.0
 */
public class NewsFragmengt extends Fragment implements NewsTab.OnTabSelectListener,
        GetChannelContract.IView {

    public static final String TAG = NewsFragmengt.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;
    private NewsTabBar mTabBar;
    private View mEmptyView;

    private List<TabFragment> mFragments;
    private List<NewsTab> mTabs;

    private GetChannelPresenter mGetChannelPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof FragmentActivity) {
            mFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        }
        queryChannels();
    }

    private void queryChannels() {
        mGetChannelPresenter = new GetChannelPresenter();
        GetChannelModel model = new GetChannelModel(mGetChannelPresenter);
        mGetChannelPresenter.setView(this).setModel(model);
        mGetChannelPresenter.queryChannels();
    }

    public void setCanceled() {
        mGetChannelPresenter.cancelQuery();
    }

    @Override
    public void showSuccessView(List<String> data) {
        mFragments = new ArrayList<>();
        mTabs = new ArrayList<>();
        for (String channel : data) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("channel", channel);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
            NewsTab tab = new NewsTab(getContext());
            tab.setTitle(channel);
            mTabs.add(tab);
        }

        setViewPager();
        setTab();
    }

    @Override
    public void showFailedView(String reason) {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        NewsDataCache.destroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        mTabBar = view.findViewById(R.id.tab_bar);
        mViewPager = view.findViewById(R.id.view_pager);
        mEmptyView = view.findViewById(R.id.empty_view);
        return view;
    }

    @Override
    public void onTabSelect(NewsTab tab) {
        mViewPager.setCurrentItem(mTabs.indexOf(tab));
    }

    private void setTab() {
        for (NewsTab tab : mTabs) {
            tab.setOnTabSelectListener(this);
            mTabBar.addTab(tab);
        }
        mTabBar.selectTab(mTabs.get(0));
    }

    private void setViewPager() {
//        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                NewsTab tab = mTabs.get(position);
                mTabBar.selectTab(tab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (mFragments != null) {
                for (TabFragment fragment : mFragments) {
                    if (fragment instanceof OnHideListener && fragment.isVisible()) {
                        ((OnHideListener) fragment).onHide();
                    }
                }
            }
        }
    }

    public interface OnHideListener {
        void onHide();
    }

}
