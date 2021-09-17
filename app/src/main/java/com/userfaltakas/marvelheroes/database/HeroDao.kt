package com.userfaltakas.marvelheroes.database

import androidx.room.*
import com.userfaltakas.marvelheroes.data.api.Result

@Dao
interface HeroDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(result: Result): Long

    @Query("SELECT * FROM  squad ORDER BY name")
    suspend fun getHeroes(): List<Result>

    @Delete
    suspend fun delete(result: Result)
}