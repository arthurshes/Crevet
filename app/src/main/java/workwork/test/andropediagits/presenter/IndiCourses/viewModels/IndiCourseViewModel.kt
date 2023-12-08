package workwork.test.andropediagits.presenter.IndiCourses.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.domain.repo.IndividualCoursesRepo
import javax.inject.Inject

class IndiCourseViewModel @Inject constructor(private val individualCoursesRepo: IndividualCoursesRepo) : ViewModel() {



   fun downloadIndiCourse(uniqueCourseNumber:Int,createrToken:String){
       viewModelScope.launch (Dispatchers.IO){

       }
   }

   fun getIndiCourses(query:String?=null){
       viewModelScope.launch (Dispatchers.IO){

       }
   }
}