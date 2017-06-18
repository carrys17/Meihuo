package com.example.shang.meihuo.module.category;


import com.example.shang.meihuo.base.BasePresenter;
import com.example.shang.meihuo.base.BaseView;
import com.example.shang.meihuo.model.CategoryResult;

public interface CategoryContract {

    interface ICategoryView extends BaseView {
        
        void getCategoryItemsFail(String failMessage);

        void setCategoryItems(CategoryResult categoryResult);

        void addCategoryItems(CategoryResult categoryResult);

        void showSwipeLoading();

        void hideSwipeLoading();

        void setLoading();

        String getCategoryName();

        void noMore();
    }
    
    interface ICategoryPresenter extends BasePresenter {
        
        void getCategoryItems(boolean isRefresh);
    }
}
