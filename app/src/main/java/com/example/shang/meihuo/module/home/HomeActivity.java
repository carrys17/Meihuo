package com.example.shang.meihuo.module.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shang.meihuo.GlideImageLoader;
import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseActivity;
import com.example.shang.meihuo.base.adapter.CommonViewPagerAdapter;
import com.example.shang.meihuo.config.GlobalConfig;
import com.example.shang.meihuo.model.PictureModel;
import com.example.shang.meihuo.module.category.CategoryFragment;
import com.example.shang.meihuo.module.navabout.NavAboutActivity;
import com.example.shang.meihuo.module.navdeedback.NavDeedBackActivity;
import com.example.shang.meihuo.module.picture.PictureActivity;
import com.example.shang.meihuo.module.web.WebViewActivity;
import com.example.shang.meihuo.utils.AlipayZeroSdk;
import com.example.shang.meihuo.utils.PerfectClickListener;
import com.example.shang.meihuo.utils.ScreenUtil;
import com.example.shang.meihuo.utils.StatusBarUtil;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * 主页面
 */

public class HomeActivity extends BaseActivity implements HomeContract.IHomeView,OnBannerListener{

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.main_tab)
    DachshundTabLayout mTabLayout;
    @BindView(R.id.main_vp)
    ViewPager mViewPager;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.mainActivity)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_banner)
    Banner mBanner;

    // 保存用户按返回键的时间
    private long mExitTime = 0;
    // 为了刷新显示，出此下策，在下面的viewpager刷新时顺便刷新上面的轮播图，因此为static类型,
    // 因为本来是banner加载失败后恢复网络，没用相应的重刷新方法
    public static HomePresenter mHomePresenter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mHomePresenter = new HomePresenter(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4 以上版本
            // 设置 Toolbar 高度为 80dp，适配状态栏
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            layoutParams.height = ScreenUtil.dip2px(this,80);
            mToolbar.setLayoutParams(layoutParams);
        }

//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        params.setMargins(0,ScreenUtil.getStatusBarHeight(this),0,0);
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//        mTabLayout.setLayoutParams(params);

        initDrawerLayout();
        mBanner.setIndicatorGravity(BannerConfig.RIGHT); //点的位置，右边
        mBanner.setOnBannerListener(this);

        String[] titles = {
                GlobalConfig.CATEGORY_NAME_APP,
                GlobalConfig.CATEGORY_NAME_ANDROID,
                GlobalConfig.CATEGORY_NAME_IOS,
                GlobalConfig.CATEGORY_NAME_FRONT_END,
                GlobalConfig.CATEGORY_NAME_RECOMMEND,
                GlobalConfig.CATEGORY_NAME_RESOURCE};

        CommonViewPagerAdapter infoPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(),titles);
        // App
        CategoryFragment appFragment = CategoryFragment.newInstance(titles[0]);
        // Android
        CategoryFragment androidFragment = CategoryFragment.newInstance(titles[1]);
        // iOS
        CategoryFragment iOSFragment = CategoryFragment.newInstance(titles[2]);
        // 前端
        CategoryFragment frontFragment = CategoryFragment.newInstance(titles[3]);
        // 瞎推荐
        CategoryFragment referenceFragment = CategoryFragment.newInstance(titles[4]);
        // 拓展资源s
        CategoryFragment resFragment = CategoryFragment.newInstance(titles[5]);

        infoPagerAdapter.addFragment(appFragment);
        infoPagerAdapter.addFragment(androidFragment);
        infoPagerAdapter.addFragment(iOSFragment);
        infoPagerAdapter.addFragment(frontFragment);
        infoPagerAdapter.addFragment(referenceFragment);
        infoPagerAdapter.addFragment(resFragment);

        mViewPager.setAdapter(infoPagerAdapter); // 适配器，自己定义的，继承的是FragmentPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager); // 绑定tablayout和viewpager
        mViewPager.setCurrentItem(1); // 设置当前在第二页
        mViewPager.setOffscreenPageLimit(6); // 一共6页，这两个数，一个是size，一个是下标


        mHomePresenter.subscribe();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomePresenter != null){
            mHomePresenter.unSubscribe();
        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * inflateHeaderView 进来的布局要宽一些
     */
    private void initDrawerLayout() {
        mNavView.inflateHeaderView(R.layout.layout_main_nav); // navigation直接绑定这个布局，不用在xml文件中绑定的
        View headerView = mNavView.getHeaderView(0);
        headerView.findViewById(R.id.ll_nav_homepage).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_scan_address).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_deedback).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_login).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_exit).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_donation).setOnClickListener(mListener);

    }

    private PerfectClickListener mListener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            // mDrawerLayout.closeDrawers(); 同样的效果，收回抽屉
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                   switch (v.getId()){
                       case R.id.ll_nav_homepage: // 返回主页
                           return;
                       case R.id.ll_nav_scan_address: // 关于项目
                           startActivity(new Intent(HomeActivity.this, NavAboutActivity.class));
                           break;
                       case R.id.ll_nav_deedback: // 问题反馈
                           startActivity(new Intent(HomeActivity.this, NavDeedBackActivity.class));
                           break;
                       case R.id.ll_nav_donation: // 捐赠开发者
                           // https://fama.alipay.com/qrcode/qrcodelist.htm?qrCodeType=P  二维码地址
                           // http://cli.im/deqr/ 解析二维码，在这个网站上，把付款二维码解析成string字符串
                           // aex01018hzmxqeqmcaffh96
                           if (AlipayZeroSdk.hasInstalledAlipayClient(HomeActivity.this)) {
                               AlipayZeroSdk.startAlipayClient(HomeActivity.this, "FKX02059NHL6XA8ASTEDEC");
                           } else {
                                Snackbar.make(mToolbar, "谢谢，您没有安装支付宝客户端", Snackbar.LENGTH_LONG).show();
                           }
                           break;
                       case R.id.ll_nav_login: // 登录github账号
                           Intent intent_login = new Intent(HomeActivity.this, WebViewActivity.class);
                           intent_login.putExtra(WebViewActivity.GANK_TITLE, "登录github");
                           intent_login.putExtra(WebViewActivity.GANK_URL, "https://github.com/login");
                           startActivity(intent_login);
                           break;
                       case R.id.ll_nav_exit:
                           finish();
                           break;
                       default:
                           break;
                   }
                }
            },260);
        }
    };


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Snackbar.make(mDrawerLayout, R.string.exit_toast, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void showBannerFail(String failMessage) {
        Toasty.error(this, failMessage).show();
    }

    @Override
    public void setBanner(List<String> imgUrls) {
        mBanner.setImages(imgUrls).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    public void OnBannerClick(int position) {
        PictureModel model = mHomePresenter.getBannerModel().get(position);
//        Intent intent = PictureActivity.newIntent(this,model.url,model.desc);
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this,mBanner,PictureActivity.TRANSIT_PIC);
//        ActivityCompat.startActivity(this,intent,optionsCompat.toBundle());
        PictureActivity.start(this,model.url,model.desc,mBanner);
    }
}
