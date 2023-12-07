package hu.bme.aut.onlab.tripplanner.data.disk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = ""
): Parcelable