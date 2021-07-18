package com.example.shows_tonimatic

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_tonimatic.databinding.ActivityShowDetailsBinding
import com.example.shows_tonimatic.databinding.DialogWriteReviewBinding
import com.example.shows_tonimatic.model.Review
import com.example.shows_tonimatic.model.Show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShowDetailsBinding
    private var adapter: ReviewsAdapter? = null
    private var extras: Bundle? = null

    companion object {

        fun buildIntent(activity: Activity, item: Show, username: String): Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra("USERNAME", username)
            intent.putExtra("ID", item.id)
            intent.putExtra("NAME", item.name)
            intent.putExtra("DESCRIPTION", item.description)
            intent.putExtra("IMAGE", item.imageResourceId)
            return intent
        }

        private var reviews = listOf(
            Review("the_office", "ime.prezimenkovic", "The show was great!", 5, R.drawable.ic_profile_placeholder),
            Review("the_office", "netko.drugi", "The show was ok", 4, R.drawable.ic_profile_placeholder),
            Review("krv_nije_voda", "ime.prezimenkovic", "Best show ever! The masterpiece! Awesome!", 5, R.drawable.ic_profile_placeholder)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        extras = intent.extras
        if (extras != null) {
            binding.showName.text = extras?.getString("NAME")
            binding.showDescription.text = extras?.getString("DESCRIPTION")
            binding.showImage.setImageResource(extras!!.getInt("IMAGE")) // ne radi ?
        }

        initRecycleView()
        initReviewButton()
    }

    private fun initRecycleView() {
        val currentShowReviews = updateRating()
        binding.reviewRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(currentShowReviews)
        binding.reviewRecycler.adapter = adapter
    }

    private fun updateRating(): MutableList<Review> {
        val currentShowReviews = mutableListOf<Review>()
        var sumOfRates = 0.0f
        var numOfRates = 0.0f

        reviews.forEach {
            if (it.show_id == extras?.getString("ID")) {
                currentShowReviews += it
                sumOfRates += it.rate
                numOfRates++
            }
        }
        binding.showRating.rating = sumOfRates / numOfRates
        return currentShowReviews
    }

    private fun initReviewButton() {
        binding.reviewButton.setOnClickListener {
            showReviewDialog()
        }
    }

    private fun showReviewDialog() {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = DialogWriteReviewBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.submitButton.setOnClickListener {
            // reviews += Review("the_office", "ja", dialogBinding.reviewComment.text.toString(), dialogBinding.reviewRate.rating.toInt(), R.drawable.ic_profile_placeholder)
            adapter?.addItem(Review("the_office", extras?.getString("USERNAME").toString(), dialogBinding.reviewComment.text.toString(), dialogBinding.reviewRate.rating.toInt(), R.drawable.ic_profile_placeholder))
            dialog.dismiss()
            // updateRating()
        }
        dialog.show()
    }
}