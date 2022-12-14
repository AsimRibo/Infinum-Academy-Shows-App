package hr.asimr.shows_asim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.databinding.ItemReviewBinding
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.utils.loadImageFrom
import hr.asimr.shows_asim.utils.loseEmailDomain

class ReviewsAdapter(private var reviews: List<Review>) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    fun updateReviews(newReviews: List<Review>){
        reviews = newReviews
        notifyDataSetChanged()
    }

    override fun getItemCount() = reviews.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.tvRating.text = item.rating.toString()
            binding.tvComment.text = item.comment
            binding.tvUsername.text = item.user.email.loseEmailDomain()
            binding.ivUser.loadImageFrom(item.user.imageUrl, R.drawable.ic_profile_placeholder)
        }
    }
}