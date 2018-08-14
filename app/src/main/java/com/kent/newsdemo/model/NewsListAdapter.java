package com.kent.newsdemo.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kent.newsdemo.GlideApp;
import com.kent.newsdemo.NetworkStateManager;
import com.kent.newsdemo.NewsDetailActivity;
import com.kent.newsdemo.R;
import com.kent.newsdemo.model.entity.SingleNews;
import com.kent.newsdemo.ui.BubbleTipView;

import java.util.List;

/**
 * author Kent
 * date 2018/7/31 031
 * version 1.0
 */
public class NewsListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private Context mContext;
    private List<SingleNews> mNewsList;
    private String mChannel;
    private Handler mHandler = new Handler();
    private Runnable mToastNoNetwork = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(mContext, R.string.no_network_toast, Toast.LENGTH_SHORT).show();
        }
    };

    private PopupWindow mTipViewWindow;

    public NewsListAdapter(Context context, List<SingleNews> data, String channel) {
        mContext = context;
        mNewsList = data;
        mChannel = channel;
    }

    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
            holder = new ViewHolder();
            holder.mImage = convertView.findViewById(R.id.image);
            holder.mTitle = convertView.findViewById(R.id.title);
            holder.mSource = convertView.findViewById(R.id.source);
            holder.mTime = convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SingleNews news = mNewsList.get(position);
        if (!TextUtils.isEmpty(news.pic)) {
            holder.mImage.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy
                    .AUTOMATIC);
            GlideApp.with(mContext).load(news.pic).apply(options).into(holder.mImage);
        } else {
            holder.mImage.setVisibility(View.GONE);
        }
        holder.mTitle.setText(news.title);
        holder.mSource.setText(news.src);
        holder.mTime.setText(optimizeTime(news.time));
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (NetworkStateManager.getsInstance().getmCurrentNetType() == NetworkStateManager
                .TYPE_NONE) {
            mHandler.removeCallbacks(mToastNoNetwork);
            mHandler.post(mToastNoNetwork);
            return;
        }
        SingleNews news = mNewsList.get(position);
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("url", news.url);
        intent.putExtra("channel", mChannel);
        mContext.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
        BubbleTipView tipView = new BubbleTipView.Builder(mContext).setDirection(BubbleTipView
                .DIR_UP)
                .addOption(new BubbleTipView.OptionInfo(R.integer.option_favorite, "收藏", R
                        .drawable.favorate_mark, new
                        Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "收藏", Toast.LENGTH_SHORT).show();
                            }
                        }))
                .addOption(new BubbleTipView.OptionInfo(R.integer.option_share, "分享", R.drawable
                        .ic_share, new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
                    }
                })).setOnOptionClickListener(new BubbleTipView.OnOptionClickListener() {
                    @Override
                    public void onOptionClick(BubbleTipView.OptionInfo info) {
                        if (info.mId == R.integer.option_share) {
                            hideTipViewWindow();
                        }
                    }
                }).build();
        tipView.create();
        tipView.measure(0, 0);
        int direction = decideDirection(view);
        if (direction == BubbleTipView.DIR_DOWN) {
            tipView.changeArrowDir(direction);
            tipView.measure(0, 0);
        }

        int y = 0;
        int yOffset = getTipViewHeightOffset(direction);
        if (direction == BubbleTipView.DIR_UP) {
            y = view.getTop() + yOffset;
        } else {
            y = view.getBottom() + tipView.getMeasuredHeight() / 2 + yOffset;
        }
        mTipViewWindow = new PopupWindow(tipView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams
                        .WRAP_CONTENT, true);
        mTipViewWindow.setTouchable(true);
        mTipViewWindow.setOutsideTouchable(true);
        mTipViewWindow.setBackgroundDrawable(new ColorDrawable());
        mTipViewWindow.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, y);
        mTipViewWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parent.setOnTouchListener(null);
            }
        });

        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setOnTouchListener(null);
                    hideTipViewWindow();
                    return true;
                }
                return false;
            }
        });

        return true;
    }

    public void hideTipViewWindow() {
        if (mTipViewWindow != null && mTipViewWindow.isShowing()) {
            mTipViewWindow.dismiss();
            mTipViewWindow = null;
        }
    }

    private int getTipViewHeightOffset(int direction) {
        int popupWindowOffset = mContext.getResources().getDimensionPixelSize(R.dimen
                .popup_window_anchor_offset);
        int bubbleViewPadding = mContext.getResources().getDimensionPixelSize(R.dimen
                .bubble_view_padding);
        if (direction == BubbleTipView.DIR_UP) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.triangle_up);
            return drawable.getIntrinsicHeight() / 2 + popupWindowOffset - bubbleViewPadding / 2;
        }
        Drawable drawable = mContext.getResources()
                .getDrawable(R.drawable.triangle_down);
        return drawable.getIntrinsicHeight() / 2 - mContext.getResources().getDimensionPixelSize
                (R.dimen
                        .divider_size) * 2 - popupWindowOffset - bubbleViewPadding / 2;
    }

    private int decideDirection(View pressedView) {
        Resources res = mContext.getResources();
        int requiredTopMargin = res.getDimensionPixelSize(R.dimen.news_tab_bar_height) +
                res.getDimensionPixelSize(R.dimen.divider_size) +
                res.getDimensionPixelSize(R.dimen.bubble_view_top_margin);

        if (pressedView.getTop() >= requiredTopMargin) {
            return BubbleTipView.DIR_UP;
        }
        return BubbleTipView.DIR_DOWN;
    }

    private String optimizeTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        String result = time.substring(0, time.lastIndexOf(":"));
        return result;
    }

    private class ViewHolder {
        private ImageView mImage;
        private TextView mTitle;
        private TextView mSource;
        private TextView mTime;
    }

}
