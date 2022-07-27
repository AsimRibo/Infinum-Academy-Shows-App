package hr.asimr.shows_asim.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.databinding.FragmentRegisterBinding
import hr.asimr.shows_asim.utils.isEmailValid

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        initEditTextListeners()
        initButtonListeners()
    }

    private fun initButtonListeners() {
        binding.btnRegister.setOnClickListener {
            if (binding.etEmail.text.toString().isEmailValid()
                && binding.etPassword.text.toString() == binding.etPasswordRepeat.text.toString()) {
                //go to login
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

        val enable = !binding.etEmail.text?.toString().isNullOrEmpty()
            && (binding.etPassword.text?.length ?: 0) >= MIN_PASSWORD_LENGTH
            && (binding.etPasswordRepeat.text?.length ?: 0) >= MIN_PASSWORD_LENGTH

        handleButtonOpacity(enable, binding.btnRegister)

        binding.btnRegister.isEnabled = enable
    }

    private fun showEmailMessage(message: String) {
        binding.tilEmail.error = message
    }

    private fun handleButtonOpacity(enabled: Boolean, button: Button) {
        button.alpha = if (enabled) 1f else 0.5f
    }
}