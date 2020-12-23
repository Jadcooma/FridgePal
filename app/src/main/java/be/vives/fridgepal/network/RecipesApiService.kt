package be.vives.fridgepal.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val BASE_URL =
    "https://edamam-recipe-search.p.rapidapi.com"

private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
    .build()

// TODO uitproberen
// https://github.com/square/moshi/issues/646
/*
val moshi = Moshi.Builder().build()
val type = Types.newParameterizedType(List::class.java, SearchResponse::class.java)
val jsonAdapter: JsonAdapter<List<SearchResponse>> = moshi.adapter(type)
val annotationData = jsonAdapter.fromJson(SearchResponse.toString())
*/

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface RecipesApiService {

    /*HEADERS : noodzakelijk voor toegang via RapîdApi (= Marketplace voor API's) :
        x-rapidapi-key => unieke toegangssleutel tot API voor een geregistreerde app op RapidApi
        x-rapidapi-host => idem aan BaseUrl TODO deze header noodzakelijk?
     */
    @Headers(
        "x-rapidapi-key: 45193a743dmshcedc423bda24bb5p1cd529jsn1bb0b2ad7386",
        "x-rapidapi-host: edamam-recipe-search.p.rapidapi.com")
    @GET("/search")
    fun getSearchRespons(@Query("q") ingredient : String):
            Deferred<SearchResponse>

}

object RecipesApi {
    val retrofitService: RecipesApiService by lazy {
        retrofit.create(RecipesApiService::class.java)
    }
}