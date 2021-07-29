package com.example.shows_tonimatic.networking

import com.example.shows_tonimatic.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ShowsApiService {
    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun getShows(): Call<ShowsResponse>

    @GET("/shows/{id}")
    fun getShow(@Path(value = "id") id: Int): Call<ShowResponse>

    @GET("/shows/{show_id}/reviews")
    fun getReviews(@Path(value = "show_id") id: Int): Call<ReviewsResponse>

    @POST("/reviews")
    fun postReview(@Body request: ReviewRequest): Call<ReviewResponse>

    @Multipart
    @PUT("/users")
    fun postImage(@Part image: MultipartBody.Part): Call<LoginResponse>

    @GET("/users/me")
    fun getMe(): Call<LoginResponse>
}
