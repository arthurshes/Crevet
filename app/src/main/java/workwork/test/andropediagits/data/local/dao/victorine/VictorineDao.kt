package workwork.test.andropediagits.data.local.dao.victorine

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineClueEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity

@Dao
interface VictorineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVictorineClue(victorineClueEntity: VictorineClueEntity)

    @Query("SELECT * FROM victorineClueEntity WHERE questionId = :questionId AND victorineAnswerId = :victorineAnswerId")
    suspend fun getVictorineClue(victorineAnswerId: Int, questionId: Int): VictorineClueEntity

    @Query("DELETE FROM victorineClueEntity")
    suspend fun deleteAllVictorineClue()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVictorine(victorineEntity: VictorineEntity)

    @Delete
    suspend fun deleteVictorine(victorineEntity: VictorineEntity)

    @Query("SELECT * FROM victorineEntityTable")
    suspend fun getAllVictorines(): List<VictorineEntity>
    //withTest
    @Query("SELECT * FROM victorineEntityTable WHERE vicotineTestId = :vicotineTestId")
    suspend fun searchVictorinesForVictorineTestId(vicotineTestId: Int): List<VictorineEntity>

    @Query("SELECT * FROM victorineEntityTable WHERE vicotineTestId = :uniqueThemeId")
    suspend fun searchVictorineForUniqueThemeId(uniqueThemeId: Int):VictorineEntity

    //withTest
    @Query("SELECT * FROM victorineEntityTable WHERE questionId = :questionId")
    suspend fun searchVictorineForQuestionId(questionId: Int): VictorineEntity

    @Query("DELETE FROM victorineEntityTable")
    suspend fun deleteAllVictorines()
    //withTest
    @Query("SELECT * FROM victorineEntityTable WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun searchAllVictorinesWithUniqueThemeId(uniqueThemeId: Int): List<VictorineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVictorineAnswerVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity)

    @Delete
    suspend fun deleteVictorineVariant(victorineAnswerVariantEntity: VictorineAnswerVariantEntity)
    @Query("SELECT * FROM victorineAnswerVariantEntityTable")
    suspend fun getAllVictorinesAnswerVariants(): List<VictorineAnswerVariantEntity>
    //withTest
    @Query("SELECT * FROM victorineAnswerVariantEntityTable WHERE victorineAnswerId = :victorineAnswerId")
    fun searchVictorineAnswerVariantWithVictorineAnswerId(victorineAnswerId: Int): List<VictorineAnswerVariantEntity>
    //withTest
    @Query("SELECT * FROM victorineAnswerVariantEntityTable WHERE questionId = :questionId")
    suspend fun searchVictorineAnswerVariantsWithQuestionId(questionId: Int): List<VictorineAnswerVariantEntity>
    //withTest
    @Query("DELETE FROM victorineAnswerVariantEntityTable")
    suspend fun deleteAllVictorineVariants()

}