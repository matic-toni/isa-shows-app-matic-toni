package com.example.shows_tonimatic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    @SerialName("comment") val comment: String?,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: Int
)
