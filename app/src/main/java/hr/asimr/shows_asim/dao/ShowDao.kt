package hr.asimr.shows_asim.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.asimr.shows_asim.models.Show

@Dao
interface ShowDao {
    @Query("SELECT * FROM shows")
    fun getAllShows() : List<Show>

    @Query("SELECT * FROM shows WHERE idShow IS :idShow")
    fun getShow(idShow: String) : Show

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<Show>)
}