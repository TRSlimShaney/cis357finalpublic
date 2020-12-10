package edu.gvsu.cis.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MessagingService", "Message Notification Body: ${it.body}")
        }

        val builder = NotificationCompat.Builder(this, "drinkchannel")
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("Take a drink!")
                .setContentText("Firebase says take a drink.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(23, builder.build())
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}