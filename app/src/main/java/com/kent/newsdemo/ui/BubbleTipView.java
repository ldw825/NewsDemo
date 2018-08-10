package com.kent.newsdemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kent.newsdemo.R;
import com.kent.newsdemo.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author Kent
 * date 2018/8/7 007
 * version 1.0
 */
public class BubbleTipView extends RelativeLayout {

    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;

    private final int DEFAULT_BG_COLOR = getResources().getColor(android.R.color.black);
    private int mBgColor;
    private LinearLayout mOptionBar;
    private ImageView mArrow;
    private int mDirection = DIR_UP;
    private List<OptionInfo> mOptionInfos;

    public BubbleTipView(Context context, int direction) {
        this(context, null);
        changeArrowDir(direction);
        changeBgColor();
    }

    public BubbleTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bubble_tip, this, true);
        mOptionBar = findViewById(R.id.option_bar);
        mArrow = findViewById(R.id.arrow);

        TypedArray a = context.obtainStyledAttributes(R.styleable.BubbleTip);
        mBgColor = a.getInt(R.styleable.BubbleTip_bgColor, -1);
        a.recycle();
        if (mBgColor == -1) {
            mBgColor = DEFAULT_BG_COLOR;
        } else {
            mBgColor = getResources().getColor(mBgColor);
        }
    }

    public void create() {
        if (mOptionInfos != null) {
            int size = mOptionInfos.size();
            int padding = getResources().getDimensionPixelSize(R.dimen.bubble_view_padding);
            for (int i = 0; i < size; i++) {
                final OptionInfo info = mOptionInfos.get(i);
                TextView option = new TextView(getContext());
                option.setGravity(Gravity.CENTER);
                option.setPadding(padding, padding, padding, padding);
                option.setTextColor(getResources().getColor(android.R.color.white));
                option.setText(info.mLabel);
                option.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.mAction.run();
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
                lp.weight = 1.0f;
                mOptionBar.addView(option, lp);
            }
        }
    }

    public void changeArrowDir(int direction) {
        if (direction == mDirection) {
            return;
        }
        if (direction == DIR_DOWN) {
            mArrow.setImageResource(R.drawable.triangle_up);
            LayoutParams lp = (LayoutParams) mArrow.getLayoutParams();
            lp.removeRule(BELOW);
            lp = (LayoutParams) mOptionBar.getLayoutParams();
            lp.addRule(BELOW, R.id.arrow);
            LogUtil.d("changeArrowDir down");
        } else {
            mArrow.setImageResource(R.drawable.triangle_down);
            LayoutParams lp = (LayoutParams) mOptionBar.getLayoutParams();
            lp.removeRule(BELOW);
            lp = (LayoutParams) mArrow.getLayoutParams();
            lp.addRule(BELOW, R.id.option_bar);
        }
        mDirection = direction;
    }

    private void changeBgColor() {
        if (mBgColor != DEFAULT_BG_COLOR) {
            Drawable drawable = tintDrawable(mOptionBar.getBackground(), mBgColor);
            mOptionBar.setBackground(drawable);
            drawable = tintDrawable(mArrow.getDrawable(), mBgColor);
            mArrow.setImageDrawable(drawable);
        }
    }

    public static Drawable tintDrawable(Drawable source, int color) {
        Drawable wrapper = DrawableCompat.wrap(source);
        DrawableCompat.setTint(wrapper, color);
        return wrapper;
    }

    public static class Builder {
        private Context mContext;
        private List<OptionInfo> mOptionInfos;
        private int mDirection;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder addOption(OptionInfo info) {
            if (mOptionInfos == null) {
                mOptionInfos = new ArrayList<>();
            }
            mOptionInfos.add(info);
            return this;
        }

        public Builder setDirection(int direction) {
            mDirection = direction;
            return this;
        }

        public BubbleTipView build() {
            BubbleTipView view = new BubbleTipView(mContext, mDirection);
            view.mOptionInfos = mOptionInfos;
            return view;
        }

    }

    public static class OptionInfo {

        public final String mLabel;
        public final Runnable mAction;

        public OptionInfo(String label, Runnable action) {
            mLabel = label;
            mAction = action;
        }

    }

}
