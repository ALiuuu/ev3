package com.aliu.ev3.ev3.weather.bean;


import com.aliu.ev3.R;

/**
 * Created by ALiu on 2018/4/7.
 */
public enum WeatherCode {

    Sunny(0, "晴", R.drawable.w0),
    Clear(1, "晴", R.drawable.w1),
    Fair(2, "晴", R.drawable.w2),
    Fair2(3, "晴", R.drawable.w3),
    Cloudy(4, "多云", R.drawable.w4),
    PartlyCloudy(5, "晴间多云", R.drawable.w5),
    PartlyCloudy2(6, "晴间多云", R.drawable.w6),
    MostlyCloudy(7, "大部多云", R.drawable.w7),
    MostlyCloudy2(8, "大部多云", R.drawable.w8),
    Overcast(9, "阴", R.drawable.w9),
    Shower(10, "阵雨", R.drawable.w10),
    Thundershower(11, "雷阵雨", R.drawable.w11),
    ThundershowerwithHail(12, "雷阵雨伴有冰雹", R.drawable.w12),
    LightRain(13, "小雨", R.drawable.w13),
    ModerateRain(14, "中雨", R.drawable.w14),
    HeavyRain(15, "大雨", R.drawable.w15),
    Storm(16, "暴雨", R.drawable.w16),
    HeavyStorm(17, "大暴雨", R.drawable.w17),
    SevereStorm(18, "特大暴雨", R.drawable.w18),
    IceRain(19, "冻雨", R.drawable.w19),
    Sleet(20, "雨夹雪", R.drawable.w20),
    SnowFlurry(21, "阵雪", R.drawable.w21),
    LightSnow(22, "小雪", R.drawable.w22),
    ModerateSnow(23, "中雪", R.drawable.w23),
    HeavySnow(24, "大雪", R.drawable.w24),
    Snowstorm(25, "暴雪", R.drawable.w25),
    Dust(26, "浮尘", R.drawable.w26),
    Sand(27, "扬沙", R.drawable.w27),
    Duststorm(28, "沙尘暴	", R.drawable.w28),
    Sandstorm(29, "强沙尘暴", R.drawable.w29),
    Foggy(30, "雾", R.drawable.w30),
    Haze(31, "霾", R.drawable.w31),
    Windy(32, "风", R.drawable.w32),
    Blustery(33, "大风", R.drawable.w33),
    Hurricane(34, "飓风", R.drawable.w34),
    TropicalStorm(35, "热带风暴", R.drawable.w35),
    Tornado(36, "龙卷风", R.drawable.w36),
    Cold(37, "冷", R.drawable.w37),
    Hot(38, "热", R.drawable.w38),
    Unknown(99, "未知", R.drawable.w99);

    // 成员变量
    private String name;
    private int    code;
    private int    res;

    // 构造方法
    private WeatherCode(int code, String name, int res) {
        this.name = name;
        this.code = code;
        this.res = res;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // 普通方法
    public static String getName(int code) {
        for (WeatherCode c : WeatherCode.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "未知";
    }

    // 普通方法
    public static int getRes(int code) {
        for (WeatherCode c : WeatherCode.values()) {
            if (c.getCode() == code) {
                return c.res;
            }
        }
        return -1;
    }

}
