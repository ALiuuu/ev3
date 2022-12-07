package com.aliu.ev3.ev3.weather.network;

import com.aliu.ev3.ev3.frame.RetrofitHelper;

import retrofit2.Retrofit;

/**
 * Created by ALiu on 2018/4/6.
 */
public class WeatherNetHelper {

    private static final String TAG = WeatherNetHelper.class.getSimpleName();


    private static Retrofit retrofit;

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (WeatherNetHelper.class) {
                if (retrofit == null) {
//                    String baseUrl = BaseAppConfig.getInstance().getDebugMode() ? TEST_BASE_URL : BASE_URL;
                    retrofit = RetrofitHelper.createRetrofit("https://api.seniverse.com/", null);

                }

            }
        }
        return retrofit;
    }


    public static WeatherService getService() {
        return getRetrofit().create(WeatherService.class);
    }
}
