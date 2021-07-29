package com.example.shows_tonimatic.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.model.*
import com.example.shows_tonimatic.networking.ApiModule
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

    private val showsLiveData: MutableLiveData<ShowsResponse> by lazy {
        MutableLiveData<ShowsResponse>()
    }

    fun getShowsLiveData(): LiveData<ShowsResponse> {
        return showsLiveData
    }

    fun getShows() {
        ApiModule.retrofit.getShows().enqueue(object :
            Callback<ShowsResponse> {
            override fun onResponse(
                call: Call<ShowsResponse>,
                response: Response<ShowsResponse>
            ) {
                showsLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                Log.d("greska:", t.message.toString())
                showsLiveData.value = ShowsResponse(emptyList(), Meta(Pagination(0, 0, 0, 0)))
            }
        })
    }

    private val postImageResultLiveData: MutableLiveData<LoginResponse> by lazy {
        MutableLiveData<LoginResponse>()
    }

    fun getPostImageResultLiveData(): LiveData<LoginResponse> {
        return postImageResultLiveData
    }

    fun postImage(image: MultipartBody.Part) {
        ApiModule.retrofit.postImage(image).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                postImageResultLiveData.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("greska:", t.message.toString())
                postImageResultLiveData.value = LoginResponse(User(0, "", ""))
            }
        })
    }

    private val getMeResultLiveData: MutableLiveData<LoginResponse> by lazy {
        MutableLiveData<LoginResponse>()
    }

    fun getMeResultLiveData(): LiveData<LoginResponse> {
        return getMeResultLiveData
    }

    fun getMe() {
        ApiModule.retrofit.getMe().enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                postImageResultLiveData.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Error:", t.message.toString())
                postImageResultLiveData.value = LoginResponse(User(0, "", ""))
            }
        })
    }
}
