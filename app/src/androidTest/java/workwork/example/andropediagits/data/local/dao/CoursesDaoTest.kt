package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.course.CoursesDao
import com.example.andropediagits.data.local.entities.course.CourseEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class CoursesDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var coursesDao: CoursesDao

    @Before
    fun setup() {
        hiltRule.inject()

        coursesDao = database.getCourseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertCourse() = runTest {

        val courseEntity = CourseEntity(courseNumber = 1, isOpen = true,possibleToOpenCourseFree = false, courseName = "normal", description = "bla bla", lastUpdateDate = Date(), isNetworkConnect = true)

        coursesDao.insertCourse(courseEntity)

        val resultCourse = coursesDao.searchCourseWithNumber( courseNumber = 1)

        assertThat(resultCourse).isEqualTo(courseEntity)
    }

 /*   @Test
    fun testGetAllMyCourses() = runBlockingTest {
        val course1 = CourseEntity(courseNumber = 1, courseName = "Course 1")
        val course2 = CourseEntity(courseNumber = 2, courseName = "Course 2")

        // Insert two courses into the database.
        coursesDao.insertCourse(course1)
        coursesDao.insertCourse(course2)

        // Retrieve all courses from the database.
        val allCourses = coursesDao.getAllMyCourses().getOrAwaitValue()

        // Check if the list contains both courses.
        assertThat(allCourses).containsExactly(course1, course2)
    }*/

    // Write similar tests for updateCourse, deleteCourse, and deleteAllCourse methods.
    // ...

}