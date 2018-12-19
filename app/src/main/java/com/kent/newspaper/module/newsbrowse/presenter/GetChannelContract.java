package com.kent.newspaper.module.newsbrowse.presenter;

import com.kent.newspaper.module.newsbrowse.model.abs.OnGetDataListener;

import java.util.List;

/**
 * author Kent
 * date 2018/8/26 026
 * version 1.0
 */
public interface GetChannelContract {

    interface IGetChannel extends OnGetDataListener<List<String>> {
        void queryChannels();

        void cancelQuery();
    }

    public interface IPresenter extends IGetChannel {
    }

    public interface IView {
        void showSuccessView(List<String> data);

        void showFailedView(String reason);
    }

    public interface IModel extends IGetChannel {
    }

}
