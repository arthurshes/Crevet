package workwork.test.andropediagits.presenter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import workwork.test.andropediagits.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    protected val navController: NavController by lazy {
        Navigation. findNavController(
            this,
        R.id. fragmentContainerView
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//        if(routeKey=="SignIn"){
//
//        } else if(routeKey=="CourseHome"){
//            val fragment = CoursesFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentCourses, fragment).commit()
//        }
    }
}