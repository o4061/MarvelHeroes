package com.userfaltakas.marvelheroes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.userfaltakas.marvelheroes.api.Resource
import com.userfaltakas.marvelheroes.constant.Constants.PAGE_OFFSET
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
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private var networkManager = NetworkManager()

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
            makeHeroRequest()
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
            addOnScrollListener(this@AllHeroesFragment.scrollListener)
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
                        allHeroesAdapter.differ.submitList(it.results?.toList())
                        isLastPage = viewModel.isLastPage()
                        if (isLastPage) {
                            removeRecyclerViewPadding()
                        }
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(tag, "ERROR! $message")
                    }
                    hideProgressBar()
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            var shouldPaginate: Boolean
            layoutManager.apply {
                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                val isAtLastItem =
                    findFirstVisibleItemPosition() + childCount >= itemCount
                val isNotAtBeginning = findFirstVisibleItemPosition() >= 0
                val isTotalMoreThanVisible = itemCount >= PAGE_OFFSET
                shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                        && isTotalMoreThanVisible && isScrolling
            }

            if (shouldPaginate) {
                makeHeroRequest()
                isScrolling = false
            }
        }
    }

    private fun showMySquad() {
        binding.squadTitle.visibility = View.VISIBLE
        binding.squadRecyclerView.visibility = View.VISIBLE
    }

    private fun hideMySquad() {
        binding.squadTitle.visibility = View.GONE
        binding.squadRecyclerView.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun removeRecyclerViewPadding() {
        binding.allHeroesRecyclerView.setPadding(0, 0, 0, 0)
    }

    private fun makeHeroRequest() {
        if (networkManager.checkNetworkAvailability(requireContext())) {
            viewModel.getHeroes()
        }
    }
}