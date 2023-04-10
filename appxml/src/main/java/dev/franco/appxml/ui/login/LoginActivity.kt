package dev.franco.appxml.ui.login

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.franco.login.LoginViewState
import dev.franco.loginapplicationxml.R
import dev.franco.loginapplicationxml.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModelXML: LoginViewModelXML by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private var dialogResult: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        startCoroutines()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogResult?.dismiss()
        dialogResult = null
    }

    private fun setupListeners() {
        with(binding) {
            tietUsername.doOnTextChanged { text, _, _, _ ->
                loginViewModelXML.setUser(text ?: "")
            }
            tietPassword.doOnTextChanged { text, _, _, _ ->
                loginViewModelXML.setPassword(text ?: "")
            }
            mbLogin.setOnClickListener {
                currentFocus?.clearFocus()
                loginViewModelXML.login()
            }
        }
    }

    private fun startCoroutines() {
        with(lifecycleScope) {
            launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    loginViewModelXML.loading.collect {
                        binding.flBlockingLayer.visibility = if (it) {
                            VISIBLE
                        } else {
                            GONE
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    loginViewModelXML.loginState.collect {
                        if (it is LoginViewState.OnSuccess) {
                            showLoginDialog(
                                getString(R.string.attention),
                                it.message,
                            )
                        } else if (it is LoginViewState.OnError) {
                            showLoginDialog(
                                getString(R.string.attention),
                                it.error,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showLoginDialog(
        title: String,
        message: CharSequence,
    ) {
        dialogResult = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.accept) { dialog, _ ->
                loginViewModelXML.idle()
                dialog?.dismiss()
            }
            .create()

        dialogResult?.show()
    }
}
