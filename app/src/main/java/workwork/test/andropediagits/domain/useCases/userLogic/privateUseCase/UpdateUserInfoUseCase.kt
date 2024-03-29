package workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase

import android.graphics.Bitmap
import android.util.Log

import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo){

    private fun infinityToInt(infinitiyBool:Boolean):Int{
        if(infinitiyBool){
            return 1
        } else {
            return 0
        }
    }

    suspend fun updateUserInfo(name:String?=null,andropointCount:Int?=null,image:Bitmap?=null,strikeModeDay:Int?=null,lastOnlineDate:String?=null,lang:String?=null,phoneName:String?=null,token:String?=null,andropointInfinity:Boolean=false){
        val userInfoLocal = userLogicRepo.getUserInfoLocal()
      if(userInfoLocal!=null){
          if(andropointInfinity){
              val updateUserEntity = UserInfoEntity(
                  name = name ?: userInfoLocal?.name ?: "",
                  andropointCount = andropointCount ?: userInfoLocal?.andropointCount ?: 0,
                  image = image ?: userInfoLocal?.image,
                  isInfinity = true,
                  token = userInfoLocal?.token ?: "",
                  lastOnlineDate = lastOnlineDate ?: userInfoLocal?.lastOnlineDate ?: "",
                  strikeModeDay = strikeModeDay ?: userInfoLocal?.strikeModeDay ?: 0,
                  lastOpenTheme = userInfoLocal?.lastOpenTheme ?: 0,
                  lastOpenCourse = userInfoLocal?.lastOpenCourse ?: 0,
                  userLanguage = lang ?: userInfoLocal?.userLanguage,
                  phoneBrand = phoneName ?: userInfoLocal?.phoneBrand ?: "Xiaomi",
                  heartsCount = userInfoLocal?.heartsCount ?: 0
              )
              Log.d("andropointCount",andropointCount.toString())
              userLogicRepo.updateUserInfoLocal(updateUserEntity)
              if (phoneName.isNullOrEmpty()) {
                  val userUpdateBackend = UserSignInModel(
                      token = userInfoLocal?.token ?: "",
                      name = name ?: userInfoLocal?.name,
                      image = image ?: userInfoLocal?.image,
                      andropointCount = andropointCount ?: userInfoLocal?.andropointCount,
                      strikeModeDay = strikeModeDay ?: userInfoLocal?.strikeModeDay,
                      lastCourseNumber = userInfoLocal?.lastOpenCourse,
                      lastThemeNumber = userInfoLocal?.lastOpenTheme,
                      isInfinity = 1,
                      heartsCount = userInfoLocal?.heartsCount ?: 0
                  )
                  userLogicRepo.updateUserInfo(userUpdateBackend)
              }
              return
          }
          val updateUserEntity = UserInfoEntity(
              name = name ?: userInfoLocal?.name ?: "",
              andropointCount = andropointCount ?: userInfoLocal?.andropointCount ?: 0,
              image = image ?: userInfoLocal?.image,
              token = userInfoLocal?.token ?: "",
              lastOnlineDate = lastOnlineDate ?: userInfoLocal?.lastOnlineDate ?: "",
              strikeModeDay = strikeModeDay ?: userInfoLocal?.strikeModeDay ?: 0,
              lastOpenTheme = userInfoLocal?.lastOpenTheme ?: 0,
              lastOpenCourse = userInfoLocal?.lastOpenCourse ?: 0,
              userLanguage = lang ?: userInfoLocal?.userLanguage,
              phoneBrand = phoneName ?: userInfoLocal?.phoneBrand ?: "Xiaomi",
              heartsCount = userInfoLocal?.heartsCount ?: 0
          )
          Log.d("andropointCount",andropointCount.toString())
          userLogicRepo.updateUserInfoLocal(updateUserEntity)
          if (phoneName.isNullOrEmpty()) {
              val userUpdateBackend = UserSignInModel(
                  token = userInfoLocal?.token ?: "",
                  name = name ?: userInfoLocal?.name,
                  image = image ?: userInfoLocal?.image,
                  andropointCount = andropointCount ?: userInfoLocal?.andropointCount,
                  strikeModeDay = strikeModeDay ?: userInfoLocal?.strikeModeDay,
                  lastCourseNumber = userInfoLocal?.lastOpenCourse,
                  lastThemeNumber = userInfoLocal?.lastOpenTheme,
                  isInfinity = infinityToInt(userInfoLocal.isInfinity ?: false),
                  heartsCount = userInfoLocal.heartsCount ?: 0
              )
              userLogicRepo.updateUserInfo(userUpdateBackend)
          }

       }
        if(userInfoLocal==null) {
            val userEntity = UserInfoEntity(
                name = name ?: "",
                image = image,
                token = token ?: "",
                phoneBrand = phoneName ?: "Xiaomi"
            )
            userLogicRepo.insertUserInfoLocal(userEntity)
            val userUpdateBackend = UserSignInModel(
                name = name ?: "",
                image = image,
                token = token ?: "",
                heartsCount = 0
            )
            userLogicRepo.sendUserInfo(userUpdateBackend)
        }
    }

}