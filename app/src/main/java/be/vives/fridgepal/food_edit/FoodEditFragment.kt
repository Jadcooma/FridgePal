package be.vives.fridgepal.food_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.vives.fridgepal.R
import be.vives.fridgepal.database.AppDatabase
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.databinding.FragmentFoodEditBinding
import be.vives.fridgepal.food_create.FoodCreateViewModel
import be.vives.fridgepal.food_create.FoodCreateViewModelFactory
import java.util.*

class FoodEditFragment : Fragment() {
    private lateinit var binding : FragmentFoodEditBinding
    private var selectedExpiryDate = Date() // huidig geselecteerde datum

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // uitbreiding fragment met databinding: inflate met DataBindingUtil
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_edit, container, false)

        val application = requireNotNull(this.activity).application

        // DAO meegeven aan ViewModel voor uitvoeren van queries op database
        val dataSource = AppDatabase.getInstance(application).FoodDao

        val viewModelFactory = FoodEditViewModelFactory(dataSource, application)

        val foodEditViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FoodEditViewModel::class.java)

        // Stel foodItem van ViewModel in op basis van ID uit navigatie argumenten
        val foodId = FoodEditFragmentArgs.fromBundle(requireArguments()).foodId
        foodEditViewModel.setFoodItemById(foodId)

        binding.foodItem = foodEditViewModel.foodItem.value
        binding.setLifecycleOwner(this)

        foodEditViewModel.foodItem.observe(viewLifecycleOwner, {
            binding.foodItem = it
        })

        binding.buttonNavigate.setOnClickListener {
            if(binding.editTextPersonName.text.isNotBlank()) {
                foodEditViewModel.setFoodItem(getFoodItemFromForm(foodId))
                foodEditViewModel.editFoodItem()
            } else{
                Toast.makeText(
                    context, "Gelieve een naam in te vullen", Toast.LENGTH_SHORT).show()
            }
        }

        foodEditViewModel.navigateToOverview.observe(viewLifecycleOwner, { hasNavigated ->
            hasNavigated?.let{
                this.findNavController().navigate(
                    R.id.action_foodEditFragment_to_foodOverviewFragment)
                foodEditViewModel.doneNavigating()
            }
        })

        // Listener voor calendarView (calendarView.date -> altijd datum van vandaag)
        binding.calendarExpiryDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendarExpiryDate : Calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            selectedExpiryDate = calendarExpiryDate.time
        }

        return binding.root
    }

    private fun getFoodItemFromForm(id: Long) : FoodItem {
        val foodItem = FoodItem(
            foodId = id,
            name = binding.editTextPersonName.text.toString(),
            expiryDate = selectedExpiryDate,
            expiryType = getExpiryType(),
        )
        return foodItem
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