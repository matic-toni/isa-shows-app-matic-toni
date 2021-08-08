package com.example.shows_tonimatic

import android.app.Application
import com.example.shows_tonimatic.db.ShowsDatabase

class ShowsApp : Application() {
    val showsDatabase by lazy {
        ShowsDatabase.getDatabase(this)
    }
}