package hr.asimr.shows_asim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hr.asimr.shows_asim.adapters.ShowsAdapter
import hr.asimr.shows_asim.databinding.FragmentShowsBinding
import hr.asimr.shows_asim.models.Show
import java.util.*

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter
    private lateinit var email: String

    private val args by navArgs<ShowsFragmentArgs>()

    private val shows = listOf(
        Show(
            UUID.randomUUID().toString(),
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees at the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, spanning a total of nine seasons.",
            R.drawable.ic_office,
            mutableListOf()
        ),
        Show(
            UUID.randomUUID().toString(),
            "Stranger Things",
            "Stranger Things is an American science fiction horror drama television series " +
                "created by the Duffer Brothers that is streaming on Netflix. The brothers serve as showrunners and are executive producers along with Shawn Levy and Dan Cohen. The first season of the series was released on Netflix on July 15, 2016.",
            R.drawable.ic_stranger_things,
            mutableListOf()
        ),
        Show(
            UUID.randomUUID().toString(),
            "Krv nije voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",
            R.drawable.ic_knv,
            mutableListOf()
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentShowsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.email

        initShowsRecycler()
        initListeners()
    }

    private fun initListeners() {
        initButtonListeners()
    }

    private fun initButtonListeners() {
        binding.btnToggleShows.setOnClickListener {
            binding.groupEmptyState.isVisible = !binding.groupEmptyState.isVisible
            binding.groupFullState.isVisible = !binding.groupFullState.isVisible
        }
    }

    private fun initShowsRecycler() {
        adapter = ShowsAdapter(shows) { show ->
            showClicked(show)
        }

        binding.rvShows.adapter = adapter
    }

    private fun showClicked(show: Show) {
        findNavController().navigate(
            ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(
                email,
                show
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}