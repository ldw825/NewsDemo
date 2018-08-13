package com.kent.newsdemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kent.newsdemo.R;
import com.kent.newsdemo.util.Utils;

/**
 * author Kent
 * date 2018/8/13 013
 * version 1.0
 */
public class BottomTab extends RelativeLayout {

    private ImageView mIcon;
    private TextView mName;

    public BottomTab(Context context) {
        this(context, null);
    }

    public BottomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_tab, this);
        setBackgroundResource(Utils.getAttrValue(context, android.R.attr
                .selectableItemBackgroundBorderless));
        setGravity(Gravity.CENTER_VERTICAL);
        mIcon = findViewById(R.id.tab_icon);
        mName = findViewById(R.id.tab_name);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomTab);
        int icon = a.getResourceId(R.styleable.BottomTab_tabIcon, -1);
        int iconSelect = a.getResourceId(R.styleable.BottomTab_tabIconSelected, -1);
        int name = a.getResourceId(R.styleable.BottomTab_tabName, -1);
        a.recycle();
        if (icon != -1) {
            if (iconSelect == -1) {
                mIcon.setImageResource(icon);
            } else {
                StateListDrawable selector = new StateListDrawable();
                selector.addState(new int[]{android.R.attr.state_selected}, getResources()
                        .getDrawable(iconSelect));
                selector.addState(new int[]{}, getResources().getDrawable(icon));
                mIcon.setImageDrawable(selector);
            }
        }
        if (name != -1) {
            mName.setText(name);
        }
    }

    public BottomTab setIcon(int resId) {
        mIcon.setImageResource(resId);
        return this;
    }

    public BottomTab setName(int resId) {
        mName.setText(resId);
        return this;
    }

    public void select(boolean selected) {
        setSelected(selected);
        mIcon.setSelected(selected);
        mName.setSelected(selected);
        if (selected) {
            mName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            mName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

}
