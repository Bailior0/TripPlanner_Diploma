package hu.bme.aut.onlab.tripplanner.views.helpers

import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import hu.bme.aut.onlab.tripplanner.R
import java.util.*

@Composable
fun DatePicker(
    dateInput: String,
    onValueChange: (String) -> Unit
) {
    val day = dateInput.substring(8, 10).toInt()
    val month = dateInput.substring(5, 7).toInt()-1
    val year = dateInput.substring(0, 4).toInt()

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            val view = View.inflate(context, R.layout.date_picker, null)
            val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ) { _, year, monthOfYear, dayOfMonth ->
                onValueChange(
                    String.format(
                        Locale.getDefault(), "%04d.%02d.%02d.",
                        year, monthOfYear + 1, dayOfMonth
                    )
                )
            }
            datePicker
        }
    )
}