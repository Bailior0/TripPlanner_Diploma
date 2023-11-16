package hu.bme.aut.onlab.tripplanner.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.helpers.ComboBox
import hu.bme.aut.onlab.tripplanner.views.helpers.DatePicker
import java.util.*

@Composable
fun TripListItem(
    item: TripListItem?,
    onOkUploadClick: (TripListItem) -> Unit,
    onOkEditClick: (TripListItem) -> Unit,
    onCancelClick: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val calString = String.format(
        Locale.getDefault(), "%04d.%02d.%02d.",
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
    )

    var countryInput by remember { mutableStateOf("") }
    var placeInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var dateInput by remember { mutableStateOf(calString) }
    var checkedState by remember { mutableStateOf(false) }

    val categoryList = mutableListOf<String>()
    for(category in TripListItem.Category.values().toList())
        categoryList.add(category.name)
    var categorySelectedIndex by remember { mutableStateOf(0) }
    var categoryExpanded by remember { mutableStateOf(false) }

    if(item != null) {
        countryInput = item.country
        placeInput = item.place
        descriptionInput = item.description
        dateInput = item.date
        categorySelectedIndex = item.category.ordinal
        checkedState = item.visited
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (
                dialogTitle,
                country,
                place,
                description,
                dateText,
                datePicker,
                categoryText,
                categoryBox,
                visitedChecker,
                cancelButton,
                okButton
            ) = createRefs()

            Text(
                text = when(item == null) {
                    true -> stringResource(R.string.new_triplist_item)
                    false -> stringResource(R.string.edit_triplist_item)
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(5.dp, 0.dp)
                    .constrainAs(dialogTitle) {
                        top.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

            OutlinedTextField(
                value = countryInput,
                onValueChange = { countryInput = it },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.country),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(country) {
                        top.linkTo(dialogTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            OutlinedTextField(
                value = placeInput,
                onValueChange = { placeInput = it },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.place),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(place) {
                        top.linkTo(country.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            OutlinedTextField(
                value = descriptionInput,
                onValueChange = { descriptionInput = it },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.description),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(description) {
                        top.linkTo(place.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = stringResource(R.string.date),
                color = Color.Gray,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(dateText) {
                        top.linkTo(description.bottom)
                        start.linkTo(parent.start)
                    }
            )

            Box(
                modifier = Modifier
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(datePicker) {
                        top.linkTo(dateText.bottom)
                        start.linkTo(parent.start)
                    }
            ) {
                DatePicker(
                    dateInput
                ) { dateInput = it }
            }

            Text(
                text = stringResource(R.string.category),
                color = Color.Gray,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(categoryText) {
                        top.linkTo(datePicker.bottom)
                        start.linkTo(parent.start)
                    }
            )

            Box(
                modifier = Modifier
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(categoryBox) {
                        top.linkTo(categoryText.bottom)
                        start.linkTo(parent.start)
                    }
            ) {
                ComboBox(
                    list = categoryList,
                    selectedIndex = categorySelectedIndex,
                    onIndexChanged = { categorySelectedIndex = it },
                    isExpanded = categoryExpanded,
                    onExpandedChanged = { categoryExpanded = it },
                    textWidth = 110.dp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .constrainAs(visitedChecker) {
                        top.linkTo(categoryBox.bottom)
                        start.linkTo(parent.start)
                    }
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it },
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = Color.LightGray,
                        checkedColor = Color.Black
                    )
                )
                Text(
                    text = stringResource(R.string.visited),
                )
            }

            Button(
                content = {
                    Text(
                        text = stringResource(R.string.button_cancel),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp, 0.dp)
                    )
                },
                onClick = {
                    onCancelClick()
                },
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(cancelButton) {
                        top.linkTo(visitedChecker.bottom)
                        end.linkTo(parent.end)
                    }
            )

            Button(
                content = {
                    Text(
                        text = stringResource(R.string.button_ok),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp, 0.dp)
                    )
                },
                onClick = {
                    if(item == null) {
                        if(placeInput.isEmpty())
                            Toast.makeText(context, "Place field must be filled", Toast.LENGTH_SHORT).show()
                        else
                            onOkUploadClick(
                                TripListItem(
                                    country = countryInput,
                                    place = placeInput,
                                    description = descriptionInput,
                                    date = dateInput,
                                    category =  TripListItem.Category.values().toList()[categorySelectedIndex],
                                    visited = checkedState,
                                    uid = "",
                                    coordinateX = "",
                                    coordinateY = ""
                                )
                            )
                    } else {
                        if(placeInput.isEmpty())
                            Toast.makeText(context, "Place field must be filled", Toast.LENGTH_SHORT).show()
                        else {
                            item.country = countryInput
                            item.place = placeInput
                            item.description = descriptionInput
                            item.date = dateInput
                            item.category = TripListItem.Category.values().toList()[categorySelectedIndex]
                            item.visited = checkedState
                            onOkEditClick(item)
                        }
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(okButton) {
                        top.linkTo(visitedChecker.bottom)
                        end.linkTo(cancelButton.start)
                    }
            )
        }
    }
}