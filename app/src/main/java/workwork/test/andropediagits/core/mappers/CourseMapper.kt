package workwork.test.andropediagits.core.mappers


import android.annotation.SuppressLint
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity
import workwork.test.andropediagits.data.remote.model.course.CourseAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualCourseGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualLessonContentGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualLessonGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualThemeGetModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
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

@SuppressLint("SimpleDateFormat")
fun CourseBuyModel.toCourseBuyEntity(): CourseBuyEntity {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    val date: Date = try {
        dateFormat.parse(dateBuy)
    } catch (e: ParseException) {
        // Handle parsing exception
        Date()
    }
    val androBuy = andropointBuy == 1
    return CourseBuyEntity(
        token = token,
        transactionId = transactionId,
        date = date,
        courseNumber = courseNumber,
        andropointBuy = androBuy
    )



//    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//    val date = dateFormat.parse(dateBuy)
//    val androBuy = andropointBuy == 1
//    return CourseBuyEntity(
//        token = token,
//        transactionId = transactionId,
//        date = date ?: Date(),
//        courseNumber = courseNumber,
//        andropointBuy = androBuy
//    )
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
