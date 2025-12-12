package hu.bme.aut.onlab.tripplanner.data.recommender

object CountryToRegionMapper {

    private val map = mapOf(
        "franciaország" to "Europe",
        "spanyolország" to "Europe",
        "olaszország" to "Europe",
        "magyarország" to "Europe",
        "görögország" to "Europe",
        "németország" to "Europe",
        "egyesült államok" to "North America",
        "usa" to "North America",
        "kanada" to "North America",
        "mexikó" to "North America",
        "brazília" to "South America",
        "argentina" to "South America",
        "japán" to "Asia",
        "kína" to "Asia",
        "indonézia" to "Asia",
        "thaiföld" to "Asia",
        "ausztrália" to "Oceania",
        "egyiptom" to "Africa",
        "dél-afrika" to "Africa"
    )

    fun getRegionForCountry(country: String): String =
        map[country.lowercase()] ?: "Europe"
}
