package be.vives.fridgepal

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.network.Recipe
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("foodExpiryDateString")
fun TextView.setFoodExpiryDateString(item: FoodItem){
    item.let {
        text = convertDateToString(item.expiryDate)
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
                .error(R.drawable.ic_broken_image))
            .into(this)
    }
}

// TODO aanklikbare url met source als weergegeven text
@BindingAdapter("linkWithSource")
fun TextView.setHyperLinkUrl( recipe: Recipe ){
    val hyperlink = "<a href='${recipe.url}'>${recipe.source}</a>"
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        this.setText(hyperlink)
    else
        this.setText(Html.fromHtml(hyperlink))
    this.movementMethod = LinkMovementMethod.getInstance()
}