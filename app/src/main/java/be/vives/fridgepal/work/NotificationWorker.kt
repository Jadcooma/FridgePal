package be.vives.fridgepal.work

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
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

            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification_alert)
                    .setContentTitle("FridgePal Melding")
                    .setContentText("U heeft producten met een waarschuwing")
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
            sb.append("Aantal over TGT: $numExpired \n")
        if (numNearExpired != 0)
            sb.append("Aantal bijna over TGT: $numNearExpired \n")
        if (numCaution != 0)
            sb.append("Aantal over THT: $numCaution \n")
        return sb.trimEnd().toString()
    }
}
