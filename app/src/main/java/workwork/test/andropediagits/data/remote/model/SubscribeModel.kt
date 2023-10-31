package workwork.test.andropediagits.data.remote.model

import java.util.Date

data class SubscribeModel(
    val dateBuy: String,
    val promoCode:String?=null,
    val term:Int,
    val token:String,
    val codeAnswer:Int?=null,
    val transactionId:String,
    val dateBuyForDate: Date?=null
)
