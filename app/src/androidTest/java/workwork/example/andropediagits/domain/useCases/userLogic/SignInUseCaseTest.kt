package workwork.example.andropediagits.domain.useCases.userLogic

import android.graphics.Bitmap
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.EmailErrorEnum
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.core.exception.NameIsEmptyException
import com.example.andropediagits.core.exception.TooBigPhotoException
import com.example.andropediagits.core.exception.TooLongUserNameException
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.remote.model.UserSignInModel
import com.example.andropediagits.data.remote.model.email.RecoverPassState
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import com.example.andropediagits.domain.useCases.userLogic.state.LanguagesEnum
import com.example.andropediagits.domain.useCases.userLogic.validators.EmailValidator
import com.example.andropediagits.domain.useCases.userLogic.validators.UserInfoUpdateEnum
import com.example.andropediagits.domain.useCases.userLogic.validators.UserInfoValidator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Named


@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class SignInUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    lateinit var signInUseCase: SignInUseCase

    @Inject
    @Named("test_updateUserInfoUseCase")
    lateinit var updateUserInfoUseCase: UpdateUserInfoUseCase


    @Inject
    @Named("test_emailValidator")
    lateinit var emailValidator: EmailValidator

    @Inject
    @Named("test_transactionRepo")
    lateinit var transactionRepo: TransactionRepo
    @Inject
    @Named("test_userInfoValidator")
    lateinit var userInfoValidator: UserInfoValidator

    @Before
    fun setup() {
        hiltRule.inject()

        signInUseCase = SignInUseCase(userLogicRepo, updateUserInfoUseCase, emailValidator,transactionRepo, userInfoValidator)
    }
//test
    @Test
    fun testRecoverPassword_EmailIsEmpty() = runTest {
        val email = ""

        // Act
        var state: RecoverPassState? = null
        signInUseCase.recoverPassword(email, isSuccess = { isSuccess -> state = isSuccess })

        // Assert
        assertEquals(state, RecoverPassState.EMAILISEMPTY)
    }
   /* @Test
    fun testRecoverPassword_Success() = runTest {
        val email = "sas"

        // Act
        var state: RecoverPassState? = null
        signInUseCase.recoverPassword(email) { isSuccess ->
            state = isSuccess
        }

        // Assert
        assertEquals(state, RecoverPassState.SUCCESS)
    }
*/
    @Test
    fun testEmailSignIn_returnSuccess()= runTest {

            val email = "test@example.com"
            val password = "securepassword"

            var emailErrorResult: EmailErrorEnum? = null

            signInUseCase.emailSignIn(
                { emailErrorResult = it },
                email,
                password
            )

            assertEquals(EmailErrorEnum.SUCCESS, emailErrorResult)
    }
    @Test
    fun testEmailSignIn_returnEmailEmptyWrongError() = runTest{
        val email = ""
        val password = "securepassword"
        var emailErrorResult: EmailErrorEnum? = null
        signInUseCase.emailSignIn({
            emailErrorResult = it
        },
            email, password)
        assertEquals(EmailErrorEnum.WrongAddressEmail,emailErrorResult)
    }
    @Test
    fun testEmailSignIn_returnEmailWrongError() = runTest{
        val email = "testn"
        val password = "securepassword"
        var emailErrorResult: EmailErrorEnum? = null
        signInUseCase.emailSignIn({
            emailErrorResult = it
        },
            email, password)

        assertEquals(EmailErrorEnum.WrongAddressEmail,emailErrorResult)
    }



    @Test
    fun testEmailSignIn_returnPasswordEmptyError() = runTest{
        val email ="test@example.com"
        val password = ""
        var emailErrorResult: EmailErrorEnum? = null
        signInUseCase.emailSignIn({
            emailErrorResult = it
        },
            email, password)
        assertEquals(EmailErrorEnum.PasswordIsEmpty,emailErrorResult)
    }
    @Test
    fun testEmailSignIn_returnTooLongPassword() = runTest{
        val email ="test@example.com"
        val password = "rewrerrwrrwrwrwrerrwrwerr"
        var emailErrorResult: EmailErrorEnum? = null
        signInUseCase.emailSignIn({
            emailErrorResult = it
        },
            email, password)
        assertEquals(EmailErrorEnum.TooLongPassword,emailErrorResult)
    }
     @Test
    fun testUpdateUserInfoWithSuccess() = runTest {
        // Arrange
        val name = "John"
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val lang = "en"
         var userInfoUpdateResult: UserInfoUpdateEnum? = null

        // Act
         signInUseCase.updateUserInfo(
             name = name,
             image = image,
             lang = lang,
             isSuccess = { result ->
                 userInfoUpdateResult = result
             })

         assertEquals(UserInfoUpdateEnum.SUCCESS, userInfoUpdateResult)
    }


    @Test
    fun testUpdateUserInfoWithTooLongUserNameError() = runTest {
        // Arrange
        val name = "Johnуццуццкцукуцкцукцукцукцкffseffsffesfsfsfesffsfsff"
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val lang = "en"
        var userInfoUpdateResult: UserInfoUpdateEnum? = null

        // Act
        signInUseCase.updateUserInfo(
            name = name,
            image = image,
            lang = lang,
            isSuccess = { result ->
                userInfoUpdateResult = result
            },
          //  isTooLongUserNameError = true
        )

        // Assert
        assertEquals(UserInfoUpdateEnum.TooLongUserName, userInfoUpdateResult)
    }

        @Test
        fun testUpdateUserInfoWithTooBigPhotoError() = runTest {
            // Arrange
            val name = "John"
            val image = Bitmap.createBitmap(12000, 12000, Bitmap.Config.ARGB_8888)
            val lang = "en"
            var userInfoUpdateResult: UserInfoUpdateEnum? = null

            // Act
            signInUseCase.updateUserInfo(
                name = name,
                image = image,
                lang = lang,
                isSuccess = { result ->
                    userInfoUpdateResult = result
                },
               // isTooBigPhotoError = true
            )

            assertEquals(UserInfoUpdateEnum.TooBigPhoto, userInfoUpdateResult)
        }

    @Test
    fun testUpdateUserInfoWithNameIsEmptyError() = runTest {
        // Arrange
        val name = ""
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val lang = "en"
        var userInfoUpdateResult: UserInfoUpdateEnum? = null

        // Act
       signInUseCase.updateUserInfo(
            name = name,
            image = image,
            lang = lang,
            isSuccess = { result ->
                userInfoUpdateResult = result
            },
          //  isNameIsEmptyError = true
        )

        // Assert
        assertEquals(UserInfoUpdateEnum.NameIsEmpty, userInfoUpdateResult)
    }

    @Test
    fun languageChooseTest_Eng() = runTest{
        // Arrange
        val languageEn = LanguagesEnum.ENGLISH
        val languageRu = LanguagesEnum.RUSSIAN
        val languageHin = LanguagesEnum.HINDI
        val languageGr = LanguagesEnum.GERMANY
        signInUseCase.languageChoose(
            isSuccess = { result ->
                assertEquals(ErrorEnum.SUCCESS, result)
            },
            language = languageEn)
        signInUseCase.languageChoose(
            isSuccess = { result ->
                assertEquals(ErrorEnum.SUCCESS, result)
            },
            language = languageRu)
        signInUseCase.languageChoose(
            isSuccess = { result ->
                assertEquals(ErrorEnum.SUCCESS, result)
            },
            language = languageHin)
        signInUseCase.languageChoose(
            isSuccess = { result ->
                assertEquals(ErrorEnum.SUCCESS, result)
            },
            language = languageGr)
    }

    @Test
    fun getMyInfSuccessTest() = runTest {
        val token = "valid_token"
        var successResult: ErrorEnum? = null

        // Act
        signInUseCase.getMyInfo(token) { isSuccess ->
            successResult = isSuccess
        }

        assertEquals(ErrorEnum.SUCCESS, successResult)
    }
}

