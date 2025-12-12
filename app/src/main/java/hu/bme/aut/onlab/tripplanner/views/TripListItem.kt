package hu.bme.aut.onlab.tripplanner.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.helpers.AutoCompleteField
import hu.bme.aut.onlab.tripplanner.views.helpers.ComboBox
import hu.bme.aut.onlab.tripplanner.views.helpers.CountryCitiesProvider
import hu.bme.aut.onlab.tripplanner.views.helpers.DatePicker
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListItem(
    item: TripListItem?,
    onOkUploadClick: (TripListItem) -> Unit,
    onOkEditClick: (TripListItem) -> Unit,
    onCancelClick: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val calString = String.format(
        Locale.getDefault(), "%04d.%02d.%02d.",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var countryInput by remember { mutableStateOf(item?.country ?: "") }
    var placeInput by remember { mutableStateOf(item?.place ?: "") }
    var descriptionInput by remember { mutableStateOf(item?.description ?: "") }
    var dateInput by remember { mutableStateOf(item?.date ?: calString) }
    var checkedState by remember { mutableStateOf(item?.visited ?: false) }

    val categoryList = TripListItem.Category.values().map { it.name }
    var categorySelectedIndex by remember { mutableStateOf(item?.category?.ordinal ?: 0) }
    var categoryExpanded by remember { mutableStateOf(false) }

    val countries = CountryCitiesProvider.countriesWithCities.keys.toList()
    val cities = CountryCitiesProvider.countriesWithCities[countryInput] ?: emptyList()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .widthIn(max = 420.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_location_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (item == null)
                        stringResource(R.string.new_triplist_item)
                    else stringResource(R.string.edit_triplist_item),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            AutoCompleteField(
                label = stringResource(R.string.country),
                value = countryInput,
                onValueChange = { newCountry ->
                    countryInput = newCountry
                    placeInput = ""
                },
                options = countries
            )
            Spacer(Modifier.height(12.dp))
                AutoCompleteField(
                    label = stringResource(R.string.place),
                    value = placeInput,
                    onValueChange = { placeInput = it },
                    options = cities
                )
            //}
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descriptionInput,
                onValueChange = { descriptionInput = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DatePicker(dateInput) { dateInput = it }
            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ComboBox(
                list = categoryList,
                selectedIndex = categorySelectedIndex,
                onIndexChanged = { categorySelectedIndex = it },
                isExpanded = categoryExpanded,
                onExpandedChanged = { categoryExpanded = it },
                textWidth = 150.dp
            )
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = stringResource(R.string.visited),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(stringResource(R.string.button_cancel))
                }
                Spacer(Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = {
                        if (placeInput.isBlank()) {
                            Toast.makeText(context, "Hely megadása kötelező", Toast.LENGTH_SHORT).show()
                            return@FilledTonalButton
                        }
                        if (item == null) {
                            onOkUploadClick(
                                TripListItem(
                                    country = countryInput,
                                    place = placeInput,
                                    description = descriptionInput,
                                    date = dateInput,
                                    category = TripListItem.Category.values()[categorySelectedIndex],
                                    visited = checkedState,
                                    uid = "",
                                    coordinateX = "",
                                    coordinateY = ""
                                )
                            )
                        } else {
                            item.country = countryInput
                            item.place = placeInput
                            item.description = descriptionInput
                            item.date = dateInput
                            item.category = TripListItem.Category.values()[categorySelectedIndex]
                            item.visited = checkedState
                            onOkEditClick(item)
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(stringResource(R.string.button_ok))
                }
            }
        }
    }
}
