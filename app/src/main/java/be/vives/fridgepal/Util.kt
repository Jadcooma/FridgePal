package be.vives.fridgepal

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Date): String {
    return SimpleDateFormat("dd-MM-yyyy")
        .format(date).toString()
}