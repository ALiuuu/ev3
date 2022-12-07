package com.aliu.ev3.ev3.weather.task;

import android.text.TextUtils;

import com.aliu.ev3.ev3.frame.GsonHelper;
import com.aliu.ev3.ev3.frame.PLog;
import com.aliu.ev3.ev3.weather.bean.WeatherCode;
import com.aliu.ev3.ev3.weather.bean.WeatherInterfaceBean;
import com.aliu.ev3.ev3.weather.network.WeatherNetHelper;
import com.aliu.ev3.ev3.weather.output.bean.WeatherBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ALiu on 2018/4/7.
 */
public class WeatherTask {


    private static final String TAG = "WeatherTask";

    private String testUrl = "https://api.seniverse.com/v3/weather/now.json?key=" + UrlTask.TIANQI_API_SECRET_KEY + "&location=" + "Shaoxing" + "&language=zh-Hans&unit=c";


    public void getWeather(final TaskListener taskListener) {
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
        WeatherInterfaceBean parse = GsonHelper.getInstance().parse(s, WeatherInterfaceBean.class);
        WeatherInterfaceBean.ResultsBean.NowBean now = parse.getResults().get(0).getNow();

        WeatherBean weatherBean = new WeatherBean();
        weatherBean.weather = WeatherCode.getName(Integer.parseInt(now.getCode()));
        weatherBean.temperature = now.getTemperature();
        weatherBean.res =  WeatherCode.getRes(Integer.parseInt(now.getCode()));

        if (listener != null) {
            listener.success(weatherBean);
        }


    }


    public interface TaskListener {
        void success(WeatherBean weatherBean);

        void failed(String msg);
    }
}
