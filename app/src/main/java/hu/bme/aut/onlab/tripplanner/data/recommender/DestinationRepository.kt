package hu.bme.aut.onlab.tripplanner.data.recommender

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DestinationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun loadAllDestinations(): List<Destination> = withContext(Dispatchers.IO) {
        val json = context.assets.open("destinations.json").use { stream ->
            val bytes = stream.readBytes()
            String(bytes, Charset.forName("UTF-8"))
        }

        val arr = JSONArray(json)
        val result = mutableListOf<Destination>()

        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)

            val catString = obj.optString("category", "SIGHTSEEING")
            val cat = try {
                Destination.Category.valueOf(catString.uppercase())
            } catch (e: Exception) {
                Destination.Category.SIGHTSEEING
            }

            val tagsJson = obj.optJSONArray("tags")
            val tags = mutableListOf<String>()
            if (tagsJson != null) {
                for (j in 0 until tagsJson.length()) {
                    tags.add(tagsJson.optString(j))
                }
            }

            val activitiesJson = obj.optJSONArray("activities")
            val activities = mutableListOf<String>()
            if (activitiesJson != null) {
                for (j in 0 until activitiesJson.length()) {
                    activities.add(activitiesJson.optString(j))
                }
            }

            val seasonsJson = obj.optJSONArray("bestSeason")
            val seasons = mutableListOf<String>()
            if (seasonsJson != null) {
                for (j in 0 until seasonsJson.length()) {
                    seasons.add(seasonsJson.optString(j))
                }
            }

            result.add(
                Destination(
                    id = obj.optString("id", "dest_$i"),
                    name = obj.optString("name", "Ismeretlen"),
                    country = obj.optString("country", "Ismeretlen ország"),
                    category = cat,
                    popularity = obj.optDouble("popularity", 0.5).toFloat(),
                    description = obj.optString("shortDescription", ""),

                    lat = obj.optDouble("lat", 0.0),
                    lon = obj.optDouble("lon", 0.0),
                    region = obj.optString("region", "Europe"),
                    climate = obj.optString("climate", "mild"),
                    tags = tags,
                    activities = activities,
                    bestSeason = seasons,
                    costLevel = obj.optInt("costLevel", 3)
                )
            )
        }

        result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun recommendForUser(
        userTrips: List<TripListItem>,
        topN: Int = 5
    ): List<RecommendationResult> = withContext(Dispatchers.Default) {
        val all = loadAllDestinations()
        HybridRecommendationEngine.recommend(
            userTrips = userTrips,
            destinations = all,
            topN = topN
        )
    }
}
