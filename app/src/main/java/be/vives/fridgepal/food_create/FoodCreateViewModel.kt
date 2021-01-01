package be.vives.fridgepal.food_create

import android.app.Application
import be.vives.fridgepal.database.FoodDao
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.viewmodels.FoodSingleViewModel
import kotlinx.coroutines.*

class FoodCreateViewModel(database : FoodDao, application: Application)
    : FoodSingleViewModel(database, application) {

    fun saveFoodItem(){
        uiScope.launch {
            foodItem.value?.let { insert(it) };
            _navigateToOverview.value = true
        }
    }

    private suspend fun insert(foodItem: FoodItem){
        withContext(Dispatchers.IO){
            database.insert(foodItem)
        }
    }

}