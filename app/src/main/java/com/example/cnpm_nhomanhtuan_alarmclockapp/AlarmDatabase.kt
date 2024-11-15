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


@Database(entities = [Alarm::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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
                    .fallbackToDestructiveMigration() // Thêm dòng này để xóa dữ liệu cũ khi thay đổi schema
                    .addCallback(AlarmDatabaseCallback()) // Giữ lại callback nếu cần
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
                        // Xử lý công việc nếu cần sau khi tạo cơ sở dữ liệu
                    }
                }
            }
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