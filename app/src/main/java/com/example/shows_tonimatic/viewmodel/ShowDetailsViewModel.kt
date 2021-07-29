package com.example.shows_tonimatic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.model.*
import com.example.shows_tonimatic.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private val showLiveData: MutableLiveData<ShowResponse> by lazy {
        MutableLiveData<ShowResponse>()
    }

    fun getShowLiveData(): LiveData<ShowResponse> {
        return showLiveData
    }

    fun getShow(id: Int) {
        ApiModule.retrofit.getShow(id).enqueue(object :
            Callback<ShowResponse> {
            override fun onResponse(
                call: Call<ShowResponse>,
                response: Response<ShowResponse>
            ) {
                showLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                Log.d("Error", t.message.toString())
                showLiveData.value = ShowResponse(Show("", 0, "", "", 0, ""))
            }
        })
    }

    private val reviewsLiveData: MutableLiveData<ReviewsResponse> by lazy {
        MutableLiveData<ReviewsResponse>()
    }

    fun getReviewsLiveData(): LiveData<ReviewsResponse> {
        return reviewsLiveData
    }

    fun getReviews(id: Int) {
        ApiModule.retrofit.getReviews(id).enqueue(object :
            Callback<ReviewsResponse> {
            override fun onResponse(
                call: Call<ReviewsResponse>,
                response: Response<ReviewsResponse>
            ) {
                reviewsLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                Log.d("Error", t.message.toString())
                reviewsLiveData.value = ReviewsResponse(emptyList(), Meta(Pagination(0, 0, 0, 0)))
            }
        })
    }

    private val postReviewResultLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getPostReviewResultLiveData(): LiveData<Boolean> {
        return postReviewResultLiveData
    }

    fun postReview(rating: Int, comment: String, showId: Int) {
        ApiModule.retrofit.postReview(ReviewRequest(comment, rating, showId)).enqueue(object :
            Callback<ReviewResponse> {
            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {
                postReviewResultLiveData.value = response.isSuccessful
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Log.d("Error:", t.message.toString())
                postReviewResultLiveData.value = false
            }
        })
    }
}
