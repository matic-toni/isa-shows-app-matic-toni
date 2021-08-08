package com.example.shows_tonimatic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shows_tonimatic.databinding.ViewShowItemBinding
import com.example.shows_tonimatic.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showName.text = item.name
            binding.showDescription.text = item.description
            binding.showImage.setImageResource(item.imageResourceId)
            binding.root.setOnClickListener {
                onClickCallback(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}