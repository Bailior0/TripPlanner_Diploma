package hu.bme.aut.onlab.tripplanner.data.disk.database

import androidx.room.*
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

@Dao
interface TripListItemDao {

    @Query("SELECT * FROM triplistitem")
    suspend fun getAll(): List<TripListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tripListItems: TripListItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tripListItems: List<TripListItem>)

    @Update
    suspend fun update(tripListItems: TripListItem)

    @Delete
    suspend fun delete(tripListItems: TripListItem)

    @Query("DELETE FROM triplistitem")
    suspend fun deleteAll()
}
