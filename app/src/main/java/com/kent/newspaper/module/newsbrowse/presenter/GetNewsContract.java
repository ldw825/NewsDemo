package com.kent.newspaper.module.newsbrowse.presenter;

import com.kent.newspaper.module.newsbrowse.entity.NewsInfo;
import com.kent.newspaper.module.newsbrowse.model.abs.OnGetDataListener;

/**
 * author Kent
 * date 2018/8/26 026
 * version 1.0
 */
public interface GetNewsContract {

    interface IGetNews extends OnGetDataListener<NewsInfo> {
        void getNews(String channel);
    }

    public interface IPresenter extends GetNewsContract.IGetNews {
    }

    public interface IView {
        void showSuccessView(NewsInfo data);

        void showFailedView(String reason);
    }

    public interface IModel extends GetNewsContract.IGetNews {
    }

}
