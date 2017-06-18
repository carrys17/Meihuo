package com.example.shang.meihuo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.shang.meihuo.R;


public class ShareUtil {
    public static void share(Context context, int stringRes) {
        share(context, context.getString(stringRes));
    }


    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //设置合适的MIME类型，并且在附件数据中的EXTRA_STREAM中放一个指向数据的URI，就可以来分享
        // 二进制数据。这个通常用来分享图片，但是也可以用来分享任何类型的二进制内容，比如视频，文件等等
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }


    public static void share(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //    ”image/jpeg”、”audio/mp4a-latm”、”audio/x-mpeg”、 “video/mp4“
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        intent.putExtra(Intent.EXTRA_TEXT, extraText); //   EXTRA_EMAIL, EXTRA_CC, EXTRA_BCC, EXTRA_SUBJECT等等
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.action_share)));
    }
}
