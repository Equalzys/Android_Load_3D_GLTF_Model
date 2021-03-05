package com.zys.loadgltf.utils;

import android.content.Context;
import android.os.Environment;

import com.arialyy.aria.core.Aria;

public class DownLoadFileUtil {

    //获取系统的公共存储路径
//   private String publicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    //获取当前App的私有存储路径
//   private String privatePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();

    public static long downLoad(Context context, String url, String fileName) {
        return Aria.download(context)
                .load(url)     //读取下载地址
                .setFilePath(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+fileName) //设置文件保存的完整路径
                .create();   //创建并启动下载
    }
}
