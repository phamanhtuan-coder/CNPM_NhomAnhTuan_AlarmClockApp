package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.room.*

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarm(id: Int): Alarm?

    @Query("SELECT * FROM alarms")
    suspend fun getAllAlarms(): List<Alarm>
}