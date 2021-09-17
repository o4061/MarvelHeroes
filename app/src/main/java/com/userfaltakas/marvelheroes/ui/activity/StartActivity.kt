package com.userfaltakas.marvelheroes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.userfaltakas.marvelheroes.database.HeroDatabase
import com.userfaltakas.marvelheroes.databinding.ActivityStartBinding
import com.userfaltakas.marvelheroes.repository.HeroesRepository

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViewModel() {
        val heroesRepository = HeroesRepository(HeroDatabase(this))
        val viewModelProviderFactory = StartViewModelProviderFactory(heroesRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(StartViewModel::class.java)
    }
}