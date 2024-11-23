package com.example.cnpm_nhomanhtuan_alarmclockapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Alarm
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.source.local.dao.AlarmDao
import com.example.cnpm_nhomanhtuan_alarmclockapp.utils.Converters



@Database(entities = [Alarm::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AlarmDatabase : RoomDatabase() {

    abstract val alarmDao: AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getInstance(context: Context): AlarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

//        fun getDatabase(context: Context): AlarmDatabase {
//            return INSTANCE ?: synchronized(this) {
//                Room.databaseBuilder(
//                    context.applicationContext,
//                    AlarmDatabase::class.java,
//                    "alarm_database"
//                )
//                    .fallbackToDestructiveMigration() // Thêm dòng này để xóa dữ liệu cũ khi thay đổi schema
//                    .addCallback(AlarmDatabaseCallback()) // Giữ lại callback nếu cần
//                    .build().also {
//                        INSTANCE = it
//                    }
//            }
//        }

//        private class AlarmDatabaseCallback : Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                INSTANCE?.let { database ->
//                    CoroutineScope(Dispatchers.IO).launch {
//                        // Xử lý công việc nếu cần sau khi tạo cơ sở dữ liệu
//                    }
//                }
//            }
//        }
    }




}