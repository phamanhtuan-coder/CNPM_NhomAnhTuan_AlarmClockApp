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
import kotlinx.coroutines.launch

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
            // Add any initial data here if needed, or leave this empty
            alarmDao.insert(Alarm(id = 1, label = "Morning Alarm", time = "07:00", days = listOf("","M","T","W","T","F",""),isEnabled = true))
        }
    }
}

class AlarmRepository(private val alarmDao: AlarmDao) {

    val alarms: Flow<List<Alarm>> = alarmDao.getAllAlarms()

    fun getAlarmById(id: Int): Flow<Alarm> = alarmDao.getAlarmById(id)

    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun update(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }
}