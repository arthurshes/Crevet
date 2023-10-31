package workwork.test.andropediagits.data.remote.model.promo

import java.util.Date

data class PromoCodeCheckModel(
    val isActual:Boolean,
    val status:Boolean,
    val endActualDate:Date,
    val promoCode:String
)
