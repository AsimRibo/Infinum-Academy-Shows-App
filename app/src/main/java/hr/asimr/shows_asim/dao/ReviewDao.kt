package hr.asimr.shows_asim.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.asimr.shows_asim.models.Review

@Dao
interface ReviewDao {
    @Query("SELECT DISTINCT * FROM reviews WHERE show_id = :idShow")
    fun getAllReviews(idShow: String): List<Review>

    @Query("SELECT * FROM reviews WHERE idReview IS :idReview")
    fun getReview(idReview: Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: Review)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews: List<Review>)

    @Query("DELETE FROM reviews")
    fun nukeReviews()
}