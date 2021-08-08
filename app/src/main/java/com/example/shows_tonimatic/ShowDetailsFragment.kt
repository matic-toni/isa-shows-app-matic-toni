package com.example.shows_tonimatic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_tonimatic.databinding.FragmentShowDetailsBinding
import com.example.shows_tonimatic.databinding.DialogWriteReviewBinding
import com.example.shows_tonimatic.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private lateinit var binding : FragmentShowDetailsBinding
    private var adapter: ReviewsAdapter? = null
    private val args: ShowDetailsFragmentArgs by navArgs()

    companion object {
        private var reviews = mutableListOf<Review>(
            Review("the_office", "ime.prezimenkovic", "The show was great!", 5, R.drawable.ic_profile_placeholder),
            Review("the_office", "netko.drugi", "The show was ok", 4, R.drawable.ic_profile_placeholder),
            Review("krv_nije_voda", "ime.prezimenkovic", "Best show ever! The masterpiece! Awesome!", 5, R.drawable.ic_profile_placeholder)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showName.text = args.showName
        binding.showDescription.text = args.showDescription
        binding.showImage.setImageResource(args.showPicture)

        initBackButton()
        initRecycleView()
        initReviewButton()
    }

    private fun initRecycleView() {
        val currentShowReviews = updateRating()
        binding.reviewRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(currentShowReviews)
        binding.reviewRecycler.adapter = adapter
    }

    private fun updateRating(): MutableList<Review> {
        val currentShowReviews = mutableListOf<Review>()
        var sumOfRates = 0.0f
        var numOfRates = 0.0f


        reviews.forEach {
            if (it.showId == args.showId) {
                currentShowReviews += it
                sumOfRates += it.rate
                numOfRates++
            }
        }

        var averageRating = 0.0f

        if(numOfRates != 0.0f) {
            averageRating = sumOfRates / numOfRates
        }

        averageRating = String.format("%.2f", averageRating).toFloat()

        binding.showRating.rating = averageRating
        binding.reviewsText.text = String.format(resources.getString(R.string.average_for_show), numOfRates.toInt(), averageRating)
        return currentShowReviews
    }

    private fun initReviewButton() {
        binding.reviewButton.setOnClickListener {
            showReviewDialog()
        }
    }

    private fun showReviewDialog() {
        val dialog = view?.let { BottomSheetDialog(it.context) }
        val dialogBinding = DialogWriteReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogBinding.root)

        dialogBinding.submitButton.setOnClickListener {
            reviews.add(Review(args.showId, args.username, dialogBinding.reviewComment.text.toString(), dialogBinding.reviewRate.rating.toInt(), R.drawable.ic_profile_placeholder))
            updateRating()
            adapter?.addItem(Review(args.showId, args.username, dialogBinding.reviewComment.text.toString(), dialogBinding.reviewRate.rating.toInt(), R.drawable.ic_profile_placeholder))
            dialog?.dismiss()
        }
        dialog?.show()
    }

    private fun initBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}