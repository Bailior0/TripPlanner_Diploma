package hu.bme.aut.onlab.tripplanner.data.recommender

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

data class UserProfile(
    val embedding: FloatArray,
    val tripsCount: Int,
    val categoryHistogram: Map<TripListItem.Category, Int>,
    val countryHistogram: Map<String, Int>
)
