package com.userfaltakas.marvelheroes.repository

import com.userfaltakas.marvelheroes.api.RetrofitInstance
import com.userfaltakas.marvelheroes.constant.Constants
import com.userfaltakas.marvelheroes.data.api.HeroesResponse
import com.userfaltakas.marvelheroes.data.api.Result
import com.userfaltakas.marvelheroes.database.HeroDatabase
import retrofit2.Response

class HeroesRepository(
    private val db: HeroDatabase
) {
    suspend fun getHeroes(offset: Int): Response<HeroesResponse> {
        val filter = mutableMapOf<String, String>()
        filter["ts"] = Constants.ts
        filter["apikey"] = Constants.API_KEY
        filter["hash"] = Constants.hash()
        filter["offset"] = offset.toString()
        return RetrofitInstance.api.getHeroes(filter)
    }

    suspend fun addHero(result: Result) = db.getHeroDao().insert(result)
    suspend fun getSquad() = db.getHeroDao().getHeroes()
    suspend fun removeHero(result: Result) = db.getHeroDao().delete(result)
}