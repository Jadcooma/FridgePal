package be.vives.fridgepal.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchHit (
    val recipe : Recipe
)