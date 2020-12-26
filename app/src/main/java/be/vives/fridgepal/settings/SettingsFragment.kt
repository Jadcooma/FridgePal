package be.vives.fridgepal.settings

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import be.vives.fridgepal.R
import com.takisoft.preferencex.PreferenceFragmentCompat
import com.takisoft.preferencex.TimePickerPreference
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    // Manager instanties : lazy ipv late init => thread-safe && eenmalige aanmaak instantie
    private val alarmManager: AlarmManager by lazy {
        context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val notificationManager by lazy{
        getNotificationManager(requireContext())
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)

        val timePickerPreference =
            findPreference<TimePickerPreference>("alarm_time")!!
        val switchPreference =
            findPreference<SwitchPreference>("alarm_enabled")!!
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)

        createNotificationChannel()

        /* alarmIntent => om alarm in te stellen */
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context, NOTIFICATION_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        switchPreference.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                if (sharedPreferences.getBoolean("alarm_enabled", false)){

                    val timeSelected : Date = timePickerPreference.time!!
                    val calendar: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, timeSelected.hours)
                        set(Calendar.MINUTE, timeSelected.minutes)
                    }

                    alarmManager.setInexactRepeating ( // setExact niet mogelijk: API level >28 required
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        calendar.timeInMillis,
                        alarmPendingIntent
                    )
                    true
                } else{
                    alarmManager.cancel(alarmPendingIntent)
                    false
                }
            }
    }

    private fun createNotificationChannel() {
    /*
         Create a notification manager object.
         Notification channels are only available in OREO and higher.
         So, add a check on SDK version.
         https://developer.android.com/codelabs/android-training-alarm-manager#3
*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Waarschuwingen over vervaldata"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun cancelAlarm() {
        // TODO annuleer intentPending in AlarmManager
    }
}
