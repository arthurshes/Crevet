package workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase

import android.util.Log
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.updateStates.UpdateLessonState
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.updateStates.UpdateThemeState

import java.util.Date
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(private val courseRepo: CourseRepo) {


    suspend fun updateLesson(uniqueLessonId:Int,updateLessonState: UpdateLessonState){
        val currentLesson = courseRepo.searchOneLevel(uniqueLessonId)
        when(updateLessonState){
            UpdateLessonState.THISLESSONFAVORITE -> {
                val updateLessonEntity = LevelEntity(
                    uniqueThemeId = currentLesson.uniqueThemeId,
                    uniqueLevelId = currentLesson.uniqueLevelId,
                    levelNumber = currentLesson.levelNumber,
                    themeNumber = currentLesson.themeNumber,
                    courseNumber = currentLesson.courseNumber,
                    lastUpdateDate = Date(),
                    levelName = currentLesson.levelName,
                    isFav = true
                )
                courseRepo.updateLesson(updateLessonEntity)
            }
            UpdateLessonState.THISLESSONREMOVEFAVORITRE -> {
                val updateLessonEntity = LevelEntity(
                    uniqueThemeId = currentLesson.uniqueThemeId,
                    uniqueLevelId = currentLesson.uniqueLevelId,
                    levelNumber = currentLesson.levelNumber,
                    themeNumber = currentLesson.themeNumber,
                    courseNumber = currentLesson.courseNumber,
                    lastUpdateDate = Date(),
                    levelName = currentLesson.levelName,
                    isFav = false
                )
                courseRepo.updateLesson(updateLessonEntity)
            }
        }
    }

    suspend fun updateTheme(uniqueThemeId:Int,updateThemeState: UpdateThemeState){
        val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        Log.d("jsonWalterWhite","victorineCorrectAnswers:${currentTheme.victorineCorrectAnswer},victorineMisstake:${currentTheme.victorineMistakeAnswer}")
        currentTheme?.let { theme ->
            when (updateThemeState) {
                UpdateThemeState.ISDUOMISTAKE -> {
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes?.plus(1),
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = true,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = theme.isFav,
                        isThemePassed = theme.isThemePassed,
                        themePrice =theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }

                UpdateThemeState.ISDUOCORRECT -> {
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect?.plus(1),
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = true,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = theme.isFav,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }

                UpdateThemeState.ISVICTORINECORRECT -> {
                    Log.d("java.lang.IndexOutOfBoundsException: Index: 2, Size: 2","ISVICTORINECORRECT")
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer?.plus(1),
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = true,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                      isFav = theme.isFav,
                    isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }

                UpdateThemeState.ISVICTORINEMISTAKE -> {
                    Log.d("java.lang.IndexOutOfBoundsException: Index: 2, Size: 2","ISVICTORINEMISTAKE")
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer?.plus(1),
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = true,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = theme.isFav,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }

                UpdateThemeState.RESETVICTORINEDATA -> {
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = 0,
                        victorineCorrectAnswer = 0,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = theme.isFav,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }
                UpdateThemeState.RESETDUODATA -> {
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes =0,
                        interactiveCodeCorrect = 0,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = theme.isFav,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }
                UpdateThemeState.THISTHEMEFAVORITE ->{
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes =theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = true,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }
                UpdateThemeState.THISTHEMEREMOVEFAV ->{
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes =theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = theme.termHourse,
                        lessonsCount = theme.lessonsCount,
                        imageTheme = theme.imageTheme,
                        termDateApi = theme.termDateApi,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = false,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }
            }
        }
    }

    suspend fun updateVictorine(uniqueThemeId: Int,misstakeCount:Int,correctCount:Int){
        val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        currentTheme?.let { theme ->
            val updateTheme = ThemeEntity(
                uniqueThemeId = theme.uniqueThemeId,
                lastUpdateDate = Date(),
                themeName = theme.themeName,
                courseNumber = theme.courseNumber,
                themeNumber = theme.themeNumber,
                interactiveCodeMistakes = theme.interactiveCodeMistakes,
                interactiveCodeCorrect = theme.interactiveCodeCorrect,
                victorineMistakeAnswer = misstakeCount,
                victorineCorrectAnswer = correctCount,
                victorineDate = theme.victorineDate,
                interactiveTestId = theme.interactiveTestId,
                interactiveQuestionCount = theme.interactiveQuestionCount,
                victorineQuestionCount = theme.victorineQuestionCount,
                vicotineTestId = theme.vicotineTestId,
                duoDate = theme.duoDate,
                isDuoInter = theme.isDuoInter,
                isVictorine = true,
                isOpen = theme.isOpen,
                termHourse = theme.termHourse,
                lessonsCount = theme.lessonsCount,
                imageTheme = theme.imageTheme,
                termDateApi = theme.termDateApi,
                lastCourseTheme = theme.lastCourseTheme,
                possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                themePrice = theme.themePrice,
                isThemePassed = theme.isThemePassed
            )
            courseRepo.updateTheme(updateTheme)
        }
    }

    suspend fun updateInteractive(uniqueThemeId: Int,misstakeCount: Int,correctCount: Int){
        val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        currentTheme?.let { theme ->
            val updateTheme = ThemeEntity(
                uniqueThemeId = theme.uniqueThemeId,
                lastUpdateDate = Date(),
                themeName = theme.themeName,
                courseNumber = theme.courseNumber,
                themeNumber = theme.themeNumber,
                interactiveCodeMistakes = misstakeCount,
                interactiveCodeCorrect = correctCount,
                victorineMistakeAnswer = theme.victorineMistakeAnswer,
                victorineCorrectAnswer = theme.victorineCorrectAnswer,
                victorineDate = theme.victorineDate,
                interactiveTestId = theme.interactiveTestId,
                interactiveQuestionCount = theme.interactiveQuestionCount,
                victorineQuestionCount = theme.victorineQuestionCount,
                vicotineTestId = theme.vicotineTestId,
                duoDate = theme.duoDate,
                isDuoInter = true,
                isVictorine = theme.isVictorine,
                isOpen = theme.isOpen,
                termHourse = theme.termHourse,
                lessonsCount = theme.lessonsCount,
                imageTheme = theme.imageTheme,
                termDateApi = theme.termDateApi,
                lastCourseTheme = theme.lastCourseTheme,
                possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                themePrice = theme.themePrice
            )
            courseRepo.updateTheme(updateTheme)
        }
    }


}