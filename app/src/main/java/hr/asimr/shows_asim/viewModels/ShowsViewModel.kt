package hr.asimr.shows_asim.viewModels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.models.api.response.ShowsResponse
import hr.asimr.shows_asim.models.api.response.UserResponse
import hr.asimr.shows_asim.networking.ApiModule
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

    private val _showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    private val _success: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val success: LiveData<Boolean> = _success

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> = _loading

    fun getAllShows() {
        _loading.postValue(true)
        ApiModule.retrofit.getAllShows()
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    if(response.isSuccessful){
                        _success.value = true
                        _showsLiveData.value = response.body()?.shows
                    }
                    else{
                        _success.value = false
                    }
                    _loading.postValue(false)

                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    _success.value = false
                    _loading.postValue(false)
                }
            })
    }

    fun updateUserImage(path: String, pref: SharedPreferences) {
        val file = File(path)
        val requestData = file.asRequestBody("multipart/form-data".toMediaType())
        ApiModule.retrofit.updateUserImage(
            MultipartBody.Part.createFormData("image", file.name, requestData)
        )
            .enqueue(object: Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful){
                        _success.value = true
                        pref.edit {
                            putString(USER_IMAGE, response.body()?.user?.imageUrl)
                        }
                    }
                    else{
                        _success.value = false
                    }

                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _success.value = false
                }
            })
    }
}