package workwork.test.andropediagits.data.remote.model.email

enum class RecoverPassState {
    NOTNETWORK,
    ERROR,
    SUCCESS,
    UNKNOWNERROR,
    TIMEOUTERROR,
    NULLPOINTERROR,
    EMAILISEMPTY,
    CODEEXIST,
    INCORRECTCODE,
    CORRECTCODE,
    CODENOTEXIST,
    PASSWORDCHANGE,
    EMAILISNOTEXIST,
    CODESENDEMAIL
}