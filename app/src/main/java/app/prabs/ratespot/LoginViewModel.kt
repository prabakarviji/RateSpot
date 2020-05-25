package app.prabs.ratespot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LoginViewModel:ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        Log.i("main",user.toString())
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}