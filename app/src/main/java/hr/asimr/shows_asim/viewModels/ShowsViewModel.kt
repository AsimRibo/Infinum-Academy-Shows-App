package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.models.Show
import java.util.UUID

class ShowsViewModel : ViewModel() {
    private val shows = listOf(
        Show(
            UUID.randomUUID().toString(),
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees at the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, spanning a total of nine seasons.",
            R.drawable.ic_office,
            listOf()
        ),
        Show(
            UUID.randomUUID().toString(),
            "Stranger Things",
            "Stranger Things is an American science fiction horror drama television series " +
                "created by the Duffer Brothers that is streaming on Netflix. The brothers serve as showrunners and are executive producers along with Shawn Levy and Dan Cohen. The first season of the series was released on Netflix on July 15, 2016.",
            R.drawable.ic_stranger_things,
            listOf()
        ),
        Show(
            UUID.randomUUID().toString(),
            "Krv nije voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",
            R.drawable.ic_knv,
            listOf()
        )
    )
}