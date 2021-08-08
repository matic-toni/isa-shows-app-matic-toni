package com.example.shows_tonimatic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.R
import com.example.shows_tonimatic.model.Review

class ShowDetailsViewModel : ViewModel() {

    private var reviews = mutableListOf(
        Review("the_office", "ime.prezimenkovic", "The show was great!", 5, R.drawable.ic_profile_placeholder),
        Review("the_office", "netko.drugi", "The show was ok", 4, R.drawable.ic_profile_placeholder),
        Review("krv_nije_voda", "ime.prezimenkovic", "Best show ever! The masterpiece! Awesome!", 5, R.drawable.ic_profile_placeholder)
    )

    private val showDetailsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    fun getShowDetailsLiveData(): LiveData<List<Review>> {
        return showDetailsLiveData
    }

    fun initShowDetails() {
        showDetailsLiveData.value = reviews
    }

    fun addReview(review: Review) {
        reviews.add(review)
        showDetailsLiveData.value = reviews
    }
}
