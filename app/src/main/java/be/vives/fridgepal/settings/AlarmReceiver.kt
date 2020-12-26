package be.vives.fridgepal.settings

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import be.vives.fridgepal.R

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var notificationManager : NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = getNotificationManager(context)
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, this::class.java)

        val contentPendingIntent = PendingIntent.getActivity(
            context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_alert)
            .setContentTitle(context.getString(R.string.app_name) +": waarschuwing")
            .setContentText("Uw voeding is bijna vervallen!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(contentPendingIntent)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

}