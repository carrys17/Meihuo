package com.example.shang.meihuo.module.category;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.shang.meihuo.ConfigManage;
import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.adapter.CommonRecyclerAdapter;
import com.example.shang.meihuo.base.adapter.CommonRecyclerHolder;
import com.example.shang.meihuo.base.adapter.ListenerWithPosition;
import com.example.shang.meihuo.model.CategoryResult;
import com.example.shang.meihuo.module.web.WebViewActivity;
import com.example.shang.meihuo.utils.TimeUtil;


//  为每一个list那种添加category，就是每一个小栏目的 内容，并设置了相应的监听事件，点击之后进入webActivity
class CategoryRecyclerAdapter extends CommonRecyclerAdapter<CategoryResult.ResultsBean> implements ListenerWithPosition.OnClickWithPositionListener<CommonRecyclerHolder>{

    CategoryRecyclerAdapter(Context context) {
        super(context, null, R.layout.item_category);
    }

    @Override
    public void convert(CommonRecyclerHolder holder, CategoryResult.ResultsBean resultsBean) {
        if (resultsBean != null) {
            ImageView imageView = holder.getView(R.id.category_item_img);
            Log.i("xyz","  "+ConfigManage.INSTANCE.isListShowImg());
            if (ConfigManage.INSTANCE.isListShowImg()) { // 列表显示图片
                imageView.setVisibility(View.VISIBLE);
                String quality = "";
                if (resultsBean.images != null && resultsBean.images.size() > 0) {
                    switch (ConfigManage.INSTANCE.getThumbnailQuality()) {
                        case 0: // 原图
                            quality = "";
                            break;
                        case 1: //
                            quality = "?imageView2/0/w/400";
                            break;
                        case 2:
                            quality = "?imageView2/0/w/190";
                            break;
                    }
                    Glide.with(mContext)
                            .load(resultsBean.images.get(0) + quality)
                            .placeholder(R.mipmap.image_default)
                            .error(R.mipmap.image_default)
                            .into(imageView);
                } else { // 列表不显示图片
                    Glide.with(mContext).load(R.mipmap.image_default).into(imageView);
                }
            } else {
                imageView.setVisibility(View.GONE);
            }

            holder.setTextViewText(R.id.category_item_desc, resultsBean.desc == null ? "unknown" : resultsBean.desc);
            holder.setTextViewText(R.id.category_item_author, resultsBean.who == null ? "unknown" : resultsBean.who);
            holder.setTextViewText(R.id.category_item_time, TimeUtil.dateFormat(resultsBean.publishedAt));
            Log.i("resultBean"," "+resultsBean.publishedAt);
            holder.setTextViewText(R.id.category_item_src, resultsBean.source == null ? "unknown" : resultsBean.source);
            holder.setOnClickListener(this, R.id.category_item_layout);
        }
    }

    @Override
    public void onClick(View v, int position, CommonRecyclerHolder holder) {
        //        Toasty.info(mContext,"跳转到相应网页！", Toast.LENGTH_SHORT,true).show();
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(WebViewActivity.GANK_TITLE, mData.get(position).desc);
        intent.putExtra(WebViewActivity.GANK_URL, mData.get(position).url);
        mContext.startActivity(intent);
    }
}
