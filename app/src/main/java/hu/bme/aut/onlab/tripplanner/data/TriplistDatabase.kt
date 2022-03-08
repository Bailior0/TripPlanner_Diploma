package hu.bme.aut.onlab.tripplanner.data

import android.content.Context
import androidx.room.*

@Database(entities = [TriplistItem::class], version = 1)
@TypeConverters(value = [TriplistItem.Category::class])
abstract class TriplistDatabase : RoomDatabase() {
    abstract fun triplistItemDao(): TriplistItemDao

    companion object {
        fun getDatabase(applicationContext: Context): TriplistDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TriplistDatabase::class.java,
                "triplist-list"
            ).build()
        }
    }
}