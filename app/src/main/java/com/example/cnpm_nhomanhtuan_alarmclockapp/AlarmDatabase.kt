package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn

@TypeConverters(Converters::class)
@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    abstract val alarmDao: AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                )
                    .addCallback(AlarmDatabaseCallback())
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

        private class AlarmDatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.alarmDao)
                    }
                }
            }
        }

        suspend fun populateDatabase(alarmDao: AlarmDao) {

            var alarm = Alarm(label = "Morning Alarm", time = "07:00 AM", days = listOf("M", "T", "W", "T", "F"), isEnabled = true)
            alarmDao.insert(alarm)
            alarm = Alarm(label = "Workout Alarm", time = "06:00 AM", days = listOf("M", "W", "F"), isEnabled = false)
            alarmDao.insert(alarm)
        }
    }
}

class AlarmRepository(private val alarmDao: AlarmDao) {

    val alarms: StateFlow<List<Alarm>> = alarmDao.getAllAlarms().stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    // Retrieves a single alarm by ID
    fun getAlarmById(id: Int) = alarmDao.getAlarmById(id)

    // Updates an existing alarm in the database
    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    // Inserts a new alarm or replaces an existing one
    suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insert(alarm)
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