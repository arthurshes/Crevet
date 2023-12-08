package workwork.test.andropediagits.domain.repo

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import workwork.test.andropediagits.core.model.SendStatus
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseBuyEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCreatorSubscribeEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVIctorineClueEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineAnswerVarEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineQuestionEntity
import workwork.test.andropediagits.data.remote.individualCourseGet.GetModerationCourseStatusModel
import workwork.test.andropediagits.data.remote.individualCourseGet.IndividualCourseCheckModel
import workwork.test.andropediagits.data.remote.individualCourseGet.SnedRatingIndiModel
import workwork.test.andropediagits.data.remote.individualCourseGet.UniqueCourseNumberGetModel
import workwork.test.andropediagits.data.remote.individualCourseGet.buyIndiCourse.BuyIndividualCourseModel
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.BuyCreatorSubscribeModel
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.CreatorSubscribeGetModel
import workwork.test.andropediagits.data.remote.model.CreatorCourseProfileGetModel
import workwork.test.andropediagits.data.remote.model.CreatorCourseProfileModel
import workwork.test.andropediagits.data.remote.model.MyIncomeModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualCourseGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualGetModel

interface IndividualCoursesRepo {

    suspend fun getCreatorCourseProfilePaging(token: String,page: Int = 1): List<IndividualCourseGetModel>

    suspend fun checkUserSendRaitingThisCourse(token: String,creatorToken: String,uniqueCourseNumber: Int):SendStatus

    suspend fun insertIndiQuestion(indiVictorineQuestionEntity: IndiVictorineQuestionEntity)


    suspend fun insertIndiClue(indiVIctorineClueEntity: IndiVIctorineClueEntity)


    suspend fun deleteIndiVictorineClue(createrToken:String,uniqueCourseNumber:Int)


    suspend fun deleteIndiVictorineAnswers(createrToken:String,uniqueCourseNumber:Int)


    suspend fun deleteIndiVicotineQuestions(createrToken:String,uniqueCourseNumber:Int)


    suspend fun getIndiClueTheme(createrToken:String,uniqueCourseNumber:Int,themeNumber:Int,questionNumber:Int):List<IndiVIctorineClueEntity>



    suspend fun getIndiAnswerVartionsTheme(createrToken:String,uniqueCourseNumber:Int,themeNumber:Int,questionNumber:Int):List<IndiVictorineAnswerVarEntity>


    suspend fun getIndiQuestionsTheme(createrToken:String,uniqueCourseNumber:Int,themeNumber:Int):List<IndiVictorineQuestionEntity>


    suspend fun insertIndiAnswerVariant(indiVictorineAnswerVarEntity: IndiVictorineAnswerVarEntity)

    suspend fun downloadOneIndiCourse(creatorToken: String,uniqueCourseNumber: Int):IndividualGetModel

    suspend fun checkOneIndiCourseBuy(token: String,creatorToken:String,uniqueCourseNumber: Int):IndividualCourseCheckModel

    suspend fun getMyBuyedIndiCourses(token: String):Response<List<IndividualCourseCheckModel>>


    suspend fun getCurrentCourseRatings(page: Int, token: String, uniqueCourseNumber: Int):Response<List<SnedRatingIndiModel>>


    suspend fun sendRatingIndiCourse( snedRatingIndiModel: SnedRatingIndiModel):SendStatus


    suspend fun getCreatorCourseProfile(token: String): CreatorCourseProfileGetModel


    suspend fun updateCreatorProfile( creatorCourseProfileModel: CreatorCourseProfileModel):SendStatus


    suspend fun getModerationStatusMyCourse(token: String,uniqueCourseNumber:Int): GetModerationCourseStatusModel


    suspend fun getMyIncome(token: String): MyIncomeModel

    suspend fun insertBuyIndiCourse(indiCourseBuyEntity: IndiCourseBuyEntity)


    suspend fun deleteIndiCourse(indiCourseBuyEntity: IndiCourseBuyEntity)


    suspend fun getAllBuyIndiCourses():List<IndiCourseBuyEntity>


    suspend fun getAllBuyIndiCoursesForCreator(creatorToken:String):List<IndiCourseBuyEntity>

    suspend fun getUnuiqueCourseNumber(token: String): UniqueCourseNumberGetModel

    suspend fun insertCreatorSubscribe(creatorSubscribeEntity: IndiCreatorSubscribeEntity)


    suspend fun getMyIndiSubs(): IndiCreatorSubscribeEntity


    suspend fun deleteIndiCreaterSubscribe()


    suspend fun updateIndiCreaterSubscribe(indiCreatorSubscribeEntity: IndiCreatorSubscribeEntity)

    suspend fun deleteAllIndiLEssonsContent()


    suspend fun createIndiLessonContent(indiLessonContentEntity: IndiLessonContentEntity)


    suspend fun getIndiLessonContent(lessonNumber:Int,uniqueCourseNumber:Int,themeNumber:Int,createrToken:String): IndiLessonContentEntity


    suspend fun deleteLessonContentIndi(indiLessonContentEntity: IndiLessonContentEntity)


    suspend fun updateLessonContentIndi(indiLessonContentEntity: IndiLessonContentEntity)


    suspend fun createIndiLesson(indiLessonEntity: IndiLessonEntity)


    suspend fun getIndiLesson(uniqueCourseNumber:Int,themeNumber:Int,createrToken:String): List<IndiLessonEntity>

    suspend fun deleteLessonIndi(indiLessonEntity: IndiLessonEntity)


    suspend fun updateLessonIndi(indiLessonEntity: IndiLessonEntity)


    suspend fun deleteAllIndiLEssons()


    suspend fun deleteAllIndiThemes()


    suspend fun createIndiThemes(indiLessonContentEntity: IndiThemeEntity)


    suspend fun getAllIndiThemes(uniqueCourseNumber:Int,createrToken:String): List<IndiThemeEntity>


    suspend fun deleteIndiThemes(indiLessonContentEntity: IndiThemeEntity)


    suspend fun updateIndiThemes(indiLessonContentEntity: IndiThemeEntity)



    suspend fun createIndiCourse(indiCourseEntity: IndiCourseEntity)


    suspend fun getMyIndiCourse(): IndiCourseEntity


    suspend fun getIndiCourseWithCreaterToken(createrToken:String,uniqueCourseNumber:Int): IndiCourseEntity


    suspend fun deleteMyIndiCourse(indiCourseEntity: IndiCourseEntity)


    suspend fun updateMyIndiCours(indiCourseEntity: IndiCourseEntity)


    suspend fun deleteAllCourseIndi()


    suspend fun getMyIndiCourses(token: String):IndividualGetModel



    suspend fun getIndiCourse( page:Int = 1, query:String): Response<List<IndividualCourseGetModel>>


    suspend fun buyLoadCourseIndi( buyIndividualCourseModel: BuyIndividualCourseModel):IndividualGetModel


    suspend fun buyCreatorSubscribe( buyCreatorSubscribeModel: BuyCreatorSubscribeModel): SendStatus


    suspend fun getAndCheckMyCreatorSubscribes(token: String): CreatorSubscribeGetModel


    suspend fun testCreateIndividualCourse( individualCreateModel: IndividualGetModel): SendStatus


    suspend fun testUpdateIndividualCourse( individualCreateModel: IndividualGetModel): SendStatus

}