package be.vives.fridgepal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "food_item_table")
data class FoodItem (
        @PrimaryKey(autoGenerate = true)
        var foodId : Int = 0,

        @ColumnInfo()
        var name: String = "",

        @ColumnInfo()
        var expiryDate: Date = Date(),

        @ColumnInfo()
        var expiryType: String = "THT",

)