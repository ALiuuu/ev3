package com.aliu.ev3.ev3;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by ALiu on 2018/4/6.
 */
public class CoreApplication extends Application {


    public Handler handler;


    public static CoreApplication instance;
    public static boolean isPad;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        handler = new Handler(getMainLooper());

        isPad = instance.isTabletDevice(this);

//        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
//        SDKInitializer.initialize(this);
//
//        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
//        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);

    }
    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    private boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
