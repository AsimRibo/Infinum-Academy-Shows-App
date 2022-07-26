package hr.asimr.shows_asim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.adapters.ReviewsAdapter
import hr.asimr.shows_asim.databinding.DialogAddReviewBinding
import hr.asimr.shows_asim.databinding.FragmentShowDetailsBinding
import hr.asimr.shows_asim.viewModels.ShowDetailsViewModel
import hr.asimr.shows_asim.viewModels.factories.ShowDetailsViewModelFactory

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val args by navArgs<ShowDetailsFragmentArgs>()
    private val viewModel by viewModels<ShowDetailsViewModel> {
        ShowDetailsViewModelFactory(args.show)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        initListeners()
        initShowDetails()
        initReviewsRecycler()
        initReviewsObserving()
    }

    private fun initReviewsObserving() {
        viewModel.showLiveData.observe(viewLifecycleOwner){ show ->
            reviewsAdapter.updateReviews(show.reviews)
        }
    }

    private fun initReviewsRecycler() {
        reviewsAdapter = ReviewsAdapter(listOf())

        binding.rvReview.adapter = reviewsAdapter

        binding.rvReview.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        //        viewModel.showLiveData.observe(viewLifecycleOwner) { show ->
        //            reviewsAdapter = ReviewsAdapter(show.reviews)
        //
        //            binding.rvReview.adapter = reviewsAdapter
        //
        //            binding.rvReview.addItemDecoration(
        //                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        //            )
        //        }
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
                    handleReview(
                        bottomSheet.rbReview.rating.toInt(),
                        bottomSheet.etComment.text.toString()
                    )
                    dialog.dismiss()
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
            binding.ivShow.setImageResource(show.imageResourceId)
            binding.tvDescription.text = show.description
            binding.toolbar.title = show.name
        }
    }

    private fun handleReview(rating: Int, reviewDetails: String) {
        viewModel.addReview(rating, reviewDetails, args.email)

        reviewsAdapter.notifyReviewAdded()
        updateGroupsVisibility()
        viewModel.calculateShowRatings()
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