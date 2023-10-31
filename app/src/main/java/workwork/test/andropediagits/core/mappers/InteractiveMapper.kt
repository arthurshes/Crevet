package workwork.test.andropediagits.core.mappers

import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveEntity
import workwork.test.andropediagits.data.remote.model.interactive.InterActiveCodeVariantModel
import workwork.test.andropediagits.data.remote.model.interactive.InteractiveAnswerModel
import workwork.test.andropediagits.data.remote.model.interactive.InteractiveCorrectCodeModel


fun InteractiveAnswerModel.toInteractiveEntity(): InteractiveEntity {
    return InteractiveEntity(
        taskId, interactiveTestId, uniqueThemeId, taskDetailsText, lastUpdateDate
    )
}

fun InteractiveCorrectCodeModel.toInteractiveCorrectCodeEntity(): InteractiveCorrectCodeEntity {
    return InteractiveCorrectCodeEntity(taskId, correctAnswer, interactiveTestId, uniqueThemeId)
}

fun InterActiveCodeVariantModel.toInteractiveCodeVariantEntity(): InteractiveCodeVariantEntity {
    return InteractiveCodeVariantEntity(variantId, interactiveTestId, taskId, text)
}

