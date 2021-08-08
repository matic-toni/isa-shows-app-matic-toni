package com.example.shows_tonimatic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shows_tonimatic.db.ShowsDatabase
import java.lang.IllegalArgumentException

class ShowViewModelFactory(private val database: ShowsDatabase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        }
        throw IllegalArgumentException("Wrong class!")
    }
}