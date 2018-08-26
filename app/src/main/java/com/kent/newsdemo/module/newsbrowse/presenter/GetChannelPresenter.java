package com.kent.newsdemo.module.newsbrowse.presenter;

import java.util.List;

/**
 * author Kent
 * date 2018/8/26 026
 * version 1.0
 */
public class GetChannelPresenter implements GetChannelContract.IPresenter {

    private GetChannelContract.IView mView;
    private GetChannelContract.IModel mModel;

    public GetChannelPresenter setView(GetChannelContract.IView view) {
        mView = view;
        return this;
    }

    public GetChannelPresenter setModel(GetChannelContract.IModel model) {
        mModel = model;
        return this;
    }

    @Override
    public void queryChannels() {
        if (mModel != null) {
            mModel.queryChannels();
        }
    }

    @Override
    public void cancelQuery() {
        if (mModel != null) {
            mModel.cancelQuery();
        }
    }

    @Override
    public void onGetDataSuccess(List<String> data) {
        if (mView != null) {
            mView.showSuccessView(data);
        }
    }

    @Override
    public void onGetDataFailed(String reason) {
        if (mView != null) {
            mView.showFailedView(reason);
        }
    }

}
