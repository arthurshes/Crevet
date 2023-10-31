package workwork.test.andropediagits.data.local.typeConverters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale


class LocalDateTypeConverter {



    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
                LocalDateTime.parse(dateString)
        }
    }




    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }

}