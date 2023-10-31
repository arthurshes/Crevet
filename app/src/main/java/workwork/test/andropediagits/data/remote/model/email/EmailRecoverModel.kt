package workwork.test.andropediagits.data.remote.model.email

data class EmailRecoverModel(
    val email:String,
    val code:String,
    val newPassword:String,
    val lang:String
)
