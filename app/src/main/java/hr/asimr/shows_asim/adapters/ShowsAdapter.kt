package hr.asimr.shows_asim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hr.asimr.shows_asim.databinding.ViewShowItemBinding
import hr.asimr.shows_asim.models.Show
import java.io.File

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

    fun updateShows(newShows: List<Show>) {
        shows = newShows
        notifyDataSetChanged()
    }

    override fun getItemCount() = shows.size

    inner class ShowViewHolder(
        private val binding: ViewShowItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show) {
            binding.tvName.text = show.title
            binding.tvDescription.text = show.description
            binding.cvShow.setOnClickListener { onClickCallback(show) }
            show.imageUrl?.let { url ->
                Glide
                    .with(binding.root)
                    .load(url)
                    .centerCrop()
                    .into(binding.ivShow)
            }
        }
    }
}