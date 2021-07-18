package com.example.shows_tonimatic.model

import androidx.annotation.DrawableRes

class Review(
    val show_id : String,
    val name: String,
    val comment: String,
    val rate: Int,
    @DrawableRes val imageResourceId: Int
)