package be.vives.fridgepal.database

import androidx.room.TypeConverter
import java.util.*

class Converters {

    // voor conversie Long => Date
    // Room library kan niet met classes zoals java.util.Date overweg

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}