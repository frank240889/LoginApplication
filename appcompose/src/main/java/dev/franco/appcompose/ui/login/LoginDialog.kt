package dev.franco.appcompose.ui.login

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import dev.franco.appcompose.ui.theme.LoginApplicationComposeTheme
import dev.franco.loginapplicationcompose.R

@Composable
internal fun LoginDialog(
    title: CharSequence,
    message: CharSequence,
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = title.toString(),
                modifier = Modifier
                    .testTag("login_dialog_title")
                    .layoutId("login_dialog_title"),
            )
        },
        text = {
            Text(
                text = message.toString(),
                modifier = Modifier
                    .testTag("login_dialog_text")
                    .layoutId("login_dialog_text"),
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            OutlinedButton(
                onClick = onConfirmButton,
                shape = RoundedCornerShape(
                    LoginApplicationComposeTheme.orientation.defaultCornerRadius,
                ),
                modifier = Modifier
                    .testTag("login_dialog_confirm_button")
                    .layoutId("login_dialog_confirm_button"),
            ) {
                Text(
                    text = stringResource(R.string.accept).uppercase(),
                )
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        shape = RoundedCornerShape(
            LoginApplicationComposeTheme.orientation.defaultCornerRadius,
        ),
        modifier = Modifier
            .testTag("login_dialog")
            .layoutId("login_dialog"),
    )
}

@Preview
@Composable
private fun LoginDialogPreview() {
    LoginApplicationComposeTheme {
        LoginDialog(
            title = "Título",
            message = "El mensaje",
            onDismissRequest = {},
            onConfirmButton = {},
        )
    }
}
