package com.aliu.ev3.ev3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aliu.ev3.R;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.xuhao.didi.socket.server.action.ServerActionAdapter;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;

import androidx.appcompat.app.AppCompatActivity;

/**
 * create by ALiu on 2019-05-18
 */
public class MapActivityOLD extends AppCompatActivity implements IClientIOCallback {

    private MapView mMapView = null;

    private LocationClient mLocationClient;

    private TextView mTvHelp;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        mTvHelp = findViewById(R.id.tv_help);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(
                new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.FOLLOWING,
                        true,
                        null));

        //定位初始化
        mLocationClient = new LocationClient(this);

//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

//设置locationClientOption
        mLocationClient.setLocOption(option);

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层


        ///==============

        mIpTextView = findViewById(R.id.tv_ip);

        mConnectTextView = findViewById(R.id.tv_start);
        mConnectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mServerManager.isLive()) {
                    mServerManager.listen();
                    mConnectTextView.setText("点击断开");
                } else {
                    mServerManager.shutdown();
                    mConnectTextView.setText("点击连接");
                }

            }
        });

        hostIP = getHostIP();
        mIpTextView.setText(hostIP);


        mServerManager = OkSocket.server(mPort).registerReceiver(new ServerActionAdapter() {
            @Override
            public void onServerListening(int serverPort) {
                Log.i(TAG, Thread.currentThread().getName() + " onServerListening,serverPort:" + serverPort);
                flushServerText();
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
                Log.i(TAG, Thread.currentThread().getName() + " onClientConnected,serverPort:" + serverPort + "--ClientNums:" + clientPool.size() + "--ClientTag:" + client.getUniqueTag());
                client.addIOCallback(MapActivityOLD.this);
            }

            @Override
            public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
                Log.i(TAG, Thread.currentThread().getName() + " onClientDisconnected,serverPort:" + serverPort + "--ClientNums:" + clientPool.size() + "--ClientTag:" + client.getUniqueTag());
                client.removeIOCallback(MapActivityOLD.this);
            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
                Log.i(TAG, Thread.currentThread().getName() + " onServerWillBeShutdown,serverPort:" + serverPort + "--ClientNums:" + clientPool
                        .size());
                shutdown.shutdown();
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {
                Log.i(TAG, Thread.currentThread().getName() + " onServerAlreadyShutdown,serverPort:" + serverPort);
                flushServerText();
            }
        });

    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        mLocationClient.start();

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }


    private void show(double latitude, double longitude) {
        mTvHelp.setVisibility(View.VISIBLE);
        mBaiduMap.clear();

//定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }


    private String boxName = "abc";


    public static void open(Context context) {
        Intent i = new Intent(context, EActivity.class);
        context.startActivity(i);
    }


    @Override
    public void onBackPressed() {

    }


    private TextView mIpTextView;
    private TextView mConnectTextView;

    private static final String TAG = "fuck";

    private IServerManager mServerManager;


    private int mPort = 8081;
    private String hostIP;


    private void flushServerText() {
        if (mServerManager.isLive()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mIpTextView.setText("服务器已启动 ip:" + hostIP + ":" + mPort);
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mIpTextView.setText("服务器已关闭");
                }
            });
        }
    }


    @Override
    public void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
        String str = new String(originalData.getBodyBytes(), Charset.forName("utf-8"));
        Log.e(TAG, "onClientRead: " + str);

        JsonObject jsonObject2 = null;
        try {
            jsonObject2 = new JsonParser().parse(str).getAsJsonObject();
            double latitude = jsonObject2.get("latitude").getAsDouble();
            double longitude = jsonObject2.get("longitude").getAsDouble();

            show(latitude, longitude);
        } catch (Exception e) {
        }


        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonParser().parse(str).getAsJsonObject();
            int cmd = jsonObject.get("cmd").getAsInt();
            if (cmd == 54) {//登陆成功
                String handshake = jsonObject.get("handshake").getAsString();
                Log.i("onClientIOServer", Thread.currentThread().getName() + " 接收到:" + client.getHostIp() + " 握手信息:" + handshake);
            } else if (cmd == 14) {//心跳
                Log.i("onClientIOServer", Thread.currentThread().getName() + " 接收到:" + client.getHostIp() + " 收到心跳");
            } else {
                Log.i("onClientIOServer", Thread.currentThread().getName() + " 接收到:" + client.getHostIp() + " " + str);
            }
        } catch (Exception e) {
            Log.i("onClientIOServer", Thread.currentThread().getName() + " 接收到:" + client.getHostIp() + " " + str);
        }
        MsgDataBean msgDataBean = new MsgDataBean(str);
        clientPool.sendToAll(msgDataBean);
    }

    @Override
    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {
        byte[] bytes = sendable.parse();
        bytes = Arrays.copyOfRange(bytes, 4, bytes.length);
        String str = new String(bytes, Charset.forName("utf-8"));
        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonParser().parse(str).getAsJsonObject();
            int cmd = jsonObject.get("cmd").getAsInt();
            switch (cmd) {
                case 54: {
                    String handshake = jsonObject.get("handshake").getAsString();
                    Log.i("onClientIOServer", Thread.currentThread().getName() + " 发送给:" + client.getHostIp() + " 握手数据:" + handshake);
                    break;
                }
                default:
                    Log.i("onClientIOServer", Thread.currentThread().getName() + " 发送给:" + client.getHostIp() + " " + str);
            }
        } catch (Exception e) {
            Log.i("onClientIOServer", Thread.currentThread().getName() + " 发送给:" + client.getHostIp() + " " + str);
        }
    }


    /**
     * 获取ip地址
     * 如果是移动网络，会显示自己的公网IP，如果是局域网，会显示局域网IP
     * 因此本例中服务器端需要断开移动网络以得到本机局域网IP
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("error", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }
}