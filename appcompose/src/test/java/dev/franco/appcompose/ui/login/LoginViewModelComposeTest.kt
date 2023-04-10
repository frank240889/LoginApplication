package dev.franco.appcompose.ui.login

import dev.franco.login.LoginViewState
import dev.franco.login.helper.ResourceManager
import dev.franco.login.usecase.LoginResult
import dev.franco.login.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelComposeTest {
    private val resourceManager = mockk<ResourceManager>()
    private val loginUseCase = mockk<LoginUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var loginViewModelCompose: LoginViewModelCompose

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        loginViewModelCompose = LoginViewModelCompose(
            loginUseCase,
            testDispatcher,
            resourceManager,
        )
    }

    @Test
    fun whenLoginIsCalledThenReturnSuccessLogin() = runTest {
        val successMessage = "Éxito!"

        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnSuccess)
        }

        coEvery { resourceManager.getString(any()) } returns successMessage

        loginViewModelCompose.login()
        assert(loginViewModelCompose.loading)
        advanceTimeBy(1000)
        assert(loginViewModelCompose.loginState is LoginViewState.OnSuccess)
        advanceTimeBy(1000)
        assert(!loginViewModelCompose.loading)
        assert(resourceManager.getString(1) == successMessage)
    }

    @Test
    fun whenLoginIsCalledThenReturnErrorLogin() = runTest {
        val errorMessage = "Error!"

        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnError(20))
        }

        coEvery { resourceManager.getString(any()) } returns errorMessage

        loginViewModelCompose.login()
        assert(loginViewModelCompose.loading)
        advanceTimeBy(1000)
        assert(loginViewModelCompose.loginState is LoginViewState.OnError)
        advanceTimeBy(1000)
        assert(!loginViewModelCompose.loading)
        assert((loginViewModelCompose.loginState as LoginViewState.OnError).error == errorMessage)
    }

    @Test
    fun whenUserTypesUserAndPasswordInternalDataUpdates() {
        val user = "Franco"
        val password = "Omar"

        with(loginViewModelCompose) {
            setUser(user)
            setPassword(password)
        }
        assert(loginViewModelCompose.loginData.user == user)
        assert(loginViewModelCompose.loginData.password == password)
    }

    @Test
    fun whenLoginIsSuccessfulOrFailedFinalStateIsIdle() = runTest {
        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnError(20))
        }

        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnSuccess)
        }

        loginViewModelCompose.login()
        loginViewModelCompose.idle()
        assert(loginViewModelCompose.loginState is LoginViewState.Idle)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}
