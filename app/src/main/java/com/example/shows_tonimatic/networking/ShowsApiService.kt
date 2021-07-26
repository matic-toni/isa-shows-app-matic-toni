package com.example.shows_tonimatic.networking

import com.example.shows_tonimatic.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShowsApiService {
    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun getShows(): Call<ShowsResponse>

    @GET("/shows/{show_id}/reviews")
    fun getReviews(@Path(value = "show_id") id: Int): Call<ReviewsResponse>

    @POST("/reviews")
    fun postReview(@Body request: ReviewRequest): Call<ReviewResponse>
}
