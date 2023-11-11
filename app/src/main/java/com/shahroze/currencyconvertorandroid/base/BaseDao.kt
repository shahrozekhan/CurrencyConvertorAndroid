package com.shahroze.currencyconvertorandroid.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ts: List<T>): List<Long>

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)

    @Delete
    fun deleteAll(ts: List<T>)
}