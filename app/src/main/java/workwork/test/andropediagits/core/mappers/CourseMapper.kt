package workwork.test.andropediagits.core.mappers


import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.remote.model.course.CourseAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import java.text.SimpleDateFormat
import java.util.Date

fun CourseAnswerModel.toCourseEntity(): CourseEntity {
        val possibleToOpenCourseFrees = possibleToOpenCourseFree == 1
    val isClosess = isClosing == 1
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date = dateFormat.parse(lastUpdateDate)

    return CourseEntity(
        courseNumber = courseNumber,
        courseName = courseName,
        possibleToOpenCourseFree = possibleToOpenCourseFrees,
        isOpen = isClosess,
        lastUpdateDate = date,
        coursePriceAndropoint = coursePriceAndropoint,
        coursePriceRub = coursePriceRub,
        description = description
    )
}

fun CourseBuyModel.toCourseBuyEntity(): CourseBuyEntity {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date = dateFormat.parse(dateBuy)
    return CourseBuyEntity(
        token = token,
        transactionId = transactionId,
        date = date ?: Date(),
        courseNumber = courseNumber,
        andropointBuy = andropointBuy
    )
}



/*
fun CourseBuyModel.toCourseBuyEntity(): CourseBuyEntity {
    return CourseBuyEntity(
        transactionId = transactionId,
        courseNumber = courseNumber,
        token = token,
        date = dateBuy
    )
}*/
