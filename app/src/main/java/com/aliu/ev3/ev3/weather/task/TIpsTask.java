package com.aliu.ev3.ev3.weather.task;

import android.text.TextUtils;

import com.aliu.ev3.ev3.frame.GsonHelper;
import com.aliu.ev3.ev3.frame.PLog;
import com.aliu.ev3.ev3.main.TipsBean;
import com.aliu.ev3.ev3.weather.bean.WeatherCode;
import com.aliu.ev3.ev3.weather.bean.WeatherInterfaceBean;
import com.aliu.ev3.ev3.weather.network.WeatherNetHelper;
import com.aliu.ev3.ev3.weather.output.bean.WeatherBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ALiu on 2018/4/7.
 */
public class TIpsTask {


    private static final String TAG = "TIpsTask";

    private String testUrl = "https://www.easy-mock.com/mock/5b52f169e37c5c2786cde78c/ev3/tips";

    public void getTips(final TaskListener taskListener) {
        PLog.e(TAG, "getWeather --> start");
        if (!TextUtils.isEmpty(testUrl)) {
            WeatherNetHelper.getService().weather(testUrl)
                    .subscribeOn(Schedulers.io())//指定 subscribe() 发生在io线程
                    .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                    .subscribe(s -> {
                        parse(s, taskListener);
                        PLog.e(TAG, "getWeather req --> " + s);
                    }, throwable -> {
                        if (taskListener != null) {
                            taskListener.failed(throwable + "");
                        }
                        PLog.e(TAG, "getWeather failed --> " + throwable);
                    });
        } else {
            //url获取失败
            if (taskListener != null) {
                taskListener.failed("url Error");
            }
        }
    }


    private void parse(String s, TaskListener listener) {
        TipsBean parse = GsonHelper.getInstance().parse(s, TipsBean.class);

        if (listener != null) {
            listener.success(parse);
        }


    }


    public interface TaskListener {
        void success(TipsBean tipsBean);

        void failed(String msg);
    }
}
