package com.example.cnpm_nhomanhtuan_alarmclockapp


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE id = :id")
    fun getAlarmById(id: Int): Flow<Alarm>

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE label LIKE '%' || :query || '%'")
    fun searchAlarms(query: String): Flow<List<Alarm>>



}