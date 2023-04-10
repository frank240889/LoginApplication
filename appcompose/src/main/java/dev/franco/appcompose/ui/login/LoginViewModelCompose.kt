package dev.franco.appcompose.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.franco.login.LoginData
import dev.franco.login.LoginViewModel
import dev.franco.login.LoginViewState
import dev.franco.login.data.source.CONNECTION_ERROR
import dev.franco.login.data.source.INVALID_CREDENTIALS_ERROR
import dev.franco.login.data.source.SERVER_ERROR
import dev.franco.login.helper.ResourceManager
import dev.franco.login.usecase.EMPTY_PASSWORD
import dev.franco.login.usecase.EMPTY_USER
import dev.franco.login.usecase.LoginResult
import dev.franco.login.usecase.LoginUseCase
import dev.franco.loginapplicationcompose.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelCompose @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
) : LoginViewModel() {

    private var _loginState by mutableStateOf<LoginViewState?>(null)
    val loginState: LoginViewState? get() = _loginState
    private var _loading by mutableStateOf(false)
    val loading: Boolean get() = _loading
    private var _loginData by mutableStateOf(LoginData())
    val loginData: LoginData get() = _loginData

    override fun setUser(user: CharSequence) {
        _loginData = _loginData.copy(user = user)
    }

    override fun setPassword(password: CharSequence) {
        _loginData = _loginData.copy(password = password)
    }

    override fun login() {
        _loading = true
        login(loginData.user, loginData.password)
    }

    fun idle() {
        _loginState = LoginViewState.Idle
    }

    private fun login(
        user: CharSequence,
        password: CharSequence,
    ) = viewModelScope.launch(dispatcher) {
        loginUseCase.login(user, password).collect {
            _loginState = getLoginViewStateResult(it)
            _loading = false
        }
    }

    private fun getLoginViewStateResult(it: LoginResult): LoginViewState {
        return if (it is LoginResult.OnSuccess) {
            LoginViewState.OnSuccess(resourceManager.getString(R.string.success_login))
        } else {
            val error = it as LoginResult.OnError
            LoginViewState.OnError(
                when (error.code) {
                    EMPTY_USER -> {
                        resourceManager.getString(R.string.empty_user)
                    }
                    EMPTY_PASSWORD -> {
                        resourceManager.getString(R.string.empty_password)
                    }
                    INVALID_CREDENTIALS_ERROR -> {
                        resourceManager.getString(R.string.invalid_credentials)
                    }
                    SERVER_ERROR -> {
                        resourceManager.getString(R.string.server_error)
                    }
                    CONNECTION_ERROR -> {
                        resourceManager.getString(R.string.io_error)
                    }
                    else -> {
                        resourceManager.getString(R.string.timeout_error)
                    }
                },
            )
        }
    }
}
