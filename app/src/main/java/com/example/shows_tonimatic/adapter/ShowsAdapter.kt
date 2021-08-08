package com.example.shows_tonimatic.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shows_tonimatic.ShowCardView
import com.example.shows_tonimatic.databinding.ViewShowItemBinding
import com.example.shows_tonimatic.model.Show


class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    inner class ShowViewHolder(private val showCardView: ShowCardView) : RecyclerView.ViewHolder(showCardView.rootView) {
        fun bind(item: Show) {
            showCardView.setShowTitle(item.title)
            showCardView.setShowDescription(item.description!!)

            showCardView.setShowImage(item.imageUrl)

            showCardView.rootView.setOnClickListener {
                onClickCallback(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val showCardView = ShowCardView(parent.context)
        return ShowViewHolder(showCardView)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
