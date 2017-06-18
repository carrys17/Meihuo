package com.example.shang.meihuo.module.navdeedback;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 问题反馈页面
 */
public class NavDeedBackActivity extends BaseActivity {

    @BindView(R.id.nav_deed_back_toolbar)
    Toolbar mToolbar;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_nav_deed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({ R.id.tv_qq, R.id.tv_email})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_qq:
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=961513094"; // uin就是qq号码
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.tv_email:
                Intent data = new Intent(Intent.ACTION_SENDTO); // sendto类型的intent
                data.setData(Uri.parse("mailto:carryslin17@gmail.com")); // mailto：+ 邮箱地址
                startActivity(data);
                break;
        }
    }
}
