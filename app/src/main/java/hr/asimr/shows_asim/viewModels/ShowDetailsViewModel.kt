package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.models.api.response.ShowResponse
import hr.asimr.shows_asim.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel(private val id: String) : ViewModel() {
    private val _showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }
    val showLiveData: LiveData<Show> = _showLiveData

    private val _averageLiveData = MutableLiveData<Float>()
    val averageLiveData = _averageLiveData

    private val _reviewStats = MutableLiveData<String>()
    val reviewStats: LiveData<String> = _reviewStats

    private val _success: MutableLiveData<Boolean> by lazy{ MutableLiveData<Boolean>() }
    val success: LiveData<Boolean> = _success

    fun getShow(){
        ApiModule.retrofit.getShow(id)
            .enqueue(object: Callback<ShowResponse>{
                override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                    _success.value = true
                    _showLiveData.value = response.body()?.show
                }

                override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                    _success.value = false
                }
            })


    }

//    fun calculateShowRatings() {
//        val df = DecimalFormat("#.##")
//        val sum = show.reviews.sumOf { it.rating }
//        val size = show.reviews.size
//        val average = if (size == 0) 0f else df.format((sum.toFloat() / size)).toFloat()
//        _reviewStats.value = "$size REVIEWS, $average AVERAGE"
//        _averageLiveData.value = average
//    }

//    fun addReview(rating: Int, reviewDetails: String, email: String){
//        show.reviews.add(Review(
//            UUID.randomUUID().toString(),
//            rating,
//            reviewDetails,
//            email.loseEmailDomain()
//        ))
//    }
}