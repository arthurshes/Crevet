package workwork.test.andropediagits.core.mappers


import workwork.test.andropediagits.data.local.entities.theme.ThemeBuyEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.data.remote.model.theme.ThemeAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeBuyModel
import java.text.SimpleDateFormat
import java.util.Date

fun ThemeAnswerModel.toThemeEntity(): ThemeEntity {
    val isOpens = isOpen == 1
    val isFullyPaids = isFullyPaid ==1
    val lastThemeCourseaaa = lastThemeCourse == 1
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date = dateFormat.parse(lastUpdateDate)
    return ThemeEntity(
        themeName = themeName,
        themeNumber = themeNumber,
        courseNumber = courseNumber,
        lastUpdateDate = date,
        uniqueThemeId = uniqueThemeId,
        isOpen = isOpens,
        vicotineTestId = vicotineTestId,
        interactiveTestId = interactiveTestId,
        isFav = false, //// Тестовое пока лень переделывать
        lessonsCount = lessonsCount,
        imageTheme = imageTheme,
        victorineQuestionCount = victorineQuestionCount,
        interactiveQuestionCount = interactiveQuestionCount,
        termDateApi = termDateApi,
        termHourse = termHourse,
        lastCourseTheme = lastThemeCourseaaa,
        possibleToOpenThemeFree = isFullyPaids,
        themePrice = themePrice,
        andropointsPrice = andropointsPrice
    )
}

fun ThemeBuyModel.toThemeBuyEntity(): ThemeBuyEntity {
    return ThemeBuyEntity(
        transactionId = token+courseNumber.toString()+uniqueThemeId.toString()+dateBuyApi,
        uniqueThemeId = uniqueThemeId,
        themeNumber = themeNumber,
        courseNumber = courseNumber,
        token = token,
        date = dateBuyForDate ?: Date()
    )
}


