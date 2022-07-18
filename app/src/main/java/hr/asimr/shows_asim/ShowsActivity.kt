package hr.asimr.shows_asim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import hr.asimr.shows_asim.adapters.ShowsAdapter
import hr.asimr.shows_asim.databinding.ActivityShowsBinding
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.utils.loseEmailDomain
import java.util.*

const val USERNAME = "USERNAME"
const val SHOW = "SHOW"

class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter
    private lateinit var username: String

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra(EMAIL)?.let {
            username = it.loseEmailDomain()
        }

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

        binding.rvShows.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        binding.rvShows.adapter = adapter
    }

    private fun showClicked(show: Show) {
        val intent = Intent(this, ShowDetailsActivity::class.java)
        intent.putExtra(SHOW, show)
        intent.putExtra(USERNAME, username)
        startActivity(intent)
    }
}