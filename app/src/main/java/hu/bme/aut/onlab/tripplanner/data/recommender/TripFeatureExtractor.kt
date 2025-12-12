package hu.bme.aut.onlab.tripplanner.data.recommender

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import kotlin.math.*

object TripFeatureExtractor {

    private val TAG_VOCAB = DestinationFeatureExtractor.TAG_VOCAB
    private val CATEGORY_VOCAB = DestinationFeatureExtractor.CATEGORY_VOCAB
    private val REGION_VOCAB = DestinationFeatureExtractor.REGION_VOCAB
    private val CLIMATE_VOCAB = DestinationFeatureExtractor.CLIMATE_VOCAB
    private val SEASON_VOCAB = DestinationFeatureExtractor.SEASON_VOCAB

    private val COUNTRY_REGION = mapOf(
        "franciaország" to "Europe",
        "spanyolország" to "Europe",
        "olaszország" to "Europe",
        "magyarország" to "Europe",
        "németország" to "Europe",
        "görögország" to "Europe",
        "egyiptom" to "Africa",
        "kanada" to "North America",
        "egyesült államok" to "North America",
        "mexikó" to "North America",
        "brazília" to "South America",
        "argentina" to "South America",
        "japán" to "Asia",
        "kína" to "Asia",
        "indonézia" to "Asia",
        "thaiföld" to "Asia",
        "ausztrália" to "Oceania"
    )

    private val COUNTRY_CLIMATE = mapOf(
        "franciaország" to "mild",
        "spanyolország" to "mild",
        "olaszország" to "mild",
        "magyarország" to "continental",
        "németország" to "continental",
        "görögország" to "mild",
        "egyiptom" to "desert",
        "indonézia" to "tropical",
        "thaiföld" to "tropical",
        "japán" to "mild",
        "kanada" to "cold",
        "ausztrália" to "mild"
    )

    fun getEmbedding(trip: TripListItem): FloatArray {
        val vector = mutableListOf<Float>()

        val categoryName = trip.category.name
        vector += oneHot(categoryName, CATEGORY_VOCAB)

        val tags = extractTagsFromTrip(trip)
        vector += multiHot(tags, TAG_VOCAB)

        val region = COUNTRY_REGION[trip.country.lowercase()] ?: "Europe"
        vector += oneHot(region, REGION_VOCAB)

        val climate = COUNTRY_CLIMATE[trip.country.lowercase()] ?: "mild"
        vector += oneHot(climate, CLIMATE_VOCAB)

        val season = extractSeasonFromDate(trip.date)
        vector += multiHot(listOf(season), SEASON_VOCAB)

        vector += listOf(0.5f)

        vector += listOf(0.5f)

        val lat = trip.coordinateX.toDoubleOrNull() ?: 0.0
        val lon = trip.coordinateY.toDoubleOrNull() ?: 0.0
        vector += geoEncode(lat, lon)

        return vector.toFloatArray()
    }


    private fun oneHot(value: String, vocab: List<String>) =
        vocab.map { if (it.equals(value, ignoreCase = true)) 1f else 0f }

    private fun multiHot(values: List<String>, vocab: List<String>) =
        vocab.map { v -> if (values.any { it.equals(v, ignoreCase = true) }) 1f else 0f }

    private fun extractSeasonFromDate(date: String): String {
        val month = date.substringAfterLast("-").toIntOrNull() ?: 6
        return when (month) {
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            in 9..11 -> "autumn"
            else -> "winter"
        }
    }

    private fun extractTagsFromTrip(trip: TripListItem): List<String> {
        val text = (trip.place + " " + trip.description).lowercase()

        val matches = mutableListOf<String>()

        for (tag in TAG_VOCAB) {
            if (text.contains(tag.lowercase())) {
                matches.add(tag)
            }
        }

        if (text.contains("tenger") || text.contains("beach")) matches.add("tengerpart")
        if (text.contains("hegy")) matches.add("hegyek")
        if (text.contains("túra")) matches.add("túrázás")
        if (text.contains("múze") ) matches.add("múzeum")
        if (text.contains("templom")) matches.add("templomok")
        if (text.contains("gasztro") || text.contains("étterem")) matches.add("gasztronómia")
        if (text.contains("park")) matches.add("parkok")

        return matches.distinct()
    }

    private fun geoEncode(lat: Double, lon: Double): List<Float> {
        val latRad = Math.toRadians(lat)
        val lonRad = Math.toRadians(lon)
        return listOf(
            sin(latRad).toFloat(),
            cos(latRad).toFloat(),
            sin(lonRad).toFloat(),
            cos(lonRad).toFloat()
        )
    }
}
