package hu.bme.aut.onlab.tripplanner.views.helpers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun ComboBox(
    list: List<String>,
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    textWidth: Dp
) {

    Button(
        onClick = { onExpandedChanged(true) },
    ) {
        if (list.isNotEmpty()) {
            Row {
                Text(
                    text = list[selectedIndex],
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(textWidth)
                )
                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
            }
        } else {
            Text(text = "—")
        }
    }

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onExpandedChanged(false) },
    ) {
        list.forEachIndexed { index, s ->
            DropdownMenuItem(
                onClick = {
                    onIndexChanged(index)
                    onExpandedChanged(false)
                }
            ) {
                Text(text = s)
            }
        }
    }
}
