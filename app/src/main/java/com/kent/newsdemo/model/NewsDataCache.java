package com.kent.newsdemo.model;

import android.util.ArrayMap;

import com.kent.newsdemo.model.entity.SingleNews;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author Kent
 * date 2018/8/21 021
 * version 1.0
 */
public class NewsDataCache {

    private ArrayMap<String, SoftReference<ArrayList<SingleNews>>> mDataMap;

    private static volatile NewsDataCache sInstance = null;

    public static NewsDataCache getInstance() {
        if (sInstance == null) {
            synchronized (NewsDataCache.class) {
                if (sInstance == null) {
                    sInstance = new NewsDataCache();
                }
            }
        }
        return sInstance;
    }

    private NewsDataCache() {
        mDataMap = new ArrayMap<>();
    }

    public static void destroy() {
        if (sInstance != null) {
            sInstance.mDataMap.clear();
            sInstance = null;
        }
    }

    public void add(String key, ArrayList<SingleNews> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        SoftReference<ArrayList<SingleNews>> sRef = mDataMap.get(key);
        if (sRef == null) {
            sRef = new SoftReference<ArrayList<SingleNews>>(list);
            mDataMap.put(key, sRef);
        } else {
            List<SingleNews> origin = sRef.get();
            if (origin == null || origin.isEmpty()) {
                sRef = new SoftReference<ArrayList<SingleNews>>(list);
                mDataMap.put(key, sRef);
            } else {
                origin.addAll(list);
            }
        }
        List<SingleNews> data = mDataMap.get(key).get();
        Collections.sort(data);
    }

    public ArrayList<SingleNews> get(String key) {
        SoftReference<ArrayList<SingleNews>> sRef = mDataMap.get(key);
        if (sRef == null) {
            return null;
        }
        return sRef.get();
    }

}
