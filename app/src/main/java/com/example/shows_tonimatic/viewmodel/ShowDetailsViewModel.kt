package com.example.shows_tonimatic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.db.ReviewEntity
import com.example.shows_tonimatic.db.ShowEntity
import com.example.shows_tonimatic.db.ShowsDatabase
import com.example.shows_tonimatic.model.*
import com.example.shows_tonimatic.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowDetailsViewModel(private val database: ShowsDatabase) : ViewModel() {

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

    private val postReviewResultLiveData: MutableLiveData<ReviewResponse> by lazy {
        MutableLiveData<ReviewResponse>()
    }

    fun getPostReviewResultLiveData(): LiveData<ReviewResponse> {
        return postReviewResultLiveData
    }

    fun postReview(rating: Int, comment: String, showId: Int) {
        ApiModule.retrofit.postReview(ReviewRequest(comment, rating, showId)).enqueue(object :
            Callback<ReviewResponse> {
            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {
                postReviewResultLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Log.d("Error:", t.message.toString())
                postReviewResultLiveData.value = ReviewResponse(Review("", "", 0,0, User(0, "", "")))
            }
        })
    }

    fun getOneShow(id: String): LiveData<ShowEntity> {
        return database.showDao().getShow(id)
    }

    fun getOneReview(id: String): LiveData<ReviewEntity> {
        return database.reviewDao().getReview(id)
    }

    fun getReviewsForShow(id: Int): LiveData<List<ReviewEntity>> {
        return database.reviewDao().getReviews(id)
    }

    fun storeReviews(reviews: List<Review>) {
        Executors.newSingleThreadExecutor().execute {
            database.reviewDao().insertReviews(reviews.map {ReviewEntity(it.id, it.comment, it.rating, it.showId, it.user)})
        }
    }

    fun storeReview(review: Review) {
        Executors.newSingleThreadExecutor().execute {
            database.reviewDao().insertReview(ReviewEntity(review.id, review.comment, review.rating, review.showId, review.user))
        }
    }

    fun deleteReview(id: String) {
        Executors.newSingleThreadExecutor().execute {
            database.reviewDao().deleteRewiev(id)
        }
    }
}
