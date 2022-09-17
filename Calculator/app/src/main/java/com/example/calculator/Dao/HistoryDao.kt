package com.example.calculator.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.calculator.model.History

// room에 연결된 Dao
// entitiy 조회등 어떻게 할건지
@Dao
interface HistoryDao {
    @Query("SELECT *FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()
//
//    @Delete
//    fun delete(history: History)
//
//    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
//    fun findByResult(result :String) :List<History>

}