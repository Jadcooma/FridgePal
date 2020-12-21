package be.vives.fridgepal.food_overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.fridgepal.database.FoodItem
import be.vives.fridgepal.databinding.ListItemFoodItemBinding

class FoodItemAdapter(val clickListener: FoodItemListener) : ListAdapter<FoodItem, FoodItemAdapter.FoodItemViewHolder>(FoodItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class FoodItemViewHolder private constructor(val binding: ListItemFoodItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: FoodItem, editClickListener: FoodItemListener){
            binding.foodItem = item
            binding.editClickListener = editClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FoodItemViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFoodItemBinding.inflate(layoutInflater, parent, false)
                return FoodItemViewHolder(binding)
            }
        }
    }

    class FoodItemDiffCallback : DiffUtil.ItemCallback<FoodItem>(){
        override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem.foodId == newItem.foodId
        }

        override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem == newItem
        }

    }

    class FoodItemListener(val clickListener: (foodId: Long) -> Unit){
        fun onClick(foodItem : FoodItem) = clickListener(foodItem.foodId)
    }

}