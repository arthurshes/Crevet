package workwork.test.andropediagits.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

interface CourseRepo {



    suspend fun getAllNotPassedThemesCourse(courseNumber:Int):List<ThemeEntity>

    suspend fun getAllFavoriteLessons(uniqueThemeId: Int):List<LevelEntity>

    suspend fun updateLesson(levelEntity: LevelEntity)

    suspend fun checkTermThemes(token: String): GetCheckUserTermModel


    suspend fun getAllFavoriteThemes():List<ThemeEntity>

    suspend fun searchVictorineForUniqueThemeId(uniqueThemeId: Int): VictorineEntity

    suspend fun checkTermCurrentTheme(themeCheckTermModel: ThemeCheckTermModel): ThemeTermCheckResponse

    suspend fun deleteAllThemes()

    suspend fun deleteAllCourse()

    suspend fun deleteAllLevelsContent()

    suspend fun getCurrentTime(): TimeAnswerModel

    suspend fun insertCourse(courseEntity: CourseEntity)
    suspend fun updateCourse(courseEntity: CourseEntity)
    suspend fun deleteCourse(courseEntity: CourseEntity)
    suspend fun getAllCourses():List<CourseEntity>
    suspend fun insertTheme(themeEntity: ThemeEntity)
    suspend fun updateTheme(themeEntity: ThemeEntity)
    suspend fun deleteTheme(themeEntity: ThemeEntity)
    suspend fun getAllThemes():List<ThemeEntity>

    suspend fun searchCourseWithNumber(courseNumber:Int): CourseEntity
    suspend fun searchThemeWithUniwueId(uniqueId:Int):ThemeEntity

    suspend fun insertLevelsContents(list: ThemeLevelContentEntity)

     suspend fun deleteAllLevelContents()

     suspend fun getAllLevelContents():List<ThemeLevelContentEntity>

    suspend fun getNextContentTestFun(courseNumber:Int,themeNumber:Int,levelNumber:Int):ThemeLevelContentEntity

    suspend fun searchLevelContetns(uniqueLevelId:Int):ThemeLevelContentEntity

    suspend fun searchOneLevelContent(uniqueLevelContentId:Int):ThemeLevelContentEntity

    suspend fun insertLevels(levelEntity: LevelEntity)

    suspend fun deleteAllLevels()

    suspend fun getAllLevels():List<LevelEntity>

    suspend fun searchOneLevel(uniqueLevelId:Int):LevelEntity

    suspend fun searchAllLevelsTheme(uniqueThemeId:Int):List<LevelEntity>

    suspend fun searchThemesWithCourseNumber(courseNumber:Int):List<ThemeEntity>

    suspend fun getAllBackendCourse(token:String,userLanguage:String): AllAnswerModel

    suspend fun getAllBackendThemes(token: String,userLanguage:String):List<ThemeAnswerModel>

    suspend fun getAllBackendLevels(token: String,userLanguage:String):List<LevelThemeAnswerModel>


    suspend fun insertVictorine(victorineEntity: VictorineEntity)


    suspend fun deleteVictorine(victorineEntity: VictorineEntity)


    suspend fun getAllVictorines():List<VictorineEntity>


    suspend fun searchVictorinesForVictorineTestId(vicotineTestId:Int):List<VictorineEntity>


    suspend fun searchVictorineForQuestionId(questionId:Int):VictorineEntity


    suspend fun deleteAllVictorines()


    suspend fun searchAllVictorinesWithUniqueThemeId(uniqueThemeId:Int):List<VictorineEntity>

    suspend fun insertVictorineAnswerVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity)


    suspend fun deleteVictorineVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity)


    suspend fun getAllVictorinesAnswerVariants():List<VictorineAnswerVariantEntity>

    suspend fun searchVictorineAnswerVariantWithVictorineAnswerId(victorineAnswerId:Int):List<VictorineAnswerVariantEntity>

   suspend fun searchVictorineAnswerVariantsWithQuestionId(questionId:Int):List<VictorineAnswerVariantEntity>

    suspend fun deleteAllVictorineVariants()


    suspend fun insertInteractiveEntity(interactiveEntity: InteractiveEntity)


    suspend fun getAllInteractiveTasks():List<InteractiveEntity>


    suspend fun searchInteractiveTaskWithTaskId(taskId:Int):InteractiveEntity


    suspend fun searchInteractiveTasksWithInteractiveTestId(interactiveTestId:Int):List<InteractiveEntity>


    suspend fun searchInteractiveTaskWithUniqueThemeId(uniqueThemeId:Int):List<InteractiveEntity>


    suspend fun deleteInteractiveTask(interactiveEntity: InteractiveEntity)


    suspend fun deleteAllInteractiveTasks()


    suspend fun insertInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity)


    suspend fun getAllInteractiveCodeVariants():List<InteractiveCodeVariantEntity>


    suspend fun searchInteractiveCodeVariantWithVariantId(variantId:Int):InteractiveCodeVariantEntity


    suspend fun searchInteractiveCodeVariantsWithTaskId(taskId:Int):List<InteractiveCodeVariantEntity>


    suspend fun searchInteractiveCodeVariantsWithInteractiveTestId(interactiveTestId:Int):List<InteractiveCodeVariantEntity>


    suspend fun deleteAllInteractiveCodeVariants()


    suspend fun deleteInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity)


    suspend fun insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity)


    suspend fun getAllInteractiveCorrectAnswers():List<InteractiveCorrectCodeEntity>


    suspend fun searchInteractiveTaskCorrectAnswerWithTaskId(taskId:Int):InteractiveCorrectCodeEntity


    suspend fun searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId:Int):List<InteractiveCorrectCodeEntity>


    suspend fun deleteAllInteractiveCorrectAnswers()


    suspend fun deleteInteractiveCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity)

    suspend fun deleteAllLevelContentWithUniqueId(uniqueLevelId:Int)

    suspend fun deleteAllLevelCourse(courseNumber:Int)

    suspend fun deleteAllThemesCourse(courseNumber:Int)


    suspend fun getCourseForNumber(
     courseNumberSearchModel: CourseNumberSearchModel
    ): CourseAnswerModel


    suspend fun getLevelForUniqueId(
        levelUniqueIdSearchModel: LevelUniqueIdSearchModel
    ):List<LevelThemeAnswerModel>


    suspend fun getAllVictorine():List<VictorineAnswerModel>


    suspend fun getAllInteractive():List<InteractiveAnswerModel>


    suspend fun getAllCorrectAnswerInteractive(): InteractiveAllCorrectCodeModel


    suspend fun sendMyVictorineProgress(victorineTestResultSendModel: VictorineTestResultSendModel)


    suspend fun sendMyInteractiveProgress(interactiveTestResultSendModel: InteractiveTestResultSendModel)

    suspend fun deletAllLevelTheme(uniqueThemeId:Int)

    suspend fun getAllCoursesAndThemesUpdateDate(): LastUpdateDateModel

   suspend fun getUserInfo(): UserInfoEntity

    suspend fun deleteAllLevelContentWithCourseNumber(courseNumber:Int)

    suspend fun getThemesForUniqueId(
       themesUniqueIdSearchModel: ThemesUniqueIdSearchModel
    ):ThemeAnswerModel

    suspend fun deleteAllThemeLevelContent(uniqueThemeId:Int)


    suspend fun insertKey(updatesKeyEntity: UpdatesKeyEntity)


    suspend fun getAllUpdatesKeys():List<UpdatesKeyEntity>


    suspend fun deleteUpdateKey(updatesKeyEntity: UpdatesKeyEntity)


    suspend fun updateUpdateKey(updatesKeyEntity: UpdatesKeyEntity)


    suspend fun getUpdateKeyWithCourseNumber(courseNumber:Int):UpdatesKeyEntity


    suspend fun getUpdateKeyWithUniqueThemeId(uniqueThemeId:Int):UpdatesKeyEntity


    suspend fun getUpdateKeyWithUniqueLevelId(uniqueLevelId:Int):UpdatesKeyEntity


    suspend fun getUpdateKeyWithInteractiveId(interactiveTestId:Int):List<UpdatesKeyEntity>


    suspend fun getUpdateKeyWithVictorineId(vicotineTestId:Int):List<UpdatesKeyEntity>

    suspend fun sendMyProgress(updateAnswerModel: UpdateAnswerModel): TermAnswerModel

    suspend fun searchThemeWithInteractiveId(interactiveTestId:Int):ThemeEntity

    suspend fun searchThemeWithVictorineTestId(vicotineTestId:Int):ThemeEntity

    suspend fun insertVictorineClue(victorineClueEntity: VictorineClueEntity)

    suspend fun getVictorineClue(victorineAnswerId:Int,questionId:Int):VictorineClueEntity

    suspend fun deleteAllVictorineClue()

//    suspend fun TestgetAllCourses(token: String, language:String): TestAllAnswer

    ///19 august update
//    suspend fun getAllCourseUpdateLang(token: String, language:String):AllAnswerModel

}