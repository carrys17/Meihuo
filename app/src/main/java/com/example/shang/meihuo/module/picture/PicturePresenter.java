package com.example.shang.meihuo.module.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.example.shang.meihuo.R;
import com.example.shang.meihuo.utils.ToastyUtil;
import com.example.shang.meihuo.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;



public class PicturePresenter implements PictureContract.Presenter {

    private Context mContext;

    public PicturePresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void saveGirl(final String url, final int width, final int height, final String title) {
        Observable.create(new OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(Utils.getContext()) // 这里的是调用Utils的getContext方法，
                            // 它调用的是getApplicationContext()，也就是说有当应用程序被杀掉的时候，图片加载才会停止。
                            .load(url)
                            .asBitmap() //  这里只允许加载静态图片，还一直以为是转换为bitmap，跟asGif（）对应。asGif（）只能加载gif图
                            .into(width, height)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (bitmap == null) {
                    subscriber.onError(new Exception("无法下载到图片！"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() { //    将bitmap转换为url，下载到本地的文件
            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                File appDir = new File(Environment.getExternalStorageDirectory(), mContext.getResources().getString(R.string.app_name));
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = title.replace('/', '-') + ".jpg";
                File file = new File(appDir, fileName); //  两个路劲结合起来，成为新的路径
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    if (bitmap != null) {
                        //  压缩图片
                        bitmap.compress(CompressFormat.JPEG, 100, fos); // 0-100,100是图片质量最好
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.fromFile(file);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                mContext.sendBroadcast(scannerIntent);
                return Observable.just(uri);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), mContext.getResources().getString(R.string.app_name));
                        String msg = String.format("图片已保存至 %s 文件夹", appDir.getAbsoluteFile());
                        ToastyUtil.showSuccess(msg);
                    }
                });

    }
}
