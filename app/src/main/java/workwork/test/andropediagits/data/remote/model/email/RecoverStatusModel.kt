package workwork.test.andropediagits.data.remote.model.email

data class RecoverStatusModel(
    val codeAnswer:Int,
    ///if 989 incorrect code email
    ///if 212 success
    /// if 606 backend error
    /// if 206 passwod chaged
    /// if 103 code sent отправили код впервые
    /// if 222 correct code email
    /// if 124 code exist код уже отправлен
    ///if 508 email is not exist
    val message:String,
    val status:Boolean
)
