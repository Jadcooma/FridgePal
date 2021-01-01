package be.vives.fridgepal

import android.widget.CalendarView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.database.isCautionRequired
import be.vives.fridgepal.database.isExpired
import be.vives.fridgepal.database.isNearlyExpired
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

@BindingAdapter("foodExpiryDateString")
fun TextView.setFoodExpiryDateString(item: FoodItem) {
    item.let {
        text = convertDateToString(item.expiryDate)
    }
}

@BindingAdapter("colorByExpiryDate")
fun TextView.setColorByExpiryDate(item: FoodItem) {
    item.let {
        val color = when {
            it.isCautionRequired() -> 0xFFFFFF66 // Yellow
            it.isExpired() -> 0xFFFF4444 // android.R.color.holo_red_light
            it.isNearlyExpired(context) -> 0xFFFFBB33 //  android.R.color.holo_orange_light
            else -> 0xFFFFFFFF // android.R.color.background_light
        }
        // color from resources : API level >= 23 (nu 19)
        setBackgroundColor(color.toInt())
    }
}

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

@BindingAdapter("minDate")
fun CalendarView.setXmlMinDate(minDate: Long){
    setMinDate(minDate)
}

/* TODO NullPointerException voor parameter foodItem voor onderstaande BindingAdapters,
        nochtans wordt @{foodItem.name} als android:text in XML wel correct uitgelezen
 */

/*
@BindingAdapter("isTHT")
fun RadioButton.setIsTHT(foodItem: FoodItem){
    isChecked = foodItem.expiryType.equals("THT")
}

@BindingAdapter("setDate")
fun CalendarView.setDate(date: Date){
    setDate(date)
}*/
