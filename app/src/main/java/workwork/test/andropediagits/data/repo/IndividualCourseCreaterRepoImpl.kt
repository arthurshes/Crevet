package workwork.test.andropediagits.data.repo

import retrofit2.Response
import workwork.test.andropediagits.core.model.SendStatus
import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCreatorSubscribeEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity
import workwork.test.andropediagits.data.remote.LogicUserApiService
import workwork.test.andropediagits.data.remote.individualCourseGet.UniqueCourseNumberGetModel
import workwork.test.andropediagits.data.remote.individualCourseGet.buyIndiCourse.BuyIndividualCourseModel
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.BuyCreatorSubscribeModel
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.CreatorSubscribeGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualCourseGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualGetModel
import workwork.test.andropediagits.domain.repo.IndividualCourseCreaterRepo
import javax.inject.Inject
import kotlin.math.log

class IndividualCourseCreaterRepoImpl @Inject constructor(private val mainDb: MainDb,private val logicUserApiService: LogicUserApiService): IndividualCourseCreaterRepo {

    private val transactionsDao = mainDb.getTransactionDao()
    private val contentDao = mainDb.getIndiLessonsContentDao()
    private val courseDao = mainDb.getIndiCourseDao()
    private val themeDao = mainDb.getIndiThemesDao()
    private val lessonDao = mainDb.getIndiLessonsDao()
    override suspend fun getUnuiqueCourseNumber(token: String): UniqueCourseNumberGetModel {
        return logicUserApiService.getUnuiqueCourseNumber(token)
    }

    override suspend fun insertCreatorSubscribe(creatorSubscribeEntity: IndiCreatorSubscribeEntity) {
        transactionsDao.insertCreatorSubscribe(creatorSubscribeEntity)
    }

    override suspend fun getMyIndiSubs(): IndiCreatorSubscribeEntity {
       return  transactionsDao.getMyIndiSubs()
    }

    override suspend fun deleteIndiCreaterSubscribe() {
        transactionsDao.deleteIndiCreaterSubscribe()
    }

    override suspend fun updateIndiCreaterSubscribe(indiCreatorSubscribeEntity: IndiCreatorSubscribeEntity) {
        transactionsDao.updateIndiCreaterSubscribe(indiCreatorSubscribeEntity)
    }


    override suspend fun deleteAllIndiLEssonsContent() {
       contentDao.deleteAllIndiLEssonsContent()
    }

    override suspend fun createIndiLessonContent(indiLessonContentEntity: IndiLessonContentEntity) {
      contentDao.createIndiLessonContent(indiLessonContentEntity)
    }

    override suspend fun getIndiLessonContent(
        lessonNumber: Int,
        uniqueCourseNumber: Int,
        themeNumber: Int,
        createrToken: String
    ): IndiLessonContentEntity {
       return contentDao.getIndiLessonContent(lessonNumber, uniqueCourseNumber, themeNumber, createrToken)
    }

    override suspend fun deleteLessonContentIndi(indiLessonContentEntity: IndiLessonContentEntity) {
        contentDao.deleteLessonContentIndi(indiLessonContentEntity)
    }

    override suspend fun updateLessonContentIndi(indiLessonContentEntity: IndiLessonContentEntity) {
        contentDao.updateLessonContentIndi(indiLessonContentEntity)
    }

    override suspend fun createIndiLesson(indiLessonEntity: IndiLessonEntity) {
        lessonDao.createIndiLesson(indiLessonEntity)
    }

    override suspend fun getIndiLesson(
        uniqueCourseNumber: Int,
        themeNumber: Int,
        createrToken: String
    ): List<IndiLessonEntity> {
        return lessonDao.getIndiLesson(uniqueCourseNumber, themeNumber, createrToken)
    }

    override suspend fun deleteLessonIndi(indiLessonEntity: IndiLessonEntity) {
        lessonDao.deleteLessonIndi(indiLessonEntity)
    }

    override suspend fun updateLessonIndi(indiLessonEntity: IndiLessonEntity) {
        lessonDao.updateLessonIndi(indiLessonEntity)
    }

    override suspend fun deleteAllIndiLEssons() {
        lessonDao.deleteAllIndiLEssons()
    }

    override suspend fun deleteAllIndiThemes() {
       themeDao.deleteAllIndiThemes()
    }

    override suspend fun createIndiThemes(indiLessonContentEntity: IndiThemeEntity) {
        themeDao.createIndiThemes(indiLessonContentEntity)
    }

    override suspend fun getAllIndiThemes(
        uniqueCourseNumber: Int,
        createrToken: String
    ): List<IndiThemeEntity> {
        return themeDao.getAllIndiThemes(uniqueCourseNumber, createrToken)
    }

    override suspend fun deleteIndiThemes(indiLessonContentEntity: IndiThemeEntity) {
       themeDao.deleteIndiThemes(indiLessonContentEntity)
    }

    override suspend fun updateIndiThemes(indiLessonContentEntity: IndiThemeEntity) {
        themeDao.updateIndiThemes(indiLessonContentEntity)
    }

    override suspend fun createIndiCourse(indiCourseEntity: IndiCourseEntity) {
        courseDao.createIndiCourse(indiCourseEntity)
    }

    override suspend fun getMyIndiCourse(): IndiCourseEntity {
        return courseDao.getMyIndiCourse()
    }

    override suspend fun getIndiCourseWithCreaterToken(
        createrToken: String,
        uniqueCourseNumber: Int
    ): IndiCourseEntity {
        return courseDao.getIndiCourseWithCreaterToken(createrToken, uniqueCourseNumber)
    }

    override suspend fun deleteMyIndiCourse(indiCourseEntity: IndiCourseEntity) {
        courseDao.deleteMyIndiCourse(indiCourseEntity)
    }

    override suspend fun updateMyIndiCours(indiCourseEntity: IndiCourseEntity) {
       courseDao.updateMyIndiCours(indiCourseEntity)
    }

    override suspend fun deleteAllCourseIndi() {
       courseDao.deleteAllCourseIndi()
    }

    override suspend fun getMyIndiCourses(token: String): IndividualGetModel {
        return logicUserApiService.getMyIndiCourses(token)
    }

    override suspend fun getIndiCourse(
        page: Int,
        query: String
    ): Response<List<IndividualCourseGetModel>> {
       return logicUserApiService.getIndiCourse(page, query)
    }

    override suspend fun buyLoadCourseIndi(buyIndividualCourseModel: BuyIndividualCourseModel): IndividualGetModel {
        return logicUserApiService.buyLoadCourseIndi(buyIndividualCourseModel)
    }

    override suspend fun buyCreatorSubscribe(buyCreatorSubscribeModel: BuyCreatorSubscribeModel): SendStatus {
        return logicUserApiService.buyCreatorSubscribe(buyCreatorSubscribeModel)
    }

    override suspend fun getAndCheckMyCreatorSubscribes(token: String): CreatorSubscribeGetModel {
        return logicUserApiService.getAndCheckMyCreatorSubscribes(token)
    }

    override suspend fun testCreateIndividualCourse(individualCreateModel: IndividualGetModel): SendStatus {
        return logicUserApiService.testCreateIndividualCourse(individualCreateModel)
    }

    override suspend fun testUpdateIndividualCourse(individualCreateModel: IndividualGetModel): SendStatus {
       return logicUserApiService.testUpdateIndividualCourse(individualCreateModel)
    }

}