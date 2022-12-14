package com.aliu.ev3.ev3

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.aliu.ev3.R
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.CoordinateConverter
import com.amap.api.maps.CoordinateConverter.CoordType
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions


class MapActivity : AppCompatActivity() {

    var mMapView: MapView? = null

    private var aMap: AMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map2)
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)
        //获取地图控件引用
        mMapView = findViewById(R.id.map)
        mMapView?.let {
            //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
            it.onCreate(savedInstanceState)
            if (aMap == null) {
                aMap = it.map
            }
        }


    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView?.onResume()
        aMap!!.clear()
//        aMap!!.addMarker(
//            MarkerOptions().position(test(29.830059, 121.530266 ))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//        )
//        aMap!!.addMarker(
//            MarkerOptions().position(test(29.82332, 121.530764))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//        )
//        aMap!!.addMarker(
//            MarkerOptions().position(test(29.820136, 121.527068))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//        )
//        aMap!!.addMarker(
//            MarkerOptions().position(test(29.82184, 121.52327))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//        )

        aMap!!.addMarker(
            MarkerOptions().position(test(28.932129, 118.781683))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        aMap!!.addMarker(
            MarkerOptions().position(test(28.928992, 118.778763))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        aMap!!.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(test(28.933597, 118.784099), 15f, 0f, 0f)
            )
        )
    }

    /**
     * 根据类型 转换 坐标
     */
    private fun convert(sourceLatLng: LatLng, coord: CoordType): LatLng? {
        val converter = CoordinateConverter(this)
        // CoordType.GPS 待转换坐标类型
        converter.from(coord)
        // sourceLatLng待转换坐标点
        converter.coord(sourceLatLng)
        // 执行转换操作
        return converter.convert()
    }


    fun test(lat: Double, lon: Double): LatLng? {
//        return convert(LatLng(lat, lon), CoordType.GPS)
        return LatLng(lat, lon)
    }


    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView?.onSaveInstanceState(outState)
    }
}