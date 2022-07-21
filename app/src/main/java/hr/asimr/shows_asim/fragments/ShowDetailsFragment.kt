package hr.asimr.shows_asim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.adapters.ReviewsAdapter
import hr.asimr.shows_asim.databinding.DialogAddReviewBinding
import hr.asimr.shows_asim.databinding.FragmentShowDetailsBinding
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.utils.loseEmailDomain
import java.text.DecimalFormat
import java.util.*

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val args by navArgs<ShowDetailsFragmentArgs>()
    private lateinit var show: Show
    private lateinit var email: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.email
        show = args.show

        initToolbar(binding.toolbar)
        initListeners()
        initShowDetails()
        initReviewsRecycler()
    }

    private fun initReviewsRecycler() {
        reviewsAdapter = ReviewsAdapter(show.reviews)

        binding.rvReview.adapter = reviewsAdapter

        binding.rvReview.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
    }

    private fun initListeners() {
        initButtonListeners()
    }

    private fun initButtonListeners() {
        binding.btnWriteReview.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
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
        binding.ivShow.setImageResource(show.imageResourceId)
        binding.tvDescription.text = show.description
        binding.toolbar.title = show.name
    }

    private fun addReview(rating: Int, review: String) {
        show.reviews.add(
            Review(
                UUID.randomUUID().toString(),
                rating,
                review,
                email.loseEmailDomain()
            )
        )
        reviewsAdapter.notifyItemInserted(show.reviews.lastIndex)
        updateGroupsVisibility()
        updateShowRatings()
    }

    private fun updateGroupsVisibility() {
        binding.groupReviews.visibility = View.VISIBLE
        binding.groupNoReview.visibility = View.GONE
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
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}