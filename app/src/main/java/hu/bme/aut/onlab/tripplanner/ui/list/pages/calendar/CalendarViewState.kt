package hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

sealed class CalendarViewState

object Loading : CalendarViewState()

data class CalendarContent(
    var trips: List<TripListItem> = emptyList(),
    val loading: Boolean = true
) : CalendarViewState()