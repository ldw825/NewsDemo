package com.kent.newsdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.kent.newsdemo.R;
import com.kent.newsdemo.util.Utils;


/**
 * author Kent
 * date 2018/7/25 025
 * version 1.0
 */
public class NewsTab extends AppCompatTextView {

    public interface OnTabSelectListener {
        void onTabSelect(NewsTab tab);
    }

    private OnTabSelectListener mListener;
    private String mTitle;
    private boolean mIsSelected;
    private Paint mPaint;

    public NewsTab(Context context) {
        this(context, null);
    }

    public NewsTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(Utils.getAttrValue(getContext(), android.R.attr
                .selectableItemBackgroundBorderless));
        setTextAppearance(getContext(), R.style.TabTextNormal);
        setPadding(24, 12, 24, 12);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTabSelect(NewsTab.this);
                }
            }
        });
    }

    public void setTitle(String title) {
        mTitle = title;
        setText(title);
    }

    public void setSelected(boolean selected) {
        if (mIsSelected == selected) {
            return;
        }
        mIsSelected = selected;
        int style = selected ? R.style.TabTextSelected : R.style.TabTextNormal;
        setTextAppearance(getContext(), style);
        invalidate();
    }

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(getResources().getColor(android.R.color.black));
            mPaint.setStrokeWidth(3);
            mPaint.setStyle(Paint.Style.STROKE);
        }
        if (mIsSelected) {
            int radius = 6;
            int offset = 3;
            canvas.drawCircle(getWidth() - getPaddingRight() + radius + offset, getHeight() / 3,
                    radius, mPaint);
        }
    }

}
