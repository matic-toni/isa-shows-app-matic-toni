package com.example.shows_tonimatic

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.shows_tonimatic.adapter.ReviewsAdapter
import com.example.shows_tonimatic.databinding.FragmentShowDetailsBinding
import com.example.shows_tonimatic.databinding.DialogWriteReviewBinding
import com.example.shows_tonimatic.model.Review
import com.example.shows_tonimatic.model.User
import com.example.shows_tonimatic.networking.ApiModule
import com.example.shows_tonimatic.viewmodel.ShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private lateinit var binding : FragmentShowDetailsBinding
    private var adapter: ReviewsAdapter? = null
    private val viewModel : ShowDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        val view = binding.root

        viewModel.getReviewsLiveData().observe(viewLifecycleOwner, { response ->
            if (response.reviews.isNotEmpty()) {
                initRecyclerView(response.reviews)
                initReviewButton(response.reviews)
            }

        })

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        viewModel.getShowLiveData().observe(viewLifecycleOwner, { response ->
            if (response != null) {
                binding.showName.text = response.show.title
                binding.showDescription.text = response.show.description
                binding.showRating.rating = response.show.averageRating?.toFloat()!!
                binding.reviewsText.text = response.show.noOfReviews.toString().plus(" reviews, " + response.show.averageRating.toFloat() + " average")
                Glide.with(this).load(response.show.imageUrl).into(binding.showImage)
            }
        })

        viewModel.getShow(prefs?.getString("id", "")!!.toInt())

        viewModel.getPostReviewResultLiveData().observe(viewLifecycleOwner, { result ->
            if (result) {
                Toast.makeText(context, "Review posted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Review posted failed!", Toast.LENGTH_SHORT).show()
            }
        })

        initBackButton()

        viewModel.getReviews(prefs.getString("id", "")!!.toInt())

        return view
    }

    private fun initRecyclerView(reviews: List<Review>) {
        binding.reviewRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(reviews)
        binding.reviewRecycler.adapter = adapter
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

        val prefs = ApiModule.prefs
        dialogBinding.submitButton.setOnClickListener {
            adapter?.addItem(
                Review(
                    "rev1",
                    dialogBinding.reviewComment.text.toString(),
                    dialogBinding.reviewRate.rating.toInt(),
                    reviews[0].showId,
                    User(prefs.getInt("user id", 0), prefs.getString("user email", "")!!, prefs.getString("user image", ""))))
            dialog?.dismiss()

            viewModel.postReview(
                dialogBinding.reviewRate.rating.toInt(),
                dialogBinding.reviewComment.text.toString(),
                reviews[0].showId
            )
        }
        dialog?.show()
    }

    private fun initBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
