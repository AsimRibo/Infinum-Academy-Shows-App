package hr.asimr.shows_asim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.asimr.shows_asim.databinding.ViewShowItemBinding
import hr.asimr.shows_asim.models.Show

class ShowsAdapter(
    private var shows: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
            ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    override fun getItemCount() = shows.size

    inner class ShowViewHolder(
        private val binding: ViewShowItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show) {
            binding.tvName.text = show.name
            binding.tvDescription.text = show.description
            binding.ivShow.setImageResource(show.imageResourceId)
            binding.cvShow.setOnClickListener { onClickCallback(show) }
        }
    }
}