package workwork.example.andropediagits.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.EmailIsEmptyException
import com.example.andropediagits.core.exception.PasswordIsEmptyException
import com.example.andropediagits.core.exception.TooLongPasswordException
import com.example.andropediagits.core.exception.WrongAddressEmialException
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.validators.EmailValidStates
import com.example.andropediagits.domain.useCases.userLogic.validators.EmailValidator
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class EmailValidatorTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    lateinit var emailValidator: EmailValidator
    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    @Before
    fun setUp(){
        hiltRule.inject()
        emailValidator = EmailValidator(userLogicRepo)
    }

    @Test
    fun testCheckEmailDate_returnErrorEmail() = runTest {
        try {
            emailValidator.emailCheckAndSend(
                "gokotkgotg",
                "3232323",
                EmailValidStates.CHECKEMAILANDPASSWORD
            )
        }catch (e: WrongAddressEmialException){
            assertThat(e.message).isEqualTo("Wrong address email check your email address and try again")
        }
    }

    @Test
    fun testCheckEmailDate_returnErrorPassword() = runTest {
        try {
            emailValidator.emailCheckAndSend(
                "gokotkgotg@gmail.com",
                "323232367676667676767676767676767676767676766767676767",
                EmailValidStates.CHECKEMAILANDPASSWORD
            )
        }catch (e: TooLongPasswordException){
            assertThat(e.message).isEqualTo("Too long password max 23 characters")
        }
    }
    @Test
    fun testCheckEmailDate_returnEmailisEmpty() = runTest {
        try {
            emailValidator.emailCheckAndSend(
                "",
                "3232323",
                EmailValidStates.CHECKEMAILANDPASSWORD
            )
        }catch (e: WrongAddressEmialException){
            assertThat(e.message).isEqualTo("Wrong address email check your email address and try again")
        }
    }
    @Test
    fun testCheckEmailDate_returnPasswordisEmpty() = runTest {
        try {
            emailValidator.emailCheckAndSend(
                "vgjrigreij@gmail",
                "",
                EmailValidStates.CHECKEMAILANDPASSWORD
            )
        }catch (e: PasswordIsEmptyException){
            assertThat(e.message).isEqualTo("Password is empty exception")
        }
    }

    @Test
    fun testCheckEmailDate_returnSuccess() = runTest {
        try {
            emailValidator.emailCheckAndSend(
                "vgjrigreij@gmail",
                "tg45t4t45g5gghg56hg",
                EmailValidStates.CHECKEMAILANDPASSWORD
            )
        }catch (e: PasswordIsEmptyException){
            assertThat(e.message).isEqualTo(null)
        }
    }

}