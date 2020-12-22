package be.vives.fridgepal.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import be.vives.fridgepal.R
import be.vives.fridgepal.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding : FragmentRecipesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipes, container, false)

        // TODO get from factory => Db access => CACHING
        val recipesViewModel = ViewModelProvider(activity!!).get(RecipesViewModel::class.java)

        binding.buttonSearch.setOnClickListener {
            recipesViewModel.getRecipeSearchResults(binding.editTextSearchTerm.text.toString())
        }

        binding.setLifecycleOwner (this)
        return binding.root
    }
}