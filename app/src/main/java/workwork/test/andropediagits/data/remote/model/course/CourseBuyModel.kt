package workwork.test.andropediagits.data.remote.model.course



data class CourseBuyModel(
    val courseNumber: Int,
    val dateBuy: String?=null,
//    val dateBuyForDate: Date? = null,
    val token: String,
    val promoCode: String? = null,
    val transactionId: String,
    val codeAnswer: Int? = null,
    val andropointBuy:Int,
    val andropointMinus:Int?=null
)



// Define the extension function here, outside the CourseBuyModel class
/*
fun CourseBuyEntity.toModel(): CourseBuyModel {
    return CourseBuyModel(
        courseNumber = this.courseNumber,
        dateBuy = this.d,
        dateBuyForDate = this.dateBuyForDate,
        token = this.token,
        promoCode = this.promoCode,
        transactionId = this.transactionId,
        codeAnswer = this.codeAnswer
    )
}*/
