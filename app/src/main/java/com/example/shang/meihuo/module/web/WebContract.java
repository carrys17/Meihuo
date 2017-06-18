package com.example.shang.meihuo.module.web;

import android.app.Activity;

import com.example.shang.meihuo.base.BasePresenter;
import com.example.shang.meihuo.base.BaseView;


public interface WebContract {

    //  处理数据显示
    interface IWebView extends BaseView {
        Activity getWebViewContext();

        void setGankTitle(String title);

        void loadGankUrl(String url);

        void initWebView();
    }

    //  对Task保存、更新等
    interface IWebPresenter extends BasePresenter {
        String getGankUrl();
    }
}
