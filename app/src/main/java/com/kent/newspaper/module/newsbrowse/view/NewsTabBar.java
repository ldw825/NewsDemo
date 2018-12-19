package com.kent.newspaper.module.newsbrowse.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author Kent
 * date 2018/7/25 025
 * version 1.0
 */
public class NewsTabBar extends HorizontalScrollView {

    private List<NewsTab> mTabs = new ArrayList<>();
    private LinearLayout mContainer;
    private int mScreenWidth;
    private Rect mRect;

    public NewsTabBar(Context context) {
        this(context, null);
    }

    public NewsTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setMotionEventSplittingEnabled(false);
        addView(mContainer);
        mScreenWidth = getScreenWidth();
    }

    public void addTab(NewsTab tab) {
        mTabs.add(tab);
        mContainer.addView(tab);
    }

    public void selectTab(NewsTab targetTab) {
        for (NewsTab tab : mTabs) {
            tab.setSelected(tab == targetTab);
        }
        checkScroll(targetTab);
    }

    private void checkScroll(NewsTab tab) {
        if (mRect == null) {
            mRect = new Rect();
        } else {
            mRect.setEmpty();
        }
        tab.getGlobalVisibleRect(mRect);
        int tabWidth = tab.getWidth();
        int offset = 0;
        if (mRect.left <= 0 || mRect.right >= mScreenWidth) {
            if (mRect.left == 0) {
                offset = -(tabWidth - mRect.right);
            } else if (mRect.left < 0) {
                offset = mRect.left;
            } else {
                offset = (mRect.left + tabWidth) - mScreenWidth;
            }
        }
        if (offset != 0) {
            smoothScrollBy(offset, 0);
        }
    }

    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
