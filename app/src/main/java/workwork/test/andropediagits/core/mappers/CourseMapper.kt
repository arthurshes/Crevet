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

    fun IndividualLessonGetModel.toIndiLessonEntity():IndiLessonEntity{
        return IndiLessonEntity(
          uniqueCourseNumber = uniqueCourseNumber,
            themeName = themeName,
            themeNumber = themeNumber,
            lessonName = lessonName,
            lessonNumber = lessonNumber,
            createrToken = createrToken
        )
    }


    fun IndividualLessonContentGetModel.toIndiLessonContentEntity():IndiLessonContentEntity{
        return IndiLessonContentEntity(
            lessonNumber = lessonNumber,
            lessonName = lessonName,
            uniqueCourseNumber = uniqueCourseNumber,
            imageSecond = imageSecond,
            textSecond = textSecond,
            textFifth = textFifth,
            imageFifth = imageFifth,
            textFirst = textFirst,
            imageFirst = imageFirst,
            textEighth = textEighth,
            textNinth = textNinth,
            textSeventh = textSeventh,
            textSixth = textSixth,
            createrToken = createrToken,
            themeNumber = themeNumber,
            themeName = themeName,
            imageEighth = imageEighth,
            imageFourth = imageFourth,
            imageNinth = imageNinth,
            imageSeventh = imageSeventh,
            imageSixth = imageSixth,
            imageTenth = imageTenth,
            imageThird = imageThird,
            textFourth = textFourth,
            textTenth = textTenth,
            textThird = textThird,
            textTitle = textTitle,
            CodeFragmentFifth = CodeFragmentFifth,
            CodeFragmentEighth = CodeFragmentEighth,
            CodeFragmentFirst = CodeFragmentFirst,
            CodeFragmentFourth = CodeFragmentFourth,
            CodeFragmentNinth = CodeFragmentNinth,
            CodeFragmentSecond = CodeFragmentSecond,
            CodeFragmentSeventh = CodeFragmentSeventh,
            CodeFragmentSixth = CodeFragmentSixth,
            CodeFragmentTenth = CodeFragmentTenth,
            CodeFragmentThird = CodeFragmentThird
        )
    }

    fun IndividualThemeGetModel.toIndiThemeEntity():IndiThemeEntity{
        return IndiThemeEntity(
            createrToken = createrToken,
            uniqueCourseNumber = uniqueCourseNumber,
            themeNumber = themeNumber,
            themeName = themeName,
            themeImage = themeImage
        )
    }


    fun IndividualCourseGetModel.toIndiCourseEntity():IndiCourseEntity{
        return IndiCourseEntity(
            courseName = courseName,
            courseDescription = courseDescription,
            createrImage = createrImage,
            createrName = createrName,
            createrToken = createrToken,
            uniqueCourseNumber = uniqueCourseNumber,
            coursePrice = coursePrice,
            versionCourse = versionCourse,
            payRequisits = payRequisits
        )
    }

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
