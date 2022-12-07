package com.aliu.ev3.ev3.bluetooth;

/**
 * Created by ALiu on 2018/4/8.
 */
public interface Ev3ServiceListener {

    void onStateChanged(int stateCode);

    void onToast(String msg);

    void onDeviceName(String deviceName);

    void onMsgRead(String msg);
}
