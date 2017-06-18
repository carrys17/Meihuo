package com.example.shang.meihuo.module.category;


import com.example.shang.meihuo.config.GlobalConfig;
import com.example.shang.meihuo.model.CategoryResult;
import com.example.shang.meihuo.module.category.CategoryContract.ICategoryPresenter;
import com.example.shang.meihuo.module.category.CategoryContract.ICategoryView;
import com.example.shang.meihuo.net.NetWork;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ICategoryPresenter
 */

public class CategoryPresenter implements ICategoryPresenter {

    private ICategoryView mCategoryICategoryView;
    private int mPage = 1;
    private Subscription mSubscription;

    public CategoryPresenter(ICategoryView androidICategoryView) {
        mCategoryICategoryView = androidICategoryView;
    }

    @Override
    public void subscribe() {
        getCategoryItems(true);
    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null  && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void getCategoryItems(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
            mCategoryICategoryView.showSwipeLoading();
        } else {
            mPage++;
        }
        mSubscription = NetWork.getGankApi()
                .getCategoryData(mCategoryICategoryView.getCategoryName(), GlobalConfig.CATEGORY_COUNT,mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCategoryICategoryView.hideSwipeLoading();
                        mCategoryICategoryView.getCategoryItemsFail(mCategoryICategoryView.getCategoryName()+" 列表数据获取失败！");
                    }

                    @Override
                    public void onNext(CategoryResult categoryResult) {
                        if (isRefresh){
                            mCategoryICategoryView.setCategoryItems(categoryResult);
                            mCategoryICategoryView.hideSwipeLoading();
                            mCategoryICategoryView.setLoading();
                        }else {
                            mCategoryICategoryView.addCategoryItems(categoryResult);
                        }
                    }
                });

    }
}
