package com.kent.newspaper.module.newsbrowse.model;

import com.kent.newspaper.module.newsbrowse.entity.NewsInfo;
import com.kent.newspaper.module.newsbrowse.model.impl.GetNewsData;
import com.kent.newspaper.module.newsbrowse.presenter.GetNewsContract;

/**
 * author Kent
 * date 2018/8/26 026
 * version 1.0
 */
public class GetNewsModel implements GetNewsContract.IModel {

    private GetNewsData mGetNewsData;
    private GetNewsContract.IPresenter mPresenter;

    public GetNewsModel(GetNewsContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getNews(String channel) {
        mGetNewsData = new GetNewsData(channel);
        mGetNewsData.addListener(this);
        mGetNewsData.doGetData();
    }

    @Override
    public void onGetDataSuccess(NewsInfo data) {
        mGetNewsData.removeListener(this);
        if (mPresenter != null) {
            mPresenter.onGetDataSuccess(data);
        }
    }

    @Override
    public void onGetDataFailed(String reason) {
        mGetNewsData.removeListener(this);
        if (mPresenter != null) {
            mPresenter.onGetDataFailed(reason);
        }
    }

}
