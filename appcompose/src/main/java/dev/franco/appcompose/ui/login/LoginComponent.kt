package dev.franco.appcompose.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import dev.franco.appcompose.ui.loader.Loader
import dev.franco.login.LoginViewState
import dev.franco.loginapplicationcompose.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoginComponent(
    loginViewModelCompose: LoginViewModelCompose,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    LoginContent(
        user = loginViewModelCompose.loginData.user.toString(),
        onUserChange = {
            loginViewModelCompose.setUser(it)
        },
        password = loginViewModelCompose.loginData.password.toString(),
        onPasswordChange = {
            loginViewModelCompose.setPassword(it)
        },
        onLogin = {
            loginViewModelCompose.login()
        },
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        showPassword = showPassword,
        onShowPassword = {
            showPassword = it
        },
    )

    val message = when (val result = loginViewModelCompose.loginState) {
        is LoginViewState.OnSuccess -> {
            showDialog = true
            result.message
        }
        is LoginViewState.OnError -> {
            showDialog = true
            result.error
        }
        else -> {
            showDialog = false
            null
        }
    }

    if (showDialog) {
        LoginDialog(
            title = stringResource(R.string.attention),
            message = message.toString(),
            onDismissRequest = {
                loginViewModelCompose.idle()
            },
            onConfirmButton = {
                loginViewModelCompose.idle()
            },
        )
    }

    if (loginViewModelCompose.loading) {
        Loader()
    }
}
