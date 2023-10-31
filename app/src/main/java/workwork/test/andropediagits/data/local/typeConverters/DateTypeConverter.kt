package workwork.test.andropediagits.data.local.typeConverters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun convertToLong(date: Date?):Long?{
        return date?.time
    }

    @TypeConverter
    fun convertToDate(long: Long?):Date?{
        return long?.let { Date(it) }
    }

}