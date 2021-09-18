package com.userfaltakas.marvelheroes.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userfaltakas.marvelheroes.api.Resource
import com.userfaltakas.marvelheroes.constant.Constants.PAGE_OFFSET
import com.userfaltakas.marvelheroes.data.api.HeroesResponse
import com.userfaltakas.marvelheroes.data.api.Result
import com.userfaltakas.marvelheroes.repository.HeroesRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class StartViewModel(
    private val heroesRepository: HeroesRepository
) : ViewModel() {
    val heroes: MutableLiveData<Resource<HeroesResponse>> = MutableLiveData()
    private var heroesResponse: HeroesResponse? = null
    private var offset = 0
    private var total = 0
    val squad: MutableLiveData<List<Result>> = MutableLiveData()

    fun getHeroes() = viewModelScope.launch {
        heroes.postValue(Resource.Loading())
        val response = heroesRepository.getHeroes(offset)
        heroes.postValue(handleHeroesResponse(response))
    }

    private fun handleHeroesResponse(response: Response<HeroesResponse>): Resource<HeroesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                offset += PAGE_OFFSET
                if (heroesResponse == null) {
                    heroesResponse = it
                    total = it.data?.total!!
                } else {
                    val oldHeroes = heroesResponse!!.data?.results
                    val newHeroes = it.data?.results
                    if (newHeroes != null) {
                        oldHeroes?.addAll(newHeroes)
                    }
                }
                return Resource.Success(heroesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun isLastPage(): Boolean {
        return offset >= total
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