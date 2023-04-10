package dev.franco.appxml.ui.login

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
class LoginViewModelXMLTest {
    private val resourceManager = mockk<ResourceManager>()
    private val loginUseCase = mockk<LoginUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var loginViewModelXML: LoginViewModelXML

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        loginViewModelXML = LoginViewModelXML(
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

        loginViewModelXML.login()
        assert(loginViewModelXML.loading.value)
        advanceTimeBy(1000)
        assert(loginViewModelXML.loginState.value is LoginViewState.OnSuccess)
        advanceTimeBy(1000)
        assert(!loginViewModelXML.loading.value)
        assert(resourceManager.getString(1) == successMessage)
    }

    @Test
    fun whenLoginIsCalledThenReturnErrorLogin() = runTest {
        val errorMessage = "Error!"

        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnError(20))
        }

        coEvery { resourceManager.getString(any()) } returns errorMessage

        loginViewModelXML.login()
        assert(loginViewModelXML.loading.value)
        advanceTimeBy(1000)
        assert(loginViewModelXML.loginState.value is LoginViewState.OnError)
        advanceTimeBy(1000)
        assert(!loginViewModelXML.loading.value)
        assert((loginViewModelXML.loginState.value as LoginViewState.OnError).error == errorMessage)
    }

    @Test
    fun whenUserTypesUserAndPasswordInternalDataUpdates() {
        val user = "Franco"
        val password = "Omar"

        with(loginViewModelXML) {
            setUser(user)
            setPassword(password)
        }
        assert(loginViewModelXML.loginData.user == user)
        assert(loginViewModelXML.loginData.password == password)
    }

    @Test
    fun whenLoginIsSuccessfulOrFailedFinalStateIsIdle() = runTest {
        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnError(20))
        }

        coEvery { loginUseCase.login(any(), any()) } returns flow {
            emit(LoginResult.OnSuccess)
        }

        loginViewModelXML.login()
        assert(loginViewModelXML.loginState.value is LoginViewState.Idle)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}
