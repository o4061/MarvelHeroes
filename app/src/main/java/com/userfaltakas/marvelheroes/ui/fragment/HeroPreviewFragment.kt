package com.userfaltakas.marvelheroes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.userfaltakas.marvelheroes.databinding.FragmentHeroPreviewBinding
import com.userfaltakas.marvelheroes.ui.activity.StartActivity
import com.userfaltakas.marvelheroes.ui.activity.StartViewModel

class HeroPreviewFragment : Fragment() {
    private var _binding: FragmentHeroPreviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StartViewModel
    private val args: HeroPreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as StartActivity).viewModel
        setContext()

        binding.closeBtn.setOnClickListener {
            val action =
                HeroPreviewFragmentDirections.actionHeroPreviewFragmentToAllHeroesFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        binding.hireBtn.setOnClickListener { }
    }

    private fun setContext() {
        val hero = args.hero

        binding.apply {
            heroName.text = hero.name
            description.text = hero.description
            Glide.with(requireContext()).load(hero.thumbnail?.getURL()).into(binding.image)
        }
    }
}