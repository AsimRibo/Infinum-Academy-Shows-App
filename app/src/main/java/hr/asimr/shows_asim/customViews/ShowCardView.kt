package hr.asimr.shows_asim.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.databinding.ViewShowItemBinding
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.utils.loadImageFrom

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
        private var binding: ViewShowItemBinding

        init {
            binding = ViewShowItemBinding.inflate(LayoutInflater.from(context), this)

            clipToPadding = false

            setPadding(
                context.resources.getDimensionPixelSize(R.dimen.spacing_8),
                context.resources.getDimensionPixelSize(R.dimen.spacing_8),
                context.resources.getDimensionPixelSize(R.dimen.spacing_8),
                context.resources.getDimensionPixelSize(R.dimen.spacing_8)
            )
        }

    fun setShow(show: Show){
        binding.tvName.text = show.title
        binding.tvDescription.text = show.description
        binding.ivShow.loadImageFrom(show.imageUrl, R.drawable.ic_show_placeholder)
    }

    fun setCardClickListener(listener: OnClickListener) = binding.cvShow.setOnClickListener(listener)
}