package be.vives.fridgepal.settings

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import be.vives.fridgepal.R
import com.takisoft.preferencex.PreferenceFragmentCompat
import com.takisoft.preferencex.TimePickerPreference
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
    // lazy ipv late init => thread-safe && only-once => beter voor manager instanties
    private val alarmManager: AlarmManager by lazy {
        context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val notificationManager: NotificationManager by lazy {
        context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val NOTIFICATION_ID = 0

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)

        val timePickerPreference =
            findPreference<TimePickerPreference>("alarm_time")
        val timeSelected = timePickerPreference!!.pickerTime

        val switchPreference =
            findPreference<SwitchPreference>("alarm_enabled")

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)

        switchPreference!!.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { switchPreference ->
                if (sharedPreferences.getBoolean("alarm_enabled", false)){
                    deliverNotification(requireContext())
                    true
                } else{
                    false
                }
            }
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, this::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_notification_alert)
            .setContentTitle(getString(R.string.app_name) + ": waarschuwing")
            .setContentText("Uw voeding is bijna vervallen!")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

    }

    private fun cancelAlarm() {

    }
}
