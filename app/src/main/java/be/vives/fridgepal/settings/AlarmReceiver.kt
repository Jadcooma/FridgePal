package be.vives.fridgepal.settings

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import be.vives.fridgepal.R
import be.vives.fridgepal.database.AppDatabase
import be.vives.fridgepal.database.FoodItem
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    // lazy niet mogelijk : context onReceive nodig
    private lateinit var notificationManager : NotificationManager
    private lateinit var dataSource : AppDatabase

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = getNotificationManager(context)
        dataSource = AppDatabase.getInstance(context)
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {

        val liveListFood = RefreshableLiveData({dataSource.FoodDao.getAllFoodSortedByDate()} )
        //TODO LiveData is niet uitleesbaar (liveListFood.value == null)
        // ook niet na refresh via RefreshableLiveData met LiveDataMediator
/*
        var listFood = mutableListOf<FoodItem>()

        liveListFood.observeForever {
            listFood.addAll(it)
        }
        liveListFood.refresh()
*/
        val contentIntent = Intent(context, this::class.java)

        val contentPendingIntent = PendingIntent.getActivity(
            context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notificationBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_alert)
            .setContentTitle(context.getString(R.string.app_name) +": waarschuwing")
            .setContentText("Aantal vervallen: xxx")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(contentPendingIntent)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

}