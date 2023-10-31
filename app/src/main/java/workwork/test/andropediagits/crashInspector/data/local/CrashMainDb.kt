package workwork.test.andropediagits.crashInspector.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

import workwork.test.andropediagits.data.local.typeConverters.DateTypeConverter

@Database(entities = [CrashEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class CrashMainDb:RoomDatabase(){
    abstract fun getCrashDao():CrashDao
}