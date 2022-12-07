package com.example.myapplicationkkk

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.Builder
import com.aliu.ev3.R
import com.aliu.ev3.ev3.bluetooth.Ev3BtManager
import com.google.zxing.Result
import com.king.zxing.CameraScan
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.config.CameraConfig


/**
 * @author  zhaoleihe@bytedance.com
 * @date  2022/5/21 2:15 下午
 */
class QRCodeActivity : CaptureActivity() {

    private val boxName = "abc"

    val handler = Handler(Looper.myLooper()!!)

    var tips: TextView? = null
    var take: TextView? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_qrcode
    }

    var index = 0
    override fun initUI() {
        super.initUI()
        tips = findViewById<TextView>(R.id.tips)
        take = findViewById<TextView>(R.id.take)

    }

    override fun initCameraScan() {
        super.initCameraScan()

        //初始化解码配置
        val decodeConfig = DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS) //如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
            .setFullAreaScan(true) //设置是否全区域识别，默认false
            .setAreaRectRatio(0.8f) //设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
            .setAreaRectVerticalOffset(0).areaRectHorizontalOffset = 0 //设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        cameraScan
            .setVibrate(true) //设置是否震动，默认为false
            .setNeedAutoZoom(false) //二维码太小时可自动缩放，默认为false
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig)) //设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现

        cameraScan.setCameraConfig(object : CameraConfig() {
            override fun options(builder: Builder): CameraSelector {
                builder.requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                return super.options(builder)
            }
        })
    }


    /**
     * 扫码结果回调
     * @param result
     * @return 返回false表示不拦截，将关闭扫码界面并将结果返回给调用界面；
     * 返回true表示拦截，需自己处理逻辑。当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
     * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调
     * 用[CameraScan.setAnalyzeImage]，
     * 因为[CameraScan.setAnalyzeImage]方法能动态控制是否继续分析图像。
     */
    override fun onScanResultCallback(result: Result?): Boolean {
        /*
         * 因为setAnalyzeImage方法能动态控制是否继续分析图像。
         *
         * 1. 因为分析图像默认为true，如果想支持连扫，返回true即可。
         * 当连扫的处理逻辑比较复杂时，请在处理逻辑前调用getCameraScan().setAnalyzeImage(false)，
         * 来停止分析图像，等逻辑处理完后再调用getCameraScan().setAnalyzeImage(true)来继续分析图像。
         *
         * 2. 如果只是想拦截扫码结果回调自己处理逻辑，但并不想继续分析图像（即不想连扫），可通过
         * 调用getCameraScan().setAnalyzeImage(false)来停止分析图像。
         */
        //暂停
        cameraScan.setAnalyzeImage(false)
        tips?.visibility = View.INVISIBLE
        take?.visibility = View.VISIBLE
        index++
        if (index % 5 == 1) {
            take?.text = "#1\n请取棉签与试管"
        } else {
            var text = ""
            if ((index % 5) == 0) {
                text = "5"
            } else {
                text = (index % 5).toString()
            }
            take?.text = "#${text}\n请取棉签"
        }
        handler.postDelayed({

            tips?.visibility = View.VISIBLE
            take?.visibility = View.INVISIBLE
            cameraScan.setAnalyzeImage(true)

        }, 10000)
        Ev3BtManager.getInstance().sendMsg(boxName, "1")
        return true
    }

    override fun onResume() {
        super.onResume()
        Ev3BtManager.getInstance().start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Ev3BtManager.getInstance().destory()
    }
}