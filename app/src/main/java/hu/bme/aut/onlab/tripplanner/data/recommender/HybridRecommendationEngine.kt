package hu.bme.aut.onlab.tripplanner.data.recommender

import android.os.Build
import androidx.annotation.RequiresApi
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

object HybridRecommendationEngine {

    @RequiresApi(Build.VERSION_CODES.O)
    fun recommend(
        userTrips: List<TripListItem>,
        destinations: List<Destination>,
        topN: Int = 5
    ): List<RecommendationResult> {

        if (destinations.isEmpty()) return emptyList()

        val userProfile = UserProfileBuilder.build(userTrips)

        if (userProfile.tripsCount == 0 || userProfile.embedding.isEmpty()) {
            return destinations
                .sortedByDescending { it.popularity }
                .take(topN)
                .map { dest ->
                    RecommendationResult(
                        destination = dest,
                        score = dest.popularity.toDouble(),
                        reasons = listOf(
                            "Még nincs elég korábbi utazási adat, ezért a legnépszerűbb úti célokat ajánljuk.",
                            "Ez később személyre szabottabb lesz, ahogy több utat rögzítesz az alkalmazásban."
                        )
                    )
                }
        }

        val results = destinations.map { dest ->
            val destEmbedding = DestinationFeatureExtractor.getEmbedding(dest)

            val sim = SimilarityUtils.cosine(userProfile.embedding, destEmbedding) // 0..1

            val rule = RuleBasedScorer.compute(userProfile, dest) // 0..1

            val alpha = 0.6f
            val beta = 0.4f
            val finalScore = (alpha * sim + beta * rule.score)

            val explanations = mutableListOf<String>()
            explanations.add("Hasonlóság a korábbi útjaid mintázatához (embedding alapú): %.2f".format(sim))
            explanations.addAll(rule.explanations)

            RecommendationResult(
                destination = dest,
                score = finalScore.toDouble(),
                reasons = explanations
            )
        }

        return results
            .sortedByDescending { it.score }
            .take(topN)
    }
}
