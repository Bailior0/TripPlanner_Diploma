package hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier

import android.graphics.Bitmap
import org.tensorflow.lite.support.label.Category

sealed class IdentifierViewState

object Loading : IdentifierViewState()

data class IdentifierContent(
    var prediction: Category? = null,
    var image: Bitmap? = null,
    val loading: Boolean = false
) : IdentifierViewState()