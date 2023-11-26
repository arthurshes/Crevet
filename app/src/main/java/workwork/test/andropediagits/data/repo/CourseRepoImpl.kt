package workwork.test.andropediagits.data.repo

import android.util.Log
import androidx.lifecycle.LiveData

import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveEntity
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineClueEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.data.remote.LogicUserApiService
import workwork.test.andropediagits.data.remote.TimeApiService
import workwork.test.andropediagits.data.remote.model.TimeAnswerModel
import workwork.test.andropediagits.data.remote.model.all.AllAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseAnswerModel
import workwork.test.andropediagits.data.remote.model.course.search.CourseNumberSearchModel
import workwork.test.andropediagits.data.remote.model.course.search.LevelUniqueIdSearchModel
import workwork.test.andropediagits.data.remote.model.course.search.ThemesUniqueIdSearchModel
import workwork.test.andropediagits.data.remote.model.interactive.InteractiveAllCorrectCodeModel
import workwork.test.andropediagits.data.remote.model.interactive.InteractiveAnswerModel
import workwork.test.andropediagits.data.remote.model.interactive.sendModels.InteractiveTestResultSendModel
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
import workwork.test.andropediagits.domain.repo.CourseRepo
import javax.inject.Inject

class CourseRepoImpl @Inject constructor(val mainDb: MainDb, val logicUserApiService: LogicUserApiService, val timeApiService: TimeApiService):
    CourseRepo {

    private val courseDao = mainDb.getCourseDao()
    private val themeDao = mainDb.getLevelDao()
    private val levelDao = mainDb.getRealLevelDao()
    private val levelContentDao = mainDb.getLevelContentDao()
    private val victorineDao = mainDb.getVictorineDao()
    private val userDao = mainDb.getUserInfoDao()
    private val interactiveDao = mainDb.getInteractiveDao()
    private val updateKeysDao = mainDb.getUpdateKeysDao()
    override suspend fun getAllNotPassedThemesCourse(courseNumber: Int): List<ThemeEntity> {
        return themeDao.getAllNotPassedThemesCourse(courseNumber)
    }

    override suspend fun getAllFavoriteLessons(uniqueThemeId: Int): List<LevelEntity> {
        return levelDao.getAllFavoriteLessons(uniqueThemeId)
    }

    override suspend fun updateLesson(levelEntity: LevelEntity) {
        levelDao.updateLesson(levelEntity)
    }

    override suspend fun checkTermThemes(token: String): GetCheckUserTermModel {
        return logicUserApiService.checkTermThemes(token)
    }

    override suspend fun getAllFavoriteThemes(): List<ThemeEntity> {
        return themeDao.getAllFavoriteThemes()
    }

    override suspend fun searchVictorineForUniqueThemeId(uniqueThemeId: Int): VictorineEntity {
        return victorineDao.searchVictorineForUniqueThemeId(uniqueThemeId)
    }

    override suspend fun checkTermCurrentTheme(themeCheckTermModel: ThemeCheckTermModel): ThemeTermCheckResponse {
        return logicUserApiService.checkTermCurrentTheme(themeCheckTermModel)
    }

    override suspend fun deleteAllThemes() {
        themeDao.deleteAllThemes()
    }

    override suspend fun deleteAllCourse() {
        courseDao.deleteAllCourse()
    }

    override suspend fun deleteAllLevelsContent() {
        levelContentDao.deleteAllLevelsContent()
    }

    override suspend fun getCurrentTime(): TimeAnswerModel {
        return timeApiService.getCurrentTime()
    }

    override suspend fun insertCourse(courseEntity: CourseEntity) {
        courseDao.insertCourse(courseEntity)
    }

    override suspend fun updateCourse(courseEntity: CourseEntity) {
       courseDao.updateCourse(courseEntity)
    }

    override suspend fun deleteCourse(courseEntity: CourseEntity) {
        courseDao.deleteCourse(courseEntity)
    }

    override suspend fun getAllCourses(): List<CourseEntity> {
        return courseDao.getAllMyCourses()
    }

    override suspend fun insertTheme(themeEntity: ThemeEntity) {
       themeDao.insertTheme(themeEntity)
    }

    override suspend fun updateTheme(themeEntity: ThemeEntity) {
        themeDao.updateTheme(themeEntity)
    }

    override suspend fun deleteTheme(themeEntity: ThemeEntity) {
        themeDao.deleteTheme(themeEntity)
    }

    override suspend fun getAllThemes(): List<ThemeEntity> {
        return themeDao.getAllMyThemes()
    }

    override suspend fun searchCourseWithNumber(courseNumber: Int): CourseEntity {
        return courseDao.searchCourseWithNumber(courseNumber)
    }

    override suspend fun searchThemeWithUniwueId(uniqueId: Int): ThemeEntity {
        return themeDao.searchThemeWithUniqueId(uniqueId)
    }

    override suspend fun insertLevelsContents(list: ThemeLevelContentEntity) {
        levelContentDao.insertLevelsContent(list)
    }

    override suspend fun deleteAllLevelContents() {
      levelContentDao.getAllLevelsContent()
    }

    override suspend fun getAllLevelContents(): List<ThemeLevelContentEntity> {
        return levelContentDao.getAllLevelsContent()
    }

    override suspend fun getNextContentTestFun(
        courseNumber: Int,
        themeNumber: Int,
        levelNumber: Int
    ): ThemeLevelContentEntity {
        return levelContentDao.getNextContentTestFun(courseNumber, themeNumber, levelNumber)
    }

    override suspend fun searchLevelContetns(uniqueLevelId: Int): ThemeLevelContentEntity{
        return levelContentDao.searchLevelContents(uniqueLevelId)
    }

    override suspend fun searchOneLevelContent(uniqueLevelContentId: Int): ThemeLevelContentEntity {
        return levelContentDao.searchOneLevelContent(uniqueLevelContentId)
    }

    override suspend fun insertLevels(list: LevelEntity) {
       levelDao.insertLevels(list)
    }

    override suspend fun deleteAllLevels() {
       levelDao.deleteAllLevels()
    }

    override suspend fun getAllLevels(): List<LevelEntity> {
        return levelDao.getAllLevels()
    }

    override suspend fun searchOneLevel(uniqueLevelId: Int): LevelEntity {
        return levelDao.searchOneLevel(uniqueLevelId)
    }

    override suspend fun searchAllLevelsTheme(uniqueThemeId: Int):List<LevelEntity> {
        Log.d("ourseRepo","unqiueThemeID:${uniqueThemeId},value db:${levelDao.searchThemeLevels(uniqueThemeId).toString()}")
        return levelDao.searchThemeLevels(uniqueThemeId)
    }

    override suspend fun searchThemesWithCourseNumber(courseNumber: Int): List<ThemeEntity> {
        return themeDao.searchThemesWithCourseNumber(courseNumber)
    }

    override suspend fun insertVictorine(victorineEntity: VictorineEntity) {
       victorineDao.insertVictorine(victorineEntity)
    }

    override suspend fun deleteVictorine(victorineEntity: VictorineEntity) {
        victorineDao.deleteVictorine(victorineEntity)
    }

    override suspend fun getAllVictorines(): List<VictorineEntity> {
        return victorineDao.getAllVictorines()
    }

    override suspend fun searchVictorinesForVictorineTestId(vicotineTestId: Int): List<VictorineEntity> {
        return victorineDao.searchVictorinesForVictorineTestId(vicotineTestId)
    }

    override suspend fun searchVictorineForQuestionId(questionId: Int): VictorineEntity {
      return victorineDao.searchVictorineForQuestionId(questionId)
    }

    override suspend fun deleteAllVictorines() {
        victorineDao.deleteAllVictorines()
    }

    override suspend fun searchAllVictorinesWithUniqueThemeId(uniqueThemeId: Int): List<VictorineEntity> {
        return victorineDao.searchAllVictorinesWithUniqueThemeId(uniqueThemeId)
    }

    override suspend fun insertVictorineAnswerVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity) {
       victorineDao.insertVictorineAnswerVariant(victorineAnswerVariantEntity)
    }

    override suspend fun deleteVictorineVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity) {
        victorineDao.deleteVictorineVariant(victorineAnswerVariantEntity)
    }

    override suspend fun getAllVictorinesAnswerVariants(): List<VictorineAnswerVariantEntity> {
        return victorineDao.getAllVictorinesAnswerVariants()
    }

    override suspend fun searchVictorineAnswerVariantWithVictorineAnswerId(victorineAnswerId: Int): List<VictorineAnswerVariantEntity> {
        return victorineDao.searchVictorineAnswerVariantWithVictorineAnswerId(victorineAnswerId)
    }

    override suspend fun searchVictorineAnswerVariantsWithQuestionId(questionId: Int,vicotineTestId:Int): List<VictorineAnswerVariantEntity> {
       return victorineDao.searchVictorineAnswerVariantsWithQuestionId(questionId,vicotineTestId)
    }

    override suspend fun deleteAllVictorineVariants() {
       victorineDao.deleteAllVictorineVariants()
    }

    override suspend fun insertInteractiveEntity(interactiveEntity: InteractiveEntity) {
        interactiveDao.insertInteractiveEntity(interactiveEntity)
    }

    override suspend fun getAllInteractiveTasks(): List<InteractiveEntity> {
        return interactiveDao.getAllInteractiveTasks()
    }

    override suspend fun searchInteractiveTaskWithTaskId(taskId: Int): InteractiveEntity {
        return interactiveDao.searchInteractiveTaskWithTaskId(taskId)
    }

    override suspend fun searchInteractiveTasksWithInteractiveTestId(interactiveTestId: Int): List<InteractiveEntity> {
        return interactiveDao.searchInteractiveTasksWithInteractiveTestId(interactiveTestId)
    }

    override suspend fun searchInteractiveTaskWithUniqueThemeId(uniqueThemeId: Int): List<InteractiveEntity> {
       return interactiveDao.searchInteractiveTaskWithUniqueThemeId(uniqueThemeId)
    }

    override suspend fun deleteInteractiveTask(interactiveEntity: InteractiveEntity) {
        interactiveDao.deleteInteractiveTask(interactiveEntity)
    }

    override suspend fun deleteAllInteractiveTasks() {
        interactiveDao.deleteAllInteractiveTasks()
    }

    override suspend fun insertInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity) {
        interactiveDao.insertInteractiveCodeVariant(interactiveCodeVariantEntity)
    }

    override suspend fun getAllInteractiveCodeVariants(): List<InteractiveCodeVariantEntity> {
       return interactiveDao.getAllInteractiveCodeVariants()
    }

    override suspend fun searchInteractiveCodeVariantWithVariantId(variantId: Int): InteractiveCodeVariantEntity {
        return interactiveDao.searchInteractiveCodeVariantWithVariantId(variantId)
    }

    override suspend fun searchInteractiveCodeVariantsWithTaskId(taskId: Int): List<InteractiveCodeVariantEntity> {
        return interactiveDao.searchInteractiveCodeVariantsWithTaskId(taskId)
    }

    override suspend fun searchInteractiveCodeVariantsWithInteractiveTestId(interactiveTestId: Int): List<InteractiveCodeVariantEntity> {
        return interactiveDao.searchInteractiveCodeVariantsWithInteractiveTestId(interactiveTestId)
    }

    override suspend fun deleteAllInteractiveCodeVariants() {
        interactiveDao.deleteAllInteractiveCodeVariants()
    }

    override suspend fun deleteInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity) {
        interactiveDao.deleteInteractiveCodeVariant(interactiveCodeVariantEntity)
    }

    override suspend fun insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity) {
      interactiveDao.insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity)
    }

    override suspend fun getAllInteractiveCorrectAnswers(): List<InteractiveCorrectCodeEntity> {
        return interactiveDao.getAllInteractiveCorrectAnswers()
    }

    override suspend fun searchInteractiveTaskCorrectAnswerWithTaskId(taskId: Int): InteractiveCorrectCodeEntity {
        return interactiveDao.searchInteractiveTaskCorrectAnswerWithTaskId(taskId)
    }

    override suspend fun searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId: Int): List<InteractiveCorrectCodeEntity> {
       return interactiveDao.searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId)
    }

    override suspend fun deleteAllInteractiveCorrectAnswers() {
       interactiveDao.deleteAllInteractiveCorrectAnswers()
    }

    override suspend fun deleteInteractiveCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity) {
       interactiveDao.deleteInteractiveCorrectAnswer(interactiveCorrectCodeEntity)
    }

    override suspend fun deleteAllLevelContentWithUniqueId(uniqueLevelId: Int) {
        levelContentDao.deleteAllLevelContentWithUniqueId(uniqueLevelId)
    }

    override suspend fun deleteAllLevelCourse(courseNumber: Int) {
        levelDao.deleteAllLevelCourse(courseNumber)
    }

    override suspend fun deleteAllThemesCourse(courseNumber: Int) {
       themeDao.deleteAllThemesCourse(courseNumber)
    }

    override suspend fun getCourseForNumber(courseNumberSearchModel: CourseNumberSearchModel): CourseAnswerModel {
        return logicUserApiService.getCourseForNumber(courseNumberSearchModel)
    }

    override suspend fun getLevelForUniqueId(levelUniqueIdSearchModel: LevelUniqueIdSearchModel): List<LevelThemeAnswerModel> {
        return logicUserApiService.getLevelForUniqueId(levelUniqueIdSearchModel)
    }

    override suspend fun getAllVictorine(): List<VictorineAnswerModel> {
        return logicUserApiService.getAllVictorine()
    }

    override suspend fun getAllInteractive(): List<InteractiveAnswerModel> {
        return logicUserApiService.getAllInteractive()
    }

    override suspend fun getAllCorrectAnswerInteractive(): InteractiveAllCorrectCodeModel {
        return logicUserApiService.getAllCorrectAnswerInteractive()
    }

    override suspend fun sendMyVictorineProgress(victorineTestResultSendModel: VictorineTestResultSendModel) {
        logicUserApiService.sendMyVictorineProgress(victorineTestResultSendModel)
    }

    override suspend fun sendMyInteractiveProgress(interactiveTestResultSendModel: InteractiveTestResultSendModel) {
       logicUserApiService.sendMyInteractiveProgress(interactiveTestResultSendModel)
    }

    override suspend fun deletAllLevelTheme(uniqueThemeId: Int) {
        levelDao.deletAllLevelTheme(uniqueThemeId)
    }

    override suspend fun getAllCoursesAndThemesUpdateDate(): LastUpdateDateModel {
        return logicUserApiService.getAllCoursesAndThemesUpdateDate()
    }

    override suspend fun getUserInfo(): UserInfoEntity {
        return userDao.getUserInfo()
    }

    override suspend fun deleteAllLevelContentWithCourseNumber(courseNumber: Int) {
        levelContentDao.deleteAllLevelContentWithCourseNumber(courseNumber)
    }

    override suspend fun getThemesForUniqueId(themesUniqueIdSearchModel: ThemesUniqueIdSearchModel): ThemeAnswerModel {
        return logicUserApiService.getThemesForUniqueId(themesUniqueIdSearchModel)
    }

    override suspend fun deleteAllThemeLevelContent(uniqueThemeId: Int) {
        levelContentDao.deleteAllThemeLevelContent(uniqueThemeId)
    }

    override suspend fun insertKey(updatesKeyEntity: UpdatesKeyEntity) {
        updateKeysDao.insertKey(updatesKeyEntity)
    }

    override suspend fun getAllUpdatesKeys(): List<UpdatesKeyEntity> {
        return updateKeysDao.getAllUpdatesKeys()
    }

    override suspend fun deleteUpdateKey(updatesKeyEntity: UpdatesKeyEntity) {
        updateKeysDao.deleteUpdateKey(updatesKeyEntity)
    }

    override suspend fun updateUpdateKey(updatesKeyEntity: UpdatesKeyEntity) {
        updateKeysDao.updateUpdateKey(updatesKeyEntity)
    }

    override suspend fun getUpdateKeyWithCourseNumber(courseNumber: Int): UpdatesKeyEntity {
       return updateKeysDao.getUpdateKeyWithCourseNumber(courseNumber)
    }

    override suspend fun getUpdateKeyWithUniqueThemeId(uniqueThemeId: Int): UpdatesKeyEntity {
        return updateKeysDao.getUpdateKeyWithUniqueThemeId(uniqueThemeId)
    }

    override suspend fun getUpdateKeyWithUniqueLevelId(uniqueLevelId: Int): UpdatesKeyEntity {
        return updateKeysDao.getUpdateKeyWithUniqueLevelId(uniqueLevelId)
    }

    override suspend fun getUpdateKeyWithInteractiveId(interactiveTestId: Int): List<UpdatesKeyEntity> {
       return updateKeysDao.getUpdateKeyWithInteractiveId(interactiveTestId)
    }

    override suspend fun getUpdateKeyWithVictorineId(vicotineTestId: Int): List<UpdatesKeyEntity> {
       return updateKeysDao.getUpdateKeyWithVictorineId(vicotineTestId)
    }

    override suspend fun sendMyProgress(updateAnswerModel: UpdateAnswerModel): TermAnswerModel {
        return logicUserApiService.sendMyProgress(updateAnswerModel)
    }

    override suspend fun searchThemeWithInteractiveId(interactiveTestId: Int): ThemeEntity {
        return themeDao.searchThemeWithInteractiveId(interactiveTestId)
    }

    override suspend fun searchThemeWithVictorineTestId(vicotineTestId: Int): ThemeEntity {
        return themeDao.searchThemeWithVictorineTestId(vicotineTestId)
    }

    override suspend fun insertVictorineClue(victorineClueEntity: VictorineClueEntity) {
        victorineDao.insertVictorineClue(victorineClueEntity)
    }

    override suspend fun getVictorineClue( questionId: Int,victorineTestId:Int): VictorineClueEntity {
       return victorineDao.getVictorineClue(questionId,victorineTestId)
    }

    override suspend fun deleteAllVictorineClue() {
        victorineDao.deleteAllVictorineClue()
    }


    ///19 august update
//    override suspend fun getAllCourseUpdateLang(
//        token: String,
//        language: String
//    ): AllAnswerModel {
//        return logicUserApiService.getAllCourseUpdateLang(token, language)
//    }

    override suspend fun getAllBackendCourse(token:String,userLanguage:String): AllAnswerModel {
        return logicUserApiService.getAllCourses(token,userLanguage)
    }

    override suspend fun getAllBackendThemes(token: String,userLanguage:String): List<ThemeAnswerModel> {
        return logicUserApiService.getAllThemes(token,userLanguage)
    }

    override suspend fun getAllBackendLevels(token: String,userLanguage:String): List<LevelThemeAnswerModel> {
        return logicUserApiService.getAllLevels(token,userLanguage)
    }

}