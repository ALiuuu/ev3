package com.aliu.ev3.ev3

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Bundle
import com.aliu.ev3.R
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplicationkkk.QRCodeActivity
import com.example.myapplicationkkk.QRCodeActivity2

/**
 * create by ALiu on 2019-05-18
 */
class GuideActivity : Activity() {


    val handler = Handler(Looper.myLooper()!!)

    val CHANNEL_ID = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        findViewById<View>(R.id.dianti).setOnClickListener {
            val intent = Intent(this@GuideActivity, LinkActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.bt_scan).setOnClickListener {
            startActivityForResult(Intent(this, QRCodeActivity::class.java), 1)
        }

        findViewById<View>(R.id.bt_scan2).setOnClickListener {
            startActivityForResult(Intent(this, QRCodeActivity2::class.java), 1)
        }

        findViewById<View>(R.id.bt_map).setOnClickListener {
            startActivityForResult(Intent(this, MapActivity::class.java), 1)
        }

//        startActivityForResult(Intent(this, MapActivity::class.java), 1)

        findViewById<View>(R.id.bt_notify).setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            handler.postDelayed({
                notify(null)
            }, 5000)
        }

//        View qinyou = findViewById(R.id.qinyou);
//        qinyou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(GuideActivity.this, MapActivity.class);
//                startActivity(intent);
//
//            }
//        });


    }

    /**
     * 发送通知（支持8.0+）
     * @param view
     */
    fun notify(view: View?) {
        // 1. Set the notification content - 创建通知基本内容
        // https://developer.android.google.cn/training/notify-user/build-notification.html#builder
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("您的核酸还有8小时到期，点击前往最快的检验站") // 这是单行
            //.setContentText("Much longer text that cannot fit one line...")
            // 这是多行
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "您的核酸还有8小时到期，点击前往最快的检验站"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // 2. Create a channel and set the importance - 8.0后需要设置Channel
        // https://developer.android.google.cn/training/notify-user/build-notification.html#builder
        createNotificationChannel()

        // 3. Set the notification's tap action - 创建一些点击事件，比如点击跳转页面
        // https://developer.android.google.cn/training/notify-user/build-notification.html#click

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MapActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        builder.setContentIntent(pendingIntent)

        // 4. Show the notification - 展示通知
        // https://developer.android.google.cn/training/notify-user/build-notification.html#notify
        val notificationManager = NotificationManagerCompat.from(this)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val description = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}