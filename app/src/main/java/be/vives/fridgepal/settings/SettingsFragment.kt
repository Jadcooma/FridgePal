package be.vives.fridgepal.settings

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.core.widget.addTextChangedListener
import androidx.preference.EditTextPreference
import be.vives.fridgepal.R
import be.vives.fridgepal.utilities.MaskWatcher
import com.takisoft.preferencex.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val timePreference: EditTextPreference? = findPreference("timeAlarm")

        timePreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_DATETIME_VARIATION_TIME // enkel cijfers als input
            editText.filters += InputFilter.LengthFilter(5)
            editText.addTextChangedListener(MaskWatcher("##:##")) // mask voor input
        }
    }
}