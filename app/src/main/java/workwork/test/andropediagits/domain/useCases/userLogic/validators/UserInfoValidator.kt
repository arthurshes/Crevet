package workwork.test.andropediagits.domain.useCases.userLogic.validators

import android.graphics.Bitmap
import workwork.test.andropediagits.core.exception.NameIsEmptyException
import workwork.test.andropediagits.core.exception.TooBigPhotoException
import workwork.test.andropediagits.core.exception.TooLongUserNameException
import workwork.test.andropediagits.core.utils.Constatns.IMAGE_MAX_SIZE_MB
import workwork.test.andropediagits.core.utils.Constatns.NAME_MAX_LENGHT


class UserInfoValidator {

     fun validateImageAndName(name:String,image:Bitmap?=null){
        if (name.isEmpty()){
            throw NameIsEmptyException()
        }
        if (name.length>NAME_MAX_LENGHT){
            throw TooLongUserNameException()
        }
        image?.let { nullImage->
            val sizeInByte = nullImage.byteCount
            val sizeInMb = sizeInByte.toDouble() / (1024*1024)
            if(sizeInMb>IMAGE_MAX_SIZE_MB){
               throw TooBigPhotoException()
            }
        }
    }

}

enum class UserInfoUpdateEnum{
    NOTNETWORK,
    ERROR,
    SUCCESS,
    UNKNOWNERROR,
    TIMEOUTERROR,
    NULLPOINTERROR,
    TooBigPhoto,
    TooLongUserName,
    NameIsEmpty,
    OFFLINE,
    OFFLINETHEME
}