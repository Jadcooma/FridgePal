package be.vives.fridgepal.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.fridgepal.database.AppDatabase
import be.vives.fridgepal.network.NetworkRecipe
import be.vives.fridgepal.network.RecipesApi
import be.vives.fridgepal.repository.RecipesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val recipesRepository = RecipesRepository(database)

    val recipes = recipesRepository.recipes
    val dataRecipes = recipes.value

    // TODO status verder uitwerken (vb geen internetconnectie)
    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getRecipeSearchResults(searchTerm: String){
        coroutineScope.launch {
            try{
                //TODO want deleteRecipes() is workaround wegens geen vervanging bij nieuwe search
                recipesRepository.deleteRecipes()
                recipesRepository.refreshRecipes(searchTerm)
            } catch (e: Exception){
                _status.value = "FAILURE: " + e.message
            }
        }
    }
}