package com.kent.newsdemo.module.newsbrowse.model.abs;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public interface OnGetDataListener<T> {

    /**
     * called on get network data success, running on main thread
     *
     * @param data the network data gotten
     */
    void onGetDataSuccess(T data);

    /**
     * called on get network data failed, running on main thread
     *
     * @param reason the reason for failure
     */
    void onGetDataFailed(String reason);

}
