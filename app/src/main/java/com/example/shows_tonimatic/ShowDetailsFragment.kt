package com.example.shows_tonimatic

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import com.example.shows_tonimatic.viewmodel.ShowDetailsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private lateinit var binding : FragmentShowDetailsBinding
    private var adapter: ReviewsAdapter? = null
    private val viewModel : ShowDetailsViewModel by viewModels {
        ShowDetailsViewModelFactory((activity?.application as ShowsApp).showsDatabase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        val view = binding.root

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)

        if (isNetworkAvailable()) {
            viewModel.getShowLiveData().observe(viewLifecycleOwner, { response ->
                if (response != null) {
                    binding.showName.text = response.show.title
                    binding.showDescription.text = response.show.description
                    binding.showRating.rating = response.show.averageRating?.toFloat()!!
                    binding.reviewsText.text = response.show.noOfReviews.toString()
                        .plus(" reviews, " + response.show.averageRating.toFloat() + " average")
                    Glide.with(this).load(response.show.imageUrl).into(binding.showImage)
                }
            })

            viewModel.getShow(prefs?.getString("id", "")!!.toInt())
        } else {
            viewModel.getOneShow(prefs?.getString("id", "")!!).observe(viewLifecycleOwner, { response ->
                if (response != null) {
                    binding.showName.text = response.title
                    binding.showDescription.text = response.description
                    binding.showRating.rating = response.averageRating?.toFloat()!!
                    binding.reviewsText.text = response.noOfReviews.toString()
                        .plus(" reviews, " + response.averageRating.toFloat() + " average")
                    Glide.with(this).load(response.imageUrl).into(binding.showImage)
                }
            })
        }

        viewModel.getPostReviewResultLiveData().observe(viewLifecycleOwner, { result ->
            if (result.review.id != "") {
                viewModel.storeReview(result.review)
            }
        })

        if (isNetworkAvailable()) {
            viewModel.getReviewsLiveData().observe(viewLifecycleOwner, { response ->
                if (response.reviews.isNotEmpty()) {
                    initRecyclerView(response.reviews)
                    initReviewButton(response.reviews)
                    viewModel.storeReviews(response.reviews)
                    binding.reviewsEmptyState.isVisible = false
                }
            })

            viewModel.getReviews(prefs.getString("id", "")!!.toInt())
        } else {
            viewModel.getReviewsForShow(prefs.getString("id", "")!!.toInt()).observe(viewLifecycleOwner, { result ->
                if (result.isNotEmpty()) {
                    initRecyclerView(result.map {Review(it.id, it.comment, it.rating, it.showId, it.user)})
                    initReviewButton(result.map {Review(it.id, it.comment, it.rating, it.showId, it.user)})
                } else {
                    binding.reviewsEmptyState.isVisible = true
                }
            })
        }

        initBackButton()

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

    private fun isNetworkAvailable(): Boolean {
        val conManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.getNetworkCapabilities(conManager.activeNetwork)
        return internetInfo != null
    }
}
