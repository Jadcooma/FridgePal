package be.vives.fridgepal.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
public abstract interface FoodDatabaseDao {
    @Insert
    fun insert(foodItem : FoodItem)

    @Update
    fun update(foodItem: FoodItem)

    @Query("DELETE FROM food_item_table WHERE foodId = :key")
    fun deleteFoodById(key: Long)

    @Query("SELECT * from food_item_table WHERE foodId = :key")
    fun getFoodById(key: Long): FoodItem?

    @Query("SELECT * from food_item_table")
    fun getAllFood(): LiveData<List<FoodItem>>

    @Query("DELETE FROM food_item_table")
    fun clear()

}