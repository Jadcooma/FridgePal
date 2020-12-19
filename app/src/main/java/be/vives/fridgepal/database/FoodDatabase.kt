package be.vives.fridgepal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // voor conversie Long => Date
abstract class FoodDatabase : RoomDatabase() {
    abstract val FoodDatabaseDao: FoodDatabaseDao

    companion object  {

        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getInstance(context: Context) : FoodDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodDatabase::class.java,
                        "food_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

