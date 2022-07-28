package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.models.api.response.ShowsResponse
import hr.asimr.shows_asim.networking.ApiModule
import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun getAllShows() {
        ApiModule.retrofit.getAllShows()
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    _success.value = true
                    _showsLiveData.value = response.body()?.shows
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    _success.value = false
                }
            })
    }

//    fun updateUserImage(path: String) {
//
//    }
}