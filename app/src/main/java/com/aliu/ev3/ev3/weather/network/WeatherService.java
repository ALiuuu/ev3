package com.aliu.ev3.ev3.weather.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by ALiu on 2018/4/3.
 */

public interface WeatherService {



    @GET
    Observable<String> weather(@Url String url);




}