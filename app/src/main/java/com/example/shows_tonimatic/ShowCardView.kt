package com.example.shows_tonimatic

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.shows_tonimatic.databinding.ViewShowItemBinding

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr){
    private var binding: ViewShowItemBinding

    init {
        binding = ViewShowItemBinding.inflate(LayoutInflater.from(context), this)
        val pixelPadding = context.resources.getDimensionPixelSize(R.dimen.show_padding)
        setPadding(pixelPadding, pixelPadding, pixelPadding, pixelPadding)

        clipToPadding = false
    }

    fun setShowImage(imageUrl: String) {
        Glide.with(this)
            .load(Uri.parse(imageUrl))
            .centerCrop()
            .into(binding.showImage)
    }

    fun setShowTitle(title: String) {
        binding.showTitle.text = title
    }

    fun setShowDescription(description: String) {
        binding.showDescription.text = description
    }
}