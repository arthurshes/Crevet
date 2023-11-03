package workwork.test.andropediagits.domain.useCases.userLogic

import android.annotation.SuppressLint
import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns.VICTORINE_REMUNERATION
import workwork.test.andropediagits.crashInspector.domain.useCase.CrashUseCase
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.AddAndropoints
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import workwork.test.andropediagits.domain.useCases.userLogic.state.StrikeModeState
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class AndropointUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo, private val transactionRepo: TransactionRepo, private val updateUserInfoUseCase: UpdateUserInfoUseCase, private val crashUseCase: CrashUseCase) {

    suspend fun addAndropoint(isSuccess:((ErrorEnum)->Unit), addAndropoints: AddAndropoints){
        try {
            when(addAndropoints){
                AddAndropoints.ADDANDROPOINTSVIEWAD -> {
                    updateAndropointPlus(andropointCount = 1)
                }
                AddAndropoints.BUYONEANDROPOINT -> {
                    updateAndropointPlus(1)
                }
                AddAndropoints.BUYTENANDROPOINT -> {
                    updateAndropointPlus(10)
                }
                AddAndropoints.BUYHUNDREDANDROPOINT -> {
                    updateAndropointPlus(100)
                }
                AddAndropoints.INFINITYANDROPOINTS ->{
                    updateAndropointPlus(999999999)
                }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            crashUseCase.sendCrash(exception = e.message(), className = this.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:Exception){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            crashUseCase.sendCrash(exception = e.message ?: "unknown error", className = this.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }catch (e: TimeoutException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            crashUseCase.sendCrash(exception = e.message ?: "timeout error", className = this.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:NullPointerException){
            Log.d("victorineerirjirjgigjitjgt",e.toString())
            crashUseCase.sendCrash(exception = e.message ?: "nullpoint exception", className = this.toString())
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun victorineProgressAndropoint(isSuccess:((ErrorEnum)->Unit)){
            try {
                updateAndropointPlus(andropointCount = VICTORINE_REMUNERATION)
                Log.d("isVicotrineAndropidneife222","isVictorineAndropointadd")
                isSuccess.invoke(ErrorEnum.SUCCESS)
            }catch (e:IOException){
                if(checkSubscibe()){
                    isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                    return
                }
                if(checkBuyCourse()){
                    isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                    return
                }
                isSuccess.invoke(ErrorEnum.NOTNETWORK)
            }catch (e:HttpException){
                Log.d("victorineerirjirjgigjitjgt",e.toString())
                crashUseCase.sendCrash(exception = e.message(), className = this.toString())
                isSuccess.invoke(ErrorEnum.ERROR)
            }catch (e:Exception){
                Log.d("victorineerirjirjgigjitjgt",e.toString())
                crashUseCase.sendCrash(exception = e.message ?: "unknown error", className = this.toString())
                isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
            }catch (e: TimeoutException){
                Log.d("victorineerirjirjgigjitjgt",e.toString())
                crashUseCase.sendCrash(exception = e.message ?: "timeout error", className = this.toString())
                isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
            }catch (e:NullPointerException){
                Log.d("victorineerirjirjgigjitjgt",e.toString())
                crashUseCase.sendCrash(exception = e.message ?: "nullpoint exception", className = this.toString())
            }
    }


////last update version
//    suspend fun interactiveProgressAndropoint(correctAnswer: Int,isSuccess: ((ErrorEnum) -> Unit)){
//        val andropointCount = INTERACTIVE_REMUNERATION
//        try {
//             updateAndropointPlus(andropointCount)
//                isSuccess.invoke(ErrorEnum.SUCCESS)
//        }catch (e:IOException){
//            if(checkSubscibe()){
//                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
//            }
//            if(checkBuyCourse()){
//                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
//            }
//            isSuccess.invoke(ErrorEnum.NOTNETWORK)
//        }catch (e:HttpException){
//            isSuccess.invoke(ErrorEnum.ERROR)
//        }catch (e:Exception){
//            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
//        }catch (e: TimeoutException){
//            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
//        }
//    }

    suspend fun strikeModeAddAndropoint(isSuccess: ((ErrorEnum) -> Unit), strikeModeState: StrikeModeState, buyThemesId:((List<Int>)->Unit)?=null) {
        when (strikeModeState) {
            StrikeModeState.ONE -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(1 * 2)
                        } else {
                            val promoLocal = userLogicRepo.getAllMyPromo()
                            if(promoLocal!=null){
                                val promoCodeModel = PromoCodeModel(userInfo?.token ?: "",promoLocal?.promoCode ?: "" , currentDate.datetime)
                                val responsePromoActual = userLogicRepo.checkActualMySubscribe(promoCodeModel)
                                if (responsePromoActual.isActual) {
                                    updateAndropointPlus( 1 * 2)
                                    return
                                }
                            }
                                updateAndropointPlus( 1)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }


            }

            StrikeModeState.TWO -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(2 * 2)
                        } else {
                            val promoLocal = userLogicRepo.getAllMyPromo()
                            if (promoLocal!=null){
                                val promoCodeModel = PromoCodeModel(userInfo?.token ?: "",promoLocal?.promoCode ?: "" , currentDate.datetime)
                                val responsePromoActual = userLogicRepo.checkActualMySubscribe(promoCodeModel)
                                if (responsePromoActual.isActual) {
                                    updateAndropointPlus( 2 * 2)
                                    return
                                }
                            }
                                updateAndropointPlus( 2)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }
            }

            StrikeModeState.THREE -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(3 * 2)
                        } else {
                            val promoLocal = userLogicRepo.getAllMyPromo()
                            if(promoLocal!=null){
                                val promoCodeModel = PromoCodeModel(userInfo?.token ?: "",promoLocal?.promoCode ?: "" , currentDate.datetime)
                                val responsePromoActual = userLogicRepo.checkActualMySubscribe(promoCodeModel)
                                if (responsePromoActual.isActual) {
                                    updateAndropointPlus( 3 * 2)
                                    return
                                }
                            }
                            updateAndropointPlus( 3)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }
            }

            StrikeModeState.FOUR -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(4 * 2)
                        } else {
                            updateAndropointPlus( 4)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }
            }

            StrikeModeState.FIVE -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(8 * 2)
                        } else {
                                updateAndropointPlus( 8)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }
            }

            StrikeModeState.SIX -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(12 * 2)
                        } else {
                                updateAndropointPlus( 12)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }
            }

            StrikeModeState.SEVEN -> {
                val userInfo = userLogicRepo.getUserInfoLocal()
                userInfo?.let { myInfo ->
                    try {
                        val currentDate = transactionRepo.getCurrentTime()
                        val sendSubscribeCheckModel =
                            SendSubscribeCheckModel(myInfo?.token ?: "", currentDate.datetime)
                        val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                        if (checkSubs.subscribeIsActual) {
                            updateAndropointPlus(18 * 2)
                        } else {
                                updateAndropointPlus( 18)
                        }
                        isSuccess.invoke(ErrorEnum.SUCCESS)
                    }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        if(checkBuyCourse()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                        checkAndGeIdtLocalBuyThemes({
                            buyThemesId?.invoke(it)
                        },{isBUy->
                            if (isBUy){
                                isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                            }
                        })
                        isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    }catch (e:HttpException){
                        isSuccess.invoke(ErrorEnum.ERROR)
                    }catch (e:Exception){
                        isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    }catch (e: TimeoutException){
                        isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    }
                }
            }
        }
    }

    suspend fun spendAndropoints(spendAndropointState: SpendAndropointState,
                                 isSuccess:((ErrorEnum)->Unit), isAndropointState:((BuyForAndropointStates)->Unit), buyThemesId: ((List<Int>) -> Unit)?=null,andropointMinusCount: Int) {
        when (spendAndropointState) {
            SpendAndropointState.SKIPDELAY -> {
                try {
                    updateAndropointMinus(andropointMinusCount,{
                        when(it){
                            BuyForAndropointStates.YESMONEY ->           isAndropointState.invoke(BuyForAndropointStates.YESMONEY)
                            BuyForAndropointStates.NOMONEY ->           isAndropointState.invoke(BuyForAndropointStates.NOMONEY)
                        }
                    })
                    isSuccess.invoke(ErrorEnum.SUCCESS)
                } catch (e: IOException) {
                    if(checkSubscibe()){
                        isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                        return
                    }
                    if(checkBuyCourse()){
                        isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                        return
                    }
                    checkAndGeIdtLocalBuyThemes({
                        buyThemesId?.invoke(it)
                    },{isBUy->
                        if (isBUy){
                            isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                        }
                    })
                    isSuccess.invoke(ErrorEnum.NOTNETWORK)
                } catch (e: HttpException) {
                    isSuccess.invoke(ErrorEnum.ERROR)
                }catch (e:Exception){
                    isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                }catch (e: TimeoutException){
                    isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                }
            }
            SpendAndropointState.COURSEOPENING -> {
                try {
                    updateAndropointMinus(andropointMinusCount,{
                        when(it){
                            BuyForAndropointStates.YESMONEY ->           isAndropointState.invoke(BuyForAndropointStates.YESMONEY)
                            BuyForAndropointStates.NOMONEY ->           isAndropointState.invoke(BuyForAndropointStates.NOMONEY)
                        }
                    })
                    isSuccess.invoke(ErrorEnum.SUCCESS)
                } catch (e: IOException) {
                    if(checkSubscibe()){
                        isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                        return
                    }
                    if(checkBuyCourse()){
                        isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                        return
                    }
                    checkAndGeIdtLocalBuyThemes({
                        buyThemesId?.invoke(it)
                    },{isBUy->
                        if (isBUy){
                            isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                        }
                    })
                    isSuccess.invoke(ErrorEnum.NOTNETWORK)
                } catch (e: HttpException) {
                    isSuccess.invoke(ErrorEnum.ERROR)
                }catch (e:Exception){
                    isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                }catch (e: TimeoutException){
                    isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                }
            }
            SpendAndropointState.THEMEOPENING -> {
                try {
                    updateAndropointMinus(andropointMinusCount,{
                        when(it){
                            BuyForAndropointStates.YESMONEY ->           isAndropointState.invoke(BuyForAndropointStates.YESMONEY)
                            BuyForAndropointStates.NOMONEY ->           isAndropointState.invoke(BuyForAndropointStates.NOMONEY)
                        }
                    })
                    isSuccess.invoke(ErrorEnum.SUCCESS)
                }catch (e:IOException){
                        if(checkSubscibe()){
                            isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                            return
                        }
                    if(checkBuyCourse()){
                        isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                        return
                    }
                    checkAndGeIdtLocalBuyThemes({
                        buyThemesId?.invoke(it)
                    },{isBUy->
                        if (isBUy){
                            isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)

                        }
                    })
                    isSuccess.invoke(ErrorEnum.NOTNETWORK)
                }catch (e:HttpException){
                    isSuccess.invoke(ErrorEnum.ERROR)
                }catch (e:Exception){
                    isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                }catch (e: TimeoutException){
                    isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                }
            }
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

    private suspend fun updateAndropointPlus(andropointCount:Int) {
        val userInfoLocal = userLogicRepo.getUserInfoLocal()
        userInfoLocal?.let { myInfo ->
            updateUserInfoUseCase.updateUserInfo(andropointCount = myInfo.andropointCount?.plus(andropointCount))
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

    private suspend fun updateAndropointMinus(andropointMinusCount:Int,isBuy:((BuyForAndropointStates)->Unit)){
        val currentMyInfo = userLogicRepo.getUserInfoLocal()


        currentMyInfo?.let { myInfo ->
            if (myInfo.andropointCount ?: 0 >=andropointMinusCount) {


                updateUserInfoUseCase.updateUserInfo(andropointCount = myInfo.andropointCount?.minus(andropointMinusCount))
                isBuy.invoke(BuyForAndropointStates.YESMONEY)
                Log.d("andropointCount", "myInfo.andropointCount " + myInfo.andropointCount.toString())
                Log.d("andropointCount", "current" + currentMyInfo.andropointCount.toString())
            }else{
                isBuy.invoke(BuyForAndropointStates.NOMONEY)
            }
        }
    }



}