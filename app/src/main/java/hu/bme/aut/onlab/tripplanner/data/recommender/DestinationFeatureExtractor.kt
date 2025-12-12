package hu.bme.aut.onlab.tripplanner.data.recommender

import kotlin.math.cos
import kotlin.math.sin

object DestinationFeatureExtractor {

    val TAG_VOCAB = listOf(
        "tengerpart", "múzeum", "kultúra", "városnézés", "gasztronómia",
        "templomok", "hegyek", "túrázás", "éjszakai élet", "resort",
        "sznorkelezés", "luxus", "borvidék", "természet", "gejzírek",
        "termálfürdők", "síelés", "vásárlás", "street food", "kikötő",
        "dombos város", "parkok", "romantikus", "technológia", "vallási helyek",
        "sziget", "piacok", "fények", "festival", "történelem",
        "folyópart", "naplemente"
    )

    val CATEGORY_VOCAB = listOf("SIGHTSEEING", "BEACHES", "OUTDOORS", "SKIING", "BUSINESS")
    val REGION_VOCAB = listOf("Europe", "Asia", "Africa", "South America", "North America", "Oceania", "Middle East")
    val CLIMATE_VOCAB = listOf("mild", "continental", "tropical", "desert", "cold")
    val SEASON_VOCAB = listOf("spring", "summer", "autumn", "winter")

    fun getEmbedding(dest: Destination): FloatArray {
        val vector = mutableListOf<Float>()

        vector += oneHot(dest.category.name, CATEGORY_VOCAB)

        vector += multiHot(dest.tags, TAG_VOCAB)

        vector += oneHot(dest.region, REGION_VOCAB)

        vector += oneHot(dest.climate, CLIMATE_VOCAB)

        vector += multiHot(dest.bestSeason, SEASON_VOCAB)

        val costNorm = ((dest.costLevel.coerceIn(1, 5) - 1) / 4f)
        vector += listOf(costNorm)

        vector += listOf(dest.popularity.coerceIn(0f, 1f))

        vector += geoEncode(dest.lat, dest.lon)

        return vector.toFloatArray()
    }

    private fun oneHot(value: String, vocab: List<String>): List<Float> =
        vocab.map { if (it.equals(value, ignoreCase = true)) 1f else 0f }

    private fun multiHot(values: List<String>, vocab: List<String>): List<Float> =
        vocab.map { v -> if (values.any { it.equals(v, ignoreCase = true) }) 1f else 0f }

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
