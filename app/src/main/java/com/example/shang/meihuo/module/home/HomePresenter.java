package com.example.shang.meihuo.module.home;


import com.example.shang.meihuo.model.CategoryResult;
import com.example.shang.meihuo.model.PictureModel;
import com.example.shang.meihuo.net.NetWork;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomePresenter implements HomeContract.IHomePresenter {

    private Subscription mSubscription;

    private HomeContract.IHomeView mHomeView;

    private List<PictureModel> mModels; //  图片的模型，包含图片的链接和描述，其实描述也没有

    HomePresenter(HomeContract.IHomeView homeView){
        this.mHomeView = homeView;
        mModels = new ArrayList<>();
    }

    @Override
    public void subscribe() {
        getBannerData();
    }

    //  该类自己的方法，不是继承而来的，比较特殊，所以在activity中定义时就直接定为HomePresenter而不是IHomePresenter
    //  主要为了服务图片的点击了之后的操作（下载）
    public List<PictureModel> getBannerModel(){
        return this.mModels;
    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void getBannerData() {
        mSubscription = NetWork.getGankApi()
                // 福利是代码家写死的string，换成别的识别不到
                .getCategoryData("福利",5,1) // 返回的observable就是CategoryResult,所以下面的subscribe就是CategoryResult类型
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mHomeView.showBannerFail("Banner 图加载失败");
                    }

                    @Override
                    public void onNext(CategoryResult categoryResult) {
                        if (categoryResult != null && categoryResult.results != null
                                && categoryResult.results.size() > 0){
                            List<String> imgUrls = new ArrayList<>(); // 图片的链接
                            for (CategoryResult.ResultsBean result : categoryResult.results) {
                                if (!result.url.isEmpty()){
                                    imgUrls.add(result.url);
                                }
                                PictureModel model = new PictureModel();
                                model.desc = result.desc;
                                model.url = result.url;
                                mModels.add(model);
                            }
                            mHomeView.setBanner(imgUrls);

                        }else{
                            mHomeView.showBannerFail("Banner 图加载失败");
                        }
                    }
                });
    }

}
