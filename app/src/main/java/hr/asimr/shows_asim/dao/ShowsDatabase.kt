package hr.asimr.shows_asim.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.asimr.shows_asim.models.Review
import hr.asimr.shows_asim.models.Show

@Database(
    entities = [
        Show::class,
        Review::class
    ],
    version = 3
)

abstract class ShowsDatabase : RoomDatabase() {
    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = database
                database
            }
        }
    }

    abstract fun showDao(): ShowDao
    abstract fun reviewDao(): ReviewDao
}