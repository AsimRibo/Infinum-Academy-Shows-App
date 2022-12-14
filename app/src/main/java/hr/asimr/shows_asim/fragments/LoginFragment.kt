package hr.asimr.shows_asim.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.databinding.FragmentLoginBinding
import hr.asimr.shows_asim.managers.SharedPreferencesManager
import hr.asimr.shows_asim.viewModels.LoginViewModel

const val MIN_PASSWORD_LENGTH = 6
const val LOGIN_PREFERENCES = "LoginPreferences"
const val REMEMBER_ME = "RememberMe"
const val USER_EMAIL = "UserEmail"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (SharedPreferencesManager.readBoolean(REMEMBER_ME)) {
            val email = SharedPreferencesManager.readString(USER_EMAIL, "").orEmpty()
            if (email.isNotEmpty()) goToShows(email)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        animateTriangleImageView()
        animateShowsTitle()
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
        checkIfJustRegistered()
        initListeners()
        initObserving()
    }

    private fun checkIfJustRegistered() {
        if(SharedPreferencesManager.readBoolean(JUST_REGISTERED)){
            binding.tvLoginHeader.text = getString(R.string.registration_successful)
            SharedPreferencesManager.writeBoolean(JUST_REGISTERED, false)
        }
    }

    private fun initObserving() {
        viewModel.getLoginResultLiveData().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                SharedPreferencesManager.writeBoolean(REMEMBER_ME, binding.chbRememberMe.isChecked)
                SharedPreferencesManager.writeString(USER_EMAIL, binding.etEmail.text.toString())

                goToShows(binding.etEmail.text.toString())
            } else {
                Toast.makeText(requireContext(), R.string.invalid_credentials, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.formValid.observe(viewLifecycleOwner) { form ->
            if (form.isValid) {
                viewModel.loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            } else {
                showEmailMessage(getString(R.string.invalid_email))
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressIndicator.isVisible = loading
        }
    }

    private fun initListeners() {
        initEditTextListeners()
        initButtonListeners()
    }

    private fun initButtonListeners() {
        initButtonLogin()
        initButtonRegister()
    }

    private fun initButtonRegister() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            resetValues()
        }
    }

    private fun initButtonLogin() {
        binding.btnLogin.setOnClickListener {
            viewModel.validateForm(binding.etEmail.text.toString())
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

    private fun animateTriangleImageView() = with(binding.ivTriangle) {
        translationY = -1600f
        animate()
            .translationY(0f)
            .setInterpolator(BounceInterpolator())
            .setDuration(1000)
            .start()
    }

    private fun animateShowsTitle() = with(binding.tvShowsHeader) {
        alpha = 0f
        scaleX = 0.5f
        scaleY = 0.5f
        animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setInterpolator(OvershootInterpolator())
            .setDuration(500)
            .setStartDelay(1000)
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}