package com.example.shang.meihuo.module.category;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;

import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseFragment;
import com.example.shang.meihuo.model.CategoryResult;
import com.example.shang.meihuo.module.category.CategoryContract.ICategoryPresenter;
import com.example.shang.meihuo.module.category.CategoryContract.ICategoryView;
import com.example.shang.meihuo.module.home.HomeActivity;
import com.example.shang.meihuo.widget.RecyclerViewDivider;
import com.example.shang.meihuo.widget.RecyclerViewWithFooter.OnLoadMoreListener;
import com.example.shang.meihuo.widget.RecyclerViewWithFooter.RecyclerViewWithFooter;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * 主页轮播下面的Fragment
 */

public class CategoryFragment extends BaseFragment implements ICategoryView, OnRefreshListener, OnLoadMoreListener {

    public static final String CATEGORY_NAME = "com.example.shang.module.category.CategoryFragment.CATEGORY_NAME";
    @BindView(R.id.recyclerView)
    RecyclerViewWithFooter mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String categoryName;
    private CategoryRecyclerAdapter mAdapter;
    private ICategoryPresenter mICategoryPresenter;

    public static CategoryFragment newInstance(String mCategoryName) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_NAME, mCategoryName);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_category;
    }

    @Override
    protected void init() {
        mICategoryPresenter = new CategoryPresenter(this);

        categoryName = getArguments().getString(CATEGORY_NAME);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new CategoryRecyclerAdapter(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置布局管理器
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));//添加分割线
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setEmpty();

        mICategoryPresenter.subscribe();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mICategoryPresenter != null) {
            mICategoryPresenter.unSubscribe();
        }
    }

    @Override
    public void onRefresh() {
        mICategoryPresenter.getCategoryItems(true);
    }

    @Override
    public void onLoadMore() {
        mICategoryPresenter.getCategoryItems(false);
    }

    @Override
    public void getCategoryItemsFail(String failMessage) {
        if (getUserVisibleHint()) {
            Toasty.error(this.getContext(), failMessage).show();
           // ToastyUtil.showError(failMessage);
        }
    }

    @Override
    public void setCategoryItems(CategoryResult categoryResult) {
        mAdapter.setData(categoryResult.results);
    }

    @Override
    public void addCategoryItems(CategoryResult categoryResult) {
        mAdapter.addData(categoryResult.results);

    }

    @Override
    public void showSwipeLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideSwipeLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setLoading() {
        if (HomeActivity.mHomePresenter!=null){
            //  轮播图的刷新
            HomeActivity.mHomePresenter.subscribe();
        }
        mRecyclerView.setLoading();
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }

    @Override
    public void noMore() {
        mRecyclerView.setEnd("没有更多数据");
    }

}
