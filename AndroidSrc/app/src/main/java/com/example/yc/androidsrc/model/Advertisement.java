package com.example.yc.androidsrc.model;

import android.content.Context;
import android.util.Log;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.service.AdvertisementService;
import com.example.yc.androidsrc.utils.ImageDownloadUtil;
import com.example.yc.androidsrc.utils.SharedPreferencesUtil;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 广告类Model层
 *
 * @author RebornC
 * Created on 2019/3/5.
 */

public class Advertisement {
    private String updateTime;
    private String version;
    private String imageUrl;
    private String httpUrl;

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getVersion() {
        return version;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    /**
     * 网络获取广告数据
     *
     * @param context
     */
    public void getAdvertisementMsg(final Context context) {
        String httpUrl = context.getResources().getString(R.string.advisement_url);
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(httpUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        AdvertisementService advertisementService = retrofit.create(AdvertisementService.class);
        advertisementService.getAdvertisementList()
                .subscribeOn(Schedulers.io()) // 在子线程里进行http访问
                .observeOn(AndroidSchedulers.mainThread()) // 在UI线程处理返回接口
                .subscribe(new Observer<List<Advertisement>>() { // 订阅
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Advertisement> advertisementList) {
                        getLatestVersion(context, advertisementList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 进行判断，获取最新的广告资源
     *
     * @param context
     * @param advertisementList
     */
    public void getLatestVersion(Context context, List<Advertisement> advertisementList) {

        // 先获取当前广告版本，0表示尚未植入广告
        String currentVersion = SharedPreferencesUtil.get(context, "version", "0").toString();
        Log.i("currentVersion", currentVersion);

        if (advertisementList.size() != 0) {
            Advertisement latestAd = advertisementList.get(advertisementList.size() - 1);
            String latestVersion = latestAd.getVersion();
            Log.i("latestVersion", latestVersion);

            // 如果发现广告版本不同，即进行更新
            if (!latestVersion.equals(currentVersion)) {
                SharedPreferencesUtil.put(context, "updateTime", latestAd.getUpdateTime());
                SharedPreferencesUtil.put(context, "version", latestAd.getVersion());
                SharedPreferencesUtil.put(context, "imageUrl", latestAd.getImageUrl());
                SharedPreferencesUtil.put(context, "httpUrl", latestAd.getHttpUrl());
                imageUrl = latestAd.getImageUrl();
                // 开启子线程从网络下载图片
                new Thread(new Runnable() {
                    public void run() {
                        ImageDownloadUtil.getPicture(imageUrl);
                    }
                }).start();
            }
        }

    }
}
