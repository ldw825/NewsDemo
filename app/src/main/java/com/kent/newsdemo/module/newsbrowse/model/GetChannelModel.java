package com.kent.newsdemo.module.newsbrowse.model;

import com.kent.newsdemo.module.newsbrowse.model.abs.GetData;
import com.kent.newsdemo.module.newsbrowse.model.impl.GetChannelData;
import com.kent.newsdemo.module.newsbrowse.presenter.GetChannelContract;

import java.util.List;

/**
 * author Kent
 * date 2018/8/26 026
 * version 1.0
 */
public class GetChannelModel implements GetChannelContract.IModel {

    private GetChannelContract.IPresenter mPresenter;
    private GetData mGetChannelData;

    public GetChannelModel(GetChannelContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void queryChannels() {
        mGetChannelData = new GetChannelData();
        mGetChannelData.addListener(this);
        mGetChannelData.doGetData();
    }

    @Override
    public void cancelQuery() {
        if (mGetChannelData != null) {
            mGetChannelData.cancelGetData();
        }
    }

    @Override
    public void onGetDataSuccess(List<String> data) {
        mGetChannelData.removeListener(this);
        if (mPresenter != null) {
            mPresenter.onGetDataSuccess(data);
        }
    }

    @Override
    public void onGetDataFailed(String reason) {
        mGetChannelData.removeListener(this);
        if (mPresenter != null) {
            mPresenter.onGetDataFailed(reason);
        }
    }

}
