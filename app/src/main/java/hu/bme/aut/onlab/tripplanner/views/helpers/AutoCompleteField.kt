package hu.bme.aut.onlab.tripplanner.views.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun AutoCompleteField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldWidth by remember { mutableStateOf(0) }
    var textFieldHeight by remember { mutableStateOf(0) }

    val density = LocalDensity.current

    val filteredOptions = remember(value, options) {
        if (value.isEmpty()) emptyList()
        else options.filter { it.contains(value, ignoreCase = true) }.take(4)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                if (!expanded) expanded = true
            },
            singleLine = true,
            label = { Text(label, color = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    expanded = focusState.isFocused && filteredOptions.isNotEmpty()
                }
                .onGloballyPositioned { coordinates ->
                    textFieldWidth = coordinates.size.width
                    textFieldHeight = coordinates.size.height
                }
        )

        if (expanded && filteredOptions.isNotEmpty()) {
            Popup(
                alignment = androidx.compose.ui.Alignment.TopStart,
                offset = IntOffset(0, with(density) { textFieldHeight.toDp().roundToPx() }),
                properties = PopupProperties(focusable = false, dismissOnClickOutside = true),
                onDismissRequest = { expanded = false }
            ) {
                Box(
                    modifier = Modifier
                        .width(with(density) { textFieldWidth.toDp() })
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(vertical = 4.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 180.dp)
                            .padding(vertical = 2.dp)
                    ) {
                        items(filteredOptions) { option ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onValueChange(option)
                                        expanded = false
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(option)
                            }
                        }
                    }
                }
            }
        }
    }
}
