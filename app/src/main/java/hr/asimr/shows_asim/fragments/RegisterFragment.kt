package hr.asimr.shows_asim.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.databinding.FragmentRegisterBinding
import hr.asimr.shows_asim.models.FormFields
import hr.asimr.shows_asim.viewModels.RegisterViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObserving()
    }

    private fun initObserving() {
        viewModel.getRegistrationResultLiveData().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), R.string.registration_fail, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.formValid.observe(viewLifecycleOwner) { formData ->
            if (formData.isValid) {
                viewModel.registerUser(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etPasswordRepeat.text.toString()
                )
            } else {
                when(formData.field){
                    FormFields.USER_EMAIL -> showErrorMessage(getString(R.string.invalid_email), binding.tilEmail)
                    FormFields.USER_PASSWORD -> showErrorMessage(getString(R.string.password_mismatch), binding.tilPassword)
                }
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
        binding.btnRegister.setOnClickListener {
            viewModel.validateForm(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordRepeat.text.toString()
            )
        }
    }

    private fun initEditTextListeners() {
        binding.etEmail.addTextChangedListener { handleLoginButton() }
        binding.etPassword.addTextChangedListener { handleLoginButton() }
        binding.etPasswordRepeat.addTextChangedListener { handleLoginButton() }
    }

    private fun handleLoginButton() {
        showErrorMessage("", binding.tilEmail)
        showErrorMessage("", binding.tilPassword)

        val enable = !binding.etEmail.text?.toString().isNullOrEmpty()
            && (binding.etPassword.text?.length ?: 0) >= MIN_PASSWORD_LENGTH
            && (binding.etPasswordRepeat.text?.length ?: 0) >= MIN_PASSWORD_LENGTH

        handleButtonOpacity(enable, binding.btnRegister)

        binding.btnRegister.isEnabled = enable
    }

    private fun showErrorMessage(message: String, til: TextInputLayout) {
        til.error = message
    }

    private fun handleButtonOpacity(enabled: Boolean, button: Button) {
        button.alpha = if (enabled) 1f else 0.5f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}