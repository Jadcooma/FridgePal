package be.vives.fridgepal.food_overview

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import be.vives.fridgepal.R
import be.vives.fridgepal.database.AppDatabase
import be.vives.fridgepal.databinding.FragmentFoodOverviewBinding

class FoodOverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //region * Boilerplate : databinding + viewModel from factory *
        // uitbreiding fragment met databinding: inflate met DataBindingUtil
        val binding : FragmentFoodOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_overview, container, false)

        val application = requireNotNull(this.activity).application
        // DAO meegeven aan ViewModel voor uitvoeren van queries op database
        val dataSource = AppDatabase.getInstance(application).FoodDao
        val viewModelFactory = FoodOverviewViewModelFactory(dataSource, application)
        val foodOverviewViewModel = ViewModelProvider(this, viewModelFactory)
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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

}