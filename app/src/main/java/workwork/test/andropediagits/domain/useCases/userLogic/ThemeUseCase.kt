package workwork.test.andropediagits.domain.useCases.userLogic

import android.annotation.SuppressLint
import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.ErrorStateView
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.theme.sendModels.ThemeCheckTermModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateAnswerModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateThemeModel
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.updateStates.UpdateLessonState
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.updateStates.UpdateThemeState
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.collections.ArrayList

class ThemeUseCase @Inject constructor(
    private val transactionRepo: TransactionRepo,
    private val userLogicRepo: UserLogicRepo,
    private val courseRepo: CourseRepo,
    private val tryAgainUseCase: TryAgainUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) {

    private var isTerm = false



    suspend fun termExitVictorine(uniqueThemeId: Int,termI:((Boolean)->Unit),termEndingDate:((String)->Unit),buythemesId:((List<Int>)->Unit)?=null){
            val theme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            if(theme.isThemePassed){
                termI.invoke(false)

            }else{
                val currentDataApi = userLogicRepo.getCurrentTime()
                val userInfo = userLogicRepo.getUserInfoLocal()
                val sendSubscribeCheckModel = SendSubscribeCheckModel(
                    token = userInfo?.token ?: "",
                    currentDate = currentDataApi.datetime
                )
                val subscribeCheck = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                //Проверка подписки
                if (subscribeCheck.subscribeIsActual) {
                    termI.invoke(false)
                    return
                }
                val buyCourses = transactionRepo.checkMyBuyCourse(userInfo?.token ?: "")
                if(buyCourses[0].codeAnswer!=707){
                    termI.invoke(false)
                    return
                }

                val buyThemes = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
                buyThemes.forEach { oneBuyTheme->
                    if(oneBuyTheme.uniqueThemeId == uniqueThemeId){
                        termI.invoke(false)
                        return
                    }
                }
                termI.invoke(true)
                termCloseTheme(uniqueThemeId,{

                },theme.victorineQuestionCount*2,{
                    termEndingDate.invoke(it)
                },buythemesId)

            }
    }

    suspend fun thisThemeVictorineYes(victorineYes:((Boolean)->Unit),uniqueThemeId: Int){
        val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        victorineYes.invoke(currentTheme.victorineQuestionCount != 0 && currentTheme.victorineQuestionCount != null)
    }

    suspend fun howManyTerm(isSuccess:((ErrorEnum) -> Unit), isTermEnd:((String)->Unit), buythemesId:((List<Int>)->Unit)?=null,themeNumber:Int,courseNumber: Int){
        try{
            val userInfoLocal = userLogicRepo.getUserInfoLocal()
            val response = courseRepo.checkTermThemes(userInfoLocal.token)
            if(response.themeNumber==themeNumber&&response.courseNumber==courseNumber){
                isTermEnd.invoke(response.dateEnding)
            }else{
                isTermEnd.invoke("")
            }

            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buythemesId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            })
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun thisThemeisPassed(uniqueThemeId: Int):Boolean{
        val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        return currentTheme.isThemePassed
    }

    suspend fun lessonIsFavorite(lessonId:Int){
        updateThemeUseCase.updateLesson(lessonId, UpdateLessonState.THISLESSONFAVORITE)
    }

    suspend fun lessonIsNotFavorite(lessonId:Int){
        updateThemeUseCase.updateLesson(lessonId,UpdateLessonState.THISLESSONREMOVEFAVORITRE)
    }

    suspend fun getAllFavoriteLessons(uniqueThemeId: Int):List<LevelEntity>{
        return courseRepo.getAllFavoriteLessons(uniqueThemeId)
    }

    suspend fun getAllFavoriteThemes():List<ThemeEntity>{
        return courseRepo.getAllFavoriteThemes()
    }

    suspend fun themeNotFavorite(uniqueThemeId: Int){

            updateThemeUseCase.updateTheme(uniqueThemeId, UpdateThemeState.THISTHEMEREMOVEFAV)

    }

    suspend fun themeIsFavorite(uniqueThemeId: Int){

            updateThemeUseCase.updateTheme(uniqueThemeId,UpdateThemeState.THISTHEMEFAVORITE)

    }


    suspend fun thisThemePassed(isSuccess: ((ErrorEnum) -> Unit),uniqueThemeId: Int,buythemesId:((List<Int>)->Unit)?=null,isThemePassed:((Boolean)->Unit)){
        try {
             val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
             isThemePassed.invoke(currentTheme?.isThemePassed ?: false)
        }catch (e:IOException){
                if(checkSubscibe()){
                    isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                    return
                }
                if (checkBuyCourse()){
                    isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                    return
                }
                checkAndGeIdtLocalBuyThemes({
                    buythemesId?.invoke(it)
                },{ isBuy->
                    if (isBuy){
                        isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                    }
                })
                isSuccess.invoke(ErrorEnum.NOTNETWORK)
            }catch (e:NullPointerException){
                isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
            }catch (e:TimeoutException){
                isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
            }catch (e:HttpException){
                isSuccess.invoke(ErrorEnum.ERROR)
            }catch (e:Exception){
                isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
            }
    }

/////Пока незнаю как рвботает, проверка результатов викторин и интерактива
    @SuppressLint("SuspiciousIndentation")
    suspend fun checkTestResults(uniqueThemeId:Int, isSuccess: ((ErrorStateView) -> Unit), dateUnlockTheme:((String)->Unit)?=null, interactiveMisstakeCount:((String)->Unit)?=null, buythemesId:((List<Int>)->Unit)?=null, correctAnswerTstCo:Int?=null, misstakesAnswersC:Int?=null, isTimerOut:Boolean) {
        try {
            val theme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)

            /// 10-2+1 = 7
            /// 7+1 = 8
            /// 10-2+0 = 8
            ///
//            var correctVictorineAnswer = correctAnswerTstCo
            var misstakeVictorine =   0

            val victorineQuestionCount = theme.victorineQuestionCount  /// 10
            if(misstakesAnswersC!=null){
                if(isTimerOut){
                    var misstimers = victorineQuestionCount?.minus(correctAnswerTstCo?.plus(misstakesAnswersC) ?: 0)
                    misstakeVictorine = misstimers?.plus(misstakesAnswersC )?: 0
                }else{
                    misstakeVictorine = misstakesAnswersC
                }
            }


//                Log.d("jsonWalterWhite","victorinesCout:${correctVictorineAnswer}")
            Log.d("jsonWalterWhite","victorinesCout$$$$$:${theme.victorineCorrectAnswer}")
                Log.d("jsonWalterWhite","victorinesQuyesCout:${victorineQuestionCount}")
               Log.d("jsonWalterWhite","correctAnswersOutside:${correctAnswerTstCo}")
            Log.d("jsonWalterWhite","misstakeAnswersOutside:${misstakesAnswersC}")

                /////last update version
//                val misstakeInteractive = theme.interactiveCodeMistakes
//                val correctInteractiveAnswer = theme.interactiveCodeCorrect
//                val interactiveQuestionCount = theme.interactiveQuestionCount
//                if (correctInteractiveAnswer == interactiveQuestionCount && victorineQuestionCount == correctVictorineAnswer)
            if(theme.isThemePassed){
                isSuccess.invoke(ErrorStateView.NEXTTHEME)
                return
            }
                if (victorineQuestionCount == correctAnswerTstCo&&misstakeVictorine == 0) {
                    Log.d("jsonWalterWhite","next theme")

                    val updateTheme = ThemeEntity(
                        uniqueThemeId =  theme.uniqueThemeId,
                        themeName = theme.themeName,
                        themeNumber = theme.themeNumber,
                        courseNumber = theme.courseNumber,
                        lessonsCount = theme.lessonsCount,
                        lastUpdateDate = theme.lastUpdateDate,
                        termHourse = null,
                        termDateApi = null,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        interactiveTestId = theme.interactiveTestId,
                        imageTheme = theme.imageTheme,
                        isOpen = theme.isOpen,
                        duoDate = theme.duoDate,
                        victorineDate = theme.victorineDate,
                        isVictorine = theme.isVictorine,
                        isDuoInter = theme.isDuoInter,
                        isFav = theme.isFav,
                        isThemePassed = true,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        lastCourseTheme = theme.lastCourseTheme,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                    isSuccess.invoke(ErrorStateView.NEXTTHEME)
                    if(!theme.lastCourseTheme){
                        openNextTheme(uniqueThemeId,{errorState->
                            when(errorState){
                                ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
                                ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
                                ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
                            }
                        },{ uniqueIds->
                            buythemesId?.invoke(uniqueIds)
                        })
                    }

                } else {
                    Log.d("jsonWalterWhite","error tests misstakes:${misstakeVictorine}, coreects:${correctAnswerTstCo}")

                    //Проверка подписки
                    val currentDataApi = userLogicRepo.getCurrentTime()
                    val userInfo = userLogicRepo.getUserInfoLocal()
                    val sendSubscribeCheckModel = SendSubscribeCheckModel(
                        token = userInfo?.token ?: "",
                        currentDate = currentDataApi.datetime
                    )
                    val subscribeCheck = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                    //Проверка подписки
                    if (subscribeCheck.subscribeIsActual) {
                        isSuccess.invoke(ErrorStateView.NEXTTHEME)
                        /////last update version
//                           interactiveMisstakeCount?.invoke("$interactiveQuestionCount/$correctInteractiveAnswer")
                        val updateTheme = ThemeEntity(
                            uniqueThemeId =  theme.uniqueThemeId,
                            themeName = theme.themeName,
                            themeNumber = theme.themeNumber,
                            courseNumber = theme.courseNumber,
                            lessonsCount = theme.lessonsCount,
                            lastUpdateDate = theme.lastUpdateDate,
                            termHourse = null,
                            termDateApi = null,
                            victorineMistakeAnswer = theme.victorineMistakeAnswer,
                            victorineCorrectAnswer = theme.victorineCorrectAnswer,
                            interactiveCodeMistakes = theme.interactiveCodeMistakes,
                            interactiveCodeCorrect = theme.interactiveCodeCorrect,
                            interactiveQuestionCount = theme.interactiveQuestionCount,
                            victorineQuestionCount = theme.victorineQuestionCount,
                            vicotineTestId = theme.vicotineTestId,
                            interactiveTestId = theme.interactiveTestId,
                            imageTheme = theme.imageTheme,
                            isOpen = theme.isOpen,
                            duoDate = theme.duoDate,
                            victorineDate = theme.victorineDate,
                            isVictorine = theme.isVictorine,
                            isDuoInter = theme.isDuoInter,
                            isFav = theme.isFav,
                            isThemePassed = true,
                            possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                            lastCourseTheme = theme.lastCourseTheme,
                            themePrice = theme.themePrice
                        )
                        courseRepo.updateTheme(updateTheme)
                        if(!theme.lastCourseTheme){
                            openNextTheme(uniqueThemeId,{errorState->
                                when(errorState){
                                    ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                    ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                    ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
                                    ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                    ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                    ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                    ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
                                    ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
                                }
                            },{ uniqueIds->
                                buythemesId?.invoke(uniqueIds)
                            })
                        }

//                        updateThemeProgress({state->
//                           when(state){
//                               ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                               ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                               ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
//                               ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                               ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                               ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                               ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
//                               ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
//                           }
//
//                        },uniqueThemeId,{uniqueThemeIds->
//                            buythemesId?.invoke(uniqueThemeIds)
//                        })
                    } else {
                        //Если есть купленный курс
                        val buyCourses = transactionRepo.checkMyBuyCourse(userInfo?.token ?: "")
                                if(buyCourses[0].codeAnswer!=707){
                                    isSuccess.invoke(ErrorStateView.NEXTTHEME)
                                    val updateTheme = ThemeEntity(
                                        uniqueThemeId =  theme.uniqueThemeId,
                                        themeName = theme.themeName,
                                        themeNumber = theme.themeNumber,
                                        courseNumber = theme.courseNumber,
                                        lessonsCount = theme.lessonsCount,
                                        lastUpdateDate = theme.lastUpdateDate,
                                        termHourse = null,
                                        termDateApi = null,
                                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                                        interactiveQuestionCount = theme.interactiveQuestionCount,
                                        victorineQuestionCount = theme.victorineQuestionCount,
                                        vicotineTestId = theme.vicotineTestId,
                                        interactiveTestId = theme.interactiveTestId,
                                        imageTheme = theme.imageTheme,
                                        isOpen = theme.isOpen,
                                        duoDate = theme.duoDate,
                                        victorineDate = theme.victorineDate,
                                        isVictorine = theme.isVictorine,
                                        isDuoInter = theme.isDuoInter,
                                        isFav = theme.isFav,
                                        isThemePassed = true,
                                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                                        lastCourseTheme = theme.lastCourseTheme,
                                        themePrice = theme.themePrice
                                    )
                                    courseRepo.updateTheme(updateTheme)
                                    ////last update version
//                                interactiveMisstakeCount?.invoke("$interactiveQuestionCount/$correctInteractiveAnswer")
//                                    openNextTheme(uniqueThemeId,{errorState->
//                                        when(errorState){
//                                            ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
//                                            ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                                            ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                                            ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
//                                            ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
//                                        }
//                                    },{ uniqueIds->
//                                        buythemesId?.invoke(uniqueIds)
//                                    })
//                                    updateThemeProgress({state->
//                                        when(state){
//                                            ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
//                                            ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                                            ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                                            ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
//                                            ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
//                                        }
//
//                                    },uniqueThemeId,{uniqueThemeIds->
//                                        buythemesId?.invoke(uniqueThemeIds)
//                                    })
                                    return
                                }
                        /////Если это купленный курс
                            val buyThemes = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
                            buyThemes.forEach { oneBuyTheme->
                                if(oneBuyTheme.uniqueThemeId == uniqueThemeId){
                                    isSuccess.invoke(ErrorStateView.NEXTTHEME)
                                    ///las update version
//                                    interactiveMisstakeCount?.invoke("$interactiveQuestionCount/$correctInteractiveAnswer")
                                    val updateTheme = ThemeEntity(
                                        uniqueThemeId =  theme.uniqueThemeId,
                                        themeName = theme.themeName,
                                        themeNumber = theme.themeNumber,
                                        courseNumber = theme.courseNumber,
                                        lessonsCount = theme.lessonsCount,
                                        lastUpdateDate = theme.lastUpdateDate,
                                        termHourse = null,
                                        termDateApi = null,
                                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                                        interactiveQuestionCount = theme.interactiveQuestionCount,
                                        victorineQuestionCount = theme.victorineQuestionCount,
                                        vicotineTestId = theme.vicotineTestId,
                                        interactiveTestId = theme.interactiveTestId,
                                        imageTheme = theme.imageTheme,
                                        isOpen = theme.isOpen,
                                        duoDate = theme.duoDate,
                                        victorineDate = theme.victorineDate,
                                        isVictorine = theme.isVictorine,
                                        isDuoInter = theme.isDuoInter,
                                        isFav = theme.isFav,
                                        isThemePassed = true,
                                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                                        lastCourseTheme = theme.lastCourseTheme,
                                        themePrice = theme.themePrice
                                    )
                                    courseRepo.updateTheme(updateTheme)
                                    if(!theme.lastCourseTheme){
                                        openNextTheme(uniqueThemeId,{errorState->
                                            when(errorState){
                                                ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                                ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                                ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
                                                ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                                ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                                ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                                ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
                                                ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
                                            }
                                        },{ uniqueIds->
                                            buythemesId?.invoke(uniqueIds)
                                        })
                                    }

//                                    updateThemeProgress({state->
//                                        when(state){
//                                            ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
//                                            ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                                            ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
//                                            ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.ERROR)
//                                            ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
//                                            ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
//                                        }
//
//                                    },uniqueThemeId,{uniqueThemeIds->
//                                        buythemesId?.invoke(uniqueThemeIds)
//                                    })
                                    return
                                }
                            }
                           ///Если нет подписок
                        if(theme.isThemePassed){
                            isSuccess.invoke(ErrorStateView.NEXTTHEME)
                            return
                        }
                            isSuccess.invoke(ErrorStateView.TERM)
                        Log.d("termHourseThemeSendApi",misstakeVictorine.toString()+"misstakes")
                        ////last update version
//                            interactiveMisstakeCount?.invoke("$interactiveQuestionCount/$correctInteractiveAnswer")
                        if(isTerm==false){
                            termCloseTheme(
                                uniqueThemeId,
                                { errorEnum ->
                                    when (errorEnum) {
                                        ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorStateView.OFFLINE)
                                        ErrorEnum.ERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                        ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorStateView.SUCCESS)
                                        ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorStateView.ERROR)
                                        ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                        ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorStateView.TRYAGAIN)
                                        ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorStateView.OFFLINE)
                                        ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
                                    }
                                },
                                ////last update version
//                                misstakeInteractive?.plus(misstakeVictorine ?: 0)?.times(2)
                                misstakeVictorine*2,
                                { dateUnlock ->
                                    dateUnlockTheme?.invoke(dateUnlock)
                                },{ uniqueThemeIds->
                                    buythemesId?.invoke(uniqueThemeIds)
                                }
                            )
                          isTerm = true
                        }

                    }
                }


        }catch (e:IOException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            if(checkSubscibe()){
                isSuccess.invoke(ErrorStateView.OFFLINE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorStateView.OFFLINE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buythemesId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorStateView.OFFLINEBUYTHEME)
                }
            })
            isSuccess.invoke(ErrorStateView.TRYAGAIN)
        }catch (e:NullPointerException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorStateView.ERROR)
        }catch (e:TimeoutException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorStateView.TRYAGAIN)
        }catch (e:HttpException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorStateView.TRYAGAIN)
        }catch (e:Exception){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorStateView.ERROR)
        }
    }



    ///Просмотр рекламы для отключения задержки
    suspend fun watchAdsToDisableDelay(uniqueThemeId: Int,isSucces:((ErrorEnum)->Unit),remainigTermHourse:((String)->Unit)){
        try {
            val myInfoLoca = userLogicRepo.getUserInfoLocal()
             val themeLocal = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            themeLocal?.let { themeNotNull->
                if (themeNotNull.termHourse?.minus(2) == 0){
                    val updataThemeEntity = ThemeEntity(
                        uniqueThemeId = themeNotNull.uniqueThemeId,
                        themeNumber = themeNotNull.themeNumber,
                        courseNumber = themeNotNull.courseNumber,
                        themeName = themeNotNull.themeName,
                        imageTheme = themeNotNull.imageTheme,
                        termDateApi = null,
                        termHourse = null,
                        lessonsCount = themeNotNull.lessonsCount,
                        lastCourseTheme = themeNotNull.lastCourseTheme,
                        lastUpdateDate = themeNotNull.lastUpdateDate,
                        interactiveCodeMistakes = themeNotNull.interactiveCodeMistakes,
                        interactiveCodeCorrect = themeNotNull.interactiveCodeCorrect,
                        victorineMistakeAnswer = themeNotNull.victorineMistakeAnswer,
                        victorineCorrectAnswer = themeNotNull.victorineCorrectAnswer,
                        victorineQuestionCount = themeNotNull.victorineQuestionCount,
                        vicotineTestId = themeNotNull.vicotineTestId,
                        interactiveTestId = themeNotNull.interactiveTestId,
                        interactiveQuestionCount = themeNotNull.interactiveQuestionCount,
                        isVictorine = themeNotNull.isVictorine,
                        isOpen = true,
                        isFav = themeNotNull.isFav,
                        isThemePassed = themeNotNull.isThemePassed,
                        possibleToOpenThemeFree = themeNotNull.possibleToOpenThemeFree,
                        isDuoInter = themeNotNull.isDuoInter,
                        victorineDate = themeNotNull.victorineDate,
                        duoDate = themeNotNull.duoDate,
                        themePrice = themeNotNull.themePrice
                    )
                    courseRepo.updateTheme(updataThemeEntity)
                    val currentTime = courseRepo.getCurrentTime()

                    val updateThemeModel = UpdateThemeModel(
                        token = myInfoLoca?.token ?: "",
                        uniqueThemeId = themeNotNull.uniqueThemeId,
                        themeNumber = themeNotNull.themeNumber,
                        isOpenTheme = true,
                        courseNumber = themeNotNull.courseNumber,
                        termHourse = null,
                        termDateApi = null,
                        interactiveId = themeNotNull.interactiveTestId,
                        victorineId = themeNotNull.vicotineTestId,
                        dateApi = currentTime.datetime,
                        lasThemePassed = false
                    )
                    val updateAnsweModel = UpdateAnswerModel(
                        token = myInfoLoca?.token ?: "",
                        updateThemes = updateThemeModel
                    )
                    val response = courseRepo.sendMyProgress(updateAnsweModel)
                    remainigTermHourse.invoke("0null")
                } else{
                    val updataThemeEntity = ThemeEntity(
                        uniqueThemeId = themeNotNull.uniqueThemeId,
                        themeNumber = themeNotNull.themeNumber,
                        courseNumber = themeNotNull.courseNumber,
                        themeName = themeNotNull.themeName,
                        imageTheme = themeNotNull.imageTheme,
                        termDateApi = themeNotNull.termDateApi,
                        termHourse = themeNotNull.termHourse?.minus(2),
                        lessonsCount = themeNotNull.lessonsCount,
                        lastCourseTheme = themeNotNull.lastCourseTheme,
                        lastUpdateDate = themeNotNull.lastUpdateDate,
                        interactiveCodeMistakes = themeNotNull.interactiveCodeMistakes,
                        interactiveCodeCorrect = themeNotNull.interactiveCodeCorrect,
                        victorineMistakeAnswer = themeNotNull.victorineMistakeAnswer,
                        victorineCorrectAnswer = themeNotNull.victorineCorrectAnswer,
                        victorineQuestionCount = themeNotNull.victorineQuestionCount,
                        vicotineTestId = themeNotNull.vicotineTestId,
                        interactiveTestId = themeNotNull.interactiveTestId,
                        interactiveQuestionCount = themeNotNull.interactiveQuestionCount,
                        isVictorine = themeNotNull.isVictorine,
                        isOpen = themeNotNull.isOpen,
                        isFav = themeNotNull.isFav,
                        isThemePassed = themeNotNull.isThemePassed,
                        possibleToOpenThemeFree = themeNotNull.possibleToOpenThemeFree,
                        isDuoInter = themeNotNull.isDuoInter,
                        victorineDate = themeNotNull.victorineDate,
                        duoDate = themeNotNull.duoDate,
                        themePrice = themeNotNull.themePrice
                    )
                    courseRepo.updateTheme(updataThemeEntity)
                    val currentTime = courseRepo.getCurrentTime()
                    val updateThemeModel = UpdateThemeModel(
                        token = myInfoLoca?.token ?: "",
                        uniqueThemeId = themeNotNull.uniqueThemeId,
                        themeNumber = themeNotNull.themeNumber,
                        isOpenTheme = themeNotNull.isOpen,
                        courseNumber = themeNotNull.courseNumber,
                        termHourse = themeNotNull.termHourse?.minus(2),
                        termDateApi = themeNotNull.termDateApi,
                        interactiveId = themeNotNull.interactiveTestId,
                        victorineId = themeNotNull.vicotineTestId,
                        dateApi = currentTime.datetime,
                        lasThemePassed = false
                    )

                    val updateAnsweModel = UpdateAnswerModel(
                        token = myInfoLoca?.token ?: "",
                        updateThemes = updateThemeModel
                    )
                    val response = courseRepo.sendMyProgress(updateAnsweModel)


                    val termCheckModel = ThemeCheckTermModel(
                        token = myInfoLoca?.token ?: "",
                        termHourse = themeNotNull.termHourse ?: 0,
                        currentDateApi =  currentTime.datetime,
                        uniqueThemeId = themeNotNull.uniqueThemeId,
                        themeNumber = themeNotNull.themeNumber,
                        courseNumber = themeNotNull.courseNumber,
                        terDateApi = themeNotNull.termDateApi ?: ""
                    )
                    val responseEnding = courseRepo.checkTermCurrentTheme(termCheckModel)
                    if(responseEnding.isEnding){
                        remainigTermHourse.invoke("0null")
                    }else{
                        remainigTermHourse.invoke(response.termToDate)
                    }
                    ///Пока незнаю стоит ли проверять так через бэкенд
                }

            }
            isSucces.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                isSucces.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSucces.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSucces.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSucces.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            isSucces.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            isSucces.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun checkAllCourseThemesTerm(courseNumber:Int,isSuccess: ((ErrorEnum) -> Unit),isTermActual:((Boolean)->Unit)){
                try {
                    val userInfo = userLogicRepo.getUserInfoLocal()
                  val allThemes = courseRepo.getAllNotPassedThemesCourse(courseNumber)
             val themeFilters = allThemes.filter {
                      it.termDateApi != null && it.termDateApi != "" && it.termHourse != null && it.termHourse != 0
                  }
                 if(themeFilters.isNotEmpty()){
                     themeFilters.forEach { themeNotnull->
                         val currentTime = courseRepo.getCurrentTime()
                         val termCheckModel = ThemeCheckTermModel(
                             token = userInfo.token ?: "",
                             termHourse = themeNotnull.termHourse ?: 0,
                             currentDateApi =  currentTime.datetime,
                             uniqueThemeId = themeNotnull.uniqueThemeId,
                             themeNumber = themeNotnull.themeNumber,
                             courseNumber = themeNotnull.courseNumber,
                             terDateApi = themeNotnull.termDateApi ?: ""
                         )
                         val response = courseRepo.checkTermCurrentTheme(termCheckModel)
                         if(response.isEnding){
                             val updateThemeEntity = ThemeEntity(
                                 uniqueThemeId = themeNotnull.uniqueThemeId,
                                 lastUpdateDate = Date(),
                                 themeName = themeNotnull.themeName,
                                 courseNumber = themeNotnull.courseNumber,
                                 themeNumber = themeNotnull.themeNumber,
                                 interactiveCodeMistakes = themeNotnull.interactiveCodeMistakes,
                                 interactiveCodeCorrect = themeNotnull.interactiveCodeCorrect,
                                 victorineMistakeAnswer = themeNotnull.victorineMistakeAnswer,
                                 victorineCorrectAnswer = themeNotnull.victorineCorrectAnswer,
                                 victorineDate = themeNotnull.victorineDate,
                                 interactiveTestId = themeNotnull.interactiveTestId,
                                 interactiveQuestionCount = themeNotnull.interactiveQuestionCount,
                                 victorineQuestionCount = themeNotnull.victorineQuestionCount,
                                 vicotineTestId = themeNotnull.vicotineTestId,
                                 duoDate = themeNotnull.duoDate,
                                 isDuoInter = themeNotnull.isDuoInter,
                                 isVictorine = themeNotnull.isVictorine,
                                 isOpen = true,
                                 termHourse = null,
                                 termDateApi = null,
                                 imageTheme = themeNotnull.imageTheme,
                                 lessonsCount = themeNotnull.lessonsCount,
                                 lastCourseTheme = themeNotnull.lastCourseTheme,
                                 isFav = themeNotnull.isFav,
                                 isThemePassed = themeNotnull.isThemePassed,
                                 possibleToOpenThemeFree = themeNotnull.possibleToOpenThemeFree,
                                 themePrice = themeNotnull.themePrice
                             )
                             courseRepo.updateTheme(updateThemeEntity)
                             isTermActual.invoke(false)
                         }else{
                             isTermActual.invoke(true)
                         }
                     }
                 } else{
                     isTermActual.invoke(false)
                 }
                    isSuccess.invoke(ErrorEnum.SUCCESS)
                }catch (e:IOException){
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }





    ///Проверка закрыта ли определенная тема
    suspend fun checkOneThemeTernAndNo(uniqueThemeId: Int, isSuccess: ((ErrorEnum) -> Unit), isNoTerm:((Boolean)->Unit),buyThemeId:((List<Int>)->Unit)?=null){
        try {
            val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            currentTheme?.let { themeNotnull->
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo->
                    themeNotnull.termHourse?.let { termNotnull->
                        val currentTime = courseRepo.getCurrentTime()
                        val termCheckModel = ThemeCheckTermModel(
                            token = myInfo.token ?: "",
                            termHourse = termNotnull,
                            currentDateApi =  currentTime.datetime,
                            uniqueThemeId = themeNotnull.uniqueThemeId,
                            themeNumber = themeNotnull.themeNumber,
                            courseNumber = themeNotnull.courseNumber,
                            terDateApi = themeNotnull.termDateApi ?: ""
                        )
                        val response = courseRepo.checkTermCurrentTheme(termCheckModel)
                        if(response.isEnding){
                            val updateThemeEntity = ThemeEntity(
                                uniqueThemeId = themeNotnull.uniqueThemeId,
                                lastUpdateDate = Date(),
                                themeName = themeNotnull.themeName,
                                courseNumber = themeNotnull.courseNumber,
                                themeNumber = themeNotnull.themeNumber,
                                interactiveCodeMistakes = themeNotnull.interactiveCodeMistakes,
                                interactiveCodeCorrect = themeNotnull.interactiveCodeCorrect,
                                victorineMistakeAnswer = themeNotnull.victorineMistakeAnswer,
                                victorineCorrectAnswer = themeNotnull.victorineCorrectAnswer,
                                victorineDate = themeNotnull.victorineDate,
                                interactiveTestId = themeNotnull.interactiveTestId,
                                interactiveQuestionCount = themeNotnull.interactiveQuestionCount,
                                victorineQuestionCount = themeNotnull.victorineQuestionCount,
                                vicotineTestId = themeNotnull.vicotineTestId,
                                duoDate = themeNotnull.duoDate,
                                isDuoInter = themeNotnull.isDuoInter,
                                isVictorine = themeNotnull.isVictorine,
                                isOpen = true,
                                termHourse = null,
                                termDateApi = null,
                                imageTheme = themeNotnull.imageTheme,
                                lessonsCount = themeNotnull.lessonsCount,
                                lastCourseTheme = themeNotnull.lastCourseTheme,
                                isFav = themeNotnull.isFav,
                                isThemePassed = themeNotnull.isThemePassed,
                                possibleToOpenThemeFree = themeNotnull.possibleToOpenThemeFree,
                                themePrice = themeNotnull.themePrice
                            )
                            courseRepo.updateTheme(updateThemeEntity)
                            isNoTerm.invoke(true)
                        }else{
                            isNoTerm.invoke(false)
                        }
                    }
                }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("nffnrfnrnfrnfnr3434434nfrnf",e.toString())
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemeId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            })
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("nffnrfnrnfrnfnr3434434nfrnf",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e: TimeoutException){
            Log.d("nffnrfnrnfrnfnr3434434nfrnf",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("nffnrfnrnfrnfnr3434434nfrnf",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    ///Просто обновление прогресса возможно так же замена функции termCloseTheme
    private suspend fun updateThemeProgress(isSuccess: ((ErrorEnum) -> Unit), uniqueThemeId:Int,buyThemeId:((List<Int>)->Unit)?=null){
        try {
            val theme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            theme?.let { themeNotNull->
                val userInfoLocal = userLogicRepo.getUserInfoLocal()
                val currentDate = courseRepo.getCurrentTime()
                val updateThemeModel = UpdateThemeModel(
                    uniqueThemeId = theme.uniqueThemeId,
                    interactiveId = theme.interactiveTestId,
                    victorineId = theme.vicotineTestId,
                    themeNumber = theme.themeNumber,
                    courseNumber = theme.courseNumber,
                    isOpenTheme = theme.isOpen,
                    token = userInfoLocal?.token ?: "",
                    dateApi = currentDate.datetime,
                    termDateApi = theme.termDateApi,
                    termHourse = theme.termHourse,
                    lasThemePassed = false
                )
                val updateAnswerModel = UpdateAnswerModel(
                    updateThemes = updateThemeModel,
                    token = userInfoLocal?.token ?: "",
                    AndropointCount = userInfoLocal?.andropointCount
                )
                courseRepo.sendMyProgress(updateAnswerModel)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            saveKeyTryAgainTheme(uniqueThemeId)
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemeId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            })
          isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            saveKeyTryAgainTheme(uniqueThemeId)
             isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            saveKeyTryAgainTheme(uniqueThemeId)
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            saveKeyTryAgainTheme(uniqueThemeId)
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

////Задержка
    private suspend fun termCloseTheme(uniqueThemeId: Int,isSuccess:((ErrorEnum)->Unit),termHourse:Int,dateUnlockTheme:((String)->Unit),buyThemeId:((List<Int>)->Unit)?=null) {
        try {
            Log.d("termHourseThemeSendApi",termHourse.toString())
            val currentThemeLocal = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            val userLocalData = userLogicRepo.getUserInfoLocal()
            userLocalData?.let { myLocalInfo ->
                currentThemeLocal?.let { theme ->
                    //// Update theme вставить в try catch
                    val dateApi = courseRepo.getCurrentTime()
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = theme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = theme.themeName,
                        courseNumber = theme.courseNumber,
                        themeNumber = theme.themeNumber,
                        interactiveCodeMistakes = theme.interactiveCodeMistakes,
                        interactiveCodeCorrect = theme.interactiveCodeCorrect,
                        victorineMistakeAnswer = theme.victorineMistakeAnswer,
                        victorineCorrectAnswer = theme.victorineCorrectAnswer,
                        victorineDate = theme.victorineDate,
                        interactiveTestId = theme.interactiveTestId,
                        interactiveQuestionCount = theme.interactiveQuestionCount,
                        victorineQuestionCount = theme.victorineQuestionCount,
                        vicotineTestId = theme.vicotineTestId,
                        duoDate = theme.duoDate,
                        isDuoInter = theme.isDuoInter,
                        isVictorine = theme.isVictorine,
                        isOpen = theme.isOpen,
                        termHourse = termHourse,
                        termDateApi = dateApi.datetime,
                        imageTheme = theme.imageTheme,
                        lessonsCount = theme.lessonsCount,
                        lastCourseTheme = theme.lastCourseTheme,
                        possibleToOpenThemeFree = theme.possibleToOpenThemeFree,
                        isFav = theme.isFav,
                        isThemePassed = theme.isThemePassed,
                        themePrice = theme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                    //// Update api theme
                    val updateThemeModel = UpdateThemeModel(
                        themeNumber = theme.themeNumber,
                        courseNumber = theme.courseNumber,
                        uniqueThemeId = theme.uniqueThemeId,
                        victorineId = theme.vicotineTestId,
                        interactiveId = theme.interactiveTestId,
                        termHourse = termHourse,
                        isOpenTheme = false,
                        token = myLocalInfo.token ?: "",
                        dateApi = dateApi.datetime,
                        termDateApi = dateApi.datetime,
                        lasThemePassed = false
                    )

                    /////Update answer model
                    val updateAnswerModel = UpdateAnswerModel(
                        token = myLocalInfo.token ?: "",
                        updateThemes = updateThemeModel,
                        AndropointCount = myLocalInfo.andropointCount
                    )
                    val response = courseRepo.sendMyProgress(updateAnswerModel)
                     dateUnlockTheme.invoke(response.termToDate)
                }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        } catch (e:IOException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemeId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            })
            saveKeyTryAgainTheme(uniqueThemeId)
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        } catch (e:HttpException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            saveKeyTryAgainTheme(uniqueThemeId)
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            saveKeyTryAgainTheme(uniqueThemeId)
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            saveKeyTryAgainTheme(uniqueThemeId)
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
}

    ////Сохранение ключа если не удалось отправить чтобы попробовать снова
    private suspend fun saveKeyTryAgainTheme(uniqueThemeId:Int){
        val searchUpdateKeyWithUniqueThemeId = courseRepo.getUpdateKeyWithUniqueThemeId(uniqueThemeId)
        val currentTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        searchUpdateKeyWithUniqueThemeId?.let { updateTheme->
            val updateUpdateKeyEntity = UpdatesKeyEntity(
                updateKeyId = updateTheme.updateKeyId,
                updateTime = Date(),
                uniqueThemeId = uniqueThemeId,
                interactiveTestId = updateTheme.interactiveTestId,
                vicotineTestId = updateTheme.vicotineTestId
            )
            courseRepo.updateUpdateKey(updateUpdateKeyEntity)
        }
        currentTheme?.let { theme->
            val updateKeyEntity = UpdatesKeyEntity(
                updateTime = Date(),
                uniqueThemeId = theme.uniqueThemeId,
                interactiveTestId = theme.interactiveTestId,
                vicotineTestId = theme.vicotineTestId
            )
            courseRepo.insertKey(updateKeyEntity)
        }
    }


    ///Открыть следующую тему
    private suspend fun openNextTheme(currentUniqueThemeId:Int,isSuccess:((ErrorEnum)->Unit),buyThemeId:((List<Int>)->Unit)?=null){
        try {
            val currentThemeLocal = courseRepo.searchThemeWithUniwueId(currentUniqueThemeId)
            currentThemeLocal?.let { theme ->
                ///Это первое действие
                val nextThemeUniqueId = theme.courseNumber + theme.themeNumber.plus(1)

                val nextTheme = courseRepo.searchThemeWithUniwueId(nextThemeUniqueId)
                if(nextTheme!=null){
                    nextTheme.let { n ->
                        ///Это второе действие
                        val userInfoLocal = userLogicRepo.getUserInfoLocal()
                        val updateThemeEntity = ThemeEntity(
                            uniqueThemeId = n.uniqueThemeId,
                            lastUpdateDate = Date(),
                            themeName = n.themeName,
                            courseNumber = n.courseNumber,
                            themeNumber = n.themeNumber,
                            interactiveCodeMistakes = n.interactiveCodeMistakes,
                            interactiveCodeCorrect = n.interactiveCodeCorrect,
                            victorineMistakeAnswer = n.victorineMistakeAnswer,
                            victorineCorrectAnswer = n.victorineCorrectAnswer,
                            victorineDate = n.victorineDate,
                            interactiveTestId = n.interactiveTestId,
                            interactiveQuestionCount = n.interactiveQuestionCount,
                            victorineQuestionCount = n.victorineQuestionCount,
                            vicotineTestId = n.vicotineTestId,
                            duoDate = n.duoDate,
                            isDuoInter = n.isDuoInter,
                            isVictorine = n.isVictorine,
                            isOpen = true,
                            termHourse = n.termHourse,
                            termDateApi = n.termDateApi,
                            imageTheme = n.imageTheme,
                            lessonsCount = n.lessonsCount,
                            lastCourseTheme = n.lastCourseTheme,
                            isFav = n.isFav,
                            isThemePassed = n.isThemePassed,
                            possibleToOpenThemeFree = n.possibleToOpenThemeFree,
                            themePrice = theme.themePrice
                        )
                        courseRepo.updateTheme(updateThemeEntity)
                        ///Это третье действие
                        val currentTime = courseRepo.getCurrentTime()
                        val updateThemeModel = UpdateThemeModel(
                            uniqueThemeId = n.uniqueThemeId,
                            interactiveId = n.interactiveTestId,
                            victorineId = n.vicotineTestId,
                            themeNumber = n.themeNumber,
                            courseNumber = n.courseNumber,
                            isOpenTheme = true,
                            token = userInfoLocal?.token ?: "",
                            dateApi = currentTime.datetime,
                            termDateApi = n.termDateApi,
                            termHourse = n.termHourse,
                            lasThemePassed = false
                        )
                        ///Это четвертое действие
                        val updateAnswerModel = UpdateAnswerModel(
                            token = userInfoLocal?.token ?: "",
                            updateThemes = updateThemeModel,
                            AndropointCount = userInfoLocal?.andropointCount
                        )
                        courseRepo.sendMyProgress(updateAnswerModel)
                        //                val updateKeyEntity = UpdatesKeyEntity(uniqueThemeId = n.uniqueThemeId, updateTime = Date())
                        //                courseRepo.insertKey(updateKeyEntity)
                    }
                }else{
                    val userInfoLocal = userLogicRepo.getUserInfoLocal()
                    val currentTime = courseRepo.getCurrentTime()
                    val updateThemeModel = UpdateThemeModel(
                        uniqueThemeId = currentThemeLocal.uniqueThemeId,
                        interactiveId = currentThemeLocal.interactiveTestId,
                        victorineId = currentThemeLocal.vicotineTestId,
                        themeNumber = currentThemeLocal.themeNumber,
                        courseNumber = currentThemeLocal.courseNumber,
                        isOpenTheme = true,
                        token = userInfoLocal?.token ?: "",
                        dateApi = currentTime.datetime,
                        termDateApi = currentThemeLocal.termDateApi,
                        termHourse = currentThemeLocal.termHourse,
                        lasThemePassed = true
                    )
                    ///Это четвертое действие
                    val updateAnswerModel = UpdateAnswerModel(
                        token = userInfoLocal?.token ?: "",
                        updateThemes = updateThemeModel,
                        AndropointCount = userInfoLocal?.andropointCount
                    )
                    courseRepo.sendMyProgress(updateAnswerModel)
                }

            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            saveKeyTryAgainTheme(currentUniqueThemeId.plus(1))
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemeId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            })

            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:NullPointerException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:HttpException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:Exception){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun checkSubscibe():Boolean{
        val userSubscribes = transactionRepo.getSubscribe()
        userSubscribes?.let { sub->
            val currentDateLocal = Date()
            if (sub.date.time>currentDateLocal.time){
                return true
            }
        }
        return false
    }

    private suspend fun checkBuyCourse():Boolean{
        val buyCourses = transactionRepo.getAllMyCourseBuy()
        buyCourses?.let { buyCoursesNotNull ->
            buyCourses.forEach { oneBuyCourse ->
                if (!oneBuyCourse.andropointBuy) {
                   return true
                }
            }
        }
        return false
    }

    suspend fun thisThemeBuy(isSuccess:((ErrorEnum)->Unit),isBuy:((Boolean)->Unit),uniqueThemeId: Int){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal()
            val buyThemes = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
            buyThemes.forEach { buyTheme->
                if (buyTheme.codeAnswer == 707){
                    isBuy.invoke(false)
                }
                if(buyTheme.uniqueThemeId == uniqueThemeId){
                    isBuy.invoke(true)
                 }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
//            if (checkSubscibe()){
//                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
//            }
            localCheckBuyThemes(uniqueThemeId,{state->
                isSuccess.invoke(state)
            },{isbuy->
                isBuy.invoke(isbuy)
            })
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun checkAndGeIdtLocalBuyThemes(isThemesId:((List<Int>)->Unit)?=null,isBuy:((Boolean)->Unit)){
        val buyThemes = transactionRepo.getAllBuyThemes()
        buyThemes?.let { buyThemesNotNull->
            val buyThemesIdList = ArrayList<Int>()
            buyThemesNotNull.forEach { oneBuyTheme->
                buyThemesIdList.add(oneBuyTheme.uniqueThemeId)
            }
            isThemesId?.invoke(buyThemesIdList)
            isBuy.invoke(true)
        }
        isBuy.invoke(false)
    }
    private suspend fun localCheckBuyThemes(uniqueThemeId:Int,isSuccess:((ErrorEnum)->Unit),isBuy:((Boolean)->Unit)){
        try {
            val themeLocal = transactionRepo.searchBuyTheme(uniqueThemeId)
            themeLocal?.let { themeBuy->
                if (themeBuy.courseNumber==uniqueThemeId) {
                    isBuy.invoke(true)
                }
            }
            isBuy.invoke(false)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException) {
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

}