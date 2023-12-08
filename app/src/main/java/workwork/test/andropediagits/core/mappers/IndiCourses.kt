package workwork.test.andropediagits.core.mappers

import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualCourseGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualLessonContentGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualLessonGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualThemeGetModel

fun IndividualLessonGetModel.toIndiLessonEntity(): IndiLessonEntity {
    return IndiLessonEntity(
        uniqueCourseNumber = uniqueCourseNumber,
        themeName = themeName,
        themeNumber = themeNumber,
        lessonName = lessonName,
        lessonNumber = lessonNumber,
        createrToken = createrToken
    )
}


fun IndividualLessonContentGetModel.toIndiLessonContentEntity(): IndiLessonContentEntity {
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

fun IndividualThemeGetModel.toIndiThemeEntity(): IndiThemeEntity {
    return IndiThemeEntity(
        createrToken = createrToken,
        uniqueCourseNumber = uniqueCourseNumber,
        themeNumber = themeNumber,
        themeName = themeName,
        themeImage = themeImage
    )
}


fun IndividualCourseGetModel.toIndiCourseEntity(): IndiCourseEntity {
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