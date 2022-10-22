package hu.bme.aut.onlab.tripplanner.data.disk.database

import androidx.room.*
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

@Database(entities = [TripListItem::class], version = 1)
@TypeConverters(value = [TripListItem.Category::class])
abstract class TripListDatabase : RoomDatabase() {

    abstract fun tripListItemDao(): TripListItemDao
}