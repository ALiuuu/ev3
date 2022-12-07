package com.aliu.ev3.ev3.weather.output;

import com.aliu.ev3.ev3.main.TipsBean;
import com.aliu.ev3.ev3.weather.output.bean.WeatherBean;
import com.aliu.ev3.ev3.weather.task.TIpsTask;
import com.aliu.ev3.ev3.weather.task.WeatherTask;

/**
 * Created by ALiu on 2018/4/6.
 */
public class WeatherManager {

    private static final String TAG = "WeatherManager";

    // ====== 单例 ======

    private static WeatherManager instance;

    public static WeatherManager getInstance() {
        if (instance == null) {
            synchronized (WeatherManager.class) {
                if (instance == null) {
                    instance = new WeatherManager();
                }
            }
        }
        return instance;
    }

    private WeatherManager() {
    }


    public void getWeahter(final WeatherListener weatherListener) {

        WeatherTask task = new WeatherTask();
        task.getWeather(new WeatherTask.TaskListener() {
            @Override
            public void success(WeatherBean weatherBean) {
                if (weatherListener!=null) {
                    weatherListener.onSuccess(weatherBean);
                }
            }

            @Override
            public void failed(String msg) {
                if (weatherListener!=null) {
                    weatherListener.onFailed(msg);
                }
            }
        });
    }


    public void getTips(final TipsListener tipsListener) {

        TIpsTask task = new TIpsTask();
        task.getTips(new TIpsTask.TaskListener() {

            @Override
            public void success(TipsBean tipsBean) {
                if (tipsListener!=null) {
                    tipsListener.onSuccess(tipsBean);
                }
            }

            @Override
            public void failed(String msg) {
                if (tipsListener!=null) {
                    tipsListener.onFailed(msg);
                }
            }
        });
    }


    public interface WeatherListener{
        void onSuccess(WeatherBean weatherBean);
        void onFailed(String msg);

    }

    public interface TipsListener{
        void onSuccess(TipsBean tipsBean);
        void onFailed(String msg);

    }
}
