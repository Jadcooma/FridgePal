package be.vives.fridgepal.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.fridgepal.database.FoodDao
import be.vives.fridgepal.database.FoodItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class FoodSingleViewModel (val database: FoodDao, application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()

    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    protected val _navigateToOverview = MutableLiveData<Boolean>()
    val navigateToOverview : LiveData<Boolean>
        get() = _navigateToOverview

    // ingesteld in fragment met viewmodel.setNewFoodItem(foodItem: FoodItem)
    protected val _foodItem = MutableLiveData<FoodItem>()
    val foodItem : LiveData<FoodItem>
        get() = _foodItem

    fun setFoodItem(foodItem: FoodItem){
        this._foodItem.value = foodItem;
    }

    fun doneNavigating() {
        _navigateToOverview.value = null;
    }

    // Cancel job bij vernietiging viewModel
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}