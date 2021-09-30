package com.userfaltakas.marvelheroes.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.userfaltakas.marvelheroes.R
import com.userfaltakas.marvelheroes.data.api.Result
import com.userfaltakas.marvelheroes.data.ui.HeroPreviewDestination
import com.userfaltakas.marvelheroes.databinding.FragmentHeroPreviewBinding
import com.userfaltakas.marvelheroes.ui.activity.StartActivity
import com.userfaltakas.marvelheroes.ui.activity.StartViewModel

class HeroPreviewFragment : Fragment() {
    private var _binding: FragmentHeroPreviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StartViewModel
    private lateinit var hero: Result
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
    }

    private fun hireHero() {
        Glide.with(requireContext()).load(hero.thumbnail?.getURL()).into(binding.image)
        binding.hireBtn.apply {
            text = getString(R.string.hire)
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_500
                )
            )
            setOnClickListener {
                viewModel.addHeroToSquad(args.hero)
                Toast.makeText(
                    requireContext(), resources.getString(
                        R.string.hero_hired
                    ), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fireHeroAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.are_you_sure))
            .setMessage(resources.getString(R.string.do_you_want_to_fire_hero))
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.removeHeroFromSquad(args.hero)
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.hero_fired),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            }.create()
    }

    private fun fireHero() {
        Glide.with(requireContext()).load(hero.thumbnail?.path).into(binding.image)
        binding.hireBtn.apply {
            text = getString(R.string.fire)
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
            setOnClickListener {
                fireHeroAlertDialog().show()
            }
        }
    }

    private fun setContext() {
        hero = args.hero

        when (args.destination) {
            HeroPreviewDestination.FROM_SQUAD -> {
                fireHero()
            }
            HeroPreviewDestination.FROM_WEB -> {
                hireHero()
            }
        }

        binding.apply {
            heroName.text = hero.name
            description.text = hero.description
        }
    }
}