package hr.asimr.shows_asim.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.ShowsApplication
import hr.asimr.shows_asim.adapters.ReviewsAdapter
import hr.asimr.shows_asim.databinding.DialogAddReviewBinding
import hr.asimr.shows_asim.databinding.FragmentShowDetailsBinding
import hr.asimr.shows_asim.networking.DeviceInternetConnection
import hr.asimr.shows_asim.utils.loadImageFrom
import hr.asimr.shows_asim.viewModels.ShowDetailsViewModel
import hr.asimr.shows_asim.viewModels.factories.ShowDetailsViewModelFactory

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var loginPreferences: SharedPreferences
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private val viewModel by viewModels<ShowDetailsViewModel>{
        ShowDetailsViewModelFactory(args.showId, (activity?.application as ShowsApplication).showsDatabase)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowDetailsBinding.inflate(layoutInflater, container, false)
        loginPreferences = requireContext().getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        viewModel.getShow(DeviceInternetConnection.isAvailable(requireContext()))
        viewModel.getShowReviews(DeviceInternetConnection.isAvailable(requireContext()))
        initListeners()
        initShowDetails()
        initReviewsRecycler()
        initReviewsObserving()
        observeRatingAndStats()
        observeSuccess()
        initLoadingProgress()
    }

    private fun initLoadingProgress() {
        viewModel.loadingShow.observe(viewLifecycleOwner) { loading ->
            binding.progressIndicatorShow.isVisible = loading
        }

        viewModel.loadingReviews.observe(viewLifecycleOwner) { loading ->
            binding.progressIndicatorReviews.isVisible = loading
        }
    }

    private fun observeSuccess() {
        viewModel.success.observe(viewLifecycleOwner){ success ->
            if (!success){
                Toast.makeText(requireContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initReviewsObserving() {
        viewModel.showReviewsLiveData.observe(viewLifecycleOwner){ reviews ->
            if (reviews.isNotEmpty()) {
                reviewsAdapter.updateReviews(reviews)
                updateGroupsVisibility()
            }
        }
    }

    private fun initReviewsRecycler() {
        reviewsAdapter = ReviewsAdapter(listOf())

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
                    dialog.dismiss()
                    handleReview(
                        bottomSheet.rbReview.rating.toInt(),
                        bottomSheet.etComment.text.toString()
                    )
                }
            }
            dialog.show()
            observeRatingAndStats()
        }
    }

    private fun observeRatingAndStats() {
        viewModel.averageLiveData.observe(viewLifecycleOwner) { average ->
            binding.rbShow.rating = average
        }
        viewModel.reviewStats.observe(viewLifecycleOwner) { stats ->
            binding.tvReviewStats.text = stats
        }
    }

    private fun initShowDetails() {
        viewModel.showLiveData.observe(viewLifecycleOwner) { show ->
            binding.tvDescription.text = show.description
            binding.toolbar.title = show.title
            binding.ivShow.loadImageFrom(show.imageUrl, R.drawable.ic_show_placeholder)
        }
    }

    private fun handleReview(rating: Int, reviewDetails: String) {
        viewModel.addReview(rating, reviewDetails, DeviceInternetConnection.isAvailable(requireContext()), loginPreferences.getString(
            USER_EMAIL, "").orEmpty())
    }

    private fun updateGroupsVisibility() {
        binding.groupReviews.isVisible = true
        binding.groupNoReview.isVisible = false
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