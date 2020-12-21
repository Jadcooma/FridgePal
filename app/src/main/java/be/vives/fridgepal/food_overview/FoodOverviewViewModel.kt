package be.vives.fridgepal.food_overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import be.vives.fridgepal.database.FoodDatabaseDao
import kotlinx.coroutines.*

class FoodOverviewViewModel( val database: FoodDatabaseDao,
                             application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listAllFood = database.getAllFood()

    val clearButtonVisible = Transformations.map(listAllFood){
        it?.isNotEmpty()
    }

    // clear database
    fun onClearPressed(){
        uiScope.launch {
            clear()
        }
    }

    suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    // Cancel job bij vernietiging viewModel
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}