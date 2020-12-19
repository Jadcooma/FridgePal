package be.vives.fridgepal.food_create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import be.vives.fridgepal.R
import be.vives.fridgepal.database.FoodDatabase
import be.vives.fridgepal.databinding.FragmentFoodCreateBinding

class FoodCreateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        
        // uitbreiding fragment met databinding: inflate met DataBindingUtil
        val binding : FragmentFoodCreateBinding = DataBindingUtil.inflate(
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
            this.findNavController().navigate(R.id.action_foodCreateFragment_to_foodOverviewFragment)
        }


        return binding.root
    }

}