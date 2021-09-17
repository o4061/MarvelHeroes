package com.userfaltakas.marvelheroes.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.userfaltakas.marvelheroes.repository.HeroesRepository

class StartViewModelProviderFactory(
    val heroesRepository: HeroesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartViewModel(heroesRepository) as T
    }
}