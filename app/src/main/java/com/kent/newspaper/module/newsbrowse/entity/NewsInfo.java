package com.kent.newspaper.module.newsbrowse.entity;

import java.util.List;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class NewsInfo {

    private String channel;

    private int num;

    private List<SingleNews> list;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<SingleNews> getList() {
        return list;
    }

    public void setList(List<SingleNews> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return super.toString() + ", channel=" + channel + ", num=" + num + ", list=" + list;
    }
}
