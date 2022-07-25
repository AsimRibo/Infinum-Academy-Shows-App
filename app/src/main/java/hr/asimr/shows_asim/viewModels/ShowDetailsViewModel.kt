package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.utils.loseEmailDomain
import java.text.DecimalFormat
import java.util.UUID

class ShowDetailsViewModel(private val show: Show) : ViewModel() {
    private val _showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>(show)
    }

    val showLiveData: LiveData<Show> = _showLiveData

    private val _averageLiveData = MutableLiveData<Float>()
    val averageLiveData = _averageLiveData

    private val _reviewStats = MutableLiveData<String>()
    val reviewStats: LiveData<String> = _reviewStats

    fun calculateShowRatings() {
        val df = DecimalFormat("#.##")
        val sum = show.reviews.sumOf { it.rating }
        val size = show.reviews.size
        val average = if (size == 0) 0f else df.format((sum.toFloat() / size)).toFloat()
        _reviewStats.value = "$size REVIEWS, $average AVERAGE"
        _averageLiveData.value = average
    }

    fun addReview(rating: Int, reviewDetails: String, email: String){
        show.reviews.add(Review(
            UUID.randomUUID().toString(),
            rating,
            reviewDetails,
            email.loseEmailDomain()
        ))
    }
}