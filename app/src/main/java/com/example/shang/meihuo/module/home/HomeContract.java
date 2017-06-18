package com.example.shang.meihuo.module.home;


import com.example.shang.meihuo.base.BasePresenter;
import com.example.shang.meihuo.base.BaseView;

import java.util.List;


public interface HomeContract {
    interface IHomeView extends BaseView {
        void showBannerFail(String failMessage);

        void setBanner(List<String> imgUrls);

    }
        
    interface IHomePresenter extends BasePresenter {
        /**
         * 获取Banner轮播图数据
         */
        void getBannerData();

    }
}
