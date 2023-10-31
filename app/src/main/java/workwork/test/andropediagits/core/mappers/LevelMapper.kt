package workwork.test.andropediagits.core.mappers


import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.data.remote.model.theme.LevelThemeAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeLevelContentModel

fun LevelThemeAnswerModel.toLevelEntity(): LevelEntity {
//    val convertedLevelContent = levelContent.map {
//        ThemeLevelContentEntity(
//            contentIndexList = it.contentIndexList,
//            courseNumber = it.courseNumber,
//            themeNumber = it.themeNumber,
//            levelNumber = it.levelNumber,
//            uniqueLevelId = it.uniqueLevelId,
//            uniqueLevelContentId = it.uniqueLevelContentId,
//            uniqueThemeId = it.uniqueThemeId,
//            lastUpdateDate = it.lastUpdateDate,
//            imageFirst = it.imageFirst,
//            textTitle = it.textTitle,
//            textFirst = it.textFirst,
//            CodeFragmentFirst = it.CodeFragmentFirst,
//            imageSecond = it.imageSecond,
//            textSecond = it.textSecond,
//            CodeFragmentSecond = it.CodeFragmentSecond,
//            imageThird = it.imageThird,
//            textThird = it.textThird,
//            CodeFragmentThird = it.CodeFragmentThird,
//            imageFourth = it.imageFourth,
//            textFourth = it.textFourth,
//            CodeFragmentFourth = it.CodeFragmentFourth,
//            imageFifth = it.imageFifth,
//            textFifth = it.textFifth,
//            CodeFragmentFifth = it.CodeFragmentFifth,
//            imageSixth = it.imageSixth,
//            textSixth = it.textSixth,
//            CodeFragmentSixth = it.CodeFragmentSixth,
//            imageSeventh = it.imageSeventh,
//            textSeventh = it.textSeventh,
//            CodeFragmentSeventh = it.CodeFragmentSeventh,
//            imageEighth = it.imageEighth,
//            textEighth = it.textEighth,
//            CodeFragmentEighth = it.CodeFragmentEighth,
//            imageNinth = it.imageNinth,
//            textNinth = it.textNinth,
//            CodeFragmentNinth = it.CodeFragmentNinth,
//            imageTenth = it.imageTenth,
//            textTenth = it.textTenth,
//            CodeFragmentTenth = it.CodeFragmentTenth
//
//        )
//    }

    return LevelEntity(
        courseNumber = courseNumber,
        levelName = levelName,
        levelNumber = levelNumber,
        themeNumber = themeNumber,
        uniqueLevelId = courseNumber+themeNumber+levelNumber,
        uniqueThemeId = uniqueThemeId,
        lastUpdateDate = lastUpdateDate
    )
}


fun ThemeLevelContentModel.toThemeLevelContentEntity(): ThemeLevelContentEntity {
    return ThemeLevelContentEntity(
        uniqueLevelContentId = uniqueLevelContentId,
        courseNumber = courseNumber,
        themeNumber = themeNumber,
        contentIndexList = contentIndexList,
        levelNumber = levelNumber,
        uniqueThemeId = uniqueThemeId,
        lastUpdateDate = lastUpdateDate,
        imageFirst = imageFirst,
        textTitle = textTitle,
        textFirst = textFirst,
        CodeFragmentFirst = CodeFragmentFirst,
        imageSecond = imageSecond,
        textSecond = textSecond,
        CodeFragmentSecond = CodeFragmentSecond,
        imageThird = imageThird,
        textThird = textFourth,
        CodeFragmentThird = CodeFragmentThird,
        imageFourth = imageFourth,
        textFourth = textFourth,
        CodeFragmentFourth = CodeFragmentFourth,
        imageFifth = imageFifth,
        textFifth = textFifth,
        CodeFragmentFifth = CodeFragmentFifth,
        imageSixth = imageSixth,
        textSixth = textSixth,
        CodeFragmentSixth = CodeFragmentSixth,
        imageSeventh = imageSeventh,
        textSeventh = textSeventh,
        CodeFragmentSeventh = CodeFragmentSeventh,
        imageEighth = imageEighth,
        textEighth = textEighth,
        CodeFragmentEighth = CodeFragmentEighth,
        imageNinth = imageNinth,
        textNinth = textNinth,
        CodeFragmentNinth = CodeFragmentNinth,
        imageTenth = imageTenth,
        textTenth = textTenth,
        CodeFragmentTenth = CodeFragmentTenth,
        uniqueLevelId=uniqueLevelId
    )
}

