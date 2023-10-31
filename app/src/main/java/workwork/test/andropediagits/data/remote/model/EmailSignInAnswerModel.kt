package workwork.test.andropediagits.data.remote.model

data class EmailSignInAnswerModel(
    val token:String,
    val isRegister:Boolean,
    val codeAnswer:Int,
    val status:Boolean
)
