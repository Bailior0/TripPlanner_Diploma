package hu.bme.aut.onlab.tripplanner.ui.list.pages.recommendation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.recommender.RecommendationResult
import hu.bme.aut.onlab.tripplanner.domain.RecommenderInteractor
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val recommenderInteractor: RecommenderInteractor,
    private val tripsInteractor: TripsInteractor
) : RainbowCakeViewModel<RecommendationViewState>(RecommendationLoading) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadRecommendations(topN: Int = 5) = execute {
        viewState = RecommendationLoading
        viewModelScope.launch {
            val trips = try {
                tripsInteractor.addListener().firstOrNull().orEmpty()
            } catch (e: Exception) {
                emptyList()
            }

            val recs = recommenderInteractor.recommend(trips, topN)
            viewState = RecommendationContent(recommendations = recs)
        }
    }

    fun sendFeedback(result: RecommendationResult, liked: Boolean) = execute {
        // TODO: persist feedback; for now just log or future expansion
    }
}
