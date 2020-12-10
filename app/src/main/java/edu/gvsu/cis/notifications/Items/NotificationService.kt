package edu.gvsu.cis.notifications.Items

import android.app.Service
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.gvsu.cis.notifications.R
import android.content.Context
import android.util.Log
import java.time.temporal.ChronoField
import java.util.*

class NotificationService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper): Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                while (true) {
                    if (TimeContent.ITEMS.size > 0) {
                        val now = Calendar.getInstance()
                        var indexes = mutableListOf<Int>()
                        TimeContent.ITEMS.forEachIndexed { index, item ->
                            val nowhour = now[Calendar.HOUR_OF_DAY]
                            val nowmin = now[Calendar.MINUTE]
                            Log.d("NotificationService", "${nowhour} ${nowmin} ${item.Hour} ${item.Minute}")
                            if (nowhour >= item.Hour && nowmin >= item.Minute) {
                                val builder = NotificationCompat.Builder(this@NotificationService, "drinkchannel")
                                        .setSmallIcon(R.drawable.clock)
                                        .setContentTitle("Take a drink!")
                                        .setContentText("Your drink alarm for ${item.toString()} just went off.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        // Set the intent that will fire when the user taps the notification
                                        .setAutoCancel(true)

                                with(NotificationManagerCompat.from(this@NotificationService)) {
                                    notify(24, builder.build())
                                }
                                indexes.add(index)
                            }
                        }
                        for (index in indexes) {
                            TimeContent.ITEMS.removeAt(index)
                        }
                    }
                    else {
                        Log.d("NotificationService", "No alarms")
                    }
                    Log.d("NotificationService", "Done processing alarms")
                    Thread.sleep(5000)
                }
            }
            catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        serviceHandler?.obtainMessage()?.also {
            msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}