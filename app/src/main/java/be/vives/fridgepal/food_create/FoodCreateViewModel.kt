package be.vives.fridgepal.food_create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import be.vives.fridgepal.database.FoodDatabaseDao

class FoodCreateViewModel(val database: FoodDatabaseDao, application: Application) : AndroidViewModel(application) {

}