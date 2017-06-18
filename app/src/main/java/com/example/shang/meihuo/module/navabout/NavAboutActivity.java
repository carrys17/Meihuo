package com.example.shang.meihuo.module.navabout;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseActivity;

import butterknife.BindView;

public class NavAboutActivity extends BaseActivity {

    @BindView(R.id.nav_about_toolbar)
    Toolbar mToolbar;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_nav_about;
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

}
