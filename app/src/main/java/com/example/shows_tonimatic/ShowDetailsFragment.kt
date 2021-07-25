package com.example.shows_tonimatic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_tonimatic.adapter.ReviewsAdapter
import com.example.shows_tonimatic.databinding.FragmentShowDetailsBinding
import com.example.shows_tonimatic.databinding.DialogWriteReviewBinding
import com.example.shows_tonimatic.model.Review
import com.example.shows_tonimatic.viewmodel.ShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private lateinit var binding : FragmentShowDetailsBinding
    private var adapter: ReviewsAdapter? = null
    private val args: ShowDetailsFragmentArgs by navArgs()
    private val viewModel : ShowDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        val view = binding.root

        binding.showName.text = args.showName
        binding.showDescription.text = args.showDescription
        binding.showImage.setImageResource(args.showPicture)

        viewModel.initShowDetails()

        viewModel.getShowDetailsLiveData().observe(viewLifecycleOwner, {reviews ->
            initRecyclerView(reviews)
            initReviewButton(reviews)
        })

        initBackButton()

        return view
    }

    private fun initRecyclerView(reviews: List<Review>) {
        val currentShowReviews = updateRating(reviews)
        binding.reviewRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(currentShowReviews)
        binding.reviewRecycler.adapter = adapter
    }

    private fun updateRating(reviews: List<Review>): List<Review> {
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

        var quotient = 0.0f

        if(numOfRates != 0.0f) {
            quotient = sumOfRates / numOfRates
        }

        val res = String.format("%.2f", quotient)

        binding.showRating.rating = quotient
        binding.reviewsText.text = (numOfRates.toInt()).toString().plus(" REVIEWS, ").plus(res).plus(" AVERAGE")
        return currentShowReviews
    }

    private fun initReviewButton(reviews: List<Review>) {
        binding.reviewButton.setOnClickListener {
            showReviewDialog(reviews)
        }
    }

    private fun showReviewDialog(reviews: List<Review>) {
        val dialog = view?.let { BottomSheetDialog(it.context) }
        val dialogBinding = DialogWriteReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogBinding.root)

        dialogBinding.submitButton.setOnClickListener {
            viewModel.addReview(Review(args.showId, args.username, dialogBinding.reviewComment.text.toString(), dialogBinding.reviewRate.rating.toInt(), R.drawable.ic_profile_placeholder))
            updateRating(reviews)
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