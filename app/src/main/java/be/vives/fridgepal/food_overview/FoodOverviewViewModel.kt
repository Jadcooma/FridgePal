package be.vives.fridgepal.food_overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.vives.fridgepal.database.FoodDao
import be.vives.fridgepal.database.isCautionRequired
import be.vives.fridgepal.database.isExpired
import be.vives.fridgepal.database.isNearlyExpired
import kotlinx.coroutines.*

class FoodOverviewViewModel(val database: FoodDao,
                            application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listAllFood = database.getAllFoodSortedByDate()

    /* Drie transformations voor notifications */
    val numFoodExpired = Transformations.map(listAllFood){
        it.let{
            it.filter{it.isExpired()}.size
        }
    }

    val numFoodNearlyExpired = Transformations.map(listAllFood){
        it.let{
            it.filter{it.isNearlyExpired(application.applicationContext)}.size
        }
    }

    val numFoodCautionRequired = Transformations.map(listAllFood){
        it.let{
            it.filter{it.isCautionRequired()}.size
        }
    }

    val clearButtonVisible = Transformations.map(listAllFood){
        it?.isNotEmpty()
    }

    //region * navigatie button EDIT *

    private val _navigateToFoodEdit = MutableLiveData<Long>()
    val navigateToFoodEdit : LiveData<Long>
        get() = _navigateToFoodEdit

    fun onFoodEditClicked(id: Long){
        _navigateToFoodEdit.value = id
    }

    fun onFoodEditNavigated(){
        _navigateToFoodEdit.value = null
    }
    //endregion

    //region * navigatie button DELETE *

    private val _navigateToFoodDelete = MutableLiveData<Long>()
    val navigateToFoodDelete : LiveData<Long>
        get() = _navigateToFoodDelete

    fun onFoodDeleteClicked(id: Long){
        _navigateToFoodDelete.value = id
    }

    //endregion

    //region * CLEAR database *
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
    //endregion

    //region * DELETE from database *
    fun onDeletePressed(id: Long){
        uiScope.launch {
            delete(id)
        }
    }

    suspend fun delete(id: Long){
        withContext(Dispatchers.IO){
            database.deleteFoodById(id)
        }
    }
    //endregion

    // Cancel job bij vernietiging viewModel
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}