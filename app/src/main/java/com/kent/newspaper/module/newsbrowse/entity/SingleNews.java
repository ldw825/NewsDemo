package com.kent.newspaper.module.newsbrowse.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class SingleNews implements Parcelable, Comparable<SingleNews> {

    public String title;

    public String time;

    public String src;

    public String category;

    public String pic;

    public String content;

    public String url;

    public String weburl;

    public SingleNews() {
    }

    protected SingleNews(Parcel in) {
        title = in.readString();
        time = in.readString();
        src = in.readString();
        category = in.readString();
        pic = in.readString();
        content = in.readString();
        url = in.readString();
        weburl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(src);
        dest.writeString(category);
        dest.writeString(pic);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(weburl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SingleNews> CREATOR = new Creator<SingleNews>() {
        @Override
        public SingleNews createFromParcel(Parcel in) {
            return new SingleNews(in);
        }

        @Override
        public SingleNews[] newArray(int size) {
            return new SingleNews[size];
        }
    };

    @Override
    public int compareTo(@NonNull SingleNews o) {
        if (TextUtils.isEmpty(this.time) && TextUtils.isEmpty(o.time)) {
            return 0;
        }
        return o.time.compareTo(this.time);
    }

}
