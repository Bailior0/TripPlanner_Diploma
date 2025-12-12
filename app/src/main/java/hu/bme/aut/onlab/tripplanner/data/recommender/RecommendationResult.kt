package hu.bme.aut.onlab.tripplanner.data.recommender

data class RecommendationResult(
    val destination: Destination,
    val score: Double,
    val reasons: List<String>
)
