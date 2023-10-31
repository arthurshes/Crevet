package workwork.test.andropediagits.core.utils


import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import workwork.test.andropediagits.presenter.signIn.SignInFragment

class AccountGoogleHelper(private val frag: SignInFragment) {
    private var signInClient: GoogleSignInClient? = null

    private fun googleSignInOptions(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("730007309774-2eq73sln1mnq2rcfpheh7rrt6dqg0bhf.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(frag.requireActivity(), gso)
    }


    fun signIn() {
        signInClient = googleSignInOptions()
        val intent = signInClient?.signInIntent
        frag.lunchActGoogle.launch(intent)
    }
}


