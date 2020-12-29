package be.vives.fridgepal.settings

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import be.vives.fridgepal.R
import be.vives.fridgepal.food_overview.FoodOverviewViewModel
import com.takisoft.preferencex.PreferenceFragmentCompat
import com.takisoft.preferencex.TimePickerPreference
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    // Manager instanties : lazy ipv late init => thread-safe && eenmalige aanmaak instantie
    private val alarmManager: AlarmManager by lazy {
        context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val notificationManager by lazy{
        getNotificationManager(requireContext())
    }
    private lateinit var timePickerPreference: TimePickerPreference
    private lateinit var switchPreference: SwitchPreference
    private lateinit var alarmIntent : Intent
    private lateinit var alarmPendingIntent : PendingIntent

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        timePickerPreference = findPreference("alarm_time")!!
        switchPreference = findPreference("alarm_enabled")!!

        // om waarde voor alarm_enabled op te halen, niet te verkrijgen uit switchPreference zelf
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)

        /* alarmIntent => om alarm in te stellen */
        alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmPendingIntent = PendingIntent.getBroadcast(
            context, NOTIFICATION_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        createNotificationChannel()

        timePickerPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{ preference: Preference, newValue: Any ->
                val timeWrapper = newValue as TimePickerPreference.TimeWrapper
                val dateFromTimePicker = SimpleDateFormat("HH:mm")
                    .parse(timeWrapper.hour.toString() +":"+ timeWrapper.minute.toString())
                if(sharedPreferences.getBoolean("alarm_enabled", false)){
                    setAlarm(dateFromTimePicker!!)
                }
                true
            }

        switchPreference.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                if (sharedPreferences.getBoolean("alarm_enabled", false)){
                    setAlarm(timePickerPreference.time!!)
                } else {
                    alarmManager.cancel(alarmPendingIntent)
                }
                true
            }
    }

    private fun setAlarm(dateTimePicker: Date) {
        val calendarAlarm: Calendar = getCalendarAlarm(dateTimePicker)

        alarmManager.setRepeating ( // setExact niet mogelijk: API level >28 required.0
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
//            calendarAlarm.timeInMillis,
            60000,
            alarmPendingIntent
        )
        Log.i("alarmMessage","Next Alarm: " +
                "Day ${calendarAlarm.get(Calendar.DAY_OF_MONTH)}" +
                " Hour ${calendarAlarm.get(Calendar.HOUR_OF_DAY)}" +
                " Minute ${calendarAlarm.get(Calendar.MINUTE)}")
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

    /* Instellen alarm : als tijdstip vandaag al gepasseerd is, dan instellen op volgende dag */
    private fun getCalendarAlarm(timeSelected: Date) : Calendar {
        val calendarAlarm : Calendar =
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, timeSelected.hours)
                set(Calendar.MINUTE, timeSelected.minutes)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        val isTimeInPast : Boolean
                = calendarAlarm.timeInMillis <= System.currentTimeMillis()

        if(isTimeInPast) {
            calendarAlarm.apply {
                set(
                    Calendar.DAY_OF_YEAR,
                    1 + calendarAlarm.get(Calendar.DAY_OF_YEAR)
                )
            }
        }
        return calendarAlarm
    }
}
