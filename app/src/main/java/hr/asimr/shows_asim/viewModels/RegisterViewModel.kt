package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.models.FormDataStatus
import hr.asimr.shows_asim.models.api.request.RegisterRequest
import hr.asimr.shows_asim.models.api.response.RegisterResponse
import hr.asimr.shows_asim.networking.ApiModule
import hr.asimr.shows_asim.utils.isEmailValid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val registrationResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registrationResultLiveData
    }

    private val _formValid: MutableLiveData<FormDataStatus> by lazy { MutableLiveData<FormDataStatus>() }
    val formValid: LiveData<FormDataStatus> = _formValid

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> = _loading

    fun validateForm(email: String, password: String, passwordRepeated: String){
        if (email.isEmailValid()){
            checkPasswordsMatch(password, passwordRepeated)
        }
        else{
            _formValid.value = FormDataStatus(false, R.string.invalid_email)
        }
    }

    private fun checkPasswordsMatch(password: String, passwordRepeated: String) {
        if (password == passwordRepeated) {
            _formValid.value = FormDataStatus(true, null)
        } else {
            FormDataStatus(false, R.string.password_mismatch)
        }
    }

    fun registerUser(email: String, password: String, passwordRepeated: String) {
        _loading.value = true
        val registerRequest = RegisterRequest(
            email = email,
            password = password,
            passwordConfirmation = passwordRepeated
        )
        ApiModule.retrofit.register(registerRequest)
            .enqueue(object: Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    _loading.value = false
                    registrationResultLiveData.value = response.isSuccessful
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _loading.value = false
                    registrationResultLiveData.value = false
                }

            })
    }
}