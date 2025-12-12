package hu.bme.aut.onlab.tripplanner.data.recommender

import kotlin.math.sqrt

object SimilarityUtils {

    fun cosine(a: FloatArray, b: FloatArray): Float {
        if (a.isEmpty() || b.isEmpty()) return 0f
        if (a.size != b.size) return 0f

        var dot = 0f
        var normA = 0f
        var normB = 0f

        for (i in a.indices) {
            dot += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }

        val denom = (sqrt(normA.toDouble()) * sqrt(normB.toDouble())).toFloat()
        return if (denom == 0f) 0f else dot / denom
    }
}
