package workwork.test.andropediagits.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import workwork.test.andropediagits.data.local.dao.ProviderSelectDao
import workwork.test.andropediagits.data.local.dao.ReminderDao
import workwork.test.andropediagits.data.local.dao.TransactionsDao
import workwork.test.andropediagits.data.local.dao.UserInfoDao
import workwork.test.andropediagits.data.local.dao.course.CoursesDao
import workwork.test.andropediagits.data.local.dao.course.IndiCourseDao
import workwork.test.andropediagits.data.local.dao.interactive.InteractiveDao
import workwork.test.andropediagits.data.local.dao.level.IndiLevelContentDao
import workwork.test.andropediagits.data.local.dao.level.IndiLevelDao
import workwork.test.andropediagits.data.local.dao.level.LevelContentDao
import workwork.test.andropediagits.data.local.dao.level.LevelDao
import workwork.test.andropediagits.data.local.dao.promo.PromoCodeDao
import workwork.test.andropediagits.data.local.dao.theme.IndiThemeDao
import workwork.test.andropediagits.data.local.dao.theme.ThemeDao
import workwork.test.andropediagits.data.local.dao.updateKey.UpdateKeysDao
import workwork.test.andropediagits.data.local.dao.victorine.IndiVictorineDao
import workwork.test.andropediagits.data.local.dao.victorine.VictorineDao
import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.BillingProviderEntity
import workwork.test.andropediagits.data.local.entities.ReminderEntity
import workwork.test.andropediagits.data.local.entities.ResetNextEntity
import workwork.test.andropediagits.data.local.entities.SubscribeEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseBuyEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCreatorSubscribeEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVIctorineClueEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineAnswerVarEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineQuestionEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveEntity
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.data.local.entities.promo.PromoCodeEntity
import workwork.test.andropediagits.data.local.entities.reset.ResetDao
import workwork.test.andropediagits.data.local.entities.theme.ThemeBuyEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineClueEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.data.local.typeConverters.BitMapTypeConverter
import workwork.test.andropediagits.data.local.typeConverters.DateTypeConverter
import workwork.test.andropediagits.data.local.typeConverters.LocalDateTypeConverter
import workwork.test.andropediagits.data.local.typeConverters.StringListTypeConverter
import workwork.test.andropediagits.data.local.typeConverters.ThemeLevelContentEntityConverter


@Database(entities = [CourseEntity::class, VictorineClueEntity::class, ThemeEntity::class, UserInfoEntity::class, LevelEntity::class, ThemeLevelContentEntity::class, SubscribeEntity::class, CourseBuyEntity::class, ThemeBuyEntity::class, VictorineEntity::class, VictorineAnswerVariantEntity::class, InteractiveCodeVariantEntity::class, InteractiveCorrectCodeEntity::class, InteractiveEntity::class, UpdatesKeyEntity::class, PromoCodeEntity::class, ReminderEntity::class, AdsEntity::class,ResetNextEntity::class,IndiCourseEntity::class, IndiLessonContentEntity::class,IndiThemeEntity::class,IndiLessonEntity::class,IndiVictorineAnswerVarEntity::class,IndiVIctorineClueEntity::class,IndiVictorineQuestionEntity::class, IndiCreatorSubscribeEntity::class, BillingProviderEntity::class, AdsProviderEntity::class, IndiCourseBuyEntity::class], version = 1)
@TypeConverters(
    DateTypeConverter::class, BitMapTypeConverter::class, StringListTypeConverter::class,
    ThemeLevelContentEntityConverter::class, LocalDateTypeConverter::class)
abstract class MainDb:RoomDatabase(){

    abstract fun getIndiVictorineDao(): IndiVictorineDao

    abstract fun getProviderDao(): ProviderSelectDao

    abstract fun getIndiCourseDao():IndiCourseDao

    abstract fun getIndiThemesDao():IndiThemeDao

    abstract fun getIndiLessonsDao():IndiLevelDao

    abstract fun getIndiLessonsContentDao():IndiLevelContentDao

    abstract fun getResetDao():ResetDao

    abstract fun getReminderDao(): ReminderDao

    abstract fun getPromoCodeDao(): PromoCodeDao
    abstract fun getUpdateKeysDao(): UpdateKeysDao
    abstract fun getLevelDao(): ThemeDao
    abstract fun getCourseDao(): CoursesDao
    abstract fun getUserInfoDao(): UserInfoDao
    abstract fun getRealLevelDao(): LevelDao

    abstract fun getTransactionDao(): TransactionsDao

    abstract fun getLevelContentDao(): LevelContentDao

    abstract fun getVictorineDao(): VictorineDao

    abstract fun getInteractiveDao(): InteractiveDao

}