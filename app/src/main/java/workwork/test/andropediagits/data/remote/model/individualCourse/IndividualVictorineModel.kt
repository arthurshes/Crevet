package workwork.test.andropediagits.data.remote.model.individualCourse

import workwork.test.andropediagits.data.remote.model.individualCourse.victorine.IndividualVictorineAnswerVarGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.victorine.IndividualVictorineClueGetModel
import workwork.test.andropediagits.data.remote.model.individualCourse.victorine.IndividualVictorineQuestionGetModel

data class IndividualVictorineModel(
    val victorineQuestions:List<IndividualVictorineQuestionGetModel>,
    val victorineAnswerVariants:List<IndividualVictorineAnswerVarGetModel>,
    val victorineClues:List<IndividualVictorineClueGetModel>?=null
)
