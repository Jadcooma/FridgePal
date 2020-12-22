package be.vives.fridgepal.food_overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import be.vives.fridgepal.R
import be.vives.fridgepal.database.FoodDatabase
import be.vives.fridgepal.databinding.FragmentFoodOverviewBinding

class FoodOverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //region * Boilerplate : databinding + viewModel from factory *
        // uitbreiding fragment met databinding: inflate met DataBindingUtil
        val binding : FragmentFoodOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_overview, container, false)

        val application = requireNotNull(this.activity).application

        // DAO meegeven aan ViewModel voor uitvoeren van queries op database
        val dataSource = FoodDatabase.getInstance(application).FoodDatabaseDao

        val viewModelFactory = FoodOverviewViewModelFactory(dataSource, application)

        val foodOverviewViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FoodOverviewViewModel::class.java)

        binding.foodOverviewViewModel = foodOverviewViewModel
        binding.setLifecycleOwner(this)
        //endregion

        binding.buttonAddFood.setOnClickListener {
            this.findNavController().navigate(R.id.action_foodOverviewFragment_to_foodCreateFragment)
        }

        val foodItemEditListener = FoodItemAdapter.FoodItemListener{
            foodId -> foodOverviewViewModel.onFoodEditClicked(foodId)
        }

        val foodItemDeleteListener = FoodItemAdapter.FoodItemListener{
                foodId -> foodOverviewViewModel.onFoodDeleteClicked(foodId)
        }

        //TODO Edit scherm
        foodOverviewViewModel.navigateToFoodEdit.observe(viewLifecycleOwner, {
            Toast.makeText(context, "EDIT PAGINA TO DO", Toast.LENGTH_SHORT).show()
        })

        foodOverviewViewModel.navigateToFoodDelete.observe(viewLifecycleOwner,{
            foodOverviewViewModel.onDeletePressed(it)
        })

        val adapter = FoodItemAdapter(foodItemEditListener, foodItemDeleteListener)
        binding.foodList.adapter = adapter

        foodOverviewViewModel.listAllFood.observe(viewLifecycleOwner, {
            it?.let{
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}