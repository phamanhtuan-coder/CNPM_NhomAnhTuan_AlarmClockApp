package com.example.cnpm_nhomanhtuan_alarmclockapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Alarm
import com.example.cnpm_nhomanhtuan_alarmclockapp.utils.AlarmScheduler
import com.example.cnpm_nhomanhtuan_alarmclockapp.di.Graph
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Time
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.repository.AlarmRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmDetailViewModel(
    private val alarmId: Int,
    private val alarmRepository: AlarmRepository = Graph.repository
) : ViewModel() {
    var state by mutableStateOf(AlarmState())

    init {
        if (alarmId > 0) {
            viewModelScope.launch {
                alarmRepository.getAlarmById(alarmId).collectLatest {
                    state = state.copy(
                        id = it.id,
                        label = it.label,
                        time = it.time,
                        days = it.days,
                        isEnabled = it.isEnabled,
                        sound = it.sound
                    )
                }
            }
        }
    }

    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.insertAlarm(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm, context: Context) {
        viewModelScope.launch {
            alarmRepository.updateAlarm(alarm)
            AlarmScheduler.scheduleAlarmIfEnabled(
                context = context,
                alarm = alarm
            )
        }
    }
}


@Suppress("UNCHECKED_CAST")
class AlarmDetailViewModelFactor(private val id:Int) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        return AlarmDetailViewModel(alarmId = id) as T
    }
}

data class AlarmState(
    var id:Int = 0,
    var label:String = "",
    var time: Time = Time(0, 0, amPm = "AM"),
    var days:List<String> = emptyList(),
    var isEnabled: Boolean = false,
    var sound: String = "Default"
)