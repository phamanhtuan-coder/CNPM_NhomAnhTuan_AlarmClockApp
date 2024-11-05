package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class DayOfWeek(val shortName: String) {
    SUNDAY("S"),
    MONDAY("M"),
    TUESDAY("T"),
    WEDNESDAY("W"),
    THURSDAY("T"),
    FRIDAY("F"),
    SATURDAY("S")
}



class AlarmDetailViewModel(
    private val id: Int,
    private val alarmRepository: AlarmRepository = Graph.repository
) : ViewModel() {

    var state by mutableStateOf(AlarmState())
        private set

    init {
        if (id > 0) {
            viewModelScope.launch {
                alarmRepository.getAlarmById(id).collectLatest { alarm ->
                    state = state.copy(
                        label = alarm.label,
                        time = alarm.time,
                        days = alarm.days,
                        isEnabled = alarm.isEnabled
                    )
                }
            }
        }
    }

    fun onChangeLabel(newLabel: String) {
        state = state.copy(label = newLabel)
    }

    fun onChangeTime(newTime: String) {
        state = state.copy(time = newTime)
    }

    fun onToggleDay(day: DayOfWeek) {
        val updatedDays = if (state.selectedDays.contains(day)) {
            state.selectedDays - day
        } else {
            state.selectedDays + day
        }
        state = state.copy(selectedDays = updatedDays)
    }

    fun onToggleEnabled(isEnabled: Boolean) {
        // Update the state with the new value
        state = state.copy(isEnabled = isEnabled)
    }

    fun saveAlarm() {
        viewModelScope.launch {
            val alarm = Alarm(id, state.label, state.time, state.days, state.isEnabled)
            if (id > 0) {
                alarmRepository.updateAlarm(alarm)
            } else {
                alarmRepository.insertAlarm(alarm)
            }
        }
    }
}

// Factory for creating instances of AlarmDetailViewModel
@Suppress("UNCHECKED_CAST")
class AlarmDetailViewModelFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmDetailViewModel(id) as T
    }
}

// Data class for the UI state
data class AlarmState(
    val selectedDays: Set<DayOfWeek> = emptySet(),
    val label: String = "",
    val time: String = "",
    val days: List<String> = listOf(),
    val isEnabled: Boolean = false
)