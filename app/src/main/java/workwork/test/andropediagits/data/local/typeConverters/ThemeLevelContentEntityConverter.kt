package workwork.test.andropediagits.data.local.typeConverters

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity

class ThemeLevelContentEntityConverter {
    @TypeConverter
    fun fromString(value: String): List<ThemeLevelContentEntity> {
        val listType = object : TypeToken<List<ThemeLevelContentEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<ThemeLevelContentEntity>): String {
        return Gson().toJson(list)
    }

}