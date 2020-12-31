package be.vives.fridgepal.database

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import be.vives.fridgepal.models.Recipe
import org.joda.time.DateTime
import org.joda.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

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

// extensie functies : omzetten Database Class naar Model Class

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

/**
 * True if expiry type is TGT and expiry date has passed
 */
fun FoodItem.isExpired(): Boolean {
    return expiryDate.before(Date(System.currentTimeMillis()))
            && expiryType.equals("TGT")
}

/**
 * True if expiry type is TGT
 * and threshold before expiry date has passed
 */
fun FoodItem.isNearlyExpired(context: Context): Boolean{
    val daysThreshold = PreferenceManager.getDefaultSharedPreferences(context)
        .getString("tgt_threshold","3")
    val todayMillis = DateTime.now().withTimeAtStartOfDay().millis

    val threshold = Date(todayMillis + TimeUnit.DAYS.toMillis(daysThreshold!!.toLong()))
    return expiryDate.before(threshold) && expiryType.equals("TGT")
}

/**
 * True if expiry type is TGT and expiry date has passed
 */
fun FoodItem.isCautionRequired(): Boolean{
    return expiryDate.before(Date(System.currentTimeMillis()))
            && expiryType.equals("THT")
}