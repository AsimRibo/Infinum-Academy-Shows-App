package hr.asimr.shows_asim.viewModels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _emailValid: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val emailValid: LiveData<Boolean> = _emailValid

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> = _loading

    fun validateEmail(email: String){
        _emailValid.value = email.isEmailValid()
    }

    fun loginUser(email: String, password: String, preferences: SharedPreferences) {
        _loading.value = true
        val loginRequest = LoginRequest(email, password)
        ApiModule.retrofit.login(loginRequest)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    _loading.value = false
                    loginResultLiveData.value = response.isSuccessful
                    preferences.edit {
                        putString(ACCESS_TOKEN_VALUE, response.headers()[ACCESS_TOKEN])
                        putString(CLIENT_VALUE, response.headers()[CLIENT])
                        putString(UID_VALUE, response.headers()[UID])
                        putString(USER_IMAGE, response.body()?.user?.imageUrl)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _loading.value = false
                    loginResultLiveData.value = false
                }
            })
    }
}