package com.android.wearnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel("default", "test", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(notificationChannel)
        val builder = Notification.Builder(this, "default")
            .setContentTitle("Test App")
            .setContentText("HELLO!!!!!!!!!")
            .setSmallIcon(android.R.drawable.alert_light_frame)
            .setContentIntent(pendingIntent)

        manager.notify(0, builder.build())

    }
}
