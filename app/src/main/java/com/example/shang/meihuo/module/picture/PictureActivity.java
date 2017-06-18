package com.example.shang.meihuo.module.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseActivity;
import com.example.shang.meihuo.utils.Utils;
import com.github.chrisbanes.photoview.PhotoView;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 大图页面
 * <p>
 */
public class PictureActivity extends BaseActivity {

    public static final String EXTRA_IMAGE_URL = "com.nanchen.aiyagirl.module.picture.PictureActivity.EXTRA_IMAGE_URL";
    public static final String EXTRA_IMAGE_TITLE = "com.nanchen.aiyagirl.module.picture.PictureActivity.EXTRA_IMAGE_TITLE";
    public static final String TRANSIT_PIC = "picture";

    String mImageUrl, mImageTitle;
    @BindView(R.id.picture_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.picture_img)
    PhotoView mImgView;
    @BindView(R.id.picture_btn_save)
    ImageButton mBtnSave;
    @BindView(R.id.picture_app_bar)
    AppBarLayout mAppBarLayout;

    private PictureContract.Presenter mPresenter;

    public static Intent newIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        intent.putExtra(EXTRA_IMAGE_TITLE, desc);
        return intent;
    }

    public static void start(Activity context, String url, String desc, Banner banner){ //启动PictureActivity，为了给HoneActivity调用
        Intent intent = new Intent(context,PictureActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        intent.putExtra(EXTRA_IMAGE_TITLE, desc);
        // activity的过度动画，平滑的将一个控件平移的过渡到第二个activity
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context, banner, TRANSIT_PIC);//与xml文件对应
        //三个参数，第一个是指当前activity，第二个和第三个参数分别是进入动画和退出动画
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_picture;
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        mPresenter = new PicturePresenter(this);
        parseIntent();
        ViewCompat.setTransitionName(mImgView, TRANSIT_PIC); // 动画切换效果

//        mAppBarLayout.setAlpha(0.7f);
        mToolbar.setTitle(TextUtils.isEmpty(mImageTitle) ? "图片预览" : mImageTitle);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Glide.with(Utils.getContext())
                .load(mImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //图片缓存， 缓存所有版本的图像（默认行为）
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mImgView.setImageDrawable(resource);
                    }
                });

    }


    @OnClick(R.id.picture_btn_save)
    public void onClick() {
//        Toasty.info(this, "点击了保存图片", Toast.LENGTH_SHORT, true).show();

        if (mPresenter != null){
            mPresenter.saveGirl(mImageUrl,mImgView.getWidth(),mImgView.getHeight(),mImageTitle);
        }
    }


    @OnClick(R.id.picture_img)
    public void onPictureClick() {
        if (getSupportActionBar() != null) {
            if (getSupportActionBar().isShowing()) {
                getSupportActionBar().hide(); // 隐藏标题栏
            } else {
                getSupportActionBar().show(); // 显示标题栏
            }
        }
    }
}
