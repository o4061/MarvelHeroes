package com.userfaltakas.marvelheroes.api

import com.userfaltakas.marvelheroes.data.api.HeroesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface HeroesAPI {
    @GET("characters")
    suspend fun getHeroes(
        @QueryMap filter: Map<String, String>
    ): Response<HeroesResponse>
}