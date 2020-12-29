package be.vives.fridgepal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import be.vives.fridgepal.database.AppDatabase
import be.vives.fridgepal.database.asDomainModel
import be.vives.fridgepal.models.Recipe
import be.vives.fridgepal.network.RecipesApi
import be.vives.fridgepal.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository (private val database: AppDatabase) {
    val recipes : LiveData<List<Recipe>> =
        Transformations.map(database.RecipeDao.getRecipes()){
            it.asDomainModel()
        }

    suspend fun refreshRecipes(searchTerm : String) {
        withContext(Dispatchers.IO){
            val recipes = RecipesApi.retrofitService.getSearchRespons(searchTerm).await()
            database.RecipeDao.insertAll(*recipes.asDatabaseModel())
        }
    }

    suspend fun deleteRecipes(){
        withContext(Dispatchers.IO){
            database.RecipeDao.clear()
        }
    }
}