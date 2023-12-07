package workwork.test.andropediagits.data.remote.individualCourseGet

data class GetModerationCourseStatusModel(
    val moderationStatus:Int,
    val isBanned:Boolean,
    val reasonForBan:String?=null
)
