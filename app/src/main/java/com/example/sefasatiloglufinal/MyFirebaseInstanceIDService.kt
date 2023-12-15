package com.example.sefasatiloglufinal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIDService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            val notificationService = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val kanal = NotificationChannel(
                    packageName,
                    "Kanal Adi",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { }
                notificationService.createNotificationChannel(kanal)

            }
            val builder = NotificationCompat.Builder(this, packageName)
                .setSmallIcon(R.drawable.homehouseicon)
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationService.notify(0, builder.build())

        }
    }
}