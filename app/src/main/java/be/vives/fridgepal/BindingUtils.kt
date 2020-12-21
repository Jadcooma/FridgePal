package be.vives.fridgepal

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.vives.fridgepal.database.FoodItem

@BindingAdapter("foodExpiryDateString")
fun TextView.setFoodExpiryDateString(item: FoodItem){
    item.let {
        text = convertDateToString(item.expiryDate)
    }
}