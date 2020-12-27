package be.vives.fridgepal.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
public abstract interface FoodDao {
    @Insert
    fun insert(foodItem : FoodItem)

    @Update
    fun update(foodItem: FoodItem)

    @Query("DELETE FROM food_item_table WHERE foodId = :key")
    fun deleteFoodById(key: Long)

    @Query("SELECT * from food_item_table WHERE foodId = :key")
    fun getFoodById(key: Long): FoodItem?

    @Query("SELECT * from food_item_table ORDER BY expiryDate DESC")
    fun getAllFoodSortedByDate(): LiveData<List<FoodItem>>

    @Query("DELETE FROM food_item_table")
    fun clear()

}