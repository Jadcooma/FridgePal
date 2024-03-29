package be.vives.fridgepal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FoodItem::class, DatabaseRecipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class) // voor conversie Long => Date
abstract class AppDatabase : RoomDatabase() {
    abstract val FoodDao: FoodDao
    abstract val RecipeDao: RecipeDao

    companion object  {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
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

