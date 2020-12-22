package be.vives.fridgepal.recipes

import android.widget.Toast
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
            var getPropertiesDeferred = RecipesApi.retrofitService.getSearchRespons(searchTerm)
            try{
                var searchRespons = getPropertiesDeferred.await()
                searchRespons.searchHits.forEach {
                    recipes.value!!.plus(it)
                }
                if(searchRespons.searchHits.isEmpty())
                    _status.value = "NO RESULTS"
                else
                    _status.value = "SUCCES"
            } catch (t: Throwable){
                    _status.value = "FAILURE"
            }
        }
    }
}