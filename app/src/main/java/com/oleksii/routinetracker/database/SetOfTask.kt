package com.oleksii.routinetracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists_table")
data class SetOfTask(
    @PrimaryKey(autoGenerate = true)
    var listId: Long = 1,

    @ColumnInfo(name = "list_name")
    var title: String = "My Tasks",

    @ColumnInfo(name = "last_opened")
    var opened: Boolean = true
)