package hu.bme.aut.onlab.tripplanner.views.helpers

import android.view.View
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.util.*

@Composable
fun DatePicker(
    dateInput: String,
    onValueChange: (String) -> Unit
) {
    val day = try { dateInput.substring(8, 10).toInt() } catch (_: Exception) { Calendar.getInstance().get(Calendar.DAY_OF_MONTH) }
    val month = try { dateInput.substring(5, 7).toInt() - 1 } catch (_: Exception) { Calendar.getInstance().get(Calendar.MONTH) }
    val year = try { dateInput.substring(0, 4).toInt() } catch (_: Exception) { Calendar.getInstance().get(Calendar.YEAR) }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            val view = View.inflate(context, hu.bme.aut.onlab.tripplanner.R.layout.date_picker, null)
            val datePicker = view.findViewById<DatePicker>(hu.bme.aut.onlab.tripplanner.R.id.datePicker)
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ) { _, y, m, d ->
                onValueChange(String.format(Locale.getDefault(), "%04d.%02d.%02d.", y, m + 1, d))
            }
            datePicker
        }
    )
}
