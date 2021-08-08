package com.example.shows_tonimatic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.R
import com.example.shows_tonimatic.model.Show

class ShowsViewModel : ViewModel() {

    private val shows = mutableListOf(
        Show("the_office", "The Office", "Lorem ipsum...", R.drawable.ic_the_office),
        Show("stranger_things", "Stranger Things", "Lorem ipsum...", R.drawable.ic_stranger_things),
        Show("krv_nije_voda", "Krv Nije Voda", "Lorem ipsum...", R.drawable.ic_krv_nije_voda)
    )

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }
}