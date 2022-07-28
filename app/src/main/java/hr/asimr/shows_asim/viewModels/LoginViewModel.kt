package hr.asimr.shows_asim.viewModels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.models.api.request.LoginRequest
import hr.asimr.shows_asim.models.api.response.LoginResponse
import hr.asimr.shows_asim.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val ACCESS_TOKEN = "access-token"
const val CLIENT = "client"
const val UID = "uid"
const val USER_IMAGE = "userImage"

class LoginViewModel() : ViewModel() {
    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun loginUser(email: String, password: String, preferences: SharedPreferences) {
        val loginRequest = LoginRequest(email, password)
        ApiModule.retrofit.login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    loginResultLiveData.value = response.isSuccessful
                    preferences.edit {
                        putString(ACCESS_TOKEN, response.headers()[ACCESS_TOKEN])
                        putString(CLIENT, response.headers()[CLIENT])
                        putString(UID, response.headers()[UID])
                        putString(USER_IMAGE, response.body()?.user?.imageUrl)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResultLiveData.value = false
                }
            })
    }
}