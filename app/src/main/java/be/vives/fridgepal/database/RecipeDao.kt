package be.vives.fridgepal.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public abstract interface RecipeDao {
    @Query("SELECT * FROM recipe_table")
    fun getRecipes() : LiveData<List<DatabaseRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg recipes: DatabaseRecipe)

    @Query("DELETE FROM recipe_table")
    fun clear()
}