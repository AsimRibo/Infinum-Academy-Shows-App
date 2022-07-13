package hr.asimr.shows_asim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import hr.asimr.shows_asim.databinding.ActivityLoginBinding
import hr.asimr.shows_asim.utils.isEmailValid

const val MIN_PASSWORD_LENGTH = 6
const val EMAIL_ERROR = "Please provide a valid email address"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        initEditTextListeners()
        initButtonListeners()
    }

    private fun initButtonListeners() {
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmailValid()) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.putExtra("Email", binding.etEmail.text.toString())
                startActivity(intent)
            } else {
                showEmailMessage(EMAIL_ERROR)
            }
        }
    }

    private fun initEditTextListeners() {
        binding.etEmail.addTextChangedListener { handleLoginButton() }
        binding.etPassword.addTextChangedListener { handleLoginButton() }
    }

    private fun handleLoginButton() {
        showEmailMessage("")

        var enable: Boolean

        (!binding.etEmail.text?.toString().isNullOrEmpty()
                && (binding.etPassword.text?.length ?: 0) >= MIN_PASSWORD_LENGTH).also {
            enable = it
        }


        handleButtonOpacity(enable, binding.btnLogin)

        binding.btnLogin.isEnabled = enable
    }

    private fun showEmailMessage(message: String) {
        binding.tilEmail.error = message
    }

    private fun handleButtonOpacity(enabled: Boolean, button: Button) {
        button.alpha = if (enabled) 1f else 0.5f
    }
}