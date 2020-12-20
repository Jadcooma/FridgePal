package be.vives.fridgepal.food_overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import be.vives.fridgepal.convertDateToString
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
        var stringDate : String
        if (listFood != null) {
            listFood.forEach { //TODO verwijzen naar localisation strings
                stringBuilder.appendLine("Id: ${it.foodId}")
                stringBuilder.appendLine("Naam: ${it.name}")
                stringBuilder.appendLine("Vervaltype: ${it.expiryType}")
                stringBuilder.appendLine("Vervaldatum: ${convertDateToString(it.expiryDate)}")
                stringBuilder.appendLine()
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
        }
    }

    // Cancel job bij vernietiging viewModel
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}