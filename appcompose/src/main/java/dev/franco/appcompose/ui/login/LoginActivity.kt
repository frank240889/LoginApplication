package dev.franco.appcompose.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import dagger.hilt.android.AndroidEntryPoint
import dev.franco.appcompose.ui.theme.LoginApplicationComposeTheme
import dev.franco.loginapplicationcompose.R

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val loginViewModelCompose: LoginViewModelCompose by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginApplicationComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Image(
                        painter = painterResource(R.drawable.bg_login),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )

                    LoginComponent(loginViewModelCompose)
                }
            }
        }
    }
}
