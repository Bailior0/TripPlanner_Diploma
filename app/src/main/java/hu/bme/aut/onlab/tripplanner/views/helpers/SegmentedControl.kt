package hu.bme.aut.onlab.tripplanner.views.helpers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedControl(texts: List<String>, qualitySwitchState: Int, onValueChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for ((index, text) in texts.withIndex()) {
            Button(
                content = {
                    Text(
                        text = text,
                    )
                },
                onClick = {
                    onValueChange(index)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (qualitySwitchState == index)
                        MaterialTheme.colors.primary
                    else Color.LightGray
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp, 0.dp)
            )
        }
    }
}