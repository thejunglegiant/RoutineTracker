package com.oleksii.routinetracker.database

import androidx.room.*
import java.time.LocalDate

@Entity(tableName = "tasks_table")
@TypeConverters(Converters::class)
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long,

    @ColumnInfo(name = "task_title")
    var title: String,

    @ColumnInfo(name = "task_details")
    var details: String,

    @ColumnInfo(name = "task_date")
    var date: LocalDate = LocalDate.MIN,

    @ColumnInfo(name = "task_stage")
    var stage: Int
)