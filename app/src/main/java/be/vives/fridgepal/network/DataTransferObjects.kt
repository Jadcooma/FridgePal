package be.vives.fridgepal.network

import be.vives.fridgepal.database.DatabaseRecipe
import be.vives.fridgepal.models.Recipe
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SearchResponse (

    @Json(name = "hits") val searchHits : List<SearchHit>
)

@JsonClass(generateAdapter = true)
data class SearchHit (
    @Json(name="recipe") val networkRecipe : NetworkRecipe,
)

@JsonClass(generateAdapter = true)
data class NetworkRecipe (
    @Json(name="label") val name: String,
    val url : String,
    val image : String,
    val source : String
)

fun SearchResponse.asDomainModel() : List<Recipe>{
    return searchHits.map{
        Recipe(
            name = it.networkRecipe.name,
            url = it.networkRecipe.url,
            image = it.networkRecipe.image,
            source = it.networkRecipe.source
        )
    }
}

fun SearchResponse.asDatabaseModel() : Array<DatabaseRecipe>{
    return searchHits.map{
        DatabaseRecipe(
            name = it.networkRecipe.name,
            url = it.networkRecipe.url,
            image = it.networkRecipe.image,
            source = it.networkRecipe.source
        )
    }.toTypedArray()
}