package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.dao.ShowsDatabase
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.models.User
import hr.asimr.shows_asim.models.api.request.ReviewRequest
import hr.asimr.shows_asim.models.api.response.ReviewResponse
import hr.asimr.shows_asim.models.api.response.ShowResponse
import hr.asimr.shows_asim.models.api.response.ShowReviewsResponse
import hr.asimr.shows_asim.networking.ApiModule
import java.util.UUID
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel(private val id: String, private val database: ShowsDatabase) : ViewModel() {
    private val _showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }
    val showLiveData: LiveData<Show> = _showLiveData

    private val _showReviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }
    val showReviewsLiveData: LiveData<List<Review>> = _showReviewsLiveData

    private val _averageLiveData = MutableLiveData<Float>()
    val averageLiveData: LiveData<Float> = _averageLiveData

    private val _reviewStats = MutableLiveData<String>()
    val reviewStats: LiveData<String> = _reviewStats

    private val _success: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val success: LiveData<Boolean> = _success

    private val _loadingShow: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loadingShow: LiveData<Boolean> = _loadingShow

    private val _loadingReviews: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loadingReviews: LiveData<Boolean> = _loadingReviews

    fun getShow(internetAvailable: Boolean) {
        _loadingShow.value = true
        if (internetAvailable) {
            ApiModule.retrofit.getShow(id)
                .enqueue(object : Callback<ShowResponse> {
                    override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                        _success.value = true
                        _loadingShow.value = false
                        _showLiveData.value = response.body()?.show
                        _averageLiveData.value = response.body()?.show?.averageRating
                        val numOfReviews = response.body()?.show?.numOfReviews
                        if (numOfReviews != null && _averageLiveData.value != null) {
                            _reviewStats.value = "$numOfReviews REVIEWS, ${_averageLiveData.value} AVERAGE"
                        }
                    }

                    override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                        _loadingShow.value = false
                        _success.value = false
                    }
                })
        } else {
            Executors.newSingleThreadExecutor().execute {
                val show = database.showDao().getShow(id)
                _success.postValue(true)
                _loadingShow.postValue(false)
                _showLiveData.postValue(show)
                _averageLiveData.postValue(show.averageRating)
                if (show.averageRating != null) {
                    _reviewStats.postValue("${show.numOfReviews} REVIEWS, ${show.averageRating} AVERAGE")
                }
            }
        }
    }

    fun getShowReviews(internetAvailable: Boolean) {
        if (internetAvailable) {
            getReviewsFromApi(internetAvailable)
        } else {
            getReviewsFromDb()
        }
    }

    private fun getReviewsFromDb() {
        Executors.newSingleThreadExecutor().execute {
            _showReviewsLiveData.postValue(database.reviewDao().getAllReviews(id))
            _success.postValue(true)
            _loadingReviews.postValue(false)
        }
    }

    private fun getReviewsFromApi(internetAvailable: Boolean) {
        ApiModule.retrofit.getShowReviews(id)
            .enqueue(object : Callback<ShowReviewsResponse> {
                override fun onResponse(call: Call<ShowReviewsResponse>, response: Response<ShowReviewsResponse>) {
                    _loadingReviews.value = false
                    _success.value = true

                    Executors.newSingleThreadExecutor().execute {
                        val reviewsToAddToApi = database.reviewDao().getReviewsNotSyncedWithApi()
                        reviewsToAddToApi.forEach { r ->
                            addReview(r.rating, r.comment, internetAvailable, "")
                        }
                        database.reviewDao().deleteReviewsAfterSyncedWithApi()
                    }

                    val reviews = response.body()?.reviews
                    _showReviewsLiveData.value = reviews
                    if (reviews != null) {
                        Executors.newSingleThreadExecutor().execute {
                            database.reviewDao().insertAllReviews(reviews)
                        }
                    }
                }

                override fun onFailure(call: Call<ShowReviewsResponse>, t: Throwable) {
                    _loadingReviews.value = false
                    _success.value = false
                }
            })
    }

    fun addReview(rating: Int, comment: String, internetAvailable: Boolean, email: String) {
        if (internetAvailable) {
            ApiModule.retrofit.addReview(ReviewRequest(comment, rating, id))
                .enqueue(object : Callback<ReviewResponse> {
                    override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                        _success.value = true
                        getShow(internetAvailable)
                        getShowReviews(internetAvailable)
                    }

                    override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                        _success.value = false
                    }
                })
        } else {
            Executors.newSingleThreadExecutor().execute {
                database.reviewDao().insertReview(
                    Review(
                        UUID.randomUUID().toString(), comment, rating, id.toInt(),
                        User("-1", email, null)
                    )
                )
                _success.postValue(true)

                _showReviewsLiveData.postValue(database.reviewDao().getAllReviews(id))
            }
        }
    }
}