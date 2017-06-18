package com.example.shang.meihuo.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseActivity;
import com.example.shang.meihuo.config.ConstantsImageUrl;
import com.example.shang.meihuo.module.home.HomeActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 闪屏页面
 *
 */

public class SplashActivity extends BaseActivity {
    private boolean isIn;

    @BindView(R.id.splash_iv_pic)
    ImageView mIvPic;
    @BindView(R.id.splash_tv_jump)
    TextView mTvJump;
    @BindView(R.id.splash_iv_defult_pic)
    ImageView mIvDefultPic;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

       int i = new Random().nextInt(ConstantsImageUrl.getMyOnepic().size());


        // 先显示默认图
        mIvDefultPic.setImageDrawable(getResources().getDrawable(R.drawable.img_transition_default));
        //这个没有被抛弃，但是setImageDrawable却是最省内存最高效的
        //  mIvDefultPic.setImageResource(R.drawable.img_transition_default);


        //使用Glide，我们就完全不用担心图片内存浪费，甚至是内存溢出的问题。因为Glide从来都不会
        // 直接将图片的完整尺寸全部加载到内存中，而是用多少加载多少。Glide会自动判断ImageView的
        // 大小，然后只将这么大的图片像素加载到内存当中，帮助我们节省内存开支
        Glide.with(this)
                .load(ConstantsImageUrl.getMyOnepic().get(i))  // load A into B 把A加载到B
                .placeholder(R.drawable.img_transition_default) // 当加载网络图片时，由于加载过程中图片未能及时显示，此时可能需要设置等待时的图片
                .error(R.drawable.img_transition_default)// 当加载图片失败时，通过error(Drawable drawable)方法设置加载失败后的图片显示：
                .into(mIvPic);
        //使用PostDelayed方法，1.5秒后调用此Runnable对象
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIvDefultPic.setVisibility(View.GONE);
            }
        }, 1500);
        // 两个结合起来相当于第一个默认图片播放1.5s，第二个随机图片播放2s，加起来一共执行了3.5s
        //还有一点就是随机图片一进来就已经加载好了，只是被盖在下面,xml布局里面，先写的随机图片
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 3500);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 跳转到主页面
     */
    private void toMainActivity() {
        if (isIn) {
            return;
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //它必需紧挨着startActivity()或者finish()函数之后调用,activity跳转动画
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
        isIn = true;
    }


    // butterknife的好处，绑定onclick就直接写监听了，不用在xml里定义onclick了，而且没有传参数，区别开来了
    @OnClick(R.id.splash_tv_jump)
    public void onClick() {
        toMainActivity();
    }
}


