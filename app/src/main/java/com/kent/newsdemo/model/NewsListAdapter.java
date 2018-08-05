package com.kent.newsdemo.model;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kent.newsdemo.GlideApp;
import com.kent.newsdemo.NetworkStateManager;
import com.kent.newsdemo.NewsDetailActivity;
import com.kent.newsdemo.R;
import com.kent.newsdemo.model.entity.SingleNews;

import java.util.List;

/**
 * author Kent
 * date 2018/7/31 031
 * version 1.0
 */
public class NewsListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

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
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
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
        if (NetworkStateManager.getsInstance().getmCurrentNetType() == NetworkStateManager.TYPE_NONE) {
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
