package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.helpers.SegmentedControl
import hu.bme.aut.onlab.tripplanner.views.theme.*
import java.util.Locale

@Composable
fun Trips(
    trips: List<TripListItem>,
    onItemClicked: (TripListItem) -> Unit,
    onFabClicked: () -> Unit,
    onItemChanged: (TripListItem) -> Unit,
    onEditClicked: (TripListItem) -> Unit,
    onDeleteClicked: (TripListItem) -> Unit
) {
    var switchState by remember { mutableIntStateOf(0) }

    val sorted = remember(trips) {
        trips.sortedBy { it.date }
    }

    val calendar = java.util.Calendar.getInstance()
    val today = String.format(
        Locale.getDefault(), "%04d.%02d.%02d.",
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH) + 1,
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabClicked() },
                modifier = Modifier.padding(bottom = 90.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            SegmentedControl(
                listOf("Future", "Past"),
                switchState
            ) { switchState = it }

            val shown = if (switchState == 0)
                sorted.filter { it.date >= today }
            else
                sorted.filter { it.date < today }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(shown) { item ->
                    TripItem(
                        item = item,
                        onItemClicked = onItemClicked,
                        onItemChanged = onItemChanged,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked
                    )
                }
            }
        }
    }
}

@Composable
fun TripItem(
    item: TripListItem,
    onItemClicked: (TripListItem) -> Unit,
    onItemChanged: (TripListItem) -> Unit,
    onEditClicked: (TripListItem) -> Unit,
    onDeleteClicked: (TripListItem) -> Unit
) {
    var checkedState by remember { mutableStateOf(item.visited) }
    checkedState = item.visited

    val categoryColor = when (item.category) {
        TripListItem.Category.OUTDOORS -> Outdoors
        TripListItem.Category.BEACHES -> Beaches
        TripListItem.Category.SIGHTSEEING -> Sightseeing
        TripListItem.Category.SKIING -> Skiing
        TripListItem.Category.BUSINESS -> Business
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 3.dp,
                color = when(item.category) {
                    TripListItem.Category.OUTDOORS -> Outdoors
                    TripListItem.Category.BEACHES -> Beaches
                    TripListItem.Category.SIGHTSEEING -> Sightseeing
                    TripListItem.Category.SKIING -> Skiing
                    TripListItem.Category.BUSINESS -> Business
                },
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onItemClicked(item) },
        shape = RoundedCornerShape(24.dp),
        elevation = 6.dp,
        backgroundColor = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight()
                    .background(categoryColor)
            )

            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = {
                            checkedState = it
                            item.visited = checkedState
                            onItemChanged(item)
                        }
                    )

                    Image(
                        painter = painterResource(
                            id = when (item.category) {
                                TripListItem.Category.OUTDOORS -> R.drawable.outdoors
                                TripListItem.Category.BEACHES -> R.drawable.beaches
                                TripListItem.Category.SIGHTSEEING -> R.drawable.sightseeing
                                TripListItem.Category.SKIING -> R.drawable.skiing
                                TripListItem.Category.BUSINESS -> R.drawable.business
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(top = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.place,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = item.country,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = item.date,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .background(Color(0x22000000), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.category.name,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(onClick = { onEditClicked(item) }) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = { onDeleteClicked(item) }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}
