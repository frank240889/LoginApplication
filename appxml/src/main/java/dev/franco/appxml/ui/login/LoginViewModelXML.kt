package dev.franco.appxml.ui.login

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
import dev.franco.loginapplicationxml.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelXML @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
) : LoginViewModel() {

    private var _loginState = MutableStateFlow<LoginViewState>(LoginViewState.Idle)
    val loginState: StateFlow<LoginViewState> get() = _loginState.asStateFlow()
    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading.asStateFlow()
    val loginData = LoginData()

    override fun setUser(user: CharSequence) {
        loginData.user = user
    }

    override fun setPassword(password: CharSequence) {
        loginData.password = password
    }

    override fun login() {
        _loading.value = true
        login(loginData.user, loginData.password)
    }

    fun idle() {
        _loginState.value = LoginViewState.Idle
    }

    private fun login(
        user: CharSequence,
        password: CharSequence,
    ) = viewModelScope.launch(dispatcher) {
        loginUseCase.login(user, password).collect {
            _loginState.value = getLoginViewStateResult(it)
            _loading.value = false
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
