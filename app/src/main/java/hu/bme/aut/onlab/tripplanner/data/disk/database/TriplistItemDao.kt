package hu.bme.aut.onlab.tripplanner.data.disk.database

import androidx.room.*
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

@Dao
interface TripListItemDao {
    @Query("SELECT * FROM triplistitem")
    suspend fun getAll(): List<TripListItem>

    @Insert
    suspend fun insert(tripListItems: TripListItem): Long

    @Update
    suspend fun update(tripListItems: TripListItem)

    @Delete
    suspend fun delete(tripListItems: TripListItem)
}