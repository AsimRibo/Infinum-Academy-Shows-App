package hr.asimr.shows_asim.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.asimr.shows_asim.databinding.FragmentLoginBinding
import hr.asimr.shows_asim.utils.isEmailValid

const val MIN_PASSWORD_LENGTH = 6
const val EMAIL_ERROR = "Please provide a valid email address"
const val LOGIN_PREFERENCES = "LoginPreferences"
const val REMEMBER_ME = "RememberMe"
const val USER_EMAIL = "UserEmail"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginPreferences = requireContext().getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
        if (loginPreferences.getBoolean(REMEMBER_ME, false)) {
            goToShows(loginPreferences.getString(USER_EMAIL, "").orEmpty())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun goToShows(email: String) {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToShowsFragment(
                email
            )
        )
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
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmailValid()) {
                loginPreferences.edit {
                    putBoolean(REMEMBER_ME, binding.chbRememberMe.isChecked)
                    if (binding.chbRememberMe.isChecked) {
                        putString(USER_EMAIL, binding.etEmail.text.toString())
                    }
                }
                goToShows(binding.etEmail.text.toString())
                resetValues()
            } else {
                showEmailMessage(EMAIL_ERROR)
            }
        }
    }

    private fun resetValues() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.chbRememberMe.isChecked = false
    }

    private fun initEditTextListeners() {
        binding.etEmail.addTextChangedListener { handleLoginButton() }
        binding.etPassword.addTextChangedListener { handleLoginButton() }
    }

    private fun handleLoginButton() {
        showEmailMessage("")

        val enable = !binding.etEmail.text?.toString().isNullOrEmpty()
            && (binding.etPassword.text?.length ?: 0) >= MIN_PASSWORD_LENGTH

        handleButtonOpacity(enable, binding.btnLogin)

        binding.btnLogin.isEnabled = enable
    }

    private fun showEmailMessage(message: String) {
        binding.tilEmail.error = message
    }

    private fun handleButtonOpacity(enabled: Boolean, button: Button) {
        button.alpha = if (enabled) 1f else 0.5f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}