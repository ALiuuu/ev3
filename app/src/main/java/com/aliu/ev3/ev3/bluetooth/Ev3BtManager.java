package com.aliu.ev3.ev3.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.aliu.ev3.ev3.BluetoothEV3Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ALiu on 2018/4/8.
 */
public class Ev3BtManager {

    private static final String TAG = "Ev3BtManager";

    // ====== 单例 ======

    private static Ev3BtManager instance;

    public static Ev3BtManager getInstance() {
        if (instance == null) {
            synchronized (Ev3BtManager.class) {
                if (instance == null) {
                    instance = new Ev3BtManager();
                }
            }
        }
        return instance;
    }

    private Ev3BtManager() {
    }

    private BluetoothEV3Service mEV3Service = null;


    //用户信息
    private List<Ev3ServiceListener> listeners = new LinkedList<>();

    public void registerListener(Ev3ServiceListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void unRegisterListener(Ev3ServiceListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void init() {
        mEV3Service = new BluetoothEV3Service(new Ev3ServiceListener() {
            @Override
            public void onStateChanged(int stateCode) {
                for (Ev3ServiceListener listener : listeners) {
                    listener.onStateChanged(stateCode);
                }
            }

            @Override
            public void onToast(String msg) {
                for (Ev3ServiceListener listener : listeners) {
                    listener.onToast(msg);
                }
            }

            @Override
            public void onDeviceName(String deviceName) {
                for (Ev3ServiceListener listener : listeners) {
                    listener.onDeviceName(deviceName);
                }
            }

            @Override
            public void onMsgRead(String msg) {
                for (Ev3ServiceListener listener : listeners) {
                    listener.onMsgRead(msg);
                }
            }
        });
    }


    public void start() {
        if (mEV3Service != null && mEV3Service.getState() == BluetoothEV3Service.STATE_NONE) {
            mEV3Service.start();
        }
    }

    public void destory() {
        if (mEV3Service != null) {
            mEV3Service.stop();
        }
    }

    public void sendMsg(String boxName, String msg) {
        Log.e("fuck", "send:  " + msg);
//        Toast.makeText(CoreApplication.instance, msg, Toast.LENGTH_SHORT).show();
        if (mEV3Service != null && mEV3Service.EV3 != null)
            mEV3Service.EV3.send(boxName, msg);
    }

    public void sendMsg(String boxName, int msg) {
        Log.e("fuck", "send:  " + msg);
//        Toast.makeText(CoreApplication.instance, msg, Toast.LENGTH_SHORT).show();
        if (mEV3Service != null && mEV3Service.EV3 != null)
            mEV3Service.EV3.send(boxName, msg);
    }

    public void connect(BluetoothDevice device) {
        if (mEV3Service != null) {
            mEV3Service.connect(device);

        }

    }

}
