package workwork.test.andropediagits.data.local.typeConverters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream

class BitMapTypeConverter {

    @TypeConverter
    fun fromByteArray(bytes: ByteArray?=null): Bitmap? {
        return bytes?.size?.let { BitmapFactory.decodeByteArray(bytes, 0, it) }
    }


    @TypeConverter
    fun toByteArray(bitmap: Bitmap?=null): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}