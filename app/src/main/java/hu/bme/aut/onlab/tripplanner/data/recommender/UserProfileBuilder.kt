package hu.bme.aut.onlab.tripplanner.data.recommender

import android.os.Build
import androidx.annotation.RequiresApi
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.exp
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.O)
object UserProfileBuilder {

    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun build(trips: List<TripListItem>): UserProfile {
        if (trips.isEmpty()) {
            return UserProfile(
                embedding = FloatArray(0),
                tripsCount = 0,
                categoryHistogram = emptyMap(),
                countryHistogram = emptyMap()
            )
        }

        val sampleEmbedding = TripFeatureExtractor.getEmbedding(trips.first())
        val dim = sampleEmbedding.size

        val sumVector = FloatArray(dim) { 0f }
        var totalWeight = 0f

        val categoryHistogram = mutableMapOf<TripListItem.Category, Int>()
        val countryHistogram = mutableMapOf<String, Int>()

        for (trip in trips) {
            val embedding = TripFeatureExtractor.getEmbedding(trip)
            val weight = computeRecencyWeight(trip.date)

            for (i in embedding.indices) {
                sumVector[i] += embedding[i] * weight
            }
            totalWeight += weight

            categoryHistogram[trip.category] = (categoryHistogram[trip.category] ?: 0) + 1

            val countryKey = trip.country.ifBlank { "Ismeretlen ország" }
            countryHistogram[countryKey] = (countryHistogram[countryKey] ?: 0) + 1
        }

        val finalEmbedding = if (totalWeight > 0f) {
            FloatArray(dim) { i -> sumVector[i] / totalWeight }
        } else {
            sumVector
        }

        return UserProfile(
            embedding = finalEmbedding,
            tripsCount = trips.size,
            categoryHistogram = categoryHistogram,
            countryHistogram = countryHistogram
        )
    }

    private fun computeRecencyWeight(dateString: String): Float {
        return try {
            val tripDate = LocalDate.parse(dateString, DATE_FORMATTER)
            val now = LocalDate.now()
            val days = max(0L, ChronoUnit.DAYS.between(tripDate, now))
            val months = days.toFloat() / 30f

            val lambda = 0.15f
            exp(-lambda * months)
        } catch (e: Exception) {
            1f
        }
    }
}
