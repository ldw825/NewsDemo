package com.kent.newspaper.module.newsbrowse.model;

import com.kent.newspaper.module.newsbrowse.model.abs.GetDataApi;

/**
 * @author Kent
 * @version 1.0
 * @date 2018/12/22
 */
public class GetDataApiHolder {

    private static GetDataApiHolder sGetDataApiHolder;
    private GetDataApi mGetDataApi;

    private GetDataApiHolder() {
    }

    public static GetDataApiHolder getInstance() {
        if (sGetDataApiHolder == null) {
            sGetDataApiHolder = new GetDataApiHolder();
        }
        return sGetDataApiHolder;
    }

    public void set(GetDataApi getDataApi) {
        if (mGetDataApi == null) {
            mGetDataApi = getDataApi;
        }
    }

    public GetDataApi get() {
        return mGetDataApi;
    }

    public static void destory() {
        if (sGetDataApiHolder != null) {
            sGetDataApiHolder.mGetDataApi = null;
            sGetDataApiHolder = null;
        }
    }

}
