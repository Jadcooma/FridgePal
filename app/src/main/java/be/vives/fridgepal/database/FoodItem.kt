package be.vives.fridgepal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "food_item_table")
data class FoodItem(
        @PrimaryKey(autoGenerate = true)
        var foodId: Long = 0L,

        @ColumnInfo()
        var name: String,

        @ColumnInfo()
        var expiryDate: Date,

        @ColumnInfo()
        var expiryType: String,

        )