package com.userfaltakas.marvelheroes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.userfaltakas.marvelheroes.api.Resource
import com.userfaltakas.marvelheroes.data.ui.HeroPreviewDestination
import com.userfaltakas.marvelheroes.databinding.FragmentAllHeroesBinding
import com.userfaltakas.marvelheroes.network.NetworkManager
import com.userfaltakas.marvelheroes.ui.activity.StartActivity
import com.userfaltakas.marvelheroes.ui.activity.StartViewModel
import com.userfaltakas.marvelheroes.ui.adapter.HeroesAdapter
import com.userfaltakas.marvelheroes.ui.adapter.SquadAdapter

class AllHeroesFragment : Fragment() {
    private var _binding: FragmentAllHeroesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StartViewModel
    private lateinit var allHeroesAdapter: HeroesAdapter
    private lateinit var squadAdapter: SquadAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as StartActivity).viewModel

        if (viewModel.heroes.value == null) {
            if (NetworkManager().checkNetworkAvailability(requireContext())) {
                viewModel.getHeroes()
            } else {
                hideLostConnectionImage()
            }
        }

        setContext()
        setUpAllHeroesRecyclerView()
        setUpSquadRecyclerView()
    }

    private fun setUpSquadRecyclerView() {
        squadAdapter = SquadAdapter()
        binding.squadRecyclerView.apply {
            adapter = squadAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        squadAdapter.setOnItemClickListener {
            val action =
                AllHeroesFragmentDirections.actionAllHeroesFragmentToHeroPreviewFragment(
                    it,
                    HeroPreviewDestination.FROM_SQUAD
                )
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    private fun setUpAllHeroesRecyclerView() {
        allHeroesAdapter = HeroesAdapter()
        binding.allHeroesRecyclerView.apply {
            adapter = allHeroesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        allHeroesAdapter.setOnItemClickListener {
            val action =
                AllHeroesFragmentDirections.actionAllHeroesFragmentToHeroPreviewFragment(
                    it,
                    HeroPreviewDestination.FROM_WEB
                )
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    private fun setContext() {
        viewModel.heroes.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.data?.let {
                        allHeroesAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(tag, "ERROR! $message")
                    }
                }
            }
        })

        viewModel.getSquad()
        viewModel.squad.observe(viewLifecycleOwner, { response ->
            if (response.isNotEmpty()) {
                showMySquad()
                response?.let {
                    squadAdapter.differ.submitList(it)
                }
            } else {
                hideMySquad()
            }
        })
    }

    private fun showMySquad() {
        binding.squadTitle.visibility = View.VISIBLE
        binding.squadRecyclerView.visibility = View.VISIBLE
    }

    private fun hideMySquad() {
        binding.squadTitle.visibility = View.GONE
        binding.squadRecyclerView.visibility = View.GONE
    }

    private fun hideLostConnectionImage() {
        binding.lostConnectionImg.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}