package be.vives.fridgepal.food_create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.fridgepal.database.FoodDatabaseDao

class FoodCreateViewModelFactory(
    private val dataSource: FoodDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory{

        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodCreateViewModel::class.java)) {
                return FoodCreateViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

}