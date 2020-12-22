package be.vives.fridgepal.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.fridgepal.network.RecipesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {

/*
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes : LiveData<List<Recipe>>
        get() = _recipes
*/

    private val _respons = MutableLiveData<String>()
    val respons : LiveData<String>
        get() = _respons

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getRecipeSearchResults(searchTerm: String){
        coroutineScope.launch {
            var getPropertiesDeferred = RecipesApi.retrofitService.getSearchRespons(searchTerm)
            try{
                var searchRespons = getPropertiesDeferred.await()
                _respons.value = "Succes: ${searchRespons.searchHits.size} recipes found!"
            } catch (t: Throwable){
                _respons.value = "Error: " + t.message
            }

        }
    }
}