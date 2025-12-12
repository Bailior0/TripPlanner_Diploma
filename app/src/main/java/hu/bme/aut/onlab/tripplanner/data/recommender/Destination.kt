package hu.bme.aut.onlab.tripplanner.data.recommender

data class Destination(
    val id: String,
    val name: String,
    val country: String,
    val category: Category,
    val popularity: Float,
    val description: String,

    val lat: Double,
    val lon: Double,
    val region: String,
    val climate: String,
    val tags: List<String>,
    val activities: List<String>,
    val bestSeason: List<String>,
    val costLevel: Int
) {
    enum class Category {
        OUTDOORS, BEACHES, SIGHTSEEING, SKIING, BUSINESS
    }
}
