package workwork.example.andropediagits.domain.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.local.entities.course.CourseEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveEntity
import com.example.andropediagits.data.local.entities.levels.LevelEntity
import com.example.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import com.example.andropediagits.data.local.entities.theme.ThemeEntity
import com.example.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import com.example.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import com.example.andropediagits.data.local.entities.victorine.VictorineClueEntity
import com.example.andropediagits.data.local.entities.victorine.VictorineEntity
import com.example.andropediagits.data.remote.model.TimeAnswerModel
import com.example.andropediagits.data.remote.model.all.AllAnswerModel
import com.example.andropediagits.data.remote.model.course.CourseAnswerModel
import com.example.andropediagits.data.remote.model.course.search.CourseNumberSearchModel
import com.example.andropediagits.data.remote.model.course.search.LevelUniqueIdSearchModel
import com.example.andropediagits.data.remote.model.course.search.ThemesUniqueIdSearchModel
import com.example.andropediagits.data.remote.model.interactive.InteractiveAllCorrectCodeModel
import com.example.andropediagits.data.remote.model.interactive.InteractiveAnswerModel
import com.example.andropediagits.data.remote.model.interactive.sendModels.InteractiveTestResultSendModel
import com.example.andropediagits.data.remote.model.theme.LevelThemeAnswerModel
import com.example.andropediagits.data.remote.model.theme.TermAnswerModel
import com.example.andropediagits.data.remote.model.theme.ThemeAnswerModel
import com.example.andropediagits.data.remote.model.theme.ThemeTermCheckResponse
import com.example.andropediagits.data.remote.model.theme.sendModels.ThemeCheckTermModel
import com.example.andropediagits.data.remote.model.updateDates.LastUpdateDateModel
import com.example.andropediagits.data.remote.model.updateModel.UpdateAnswerModel
import com.example.andropediagits.data.remote.model.victorine.VictorineAnswerModel
import com.example.andropediagits.data.remote.model.victorine.sendModels.VictorineTestResultSendModel
import com.example.andropediagits.testModel.TestAllAnswer
import java.util.Date

class FakeCourseRepo() : CourseRepo {

    private val courses = mutableListOf<CourseEntity>()
    private val themes = mutableListOf<ThemeEntity>()
    private val levels = mutableListOf<LevelEntity>()
    private val levelContents = mutableListOf<ThemeLevelContentEntity>()
    private val victorines = mutableListOf<VictorineEntity>()
    private val victorineAnswerVariants = mutableListOf<VictorineAnswerVariantEntity>()
    private val interactiveTasks = mutableListOf<InteractiveEntity>()
    private val interactiveCodeVariants = mutableListOf<InteractiveCodeVariantEntity>()
    private val interactiveCorrectAnswers = mutableListOf<InteractiveCorrectCodeEntity>()
    private val victorineClues = mutableListOf<VictorineClueEntity>()
    private val updatesKeys = mutableListOf<UpdatesKeyEntity>()
    private val testTimeAnswerModel = TimeAnswerModel(
        abbreviation = "PST",
        client_ip = "192.168.0.1",
        datetime = "2023-07-29 12:34:56",
        day_of_week = 6,
        day_of_year = 210,
        dst = true,
        dst_from = "ew", // You should replace this with a meaningful value if possible
        dst_offset = 3600,
        dst_until = "rw", // You should replace this with a meaningful value if possible
        raw_offset = 28800,
        timezone = "ewe",
        unixtime = 1679810096,
        utc_datetime = "2023-07-29 19:34:56",
        utc_offset = "-07:00",
        week_number = 30
    )

    override suspend fun searchVictorineForUniqueThemeId(uniqueThemeId: Int): VictorineEntity {
        TODO("Not yet implemented")
    }


    // Implement the suspend functions
    override suspend fun checkTermCurrentTheme(themeCheckTermModel: ThemeCheckTermModel): ThemeTermCheckResponse {
        if(themeCheckTermModel.termHourse==0){
            return ThemeTermCheckResponse(remainingHours = 0,isEnding = true)
        }else{
            return ThemeTermCheckResponse(remainingHours = themeCheckTermModel.termHourse,isEnding = false)
        }
    }

    override suspend fun deleteAllThemes() {
        themes.clear()
    }

    override suspend fun deleteAllCourse() {
        courses.clear()
    }

    override suspend fun deleteAllLevelsContent() {
        levelContents.clear()
    }

    override suspend fun getCurrentTime(): TimeAnswerModel {
        return testTimeAnswerModel
    }

    override suspend fun insertCourse(courseEntity: CourseEntity) {
        courses.add(courseEntity)
    }

    override suspend fun updateCourse(courseEntity: CourseEntity) {
        val index = courses.indexOfFirst { it.courseNumber == courseEntity.courseNumber }
        if (index != -1) {
            courses[index] = courseEntity
        }
    }

    override suspend fun deleteCourse(courseEntity: CourseEntity) {
        courses.remove(courseEntity)
    }

    override suspend fun getAllCourses(): List<CourseEntity> {
        return courses
    }

    override suspend fun insertTheme(themeEntity: ThemeEntity) {
        themes.add(themeEntity)
    }

    override suspend fun updateTheme(themeEntity: ThemeEntity) {
        val index = themes.indexOfFirst { it.uniqueThemeId == themeEntity.uniqueThemeId }
        if (index != -1) {
            themes[index] = themeEntity
        }
    }

    override suspend fun deleteTheme(themeEntity: ThemeEntity) {
        themes.remove(themeEntity)
    }

    override suspend fun getAllThemes(): List<ThemeEntity> {
        return themes
    }

    override suspend fun searchCourseWithNumber(courseNumber: Int): CourseEntity {
        return courses.find { it.courseNumber == courseNumber }
            ?: throw IllegalArgumentException("Course with number $courseNumber not found.")
    }

    override suspend fun searchThemeWithUniwueId(uniqueId: Int): ThemeEntity {
        return themes.find { it.uniqueThemeId == uniqueId }!!
    }

    override suspend fun insertLevelsContents(list: ThemeLevelContentEntity) {
        levelContents.add(list)
    }

    override suspend fun deleteAllLevelContents() {
        levelContents.clear()
    }

    override suspend fun getAllLevelContents(): List<ThemeLevelContentEntity> {
        return levelContents
    }

    override suspend fun getNextContentTestFun(
        courseNumber: Int,
        themeNumber: Int,
        levelNumber: Int
    ): ThemeLevelContentEntity {
        TODO("Not yet implemented")
    }

    override suspend fun searchLevelContetns(uniqueLevelId: Int): ThemeLevelContentEntity {
        return levelContents.find { it.uniqueLevelId == uniqueLevelId }!!
    }

    override suspend fun searchOneLevelContent(uniqueLevelContentId: Int): ThemeLevelContentEntity {
        return levelContents.find { it.uniqueLevelContentId == uniqueLevelContentId }!!
    }

    override suspend fun insertLevels(levelEntity: LevelEntity) {
        levels.add(levelEntity)
    }

    override suspend fun deleteAllLevels() {
        levels.clear()
    }

    override suspend fun getAllLevels(): List<LevelEntity> {
        return levels
    }

    override suspend fun searchOneLevel(uniqueLevelId: Int): LevelEntity {
        return levels.find { it.uniqueLevelId == uniqueLevelId }!!
    }

    override suspend fun searchAllLevelsTheme(uniqueThemeId: Int): List<LevelEntity> {
        return levels.filter { it.uniqueThemeId == uniqueThemeId }
    }

    override suspend fun searchThemesWithCourseNumber(courseNumber: Int): List<ThemeEntity> {
        return themes.filter { it.courseNumber == courseNumber }
    }

    override suspend fun getAllBackendCourse(
        token: String,
        userLanguage: String
    ): AllAnswerModel {
        TODO()
    }

    override suspend fun getAllBackendThemes(
        token: String,
        userLanguage: String
    ): List<ThemeAnswerModel> {
        TODO()
    }

    override suspend fun getAllBackendLevels(
        token: String,
        userLanguage: String
    ): List<LevelThemeAnswerModel> {
        TODO()
    }

    override suspend fun insertVictorine(victorineEntity: VictorineEntity) {
        victorines.add(victorineEntity)
    }

    override suspend fun deleteVictorine(victorineEntity: VictorineEntity) {
        victorines.remove(victorineEntity)
    }

    override suspend fun getAllVictorines(): List<VictorineEntity> {
        return victorines
    }

    override suspend fun searchVictorinesForVictorineTestId(vicotineTestId: Int): List<VictorineEntity> {
        return victorines.filter { it.vicotineTestId == vicotineTestId }
    }

    override suspend fun searchVictorineForQuestionId(questionId: Int): VictorineEntity {
        return victorines.find { it.questionId == questionId }!!
    }

    override suspend fun deleteAllVictorines() {
        victorines.clear()
    }

    override suspend fun searchAllVictorinesWithUniqueThemeId(uniqueThemeId: Int): List<VictorineEntity> {
        return victorines.filter { it.uniqueThemeId == uniqueThemeId }
    }

    override suspend fun insertVictorineAnswerVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity) {
        victorineAnswerVariants.add(victorineAnswerVariantEntity)
    }

    override suspend fun deleteVictorineVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity) {
        victorineAnswerVariants.remove(victorineAnswerVariantEntity)
    }

    override suspend fun getAllVictorinesAnswerVariants(): List<VictorineAnswerVariantEntity> {
        return victorineAnswerVariants
    }

    override suspend fun searchVictorineAnswerVariantWithVictorineAnswerId(victorineAnswerId: Int): List<VictorineAnswerVariantEntity>{
        return victorineAnswerVariants.filter { it.victorineAnswerId == victorineAnswerId }
    }

    override suspend fun searchVictorineAnswerVariantsWithQuestionId(questionId: Int): List<VictorineAnswerVariantEntity> {
        return victorineAnswerVariants.filter { it.questionId == questionId }
    }

    override suspend fun deleteAllVictorineVariants() {
        victorineAnswerVariants.clear()
    }

    override suspend fun insertInteractiveEntity(interactiveEntity: InteractiveEntity) {
        interactiveTasks.add(interactiveEntity)
    }

    override suspend fun getAllInteractiveTasks(): List<InteractiveEntity> {
        return interactiveTasks
    }

    override suspend fun searchInteractiveTaskWithTaskId(taskId: Int): InteractiveEntity {
        return interactiveTasks.find { it.taskId == taskId }!!
    }

    override suspend fun searchInteractiveTasksWithInteractiveTestId(interactiveTestId: Int): List<InteractiveEntity> {
        return interactiveTasks.filter { it.interactiveTestId == interactiveTestId }
    }

    override suspend fun searchInteractiveTaskWithUniqueThemeId(uniqueThemeId: Int): List<InteractiveEntity> {
        return interactiveTasks.filter { it.uniqueThemeId == uniqueThemeId }
    }

    override suspend fun deleteInteractiveTask(interactiveEntity: InteractiveEntity) {
        interactiveTasks.remove(interactiveEntity)
    }


    override suspend fun deleteAllInteractiveTasks() {
        interactiveTasks.clear()
    }

    override suspend fun insertInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity) {
        interactiveCodeVariants.add(interactiveCodeVariantEntity)
    }


    override suspend fun getAllInteractiveCodeVariants(): List<InteractiveCodeVariantEntity> {
        return interactiveCodeVariants
    }


    override suspend fun searchInteractiveCodeVariantWithVariantId(variantId: Int): InteractiveCodeVariantEntity {
        return interactiveCodeVariants.find { it.variantId == variantId }!!
    }

    override suspend fun searchInteractiveCodeVariantsWithTaskId(taskId: Int): List<InteractiveCodeVariantEntity> {


        return interactiveCodeVariants.filter { it.taskId == taskId }
    }

    override suspend fun searchInteractiveCodeVariantsWithInteractiveTestId(interactiveTestId: Int): List<InteractiveCodeVariantEntity> {
        return interactiveCodeVariants.filter { it.interactiveTestId == interactiveTestId }
    }

    override suspend fun deleteAllInteractiveCodeVariants() {
        interactiveCodeVariants.clear()
    }


    override suspend fun deleteInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity) {
        interactiveCodeVariants.remove(interactiveCodeVariantEntity)
    }

    override suspend fun insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity) {
        interactiveCorrectAnswers.add(interactiveCorrectCodeEntity)
    }


    override suspend fun getAllInteractiveCorrectAnswers(): List<InteractiveCorrectCodeEntity> {
        return interactiveCorrectAnswers
    }


    override suspend fun searchInteractiveTaskCorrectAnswerWithTaskId(taskId: Int):InteractiveCorrectCodeEntity {
        return interactiveCorrectAnswers.find { it.taskId == taskId }!!
    }

    override suspend fun searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId: Int): List<InteractiveCorrectCodeEntity> {
        return interactiveCorrectAnswers.filter { it.uniqueThemeId == uniqueThemeId }
    }


    override suspend fun deleteAllInteractiveCorrectAnswers() {
        interactiveCorrectAnswers.clear()
    }

    override suspend fun deleteInteractiveCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity) {
        interactiveCorrectAnswers.remove(interactiveCorrectCodeEntity)
    }

    override suspend fun deleteAllLevelContentWithUniqueId(uniqueLevelId: Int) {
        levelContents.removeAll { it.uniqueLevelId == uniqueLevelId }
    }


    override suspend fun deleteAllLevelCourse(courseNumber: Int) {
        levels.removeAll { it.courseNumber == courseNumber }
    }

    override suspend fun deleteAllThemesCourse(courseNumber: Int) {
        themes.removeAll { it.courseNumber == courseNumber }
    }


    override suspend fun getCourseForNumber(courseNumberSearchModel: CourseNumberSearchModel): CourseAnswerModel {
        TODO()
    }


    override suspend fun getLevelForUniqueId(levelUniqueIdSearchModel: LevelUniqueIdSearchModel): List<LevelThemeAnswerModel> {
        TODO()
    }

    override suspend fun getAllVictorine(): List<VictorineAnswerModel> {
        TODO()
    }

    override suspend fun getAllInteractive(): List<InteractiveAnswerModel> {
        TODO()
    }


    override suspend fun getAllCorrectAnswerInteractive(): InteractiveAllCorrectCodeModel {
        TODO()
    }

    override suspend fun sendMyVictorineProgress(victorineTestResultSendModel: VictorineTestResultSendModel) {
        TODO()
    }

    override suspend fun sendMyInteractiveProgress(interactiveTestResultSendModel: InteractiveTestResultSendModel) {
        TODO()
    }


    override suspend fun deletAllLevelTheme(uniqueThemeId: Int) {
        levels.removeAll { it.uniqueThemeId == uniqueThemeId }
    }

    override suspend fun getAllCoursesAndThemesUpdateDate(): LastUpdateDateModel {
        TODO()
    }

    override suspend fun getUserInfo(): UserInfoEntity {
        TODO()
    }

    override suspend fun deleteAllLevelContentWithCourseNumber(courseNumber: Int) {
        levelContents.removeAll { it.courseNumber == courseNumber }
    }

    override suspend fun getThemesForUniqueId(themesUniqueIdSearchModel: ThemesUniqueIdSearchModel): ThemeAnswerModel {
        TODO()
    }

    override suspend fun deleteAllThemeLevelContent(uniqueThemeId: Int) {
        levelContents.removeAll { it.uniqueThemeId == uniqueThemeId }
    }

    override suspend fun insertKey(updatesKeyEntity: UpdatesKeyEntity) {
        updatesKeys.add(updatesKeyEntity)
    }


    override suspend fun getAllUpdatesKeys(): List<UpdatesKeyEntity> {
        return updatesKeys
    }


    override suspend fun deleteUpdateKey(updatesKeyEntity: UpdatesKeyEntity) {
        updatesKeys.remove(updatesKeyEntity)
    }


    override suspend fun updateUpdateKey(updatesKeyEntity: UpdatesKeyEntity) {
        val index = updatesKeys.indexOfFirst { it.courseNumber == updatesKeyEntity.courseNumber }


        if (index != -1) {
            updatesKeys[index] = updatesKeyEntity
        }
    }

    override suspend fun getUpdateKeyWithCourseNumber(courseNumber: Int): UpdatesKeyEntity {
        return updatesKeys.find { it.courseNumber == courseNumber }!!
    }

    override suspend fun getUpdateKeyWithUniqueThemeId(uniqueThemeId: Int): UpdatesKeyEntity {
        return updatesKeys.find { it.uniqueThemeId == uniqueThemeId }!!
    }

    override suspend fun getUpdateKeyWithUniqueLevelId(uniqueLevelId: Int): UpdatesKeyEntity {
        return updatesKeys.find { it.uniqueLevelId == uniqueLevelId }!!
    }

    override suspend fun getUpdateKeyWithInteractiveId(interactiveTestId: Int): List<UpdatesKeyEntity> {
        return updatesKeys.filter { it.interactiveTestId == interactiveTestId }
    }

    override suspend fun getUpdateKeyWithVictorineId(vicotineTestId: Int):List<UpdatesKeyEntity> {
        return updatesKeys.filter { it.vicotineTestId == vicotineTestId }
    }

    override suspend fun sendMyProgress(updateAnswerModel: UpdateAnswerModel): TermAnswerModel {
       return TermAnswerModel("hi","hello",false)
           //TODO()
    }


    override suspend fun searchThemeWithInteractiveId(interactiveTestId: Int): ThemeEntity {
        return themes.find { it.interactiveTestId == interactiveTestId }!!
    }

    override suspend fun searchThemeWithVictorineTestId(vicotineTestId: Int): ThemeEntity {
        return themes.find { it.vicotineTestId == vicotineTestId }!!
    }

    override suspend fun insertVictorineClue(victorineClueEntity: VictorineClueEntity) {
        victorineClues.add(victorineClueEntity)
    }


    override suspend fun getVictorineClue(victorineAnswerId: Int, questionId: Int): VictorineClueEntity {
        return victorineClues.find { it.victorineAnswerId == victorineAnswerId && it.questionId == questionId }
            ?: throw IllegalArgumentException("Victorine clue not found.")
    }


    override suspend fun deleteAllVictorineClue() {
        victorineClues.clear()
    }

    override suspend fun TestgetAllCourses(token: String, language: String): TestAllAnswer {
        TODO("Not yet implemented")
    }
}