package com.userfaltakas.marvelheroes.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userfaltakas.marvelheroes.api.Resource
import com.userfaltakas.marvelheroes.data.api.HeroesResponse
import com.userfaltakas.marvelheroes.data.api.Result
import com.userfaltakas.marvelheroes.repository.HeroesRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class StartViewModel(
    private val heroesRepository: HeroesRepository
) : ViewModel() {
    val heroes: MutableLiveData<Resource<HeroesResponse>> = MutableLiveData()
    val squad: MutableLiveData<List<Result>> = MutableLiveData()
    private var offset = 0

    fun getHeroes() = viewModelScope.launch {
        heroes.postValue(Resource.Loading())
        val response = heroesRepository.getHeroes(offset)
        heroes.postValue(handleHeroesResponse(response))
    }

    private fun handleHeroesResponse(response: Response<HeroesResponse>): Resource<HeroesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addHeroToSquad(result: Result) = viewModelScope.launch {
        heroesRepository.addHero(result)
    }

    fun removeHeroFromSquad(result: Result) = viewModelScope.launch {
        heroesRepository.removeHero(result)
    }

    fun getSquad() = viewModelScope.launch {
        val response = heroesRepository.getSquad()
        squad.postValue(response)
    }
}