package workwork.test.andropediagits.data.remote.individualCourseGet

data class SnedRatingIndiModel(
    val creatorToken:String,
    val uniqueCourseNumber:Int,
    val raiting:Float,
    val raitingText:String?=null,
    val raitingDate:String,
    val token:String
)
