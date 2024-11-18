package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmapp.Alarm
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmScreenViewModel(
    private val alarmRepository: AlarmRepository = Graph.repository
) : ViewModel() {
    var state by mutableStateOf(AlarmScreenState())
        private set

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("AlarmScreenViewModel", "Unhandled exception: ${exception.message}", exception)
    }

    init {
            getAllAlarms()
    }

    private fun getAllAlarms() {
        viewModelScope.launch(exceptionHandler) {
            try {
                alarmRepository.alarms.collectLatest { alarms ->
                    state = state.copy(alarms = alarms)
                    Log.d("AlarmScreenViewModel", "Fetched all alarms: $alarms")
                }
            } catch (e: Exception) {
                Log.e("AlarmScreenViewModel", "Error fetching alarms: ${e.message}", e)
            }
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(exceptionHandler) {
            try {
                alarmRepository.deleteAlarm(alarm = alarm)
                Log.d("AlarmScreenViewModel", "Deleted alarm: $alarm")
            } catch (e: Exception) {
                Log.e("AlarmScreenViewModel", "Error deleting alarm: ${e.message}", e)
            }
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(exceptionHandler) {
            try {
                alarmRepository.updateAlarm(alarm = alarm)
                Log.d("AlarmScreenViewModel", "Updated alarm: $alarm")
            } catch (e: Exception) {
                Log.e("AlarmScreenViewModel", "Error updating alarm: ${e.message}", e)
            }
        }
    }
}

data class AlarmScreenState(
    val alarms: List<Alarm> = emptyList(),
    val alarmFound: Alarm = Alarm()
)
