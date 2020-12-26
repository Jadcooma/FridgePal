package be.vives.fridgepal.settings

import android.app.NotificationManager
import android.content.Context

/* Gedeclareerd in aparte file wegens gebruik door twee classes
    om code duplicatie te vermijden
*/

val NOTIFICATION_ID = 0
val PRIMARY_CHANNEL_ID = "primary_notification_channel"

fun getNotificationManager(context: Context) : NotificationManager{
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}