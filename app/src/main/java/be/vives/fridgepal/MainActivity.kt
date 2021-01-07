package be.vives.fridgepal

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.work.*
import be.vives.fridgepal.database.AppDatabase
import be.vives.fridgepal.food_overview.FoodOverviewViewModel
import be.vives.fridgepal.food_overview.FoodOverviewViewModelFactory
import be.vives.fridgepal.work.NotificationWorker
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var preferences : SharedPreferences
    private lateinit var viewmodel : FoodOverviewViewModel
    private var numFoodExpired = -1
    private var numFoodNearlyExpired = -1
    private var numFoodCautionRequired = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val application = this.application
        val dataSource = AppDatabase.getInstance(application).FoodDao
        val viewModelFactory = FoodOverviewViewModelFactory(dataSource, application)
        viewmodel = ViewModelProvider(this, viewModelFactory)
            .get(FoodOverviewViewModel::class.java)

        // TODO Bug: numFood's worden niet geobserveerd => waarde blijft -1
        viewmodel.numFoodExpired.observe(this, { this.numFoodExpired = it })
        viewmodel.numFoodNearlyExpired.observe(this, { this.numFoodNearlyExpired = it })
        viewmodel.numFoodCautionRequired.observe(this, { this.numFoodCautionRequired = it })

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onStop() {
        // noodzakelijk, zoniet dubbele registratie bij herstart
        unregisterListener()
        super.onStop()
    }

    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if(key.equals("alarm_time") && sharedPreferences.getBoolean("alarm_enabled", false)){
                setupNotificationWork()
            } else if (key.equals("alarm_enabled") && sharedPreferences.getBoolean(
                    "alarm_enabled",
                    false
                ) )
                setupNotificationWork()
        }

    private fun setupNotificationWork(){
        val dataNumWarnings : Data = workDataOf(
            "KEY_NUM_EXPIRED" to numFoodExpired,
            "KEY_NUM_NEAREXPIRED" to numFoodNearlyExpired,
            "KEY_NUM_CAUTION" to numFoodCautionRequired
        )

        //region Berkenen van delay voor 1e uitvoering PeriodicWork via gekozen tijdstip

        // stap 1 : Period vanuit ingesteld tijdstip "HH:mm",
        // (tijdstip opgehaald met behorende key uit SharedPreferences)
        val periodAlarm : Period = getPeriodFromString(
            preferences.getString("alarm_time", "")!!
        )

        // stap 2 : DateTime aanmaken vanuit uren/minuten Period en vanuit DateTime.now
        val timeAlarm = DateTime.now() .withTimeAtStartOfDay()
            .plusHours(periodAlarm.hours)
            .plusMinutes(periodAlarm.minutes)

        // stap 3 : DateTime gebruiken om aantal minuten benodigde vertraging te berekenen
        val minutesDelayAlarm : Long = getMinutesDelayAlarm(timeAlarm)
        //endregion

        val repeatingRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(minutesDelayAlarm, TimeUnit.MINUTES)
            .setInputData(dataNumWarnings)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            NotificationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }

    // nodig bij stap 1
    private fun getPeriodFromString(stringDuration: String): Period {
        val formatter = PeriodFormatterBuilder()
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .toFormatter()
        return formatter.parsePeriod(stringDuration)
    }

    // nodig bij stap 2
    private fun getMinutesDelayAlarm(timeAlarm : DateTime): Long {
        val delay : Duration
        if (DateTime.now() < timeAlarm) {
            delay = Duration(DateTime.now(), timeAlarm)}
        else {
            delay = Duration(DateTime.now(), timeAlarm.plusDays(1))}
        return delay.standardMinutes
    }

    fun unregisterListener() {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

}