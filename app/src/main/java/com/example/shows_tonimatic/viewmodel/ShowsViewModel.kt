package com.example.shows_tonimatic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.db.ShowEntity
import com.example.shows_tonimatic.db.ShowsDatabase
import com.example.shows_tonimatic.model.*
import com.example.shows_tonimatic.networking.ApiModule
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowsViewModel(private val database: ShowsDatabase) : ViewModel() {

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
                Log.d("Error:", t.message.toString())
                postImageResultLiveData.value = LoginResponse(User(0, "", ""))
            }
        })
    }

    private val meResultLiveData: MutableLiveData<LoginResponse> by lazy {
        MutableLiveData<LoginResponse>()
    }

    fun getMeResultLiveData(): LiveData<LoginResponse> {
        return meResultLiveData
    }

    fun getMe() {
        ApiModule.retrofit.getMe().enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                meResultLiveData.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Error:", t.message.toString())
                meResultLiveData.value = LoginResponse(User(0, "", ""))
            }
        })
    }

    fun getAllShows(): LiveData<List<ShowEntity>> {
        return database.showDao().getAllShows()
    }

    fun storeShows(shows: List<Show>) {
        Executors.newSingleThreadExecutor().execute {
            database.showDao().insertShows(shows.map {ShowEntity(it.id, it.averageRating, it.description, it.imageUrl, it.noOfReviews, it.title)})
        }
    }
}
