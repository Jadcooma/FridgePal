package be.vives.fridgepal.food_edit

import android.app.Application
import be.vives.fridgepal.database.FoodDao
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.viewmodels.FoodSingleViewModel
import kotlinx.coroutines.*

class FoodEditViewModel (database : FoodDao, application: Application)
    : FoodSingleViewModel(database, application) {

    fun setFoodItemById(id: Long){
        uiScope.launch {
            val foodItemFound = findFoodItemById(id)
            setFoodItem(foodItemFound!!)
        }
    }

    private suspend fun findFoodItemById(id: Long) : FoodItem?{
        return withContext(Dispatchers.IO){
            return@withContext database.getFoodById(id)
        }
    }

    fun editFoodItem(){
        uiScope.launch {
            foodItem.value?.let { update(it)
            _navigateToOverview.value = true
            }
        }
    }

    private suspend fun update(foodItem: FoodItem){
        withContext(Dispatchers.IO){
            database.update(foodItem)
        }
    }
}