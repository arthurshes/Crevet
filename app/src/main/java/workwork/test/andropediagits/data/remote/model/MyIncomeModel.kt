package workwork.test.andropediagits.data.remote.model

data class MyIncomeModel(
    val currentMouthName:String,
    val mouthIncome:Int,
    val incomeTax:Int,
    val coursesIncome:List<IncomeCourses>,
    val incomeLastThree:List<IncomeMouthModel>,
    val myRaitingLastThreeMouth:List<MyCourseRatingMouthModel>
)
