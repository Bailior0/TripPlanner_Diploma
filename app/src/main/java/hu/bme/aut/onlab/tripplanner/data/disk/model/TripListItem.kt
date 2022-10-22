package hu.bme.aut.onlab.tripplanner.data.disk.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "triplistitem")
data class TripListItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "Place") var place: String,
    @ColumnInfo(name = "Country") var country: String,
    @ColumnInfo(name = "Description") var description: String,
    @ColumnInfo(name = "Date") var date: String,
    @ColumnInfo(name = "Category") var category: Category,
    @ColumnInfo(name = "Visited") var visited: Boolean
) : Parcelable {
    enum class Category {
        OUTDOORS, BEACHES, SIGHTSEEING, SKIING, BUSINESS;
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