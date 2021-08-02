package com.example.shows_tonimatic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE show_id IS :showId ")
    fun getReviews(showId: Int): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE review_id IS :id")
    fun getReview(id: String): LiveData<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(reviews: List<ReviewEntity>)

    @Query("DELETE FROM review WHERE review_id IS :id")
    fun deleteRewiev(id: String)
}
