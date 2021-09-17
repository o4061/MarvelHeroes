package com.userfaltakas.marvelheroes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.userfaltakas.marvelheroes.api.Resource
import com.userfaltakas.marvelheroes.databinding.FragmentAllHeroesBinding
import com.userfaltakas.marvelheroes.ui.activity.StartActivity
import com.userfaltakas.marvelheroes.ui.activity.StartViewModel
import com.userfaltakas.marvelheroes.ui.adapter.HeroesAdapter
import com.userfaltakas.marvelheroes.ui.adapter.SquadAdapter

class AllHeroesFragment : Fragment() {
    private var _binding: FragmentAllHeroesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StartViewModel
    lateinit var allHeroesAdapter: HeroesAdapter
    lateinit var squadAdapter: SquadAdapter
    val TAG = "AllHeroesFragment"

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

        setContext()
        hideMySquad()
        setUpAllHeroesRecyclerView()
        setUpSquadRecyclerView()
    }

    private fun setUpSquadRecyclerView() {
        squadAdapter = SquadAdapter()
        binding.squadRecyclerView.apply {
            adapter = squadAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
                AllHeroesFragmentDirections.actionAllHeroesFragmentToHeroPreviewFragment(it)
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
                        Log.e(TAG, "ERROR! $message")
                    }
                }
            }
        })
    }

    private fun hideMySquad() {
        binding.squadTitle.visibility = View.GONE
        binding.squadRecyclerView.visibility = View.GONE
    }

    private fun showMySquad() {
        binding.squadTitle.visibility = View.VISIBLE
        binding.squadRecyclerView.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}