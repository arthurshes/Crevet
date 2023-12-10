package workwork.test.andropediagits.core.mappers


import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.UserSignInModel

fun UserSignInModel.toUserInfoLocalEntity(): UserInfoEntity {
    val isInfinitys = isInfinity == 1
    return UserInfoEntity(
        name = name ?: "DefaultName",
        token = token,
        image = image,
        lastOpenTheme = lastThemeNumber ?: 0,
        lastOpenCourse = lastCourseNumber ?: 0,
        userLanguage = userlanguage ?: "eng",
        andropointCount = andropointCount ?: 0,
        strikeModeDay = strikeModeDay ?: 0,
        isInfinity = isInfinitys,
        heartsCount = heartsCount
    )
}
