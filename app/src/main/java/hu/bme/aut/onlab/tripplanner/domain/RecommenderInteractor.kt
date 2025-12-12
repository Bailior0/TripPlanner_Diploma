package hu.bme.aut.onlab.tripplanner.domain

import android.os.Build
import androidx.annotation.RequiresApi
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.recommender.DestinationRepository
import hu.bme.aut.onlab.tripplanner.data.recommender.RecommendationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecommenderInteractor @Inject constructor(
    private val destinationRepository: DestinationRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun recommend(userTrips: List<TripListItem>, topN: Int = 5): List<RecommendationResult> =
        withContext(Dispatchers.Default) {
            destinationRepository.recommendForUser(userTrips, topN)
        }
}
