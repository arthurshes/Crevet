package workwork.example.andropediagits.domain.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.andropediagits.core.model.SendStatus
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.local.entities.promo.PromoCodeEntity
import com.example.andropediagits.data.remote.model.EmailSignInAnswerModel

import com.example.andropediagits.data.remote.model.StrikeModeAnswerModel
import com.example.andropediagits.data.remote.model.TimeAnswerModel
import com.example.andropediagits.data.remote.model.UserSignInModel
import com.example.andropediagits.data.remote.model.email.EmailRecoverModel
import com.example.andropediagits.data.remote.model.email.EmailSignInModel
import com.example.andropediagits.data.remote.model.email.RecoverStatusModel
import com.example.andropediagits.data.remote.model.promo.PromoCodeCheckModel
import com.example.andropediagits.data.remote.model.promo.PromoCodeModel
import com.example.andropediagits.data.remote.model.promo.PromoCodeResponse
import com.example.andropediagits.data.remote.model.strike.StrikeModeSendModel
import kotlinx.coroutines.delay
import java.util.*

class FakeUserLogicRepo : UserLogicRepo {
    private val fakePromoCodes = MutableLiveData<PromoCodeEntity>()
    private val fakeUserInfo = MutableLiveData<UserInfoEntity>()
    override suspend fun recoverPasswordForEmail(emailRecoverModel: EmailRecoverModel): RecoverStatusModel {
        //return RecoverStatusModel(222,"",true)
        TODO()
    }

    override suspend fun checkActualMySubscribe(promoCodeModel: PromoCodeModel): PromoCodeCheckModel {
        // Simulate a delay to mimic network request
        delay(500)
        // Implement the logic to check the actual subscription here
        // For the fake implementation, let's return an empty PromoCodeCheckModel
        return PromoCodeCheckModel(true, true, Date(), "promo123")
    }

    override suspend fun insertPromoCode(promoCodeEntity: PromoCodeEntity) {
        fakePromoCodes.postValue(promoCodeEntity)
    }

    override suspend fun getAllMyPromo(): PromoCodeEntity {
     //   return fakeUserInfo // Replace this with the actual LiveData containing promo codes
        return PromoCodeEntity("1","2","1",Date())
    }

    override suspend fun deletePromoCode(promoCodeEntity: PromoCodeEntity) {
        fakeUserInfo.postValue(null)
    }

    override suspend fun updatePromoCode(promoCodeEntity: PromoCodeEntity) {
        // Find the promo code in the list and update it
        /*val existingPromoCode = fakePromoCodes.find { it.promoId == promoCodeEntity.promoId }
        existingPromoCode?.let {
            it.promoCode = promoCodeEntity.promoCode
            it.token = promoCodeEntity.token
            it.promoDate = promoCodeEntity.promoDate
            // Update other properties as needed
        }*/
        TODO()
    }

    override suspend fun sendPromoCode(promoCodeModel: PromoCodeModel): PromoCodeResponse {
        // Simulate a delay to mimic network request
        val test="SPECIAL123"
        delay(500)
        // Implement the logic to send the promo code here
        // For the fake implementation, let's return a success response
        return PromoCodeResponse(true, promoCodeModel.promoCode==test, "Promo code sent successfully", 200,userPromoExist = false, promoDate = "")
    }


    override suspend fun emailSignIn(emailSignInModel: EmailSignInModel): EmailSignInAnswerModel {
        // Simulate a delay to mimic network request
        delay(500)
        // Implement the logic for email sign-in here
        // For the fake implementation, let's return a success response
        return EmailSignInAnswerModel("fake_token", 200, true)
    }

    override suspend fun sendUserInfo(userSignInModel: UserSignInModel): SendStatus {
        // Simulate a delay to mimic network request
        delay(500)
        // Implement the logic to send user info here
        // For the fake implementation, let's return a success response
        return SendStatus(true, "User info sent successfully", 200)
    }

    override suspend fun insertUserInfoLocal(userInfoEntity: UserInfoEntity) {
        fakeUserInfo.postValue(userInfoEntity)
    }

    override suspend fun updateUserInfoLocal(userInfoEntity: UserInfoEntity) {
        fakeUserInfo.postValue(userInfoEntity)
    }

    override suspend fun deleteUserInfoLocal(userInfoEntity: UserInfoEntity) {
        fakeUserInfo.postValue(null)
    }

    override suspend fun getUserInfoLocal(): UserInfoEntity {
        return UserInfoEntity(
            token = "your_token_here",
            name = "John Doe",
            image = null,
            userLanguage = "English",
            phoneBrand = "Samsung",
            andropointCount = 100,
            lastOnlineDate = "2023-09-17",
            strikeModeDay = 5,
            lastOpenCourse = 1,
            lastOpenTheme = 2)
    }

    override suspend fun getCurrentTime(): TimeAnswerModel {
        // Simulate a delay to mimic network request
        delay(500)
        // Return a fake TimeAnswerModel
        return TimeAnswerModel("", "", "", 0, 0, false, "", 0, "", 0, "", 0, "", "", 0)
    }

    override suspend fun getCurrentUserInfo(token: String): UserSignInModel {
        // Simulate a delay to mimic network request
        delay(500)
        // Implement the logic to get the current user info here
        // For the fake implementation, let's return a fake UserSignInModel
        return UserSignInModel(
            name = "John Doe",
            image = null, // Assuming you have the Bitmap instance here
            token = "your_auth_token_here",
            userlanguage = "English",
            andropointCount = 100,
            lastOnlineDate = "2023-08-04",
            strikeModeDay = 2,
            lastCourseNumber = 5,
            lastThemeNumber = 10
        )
    }

    override suspend fun getMyStrikeModeInfo(strikeModeSendModel: StrikeModeSendModel): StrikeModeAnswerModel {
        // Simulate a delay to mimic network request
       // delay(500)
        return StrikeModeAnswerModel("", 3,"userToken")
        TODO()
    }

    override suspend fun updateUserInfo(userSignInModel: UserSignInModel): SendStatus {

        delay(500)

        return SendStatus(true, "User info updated successfully", 200)
       // TODO()
    }
}