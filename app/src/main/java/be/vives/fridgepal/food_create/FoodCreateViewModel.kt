package be.vives.fridgepal.food_create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.vives.fridgepal.database.FoodDatabaseDao
import be.vives.fridgepal.database.FoodItem
import kotlinx.coroutines.*

class FoodCreateViewModel(val database: FoodDatabaseDao,
                          application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToOverview = MutableLiveData<Boolean>()
    val navigateToOverview : LiveData<Boolean>
        get() = _navigateToOverview

    private val newFoodItem = MutableLiveData<FoodItem>()

    var addButtonEnabled = Transformations.map(newFoodItem){
        null != it
    }
    fun doneNavigating() {
        _navigateToOverview.value = null;
    }

    // Cancel job bij vernietiging viewModel
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setNewFoodItem(foodItem: FoodItem){
        newFoodItem.value = foodItem;
    }

    fun saveFoodItem(foodItem: FoodItem){
        uiScope.launch {
            insert(foodItem);
            _navigateToOverview.value = true
        }
    }

    private suspend fun insert(foodItem: FoodItem){
        withContext(Dispatchers.IO){
            database.insert(foodItem)
        }
    }


}