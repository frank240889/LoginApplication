package dev.franco.appcompose.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import dev.franco.appcompose.ui.theme.DEFAULT_MAX_LENGTH_INPUT_PASSWORD
import dev.franco.appcompose.ui.theme.DEFAULT_MAX_LENGTH_INPUT_USER
import dev.franco.appcompose.ui.theme.LoginApplicationComposeTheme
import dev.franco.loginapplicationcompose.R

@Composable
internal fun LoginContent(
    modifier: Modifier = Modifier,
    showPassword: Boolean = false,
    onShowPassword: (Boolean) -> Unit,
    user: CharSequence,
    onUserChange: (CharSequence) -> Unit,
    password: CharSequence,
    onPasswordChange: (CharSequence) -> Unit,
    onLogin: () -> Unit,
) {
    val focusRequest = LocalFocusManager.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = LoginApplicationComposeTheme.orientation.defaultHorizontalPadding,
                vertical = LoginApplicationComposeTheme.orientation.defaultVerticalPadding,
            )
            .testTag("card_login_content")
            .layoutId("card_login_content"),
        shape = RoundedCornerShape(LoginApplicationComposeTheme.orientation.defaultCornerRadius),
        elevation = LoginApplicationComposeTheme.orientation.defaultElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    LoginApplicationComposeTheme.orientation.defaultPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "",
                Modifier.size(LoginApplicationComposeTheme.orientation.defaultIconSize),
            )

            Spacer(
                modifier = Modifier
                    .height(LoginApplicationComposeTheme.orientation.defaultVerticalSpacing),
            )

            OutlinedTextField(
                value = user.toString(),
                onValueChange = {
                    onUserChange.invoke(it.take(DEFAULT_MAX_LENGTH_INPUT_USER))
                },
                label = {
                    Text(
                        text = stringResource(R.string.email_or_user),
                    )
                },
                maxLines = 1,
                singleLine = true,
                shape = RoundedCornerShape(
                    LoginApplicationComposeTheme.orientation.defaultCornerRadius,
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(ImeAction.Next)
                },
                modifier = Modifier
                    .testTag("login_user_input")
                    .layoutId("login_user_input"),
            )

            Spacer(
                modifier = Modifier
                    .height(LoginApplicationComposeTheme.orientation.defaultVerticalSpacing),
            )

            OutlinedTextField(
                value = password.toString(),
                onValueChange = {
                    onPasswordChange.invoke(it.take(DEFAULT_MAX_LENGTH_INPUT_PASSWORD))
                },
                label = {
                    Text(
                        text = stringResource(R.string.password),
                    )
                },
                visualTransformation = if (!showPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onShowPassword.invoke(showPassword.not())
                        },
                        modifier = Modifier,
                    ) {
                        Icon(
                            imageVector = if (!showPassword) {
                                Icons.Rounded.Visibility
                            } else {
                                Icons.Rounded.VisibilityOff
                            },
                            contentDescription = "",
                        )
                    }
                },
                maxLines = 1,
                singleLine = true,
                shape = RoundedCornerShape(
                    LoginApplicationComposeTheme.orientation.defaultCornerRadius,
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(ImeAction.Done)
                },
                modifier = Modifier
                    .testTag("login_password_input")
                    .layoutId("login_password_input"),
            )

            Spacer(
                modifier = Modifier
                    .height(LoginApplicationComposeTheme.orientation.defaultVerticalSpacing),
            )

            Button(
                onClick = {
                    onLogin.invoke()
                    focusRequest.clearFocus()
                },
                shape = RoundedCornerShape(
                    LoginApplicationComposeTheme.orientation.defaultCornerRadius,
                ),
                modifier = Modifier
                    .testTag("login_button")
                    .layoutId("login_button"),
            ) {
                Text(
                    text = stringResource(
                        R.string.login,
                    ).uppercase(),
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LoginContentPreview() {
    LoginApplicationComposeTheme {
        LoginContent(
            user = "franco omar",
            onUserChange = {},
            password = "franco",
            onPasswordChange = {},
            onLogin = {},
            onShowPassword = {},
        )
    }
}
