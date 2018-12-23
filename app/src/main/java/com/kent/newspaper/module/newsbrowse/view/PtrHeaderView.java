package com.kent.newspaper.module.newsbrowse.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kent.newspaper.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * @author Kent
 * @version 1.0
 * @date 2018/12/23
 */
public class PtrHeaderView extends RelativeLayout implements PtrUIHandler {

    private TextView mTextView;
    private float mTextSize;

    public PtrHeaderView(Context context) {
        this(context, null);
    }

    public PtrHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ptr_header, this, true);
        mTextView = findViewById(R.id.textview);
        mTextSize = mTextView.getTextSize();
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        float alpha = (float) currentPos / mOffsetToRefresh;
        if (alpha <= 1.0f) {
            float textSize = alpha * mTextSize;
            mTextView.setAlpha(alpha);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

}
