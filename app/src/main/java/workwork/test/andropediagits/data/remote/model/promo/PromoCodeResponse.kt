package workwork.test.andropediagits.data.remote.model.promo

import java.util.Date

data class PromoCodeResponse(
    val status:Boolean,
    val promoExist:Boolean,
    val message:String,
    val codeAnswer:Int,
    val promoDate:String,
    val userPromoExist:Boolean
)
