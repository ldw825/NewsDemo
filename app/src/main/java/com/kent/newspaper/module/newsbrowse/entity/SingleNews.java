package com.kent.newspaper.module.newsbrowse.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class SingleNews implements Comparable<SingleNews> {

    private String title;

    private String time;

    private String src;

    private String category;

    private String pic;

    private String content;

    private String url;

    private String weburl;

    @Override
    public int compareTo(@NonNull SingleNews o) {
        if (TextUtils.isEmpty(this.time) && TextUtils.isEmpty(o.time)) {
            return 0;
        }
        return o.time.compareTo(this.time);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
        android.util.Log.d("ldwlog", "setPic, pic=" + pic);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

}
