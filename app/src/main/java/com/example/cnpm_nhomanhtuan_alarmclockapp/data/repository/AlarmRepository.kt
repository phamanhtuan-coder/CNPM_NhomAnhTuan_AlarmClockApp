package com.example.cnpm_nhomanhtuan_alarmclockapp.data.repository

import android.util.Log
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Alarm
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.source.local.dao.AlarmDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn

class AlarmRepository(private val alarmDao: AlarmDao) {

    val alarms: StateFlow<List<Alarm>> = alarmDao.getAllAlarms().stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    // Retrieves a single alarm by ID
    fun getAlarmById(id: Int) = alarmDao.getAlarmById(id)

    suspend fun isAlarmEnabled(id: Int): Boolean {
        val isEnabled = alarmDao.getIsEnabled(id).firstOrNull()
        Log.d("AlarmRepository", "is_enabled for alarm ID $id: $isEnabled")
        return isEnabled ?: false // Nếu null, mặc định trả về false
    }


    // Updates an existing alarm in the database
    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.update(alarm)
        alarmDao.getAllAlarms()
    }

    // Inserts a new alarm or replaces an existing one
    suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insert(alarm)
        alarmDao.getAllAlarms()
    }

    // Deletes an alarm from the database
    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    // Searches alarms by label
    fun searchAlarms(query: String): Flow<List<Alarm>> {
        return alarmDao.searchAlarms(query)
    }


}