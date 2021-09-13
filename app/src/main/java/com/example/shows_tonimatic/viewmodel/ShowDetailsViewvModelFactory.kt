package com.example.shows_tonimatic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shows_tonimatic.db.ShowsDatabase
import java.lang.IllegalArgumentException

class ShowDetailsViewModelFactory(private val database: ShowsDatabase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(database) as T
        }
        throw IllegalArgumentException("Wrong class!")
    }
}