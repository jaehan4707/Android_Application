package com.example.calculator.model
import androidx.room.ColumnInfo
import androidx.room.Entity // room 사용
import androidx.room.PrimaryKey

// 데이터 클래스로 선언
@Entity
data class History(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name="expression") val expression: String?,
    @ColumnInfo(name= "result") val result: String?
)