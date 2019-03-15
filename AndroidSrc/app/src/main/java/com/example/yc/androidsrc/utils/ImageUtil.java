package com.example.yc.androidsrc.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    /**
     * 将Bitmap保存到SD卡
     * @param context
     * @param mybitmap
     * @return
     */
    public static boolean saveBitmapToSdCard(Context context, Bitmap mybitmap) {
        boolean result = false;
        // 创建位图保存目录
        String path = Environment.getExternalStorageDirectory() + "/AntiHomebody/";
        File sd = new File(path);
        if (!sd.exists()){
            sd.mkdir();
        }
        File file = new File(path + String.valueOf(System.currentTimeMillis()) + ".png");
        FileOutputStream fileOutputStream = null;
        if (!file.exists()){
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    fileOutputStream = new FileOutputStream(file);
                    mybitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    // 通知系统相册
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    ToastUtil.showShort(context, "已将图片保存至本地");
                    result = true;
                }
                else{
                    ToastUtil.showShort(context, "不能读取到SD卡");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ToastUtil.showShort(context, "保存失败");
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.showShort(context, "保存失败");
            }
        }
        return result;
    }

    /**
     * 获取View的缓存视图
     * PS:前提是这个View已经渲染完成并显示在页面上
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 将bitmap转为byte[]
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
