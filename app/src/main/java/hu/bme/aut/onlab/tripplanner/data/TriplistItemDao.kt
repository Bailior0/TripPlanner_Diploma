package hu.bme.aut.onlab.tripplanner.data

import androidx.room.*

@Dao
interface TriplistItemDao {
    @Query("SELECT * FROM triplistitem")
    fun getAll(): List<TriplistItem>

    @Insert
    fun insert(triplistItems: TriplistItem): Long

    @Update
    fun update(triplistItems: TriplistItem)

    @Delete
    fun deleteItem(triplistItems: TriplistItem)
}