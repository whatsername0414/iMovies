package com.example.imovies.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.imovies.R
import com.example.imovies.common.Constant.LAST_PAGE_VISIT
import com.example.imovies.common.dialog.CommonDialog
import com.example.imovies.common.setSingleClickListener
import com.example.imovies.data.repository.common.local.preferences.AppPreferences
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.databinding.FragmentFavoritesBinding
import com.example.imovies.ui.main.home.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding get() = _binding!!

    private val favoriteAdapter by lazy { MovieAdapter() }
    private val homeViewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        homeViewModel.doGetFavorites()
        appPreferences.saveString(LAST_PAGE_VISIT, R.id.favoritesFragment.toString())

        favoriteAdapter.apply {
            onFavoriteClicked = { movie ->
                homeViewModel.doUpdateFavorite(movie)
            }
            onMovieClicked = { movie ->
                findNavController().navigate(FavoritesFragmentDirections
                    .actionFavoritesFragmentToMovieDetailsFragment(movie.id))
            }
        }

        binding.apply {
            favoriteRv.adapter = favoriteAdapter
            appBarLayout.toolbar.apply {
                val appBarConfiguration = AppBarConfiguration(findNavController().graph)
                this.setupWithNavController(findNavController(), appBarConfiguration)
            }
            addButton.setSingleClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun observers() {
        homeViewModel.favorites.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.emptyLayout.isVisible = res.data.isEmpty()
                    favoriteAdapter.submitList(res.data)
                }
                is Resource.Error -> {
                    CommonDialog.with(this).show()
                }
            }
        }
    }
}