package com.example.shows_tonimatic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shows_tonimatic.databinding.ViewReviewItemBinding
import com.example.shows_tonimatic.model.Review

class ReviewsAdapter(
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {


    inner class ReviewViewHolder(val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.reviewName.text = item.name
            binding.reviewComment.text = item.comment
            binding.reviewProfilePicture.setImageResource(item.imageResourceId)
            binding.reviewRate.text = item.rate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context))
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