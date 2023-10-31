package workwork.example.andropediagits.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.NameIsEmptyException
import com.example.andropediagits.core.exception.TooBigPhotoException
import com.example.andropediagits.core.exception.TooLongUserNameException
import com.example.andropediagits.core.utils.Constatns.IMAGE_MAX_SIZE_MB
import com.example.andropediagits.domain.useCases.userLogic.validators.UserInfoUpdateEnum
import com.example.andropediagits.domain.useCases.userLogic.validators.UserInfoValidator
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

@SmallTest
class UserInfoValidatorTest {
    lateinit var userInfoValidator: UserInfoValidator

    @Before
    fun setUp(){
        userInfoValidator = UserInfoValidator()
    }

    @Test
    fun testName_returnErrorNameisEmpty() {
        try {
            userInfoValidator.validateImageAndName("")
        }catch (e: NameIsEmptyException){
            assertThat(e.message).isEqualTo("Name is empty exception")
        }
    }

    @Test
    fun testImage_returnErrorImageBig(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
          context,
            IMAGE_MAX_SIZE_MB * 1024 * 1024 + 1
        )
        try {
            userInfoValidator.validateImageAndName("rrjvirjv", testImage)
        }catch (e: TooBigPhotoException){
            assertThat(e.message).isEqualTo("Too big photo max size 128mb")
        }

    }

    @Test
    fun testName_returnNameIsSoLong(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
            context,
            IMAGE_MAX_SIZE_MB
        )
        try {
            userInfoValidator.validateImageAndName("rrjvigrgregregregergergergergergergregrrjv", testImage)
        }catch (e: TooLongUserNameException){
            assertThat(e.message).isEqualTo("Too long username max 29 characters")
        }
    }

    @Test
    fun testNameAndImage_returnSuccess(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
            context,
            IMAGE_MAX_SIZE_MB
        )
        try {
            userInfoValidator.validateImageAndName("rrjvigrgrev", testImage)
        }catch (e: TooLongUserNameException){
            assertThat(e.message).isEqualTo(null)
        }catch (e:TooBigPhotoException){
            assertThat(e.message).isEqualTo(null)
        }catch (e:NameIsEmptyException){
            assertThat(e.message).isEqualTo(null)
        }
    }

    @Test
    fun testName_returnSuccess(){
        try {
            userInfoValidator.validateImageAndName("rrjvigrgrev")
        }catch (e: TooLongUserNameException){
            assertThat(e.message).isEqualTo(null)
        }catch (e:TooBigPhotoException){
            assertThat(e.message).isEqualTo(null)
        }catch (e:NameIsEmptyException){
            assertThat(e.message).isEqualTo(null)
        }
    }

    @Test
    fun testNameAndImage_returnNameIsLongAndImageSoBig(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
            context,
            IMAGE_MAX_SIZE_MB * 1024 * 1024 + 1
        )
        try {
            userInfoValidator.validateImageAndName("rrjvigrgregregregergergergergergergregrrjv", testImage)
        }catch (e: TooBigPhotoException){
            assertThat(e.message).isEqualTo("Too big photo max size 128mb")
        }catch (e:TooLongUserNameException){
            assertThat(e.message).isEqualTo("Too long username max 29 characters")
        }
    }


    @Test
    fun testNameAndImage_returnNameIsEmptyAndImageSoBig(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
            context,
            IMAGE_MAX_SIZE_MB * 1024 * 1024 + 1
        )
        try {
            userInfoValidator.validateImageAndName("", testImage)
        }catch (e: TooBigPhotoException){
            assertThat(e.message).isEqualTo("Too big photo max size 128mb")
        }catch (e:NameIsEmptyException){
            assertThat(e.message).isEqualTo("Name is empty exception")
        }
    }

    @Test
    fun testNameAndImage_returnNameIsEmptyAndImageSoBigLamdaCathcNameIsEmpty(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
            context,
            IMAGE_MAX_SIZE_MB * 1024 * 1024 + 1
        )
        var isSuccess:UserInfoUpdateEnum?=null
        try {
            userInfoValidator.validateImageAndName("", testImage)
        }catch (e: TooBigPhotoException){
            isSuccess = UserInfoUpdateEnum.TooBigPhoto
        }catch (e:NameIsEmptyException){
            isSuccess = UserInfoUpdateEnum.NameIsEmpty
        }

        assertThat(isSuccess).isEqualTo(UserInfoUpdateEnum.NameIsEmpty)
    }

    @Test
    fun testNameAndImage_returnNameIsEmptyAndImageSoBigLamdaCathcImageTooBig(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testImage = createTestImage(
            context,
            IMAGE_MAX_SIZE_MB * 1024 * 1024 + 1
        )
        var isSuccess:UserInfoUpdateEnum?=null
        try {
            userInfoValidator.validateImageAndName("dzrgrdgergreg", testImage)
        }catch (e: TooBigPhotoException){
//            assertThat(e.message).isEqualTo("Too big photo max size 128mb")
            isSuccess = UserInfoUpdateEnum.TooBigPhoto
        }catch (e:NameIsEmptyException){
            isSuccess = UserInfoUpdateEnum.NameIsEmpty
        }
//
//        assertThat(isSuccess).isEqualTo(UserInfoUpdateEnum.NameIsEmpty)
        assertThat(isSuccess).isEqualTo(UserInfoUpdateEnum.TooBigPhoto)
    }



    private fun createTestImage(context:Context,size:Int):Bitmap{
        val width = 100
        val height = 100
        val config = Bitmap.Config.ARGB_8888
        return Bitmap.createBitmap(width, height, config).apply {
             size
        }

    }
}