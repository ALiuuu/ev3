package com.aliu.ev3.ev3;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliu.ev3.R;
import com.aliu.ev3.ev3.bluetooth.Ev3BtManager;
import com.aliu.ev3.ev3.bluetooth.Ev3ServiceListener;
import com.aliu.ev3.ev3.frame.PLog;
import com.example.myapplicationkkk.QRCodeActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LinkActivity extends AppCompatActivity {


    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 2;


    private TextView mTvOpenBlueTooth;
    private TextView mTvTittle;


    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private Ev3ServiceListener ev3ServiceListener = new

            Ev3ServiceListener() {
                @Override
                public void onStateChanged(int stateCode) {
                    switch (stateCode) {
                        case BluetoothEV3Service.STATE_CONNECTED:
                            mTvTittle.setText("点击进入");
                            mTvTittle.setBackgroundResource(R.drawable.shape_next_bg_green);
                            mTvTittle.setOnClickListener(v -> {
//                                EActivity.open(LinkActivity.this);
                                Intent i = new Intent(LinkActivity.this, QRCodeActivity.class);
                                startActivity(i);
                                finish();
                            });
//                        mTvState.setText(R.string.title_connected_to);
                            break;
                        case BluetoothEV3Service.STATE_CONNECTING:
                            mTvTittle.setText("连接中");
                            mTvTittle.setBackgroundResource(R.drawable.shape_next_bg_orange);
                            mTvTittle.setOnClickListener(null);
//                        mTvState.setText(R.string.title_connecting);
                            break;
                        case BluetoothEV3Service.STATE_LISTEN:
                            mTvTittle.setText("异常");
                            mTvTittle.setBackgroundResource(R.drawable.shape_next_bg_red);
                            mTvTittle.setOnClickListener(null);

                        case BluetoothEV3Service.STATE_NONE:
                            mTvTittle.setText("无连接");
                            mTvTittle.setBackgroundResource(R.drawable.shape_next_bg_red);
                            mTvTittle.setOnClickListener(null);

//                        mTvState.setText(R.string.title_not_connected);
                            break;
                    }
                }

                @Override
                public void onToast(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDeviceName(String deviceName) {
//                mTvName.setText(deviceName);
                }

                @Override
                public void onMsgRead(String msg) {

                }
            };
    private RelativeLayout mRlBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CoreApplication.isPad) {
            setContentView(R.layout.activity_link_pad);

        } else {
            setContentView(R.layout.activity_link);
        }
        // 隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();


        checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, isSuccess -> {
            if (isSuccess) {
                PLog.e(TAG, "initPermissions: initConfig 获取权限成功");
                init();
            } else {
                PLog.e(TAG, "initPermissions: initConfig 获取权限失败");
            }
        });


        //                        mTvState.setText(R.string.title_connected_to);
//                        mTvState.setText(R.string.title_connecting);
//                        mTvState.setText(R.string.title_not_connected);
//                mTvName.setText(deviceName);

    }

    private void init() {
        findViewById(R.id.tv_bug).setOnLongClickListener(v -> {
//            EActivity.open(this);
            Intent i = new Intent(LinkActivity.this, QRCodeActivity.class);
            startActivity(i);
            return true;
        });

        mTvOpenBlueTooth = findViewById(R.id.tv_openbluetooth);
        mTvTittle = findViewById(R.id.tv_tittle);
        mRlBg = findViewById(R.id.rl_openbg);

        initBlueToothList();

        Ev3BtManager.getInstance().init();

        Ev3BtManager.getInstance().registerListener(ev3ServiceListener);

    }

    private void initBlueToothList() {

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkBlueTooth();
    }

    private void checkBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            mPairedDevicesArrayAdapter.clear();
            mTvOpenBlueTooth.setText("尚未启动蓝牙 点击开启");
            mRlBg.setBackgroundColor(0xffffaaaa);
            mRlBg.setOnClickListener(v -> {
                openBlueToothSetting();
            });
        } else {
            getPairedDevice();
            mTvOpenBlueTooth.setText("已启动蓝牙 点击搜索");
            mRlBg.setBackgroundColor(0xff43D17C);
            mRlBg.setOnClickListener(v -> {
                doDiscovery();
            });
        }
    }

    private void openBlueToothSetting() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        return;
    }

    private void getPairedDevice() {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        mPairedDevicesArrayAdapter.clear();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private void ensureDiscoverable() {
        Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Ev3BtManager.getInstance().unRegisterListener(ev3ServiceListener);

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        // Indicate scanning in the title
        mTvOpenBlueTooth.setText("搜索中");
        mRlBg.setOnClickListener(null);
        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }


    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // 根据Mac获取设备对象
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            // 连接设备
            Ev3BtManager.getInstance().connect(device);
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //搜索完毕
                mTvOpenBlueTooth.setText("搜索完毕 点击重试");
                mRlBg.setOnClickListener(v -> {
                    doDiscovery();
                });
            }
        }
    };


    private Map<Integer, OnPermissionResultListener> permissionMap;


    /**
     * 权限申请
     *
     * @param permissions 权限
     * @param listener    执行完毕后的监听
     */
    public void checkPermission(String[] permissions, OnPermissionResultListener listener) {
        boolean isRequest = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    isRequest = true;
                    break;
                }
            }
            if (isRequest) {
                int code = 100;
                if (permissionMap == null) {
                    permissionMap = new HashMap<>();
                } else {
                    Set<Integer> integerSet = permissionMap.keySet();
                    int max = code;
                    for (int integer : integerSet) {
                        if (integer > max)
                            max = integer;
                    }
                    code = max + 1;// 在原先最大值上加1
                }
                permissionMap.put(code, listener);
                ActivityCompat.requestPermissions(this, permissions, code);
                return;
            }
        }
        if (listener != null)
            listener.permissionResult(true);
    }

}
