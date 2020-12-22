package be.vives.fridgepal.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.fridgepal.databinding.ListItemRecipeBinding
import be.vives.fridgepal.network.Recipe

class RecipeAdapter : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class RecipeViewHolder private constructor(val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Recipe){
            binding.recipe = recipe
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent:ViewGroup): RecipeViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecipeBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(binding)
            }
        }
    }
    class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>(){
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }

}