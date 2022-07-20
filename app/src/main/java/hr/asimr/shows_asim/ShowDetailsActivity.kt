package hr.asimr.shows_asim

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.asimr.shows_asim.adapters.ReviewsAdapter
import hr.asimr.shows_asim.databinding.ActivityShowDetailsBinding
import hr.asimr.shows_asim.databinding.DialogAddReviewBinding
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.models.Show
import java.text.DecimalFormat
import java.util.*

class ShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var show: Show
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar)
        initListeners()
        initShowDetails()
        initReviewsRecycler()
    }

    private fun initReviewsRecycler() {
        reviewsAdapter = ReviewsAdapter(show.reviews)

        binding.rvReview.adapter = reviewsAdapter

        binding.rvReview.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun initListeners() {
        initButtonListeners()
    }

    private fun initButtonListeners() {
        binding.btnWriteReview.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val bottomSheet = DialogAddReviewBinding.inflate(layoutInflater)
            dialog.setContentView(bottomSheet.root)

            bottomSheet.btnSubmit.setOnClickListener {
                if (bottomSheet.rbReview.rating != 0f) {
                    addReview(
                        bottomSheet.rbReview.rating.toInt(),
                        bottomSheet.etComment.text.toString()
                    )
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    private fun initShowDetails() {
        intent.getParcelableExtra<Show>(SHOW)?.let {
            binding.ivShow.setImageResource(it.imageResourceId)
            binding.tvDescription.text = it.description
            binding.toolbar.title = it.name
            show = it
        }

        intent.getStringExtra(USERNAME)?.let {
            username = it
        }
    }

    private fun addReview(rating: Int, review: String) {
        show.reviews.add(
            Review(
                UUID.randomUUID().toString(),
                rating,
                review,
                username
            )
        )
        reviewsAdapter.notifyItemInserted(show.reviews.lastIndex)
        updateGroupsVisibility()
        updateShowRatings()
    }

    private fun updateGroupsVisibility() {
        binding.groupReviews.visibility = View.VISIBLE
        binding.groupNoReview.visibility = View.GONE
//        binding.groupNoReview.isVisible = false
//        binding.groupReviews.isVisible = true
    }

    private fun updateShowRatings() {
        val df = DecimalFormat("#.##")
        val sum = show.reviews.sumOf { it.rating }
        val average = df.format((sum.toFloat() / show.reviews.size)).toFloat()
        val size = show.reviews.size

        binding.rbShow.rating = average
        binding.tvReviewStats.text = "$size REVIEWS, $average AVERAGE"
    }

    private fun initToolbar(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}