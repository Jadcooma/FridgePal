package be.vives.fridgepal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import be.vives.fridgepal.models.Recipe
import java.util.*

@Entity(tableName = "food_item_table")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    var foodId: Long = 0L,
    var name: String,
    var expiryDate: Date,
    var expiryType: String,
)

@Entity(tableName= "recipe_table")
data class DatabaseRecipe(
    @PrimaryKey
    val url : String,
    val image : String,
    val source : String,
    val name: String
)

// extensiefuncties : omzetten Database Class naar Model Class

fun List<DatabaseRecipe>.asDomainModel(): List<Recipe>{
    return map{
        Recipe(
            name = it.name,
            url = it.url,
            image = it.image,
            source = it.source
        )
    }
}