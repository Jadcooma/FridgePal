package be.vives.fridgepal.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import be.vives.fridgepal.MainActivity
import be.vives.fridgepal.R

class NotificationWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "SetNotificationWorker"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val KEY_NUM_EXPIRED = "EXPIRED"
        const val KEY_NUM_NEAREXPIRED = "NEAREXPIRED"
        const val KEY_NUM_CAUTION = "CAUTION"
    }

    override suspend fun doWork(): Result {

        val numExpired = inputData.getInt(KEY_NUM_EXPIRED, -1)
        val numNearExpired = inputData.getInt(KEY_NUM_NEAREXPIRED, -1)
        val numCaution = inputData.getInt(KEY_NUM_CAUTION, -1)

        // Niets (bijna) vervallen -> geen notification tonen
        if (numExpired == 0 && numNearExpired == 0 && numCaution == 0) {
            return Result.failure()
        } else {
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val contentIntent = Intent(applicationContext, MainActivity::class.java)
            val contentPendingIntent: PendingIntent? =
                TaskStackBuilder.create(applicationContext).run {
                    addNextIntentWithParentStack(contentIntent) // Add the intent, which inflates the back stack
                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)}

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                // Create the required NotificationChannel with all parameters if Android Version >= Oreo
                val notificationChannel = NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    applicationContext.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.description =
                    applicationContext.getString(R.string.notification_channel_descr_NL)
                // No new channel created if one with same ID already exists
                notificationManager.createNotificationChannel(notificationChannel)
            }

            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification_alert)
                    .setContentTitle(applicationContext.getString(R.string.notification_NL))
                    .setContentText(applicationContext.getString(R.string.notification_descr_NL))
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(getWarningSummary(numExpired, numNearExpired, numCaution)))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(contentPendingIntent)

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            return Result.success()
        }
    }

    private fun getWarningSummary( numExpired: Int, numNearExpired: Int, numCaution: Int): String {
        val sb = StringBuilder()
        if (numExpired != 0)
            sb.append(applicationContext.getString(R.string.num_expired_NL) + " $numExpired \n")
        if (numNearExpired != 0)
            sb.append(applicationContext.getString(R.string.num_near_expired_NL) + "$numNearExpired \n")
        if (numCaution != 0)
            sb.append(applicationContext.getString(R.string.num_caution_NL) + "$numCaution \n")
        return sb.trimEnd().toString()
    }
}
