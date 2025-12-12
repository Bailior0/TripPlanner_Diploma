package hu.bme.aut.onlab.tripplanner.data.recommender

import android.os.Build
import androidx.annotation.RequiresApi
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import java.time.LocalDate

object RuleBasedScorer {

    data class RuleScore(
        val score: Float,
        val explanations: List<String>
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun compute(user: UserProfile, dest: Destination): RuleScore {
        if (user.tripsCount == 0) {
            return RuleScore(0f, listOf("Még nincs elegendő utazási adat a személyre szabáshoz."))
        }

        val reasons = mutableListOf<String>()
        var score = 0f

        val topCategory = user.categoryHistogram.maxByOrNull { it.value }?.key
        if (topCategory != null && topCategory.name.equals(dest.category.name, ignoreCase = true)) {
            score += 0.30f
            reasons.add("Utazásaid nagy része ${topCategory.name.lowercase()} kategóriájú helyekre irányul.")
        }

        val mostVisitedCountry = user.countryHistogram.maxByOrNull { it.value }?.key?.lowercase()
        if (mostVisitedCountry != null) {
            val visitedRegion = CountryToRegionMapper.getRegionForCountry(mostVisitedCountry)
            if (visitedRegion == dest.region) {
                score += 0.20f
                reasons.add("Gyakran utazol a(z) ${dest.region} régióba, ezért ez a hely is illeszkedik a szokásaidhoz.")
            }
        }

        val userCountriesLower = user.countryHistogram.keys.map { it.lowercase() }
        if (!userCountriesLower.contains(dest.country.lowercase())) {
            score += 0.20f
            reasons.add("Még nem jártál ${dest.country} területén — új élményt adhat.")
        } else {
            reasons.add("${dest.country} már ismerős a korábbi útjaid alapján.")
        }

        val nowMonth = LocalDate.now().month.value
        val currentSeason = when (nowMonth) {
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            in 9..11 -> "autumn"
            else -> "winter"
        }
        if (dest.bestSeason.contains(currentSeason)) {
            score += 0.15f
            reasons.add("Jelenleg kedvező szezon van ehhez az úti célhoz (${currentSeason}).")
        }

        val favCategoriesSorted = user.categoryHistogram
            .entries
            .sortedByDescending { it.value }
            .map { it.key }

        if (favCategoriesSorted.isNotEmpty()) {
            val top = favCategoriesSorted.first()
            if (top == TripListItem.Category.OUTDOORS && dest.tags.any { it.contains("túrázás", true) || it.contains("hegy", true) }) {
                score += 0.10f
                reasons.add("Sok OUTDOORS jellegű utad volt, ez a hely is ilyen jellegű programokat kínál.")
            }
            if (top == TripListItem.Category.BEACHES && dest.tags.any { it.contains("tengerpart", true) }) {
                score += 0.10f
                reasons.add("Korábbi útjaid alapján kedveled a tengerparti helyeket.")
            }
        }

        return RuleScore(
            score = score.coerceAtMost(1f),
            explanations = reasons
        )
    }
}
