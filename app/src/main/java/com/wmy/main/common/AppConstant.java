package com.wmy.main.common;

import android.os.Environment;

/**
 * @author wmy
 * @Description:
 * @FileName: AppConstant
 * @Date 2018/4/19/019 17:35
 */
public class AppConstant {
    private static AppConstant appConstant;

    private AppConstant() {
    }

    public static AppConstant getAppConstant() {
        if (appConstant == null) {
            appConstant = new AppConstant();
        }
        return appConstant;
    }


    /**
     * 文件存储目录
     */
    public static String filepath= Environment.getExternalStorageDirectory().getPath()+"/TP_photo";

//    public static String BaseUrl="http://localhost:8080";
    public  String BaseUrl="";

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        BaseUrl = baseUrl;
    }

    //WHAT 0-10 预留值
    public interface WHAT {
        int SUCCESS = 0;
        int FAILURE = 1;
        int ERROR = 2;
    }

    public interface KEY{
        String IMG_PATH = "IMG_PATH";
        String VIDEO_PATH = "VIDEO_PATH";
        String PIC_WIDTH = "PIC_WIDTH";
        String PIC_HEIGHT = "PIC_HEIGHT";
    }

    public interface REQUEST_CODE {
        int CAMERA = 0;
    }

    public interface RESULT_CODE {
        int RESULT_OK = -1;
        int RESULT_CANCELED = 0;
        int RESULT_ERROR = 1;
    }
}
