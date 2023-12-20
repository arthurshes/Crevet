package workwork.test.andropediagits.data.local.dao.sqlInteractive

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import workwork.test.andropediagits.data.local.entities.sqlInteractive.SqlTableInteractiveCorrectAnswerEntity
import workwork.test.andropediagits.data.local.entities.sqlInteractive.SqlTableInteractiveEntity

@Dao
interface SqlInteractiveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSqlInteractive(sqlTableInteractiveEntity: SqlTableInteractiveEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSqlInteractiveCorrect(sqlTableInteractiveCorrectAnswerEntity: SqlTableInteractiveCorrectAnswerEntity)

    @Query("SELECT * FROM SqlTableInteractiveEntity WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun getCurrentThemeSqlInteractiveTasks(uniqueThemeId:Int):List<SqlTableInteractiveEntity>

    @Query("SELECT * FROM SqlTableInteractiveCorrectAnswerEntity WHERE uniqueThemeId = :uniqueThemeId AND testId = :testId")
    suspend fun getCorrectAnswerSqlInteractive(testId:Int,uniqueThemeId:Int):SqlTableInteractiveCorrectAnswerEntity

    @Query("DELETE FROM SqlTableInteractiveEntity")
    suspend fun deleteAllSqlInteractive()

    @Query("DELETE FROM SqlTableInteractiveCorrectAnswerEntity")
    suspend fun deleteALLSQlCorrectAnswer()

}