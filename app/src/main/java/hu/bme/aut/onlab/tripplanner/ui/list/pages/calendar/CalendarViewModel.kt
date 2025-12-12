package hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar

import androidx.lifecycle.viewModelScope
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val calendarPresenter: CalendarPresenter) : RainbowCakeViewModel<CalendarViewState>(
    Loading
) {

    fun addListener() = execute {
        viewState = CalendarContent(loading = true)
        viewModelScope.launch {
            calendarPresenter.addListener().collect {
                viewState = CalendarContent(loading = true)
                viewState = CalendarContent(
                    trips = it,
                    loading = false
                )
            }
        }
    }
}