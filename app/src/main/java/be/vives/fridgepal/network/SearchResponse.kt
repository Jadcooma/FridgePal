package be.vives.fridgepal.network

import com.squareup.moshi.Json

data class SearchResponse (
    @Json(name = "hits")
    val searchHits : List<SearchHit>
)