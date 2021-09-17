package com.userfaltakas.marvelheroes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.userfaltakas.marvelheroes.api.Resource

@Dao
interface HeroDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(result: Resource): Long

    @Query("SELECT * FROM  squad")
    fun getHeroes(): LiveData<List<Result>>

    @Delete
    suspend fun delete(result: Result)
}