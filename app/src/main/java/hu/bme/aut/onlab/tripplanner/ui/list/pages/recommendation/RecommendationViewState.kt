package hu.bme.aut.onlab.tripplanner.ui.list.pages.recommendation

import hu.bme.aut.onlab.tripplanner.data.recommender.RecommendationResult

sealed class RecommendationViewState

object RecommendationLoading : RecommendationViewState()

data class RecommendationContent(
    val recommendations: List<RecommendationResult> = emptyList(),
    val isRefreshing: Boolean = false
) : RecommendationViewState()
