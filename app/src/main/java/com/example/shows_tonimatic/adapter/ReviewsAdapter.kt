package com.example.shows_tonimatic.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shows_tonimatic.R
import com.example.shows_tonimatic.databinding.ViewReviewItemBinding
import com.example.shows_tonimatic.model.Review

class ReviewsAdapter(
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {


    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.reviewName.text = item.user.email
            binding.reviewComment.text = item.comment
            Glide.with(itemView)
                .load(Uri.parse(item.user.imageUrl.toString()))
                .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_placeholder))
                .centerCrop()
                .into(binding.reviewProfilePicture)
            binding.reviewRate.text = item.rating.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(review: Review) {
        items = items + review
        notifyItemInserted(items.lastIndex)
    }
}