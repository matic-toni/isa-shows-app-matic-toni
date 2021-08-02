package com.example.shows_tonimatic.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE show_id IS :showId ")
    fun getReviews(showId: Int): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id = :id")
    fun getReview(id: String): LiveData<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(reviews: List<ReviewEntity>)
}
