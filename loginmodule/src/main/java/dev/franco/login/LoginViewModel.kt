package dev.franco.login

import androidx.lifecycle.ViewModel

abstract class LoginViewModel : ViewModel() {
    abstract fun setUser(user: CharSequence)

    abstract fun setPassword(password: CharSequence)

    abstract fun login()
}

sealed class LoginViewState {
    class OnSuccess(val message: CharSequence) : LoginViewState()
    class OnError(val error: CharSequence) : LoginViewState()
    object Idle : LoginViewState()
}

data class LoginData(
    var user: CharSequence = "",
    var password: CharSequence = "",
)
