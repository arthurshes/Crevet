package workwork.test.andropediagits.data.local.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListTypeConverter {
    @TypeConverter
    fun fromString(string: String?):List<String>?{
        val listType = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(string,listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?):String?{
        return Gson().toJson(list)
    }
}