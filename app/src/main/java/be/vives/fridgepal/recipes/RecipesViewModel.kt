package be.vives.fridgepal.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.fridgepal.network.Recipe
import be.vives.fridgepal.network.RecipesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes : LiveData<List<Recipe>>
        get() = _recipes

    // TODO omvormen naar enum?
    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getRecipeSearchResults(searchTerm: String){
        coroutineScope.launch {
            var getSearchResponsDeferred = RecipesApi.retrofitService.getSearchRespons(searchTerm)
            try{
                var searchRespons = getSearchResponsDeferred.await()
                var foundRecipes = mutableListOf<Recipe>()
                searchRespons.searchHits.forEach {
                    foundRecipes.add(it.recipe)
                }
                _recipes.value = foundRecipes
                _status.value = "SUCCES!"
            } catch (t: Throwable){
                    _status.value = "FAILURE: " + t.message
            }
        }
    }
}