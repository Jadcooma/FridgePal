package be.vives.fridgepal.food_overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import be.vives.fridgepal.database.FoodDatabaseDao

class FoodOverviewViewModel( val database: FoodDatabaseDao, application: Application) : AndroidViewModel(application) {

}