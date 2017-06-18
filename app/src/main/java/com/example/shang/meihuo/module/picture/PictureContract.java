package com.example.shang.meihuo.module.picture;


import com.example.shang.meihuo.base.BasePresenter;

public interface PictureContract {

    interface Presenter extends BasePresenter {
        void saveGirl(String url, int width, int height, String title);
    }
}
