package com.kent.newsdemo.module.newsbrowse.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    private Builder.Data mData;

    private BubbleTipView(Context context) {
        super(context);
    }

    private BubbleTipView(Context context, Builder.Data data) {
        this(context, null, 0);
        mData = data;
        changeArrowDir(data.mDirection);
        changeBgColor();
    }

    public BubbleTipView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if (mData.mOptionInfos != null) {
            int size = mData.mOptionInfos.size();
            for (int i = 0; i < size; i++) {
                final OptionInfo info = mData.mOptionInfos.get(i);
                View option = LayoutInflater.from(getContext()).inflate(R.layout
                        .list_item_option, this, false);
                ImageView imageView = option.findViewById(R.id.imageView);
                imageView.setImageResource(info.mIcon);
                imageView.setContentDescription(info.mLabel);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.mAction.run();
                        v.setSelected(!v.isSelected());
                        if (mData.mOnOptionClickListener != null) {
                            mData.mOnOptionClickListener.onOptionClick(info);
                        }
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams
                        .WRAP_CONTENT);
                lp.weight = 1.0f;
                mOptionBar.addView(option, lp);
            }
        }
    }

    public void changeArrowDir(int direction) {
        if (direction == mData.mDirection) {
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
        mData.mDirection = direction;
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
        private Data mData;

        public Builder(Context context) {
            mContext = context;
            mData = new Data();
        }

        public Builder addOption(OptionInfo info) {
            if (mData.mOptionInfos == null) {
                mData.mOptionInfos = new ArrayList<>();
            }
            mData.mOptionInfos.add(info);
            return this;
        }

        public Builder setDirection(int direction) {
            mData.mDirection = direction;
            return this;
        }

        public Builder setOnOptionClickListener(OnOptionClickListener listener) {
            mData.mOnOptionClickListener = listener;
            return this;
        }

        public BubbleTipView build() {
            BubbleTipView view = new BubbleTipView(mContext, mData);
            return view;
        }

        private static final class Data {
            private List<OptionInfo> mOptionInfos;
            private int mDirection = DIR_UP;
            private OnOptionClickListener mOnOptionClickListener;
        }

    }

    public static class OptionInfo {
        public final int mId;
        public final String mLabel;
        public final int mIcon;
        public final Runnable mAction;

        public OptionInfo(int id, String label, int icon, Runnable action) {
            mId = id;
            mLabel = label;
            mIcon = icon;
            mAction = action;
        }

    }

    public interface OnOptionClickListener {
        void onOptionClick(OptionInfo info);
    }

}
