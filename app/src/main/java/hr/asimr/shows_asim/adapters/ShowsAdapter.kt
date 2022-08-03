package hr.asimr.shows_asim.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.asimr.shows_asim.customViews.ShowCardView
import hr.asimr.shows_asim.models.Show

class ShowsAdapter(
    private var shows: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(ShowCardView(parent.context))
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.getShowCardView().setShow(shows[position])
        holder.getShowCardView().setCardClickListener{
            onClickCallback(shows[position])
        }
    }

    fun updateShows(newShows: List<Show>) {
        shows = newShows
        notifyDataSetChanged()
    }

    override fun getItemCount() = shows.size

    inner class ShowViewHolder(
        private val showCardView: ShowCardView
    ) : RecyclerView.ViewHolder(showCardView) {

        fun getShowCardView() = showCardView
    }
}