package hr.asimr.shows_asim.viewModels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.dao.ShowsDatabase
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.models.api.response.ShowsResponse
import hr.asimr.shows_asim.models.api.response.UserResponse
import hr.asimr.shows_asim.networking.ApiModule
import java.io.File
import java.util.concurrent.Executors
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel(val database: ShowsDatabase) : ViewModel() {

    private val _showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    private val _success: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val success: LiveData<Boolean> = _success

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> = _loading

    fun getAllShows(internetAvailable: Boolean) {
        _loading.value = true
        if(internetAvailable){
            ApiModule.retrofit.getAllShows()
                .enqueue(object : Callback<ShowsResponse> {
                    override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                        if(response.isSuccessful){
                            _success.value = true
                            _showsLiveData.value = response.body()?.shows
                            Executors.newSingleThreadExecutor().execute{
                                database.showDao().insertAllShows(response.body()?.shows!!)
//                                database.reviewDao().nukeReviews()
                            }
                        }
                        else{
                            _success.value = false
                        }
                        _loading.value = false
                    }

                    override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                        _success.value = false
                        _loading.value = false
                    }
                })
        }
        else{
            Executors.newSingleThreadExecutor().execute{
                val allShows = database.showDao().getAllShows()
                _success.postValue(true)
                _loading.postValue(false)
                _showsLiveData.postValue(allShows)
            }
        }

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
                        pref.edit {
                            putString(USER_IMAGE, response.body()?.user?.imageUrl)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _success.value = false
                }
            })
    }
}