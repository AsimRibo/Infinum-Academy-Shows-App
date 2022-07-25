package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.models.Show
import java.text.DecimalFormat

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
        val sum = _showLiveData.value!!.reviews.sumOf { it.rating }
        val size = _showLiveData.value!!.reviews.size
        val average = if (size == 0) 0f else df.format((sum.toFloat() / size)).toFloat()
        _reviewStats.value = "$size REVIEWS, $average AVERAGE"
        _averageLiveData.value = average
    }

    fun addReview(review: Review){
        _showLiveData.value!!.reviews += review
    }
}