package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmScreenViewModel(
    private val repository: AlarmRepository = Graph.repository
): ViewModel() {
    var state by mutableStateOf(AlarmListState())
        private set
    var searchQuery by mutableStateOf("")
    init {
      getAllAlarms()
    }
    private fun getAllAlarms() {
        viewModelScope.launch {
            repository.alarms.collectLatest {
                state = state.copy(alarms = it)
            }
        }
    }
    fun toggleAlarm(alarm: Alarm) {
        viewModelScope.launch {
            repository.updateAlarm(alarm.copy(isEnabled = !alarm.isEnabled))
        }
    }
    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        viewModelScope.launch {
            repository.searchAlarms(query).collectLatest {
                state = state.copy(alarms = it)
            }
        }
    }
}

data class AlarmListState(
    val alarms: List<Alarm> = listOf(
        Alarm( label = "Morning Alarm", time = "07:00 AM", days = listOf("M", "T", "W", "T", "F"), isEnabled = true),
        Alarm( label = "Workout Alarm", time = "06:00 AM", days = listOf("M", "W", "F"), isEnabled = false),
    ),
    val foundAlarms: Alarm = Alarm()
)