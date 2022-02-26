package hu.bme.aut.onlab.tripplanner.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "triplistitem")
data class TriplistItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "Place") var place: String,
    @ColumnInfo(name = "Country") var country: String,
    @ColumnInfo(name = "Date") var date: String,
    @ColumnInfo(name = "Category") var category: Category,
    @ColumnInfo(name = "Visited") var visited: Boolean
) {
    enum class Category {
        OUTDOORS, BEACHES, SIGTSEEING, SKIING, BUSINESS;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Category? {
                var ret: Category? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: Category): Int {
                return category.ordinal
            }
        }
    }
}