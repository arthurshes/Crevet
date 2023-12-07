package workwork.test.andropediagits.data.remote


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import workwork.test.andropediagits.core.model.SendStatus
import workwork.test.andropediagits.data.remote.individualCourseGet.GetModerationCourseStatusModel
import workwork.test.andropediagits.data.remote.individualCourseGet.SnedRatingIndiModel
import workwork.test.andropediagits.data.remote.individualCourseGet.UniqueCourseNumberGetModel
import workwork.test.andropediagits.data.remote.individualCourseGet.buyIndiCourse.BuyIndividualCourseModel
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.BuyCreatorSubscribeModel
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.CreatorSubscribeGetModel
import workwork.test.andropediagits.data.remote.model.CheckAdsLimitModel
import workwork.test.andropediagits.data.remote.model.CreatorCourseProfileGetModel
import workwork.test.andropediagits.data.remote.model.CreatorCourseProfileModel
import workwork.test.andropediagits.data.remote.model.DeleteMyAccModel
import workwork.test.andropediagits.data.remote.model.EmailSignInAnswerModel
import workwork.test.andropediagits.data.remote.model.MyIncomeModel
import workwork.test.andropediagits.data.remote.model.StrikeModeAnswerModel
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.data.remote.model.adsTerm.AdsTermGetModel
import workwork.test.andropediagits.data.remote.model.adsTerm.AdsTermUserSendModel
import workwork.test.andropediagits.data.remote.model.all.AllAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CoursesUpdateAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CoursesUpdateCheckSendModel
import workwork.test.andropediagits.data.remote.model.course.search.CourseNumberSearchModel
import workwork.test.andropediagits.data.remote.model.course.search.LevelUniqueIdSearchModel
import workwork.test.andropediagits.data.remote.model.course.search.ThemesUniqueIdSearchModel
import workwork.test.andropediagits.data.remote.model.email.EmailRecoverModel
import workwork.test.andropediagits.data.remote.model.email.EmailSignInModel
import workwork.test.andropediagits.data.remote.model.email.RecoverStatusModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualCourseGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualGetModel
import workwork.test.andropediagits.data.remote.model.interactive.InteractiveAllCorrectCodeModel
import workwork.test.andropediagits.data.remote.model.interactive.InteractiveAnswerModel
import workwork.test.andropediagits.data.remote.model.interactive.sendModels.InteractiveTestResultSendModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeCheckModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeResponse
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetCheckGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetMethodGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.UpdateNewPasswordModel
import workwork.test.andropediagits.data.remote.model.strike.StrikeModeSendModel
import workwork.test.andropediagits.data.remote.model.theme.LevelThemeAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.TermAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeTermCheckResponse
import workwork.test.andropediagits.data.remote.model.theme.beta.GetCheckUserTermModel
import workwork.test.andropediagits.data.remote.model.theme.sendModels.ThemeCheckTermModel
import workwork.test.andropediagits.data.remote.model.updateDates.LastUpdateDateModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateAnswerModel
import workwork.test.andropediagits.data.remote.model.victorine.VictorineAnswerModel
import workwork.test.andropediagits.data.remote.model.victorine.sendModels.VictorineTestResultSendModel

interface LogicUserApiService {





    @POST("user/account/delete")
    suspend fun deleteMyAccount(@Body DeleteMyAccModel: DeleteMyAccModel):SendStatus



    ///Test funs
    @GET("user/indiCourses/getBuyedCourses")
    suspend fun getMyBuyedIndiCourses(@Query("token")token: String):Response<List<IndividualCourseGetModel>>

    @GET("user/indiCourses/ratingGet")
    suspend fun getCurrentCourseRatings(@Query("page")page: Int,@Query("token")token: String,@Query("uniqueCourseNumber")uniqueCourseNumber: Int):Response<List<SnedRatingIndiModel>>

    @POST("user/indiCourses/ratingSend")
    suspend fun sendRatingIndiCourse(@Body snedRatingIndiModel: SnedRatingIndiModel):SendStatus

    @GET("user/indiCourses/getCreatorProfile")
    suspend fun getCreatorCourseProfile(@Query("token")token: String): CreatorCourseProfileGetModel

    @POST("user/individualCourseCreater/updateProfile")
    suspend fun updateCreatorProfile(@Body creatorCourseProfileModel: CreatorCourseProfileModel):SendStatus

    @GET("user/individualCourseCreater/moderationStatus")
    suspend fun getModerationStatusMyCourse(@Query("token")token: String,@Query("uniqueCourseNumber")uniqueCourseNumber:Int): GetModerationCourseStatusModel

    @GET("user/individualCourseCreater/getIncome")
    suspend fun getMyIncome(@Query("token")token: String): MyIncomeModel

    @GET("user/createIndividualCourse/getUniqueCourseNumber")
    suspend fun getUnuiqueCourseNumber(@Query("token")token: String): UniqueCourseNumberGetModel

    @POST("user/buyCreatorSubscribe/buy")
    suspend fun buyCreatorSubscribe(@Body buyCreatorSubscribeModel: BuyCreatorSubscribeModel):SendStatus

    @GET("user/buyCreatorSubscribe/get")
    suspend fun getAndCheckMyCreatorSubscribes(@Query("token") token: String): CreatorSubscribeGetModel

    @POST("user/createIndividualCourse/create")
    suspend fun testCreateIndividualCourse(@Body individualCreateModel: IndividualGetModel):SendStatus

    @POST("user/createIndividualCourse/update")
    suspend fun testUpdateIndividualCourse(@Body individualCreateModel: IndividualGetModel):SendStatus

    @GET("user/myIndiCourses/get")
    suspend fun getMyIndiCourses(@Query("token") token: String):IndividualGetModel


    @GET("user/get/indiCourses")
    suspend fun getIndiCourse(@Query("page") page:Int = 1,@Query("query") query:String):Response<List<IndividualCourseGetModel>>

    @POST("user/indiCourses/download")
    suspend fun buyLoadCourseIndi(@Body buyIndividualCourseModel: BuyIndividualCourseModel):IndividualGetModel


    ///Test funs

    @POST("user/resetPassword/resetPassword")
    suspend fun sendUpdateNewPassword(@Body updateNewPasswordModel: UpdateNewPasswordModel):SendStatus

    @GET("user/resetPassword/getCheckMethod")
    suspend fun getUserCheckMethod(@Query("email")email:String): ResetMethodGetModel


    @POST("user/resetPassword/checkText")
    suspend fun checkResetText(@Body resetTextCheckSendModel: ResetTextCheckSendModel): ResetCheckGetModel

    @POST("user/resetPassword/checkDate")
    suspend fun checkResetDate(@Body resetDateCheckSendModel: ResetDateCheckSendModel):ResetCheckGetModel

    @POST("user/resetPassword/sendText")
    suspend fun resetTextSend(@Body resetTextSendModel: ResetTextSendModel):SendStatus

    @POST("user/resetPassword/sendDate")
    suspend fun resetDateSend(@Body resetDateSendModel: ResetDateSendModel):SendStatus

    @GET("user/adsTerm/delete")
    suspend fun deleteAdsTerm (@Query("token") token:String): AdsTermGetModel

    @POST("user/adsTerm/get")
    suspend fun sendAdsTerm(@Body adsTermUserSendModel: AdsTermUserSendModel):AdsTermGetModel

    @POST("user/ads/checkTwoHoursLimit")
    suspend fun checkLimitTwoHoursAds(@Body checkAdsLimitModel: CheckAdsLimitModel):CheckAdsLimitGetModel

    @POST("user/ads/checkLimitAds")
    suspend fun checkLimittermAds(@Body checkAdsLimitModel: CheckAdsLimitModel):CheckAdsLimitGetModel

    @GET("user/check/checkTerm")
    suspend fun checkTermThemes(@Query("token") token: String): GetCheckUserTermModel

    @POST("user/promo/check")
    suspend fun checkActualMySubscribe(@Body promoCodeModel: PromoCodeModel): PromoCodeCheckModel

    @POST("user/promo/send")
    suspend fun sendPromoCode(@Body promoCodeModel: PromoCodeModel): PromoCodeResponse

    @POST("user/signIn/email")
    suspend fun sendEmailData(@Body emailSignInModel: EmailSignInModel): EmailSignInAnswerModel

    @POST("user/signIn/recover")
    suspend fun recoverPasswordForEmail(@Body emailRecoverModel: EmailRecoverModel): RecoverStatusModel

    @POST("user/userInfo/send")
    suspend fun sendUserInfo(@Body userSignInModel: UserSignInModel): SendStatus

    @POST("user/userInfo/update")
    suspend fun updateUserInfo(@Body userSignInModel: UserSignInModel):SendStatus

    @GET("user/userInfo/get")
    suspend fun getMyInfo(
        @Query("token") token:String
    ):UserSignInModel


    @POST("user/theme/checkTerm")
    suspend fun checkTermCurrentTheme(@Body themeCheckTermModel: ThemeCheckTermModel): ThemeTermCheckResponse

     @POST("user/userInfo/strikeMode")
     suspend fun getMyStrikeModeInfo(
         @Body strikeModeSendModel: StrikeModeSendModel
     ): StrikeModeAnswerModel



     @GET("course/allCourses/get")
     suspend fun getAllCourses(@Query("token") token: String,@Query("languageUser") language:String): AllAnswerModel

//     @GET("/newLang/allCourses/get")
//     suspend fun getAllCourseUpdateLang(@Query("token") token: String,@Query("languageUser") language:String):AllAnswerModel

     @POST("course/allCourses/check")
     suspend fun checkUpdateCourses(@Body coursesUpdateCheckSendModel: CoursesUpdateCheckSendModel): CoursesUpdateAnswerModel

     @GET("course/Alllevels/get")
     suspend fun getAllLevels(@Query("token")token: String,@Query("languageUser") language:String):List<LevelThemeAnswerModel>

     @GET("course/Allthemes/get")
     suspend fun getAllThemes(@Query("token")token: String,@Query("languageUser") language:String):List<ThemeAnswerModel>

     @POST("course/theme/get")
     suspend fun getThemesForUniqueId(
         @Body themesUniqueIdSearchModel: ThemesUniqueIdSearchModel
     ):ThemeAnswerModel


     @POST("course/course/get")
     suspend fun getCourseForNumber(
         @Body courseNumberSearchModel: CourseNumberSearchModel
     ): CourseAnswerModel

     @POST("course/level/get")
     suspend fun getLevelForUniqueId(@Body levelUniqueIdSearchModel: LevelUniqueIdSearchModel):List<LevelThemeAnswerModel>

     @GET("course/Allvictorine/get")
     suspend fun getAllVictorine():List<VictorineAnswerModel>


     @GET("course/interactiveAnswers/get")
     suspend fun getAllCorrectAnswerInteractive(): InteractiveAllCorrectCodeModel

     @POST("user/progress/victorine/send")
     suspend fun sendMyVictorineProgress(@Body victorineTestResultSendModel: VictorineTestResultSendModel)
     
     @POST("user/progress/interactive/send")
     suspend fun sendMyInteractiveProgress(@Body interactiveTestResultSendModel: InteractiveTestResultSendModel)

//     @POST("user/progress/theme/send")
//     suspend fun sendMyThemeOpening(themeSendModel: ThemeSendModel)

    @GET("course/dates/get")
    suspend fun getAllCoursesAndThemesUpdateDate(): LastUpdateDateModel

    @POST("user/progress/update")
    suspend fun sendMyProgress(@Body updateAnswerModel: UpdateAnswerModel): TermAnswerModel

    @GET("course/Allinteractive/get")
    suspend fun getAllInteractive():List<InteractiveAnswerModel>
}