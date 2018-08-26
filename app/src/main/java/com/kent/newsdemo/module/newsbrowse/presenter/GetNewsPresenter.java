package com.kent.newsdemo.module.newsbrowse.presenter;

import com.kent.newsdemo.module.newsbrowse.entity.NewsInfo;

/**
 * author Kent
 * date 2018/8/26 026
 * version 1.0
 */
public class GetNewsPresenter implements GetNewsContract.IPresenter {

    private GetNewsContract.IView mView;
    private GetNewsContract.IModel mModel;

    public GetNewsPresenter setView(GetNewsContract.IView view) {
        mView = view;
        return this;
    }

    public GetNewsPresenter setModel(GetNewsContract.IModel model) {
        mModel = model;
        return this;
    }

    @Override
    public void getNews(String channel) {
        if (mModel != null) {
            mModel.getNews(channel);
        }
    }

    @Override
    public void onGetDataSuccess(NewsInfo data) {
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
