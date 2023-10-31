package workwork.test.andropediagits.data.local.dao.interactive
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity

import workwork.test.andropediagits.data.local.entities.interactive.InteractiveEntity

@Dao
interface InteractiveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteractiveEntity(interactiveEntity: InteractiveEntity)

    @Query("SELECT * FROM interactiveEntityTable")
    suspend fun getAllInteractiveTasks():List<InteractiveEntity>
    //with test
    @Query("SELECT * FROM interactiveEntityTable WHERE taskId = :taskId")
    suspend fun searchInteractiveTaskWithTaskId(taskId:Int):InteractiveEntity
    //with test
    @Query("SELECT * FROM interactiveEntityTable WHERE interactiveTestId = :interactiveTestId")
    suspend fun searchInteractiveTasksWithInteractiveTestId(interactiveTestId:Int):List<InteractiveEntity>
    //with test
    @Query("SELECT * FROM interactiveEntityTable WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun searchInteractiveTaskWithUniqueThemeId(uniqueThemeId:Int):List<InteractiveEntity>

    @Delete
    suspend fun deleteInteractiveTask(interactiveEntity: InteractiveEntity)

    @Query("DELETE FROM interactiveEntityTable")
    suspend fun deleteAllInteractiveTasks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity)
    //with test
    @Query("SELECT * FROM interactiveCodeVariantEntityTable")
    suspend fun getAllInteractiveCodeVariants():List<InteractiveCodeVariantEntity>
    //with test
    @Query("SELECT * FROM interactiveCodeVariantEntityTable WHERE variantId = :variantId")
    suspend fun searchInteractiveCodeVariantWithVariantId(variantId:Int):InteractiveCodeVariantEntity
    //with test
    @Query("SELECT * FROM interactiveCodeVariantEntityTable WHERE taskId = :taskId")
    suspend fun searchInteractiveCodeVariantsWithTaskId(taskId:Int):List<InteractiveCodeVariantEntity>
    //with test
    @Query("SELECT * FROM interactiveCodeVariantEntityTable WHERE interactiveTestId = :interactiveTestId")
    suspend fun searchInteractiveCodeVariantsWithInteractiveTestId(interactiveTestId:Int):List<InteractiveCodeVariantEntity>

    @Query("DELETE FROM interactiveCodeVariantEntityTable")
    suspend fun deleteAllInteractiveCodeVariants()

    @Delete
    suspend fun deleteInteractiveCodeVariant(interactiveCodeVariantEntity: InteractiveCodeVariantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity)

    @Query("SELECT * FROM interactiveCorrectCodeEntityTable")
    suspend fun getAllInteractiveCorrectAnswers():List<InteractiveCorrectCodeEntity>
    //with test
    @Query("SELECT * FROM interactiveCorrectCodeEntityTable WHERE taskId = :taskId")
    suspend fun searchInteractiveTaskCorrectAnswerWithTaskId(taskId:Int):InteractiveCorrectCodeEntity
    //with test
    @Query("SELECT * FROM interactiveCorrectCodeEntityTable WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId:Int):List<InteractiveCorrectCodeEntity>

    @Query("DELETE FROM interactiveCorrectCodeEntityTable")
    suspend fun deleteAllInteractiveCorrectAnswers()

    @Delete
    suspend fun deleteInteractiveCorrectAnswer(interactiveCorrectCodeEntity: InteractiveCorrectCodeEntity)

}