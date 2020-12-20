package be.vives.fridgepal.food_create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import be.vives.fridgepal.R
import be.vives.fridgepal.database.FoodDatabase
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.databinding.FragmentFoodCreateBinding
import java.util.*

class FoodCreateFragment : Fragment() {

    private lateinit var binding : FragmentFoodCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        
        // uitbreiding fragment met databinding: inflate met DataBindingUtil
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_create, container, false)

        val application = requireNotNull(this.activity).application

        // DAO meegeven aan ViewModel voor uitvoeren van queries op database
        val dataSource = FoodDatabase.getInstance(application).FoodDatabaseDao

        val viewModelFactory = FoodCreateViewModelFactory(dataSource, application)

        val foodCreateViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FoodCreateViewModel::class.java)

        binding.foodCreateViewModel = foodCreateViewModel
        binding.setLifecycleOwner(this)

        binding.buttonCreate.setOnClickListener {
            if(checkFoodComplete()){ // controleer input velden voor aanmaak FoodItem
                foodCreateViewModel.setNewFoodItem(getFoodItemFromForm())
                this.findNavController().navigate(R.id.action_foodCreateFragment_to_foodOverviewFragment)
            } else{
                Toast.makeText(context, "Gelieve alle velden in te vullen", Toast.LENGTH_SHORT).show()
            }
        }

        foodCreateViewModel.navigateToOverview.observe(viewLifecycleOwner, { hasNavigated ->
            hasNavigated?.let{
                this.findNavController().navigate(R.id.action_foodCreateFragment_to_foodOverviewFragment)
                foodCreateViewModel.doneNavigating()
            }

        })

        return binding.root
    }

    private fun checkFoodComplete(): Boolean {
        val nameNotEmpty: Boolean = binding.editTextPersonName.text.isNotBlank()
        val dateNotEmpty: Boolean = true // TODO check if calendarView.selection()

        return (nameNotEmpty && dateNotEmpty)
    }

    private fun getFoodItemFromForm() : FoodItem {
        return FoodItem(
            foodId = 1, //TODO get latest foodId from Database and add one
            name = binding.editTextPersonName.text.toString(),
            expiryDate = Date(), // TODO set time from calendarView
            expiryType = getExpiryType(),
        )
    }

    // hulpfunctie: omzetting id expiryType naar string
    private fun getExpiryType(): String {
        when (binding.radioGroupType.checkedRadioButtonId){
            R.id.radioTHT -> return "THT"
            R.id.radioTGT -> return "TGT"
            else -> return ""
        }
    }

}