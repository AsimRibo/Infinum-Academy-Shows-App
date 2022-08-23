package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.managers.SharedPreferencesManager
import hr.asimr.shows_asim.models.FormDataStatus
import hr.asimr.shows_asim.models.FormFields
import hr.asimr.shows_asim.models.api.request.LoginRequest
import hr.asimr.shows_asim.models.api.response.UserResponse
import hr.asimr.shows_asim.networking.ACCESS_TOKEN
import hr.asimr.shows_asim.networking.ApiModule
import hr.asimr.shows_asim.networking.CLIENT
import hr.asimr.shows_asim.networking.UID
import hr.asimr.shows_asim.utils.isEmailValid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val USER_IMAGE = "userImage"
const val ACCESS_TOKEN_VALUE = "access_token_value"
const val CLIENT_VALUE = "client_value"
const val UID_VALUE = "uid_value"

class LoginViewModel : ViewModel() {
    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    private val _formValid: MutableLiveData<FormDataStatus> by lazy { MutableLiveData<FormDataStatus>() }
    val formValid: LiveData<FormDataStatus> = _formValid

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> = _loading

    fun validateForm(email: String) {
        if (email.isEmailValid()) {
            _formValid.value = FormDataStatus(true, FormFields.USER_EMAIL)
        } else {
            _formValid.value = FormDataStatus(false, FormFields.USER_EMAIL)
        }
    }

    fun loginUser(email: String, password: String) {
        _loading.value = true
        val loginRequest = LoginRequest(email, password)
        ApiModule.retrofit.login(loginRequest)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    _loading.value = false
                    loginResultLiveData.value = response.isSuccessful

                    SharedPreferencesManager.writeString(ACCESS_TOKEN_VALUE, response.headers()[ACCESS_TOKEN])
                    SharedPreferencesManager.writeString(CLIENT_VALUE, response.headers()[CLIENT])
                    SharedPreferencesManager.writeString(UID_VALUE, response.headers()[UID])
                    SharedPreferencesManager.writeString(USER_IMAGE, response.body()?.user?.imageUrl)

                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _loading.value = false
                    loginResultLiveData.value = false
                }
            })
    }
}