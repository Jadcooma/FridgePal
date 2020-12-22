package be.vives.fridgepal.network

import com.squareup.moshi.Json

data class Recipe (
    @Json(name="label") val name: String,
    val url : String,
    val image : String,
    val source : String
)