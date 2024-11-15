package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmScreenViewModel (
    private val alarmRepository: AlarmRepository = Graph.repository
):ViewModel(){
    var state by mutableStateOf(AlarmScreenState())
        private set

    init {
        getAllAlarms()
    }

    private fun getAllAlarms(){
        viewModelScope.launch {
            alarmRepository.alarms.collectLatest {
                state = state.copy(
                    alarms = it
                )
            }
        }
    }
    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch {
            alarmRepository.deleteAlarm(alarm = alarm)
        }
    }

    fun getAlarmByID(id:Int){
        viewModelScope.launch {
            alarmRepository.getAlarmById(id).collectLatest {
                state = state.copy(
                    alarmFound = it
                )
            }
        }
    }
}

data class AlarmScreenState(
    val alarms:List<Alarm> = emptyList(),
    val alarmFound: Alarm = Alarm()
)