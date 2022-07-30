package hr.asimr.shows_asim.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import hr.asimr.shows_asim.R

fun ImageView.loadImageFrom(url: String?) {
    Glide
        .with(this)
        .load(url)
        .placeholder(R.drawable.ic_profile_placeholder)
        .centerCrop()
        .into(this)
}
