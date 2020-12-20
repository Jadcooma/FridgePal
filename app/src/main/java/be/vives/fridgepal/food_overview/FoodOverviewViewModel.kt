package be.vives.fridgepal.food_overview

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.vives.fridgepal.database.FoodDatabaseDao
import be.vives.fridgepal.database.FoodItem
import kotlinx.coroutines.*

class FoodOverviewViewModel( val database: FoodDatabaseDao,
                             application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listAllFood = database.getAllFood()

    val clearButtonVisible = Transformations.map(_listAllFood){
        it?.isNotEmpty()
    }

/*  TODO : LiveData voor RecyclerView
    private val _listAllFood = MutableLiveData<List<FoodItem>>()
    val listAllFood : LiveData<List<FoodItem>>
        get() = _listAllFood
*/

    val stringListFood = Transformations.map(_listAllFood){
        listFood -> formatListFood(listFood)
    }

    private fun formatListFood(listFood: List<FoodItem>?) : String {
        val stringBuilder = StringBuilder()
        if (listFood != null) {
            listFood.forEach {
                stringBuilder.appendLine("FoodId: ${it.foodId}")
                stringBuilder.appendLine("Name: ${it.name}")
                stringBuilder.appendLine("ExpiryType: ${it.expiryType}")
                stringBuilder.appendLine("ExpiryDate: ${it.expiryDate}")
            }
        } else{
            stringBuilder.appendLine("Uw lijstje is leeg")
        }
        return stringBuilder.toString()
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
            Log.i("FoodOverViewModel", "database.clear() called")
        }
    }

    // Cancel job bij vernietiging viewModel
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}