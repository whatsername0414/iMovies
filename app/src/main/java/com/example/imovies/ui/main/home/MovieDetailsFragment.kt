package com.example.imovies.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.imovies.R
import com.example.imovies.common.Constant.LAST_PAGE_VISIT
import com.example.imovies.common.Extensions.convertToCurrency
import com.example.imovies.common.Extensions.loadImage
import com.example.imovies.common.dialog.CommonLoadingDialog
import com.example.imovies.common.setSingleClickListener
import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.common.local.preferences.AppPreferences
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding get() = _binding!!

    private val loadingDialog by lazy { CommonLoadingDialog(requireContext()) }

    private val homeViewModel by viewModels<HomeViewModel>()
    private val args by navArgs<MovieDetailsFragmentArgs>()

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        homeViewModel.doGetMovie(args.id)
        val prefValue = "${R.id.movieDetailsFragment}:${args.id}"
        appPreferences.saveString(LAST_PAGE_VISIT, prefValue)

        binding.apply {
            appBarLayout.toolbar.apply {
                val appBarConfiguration = AppBarConfiguration(findNavController().graph)
                this.setupWithNavController(findNavController(), appBarConfiguration)
            }
            retryButton.setSingleClickListener {
                homeViewModel.doGetMovie(args.id)
            }
        }
    }

    private fun observers() {
        homeViewModel.movie.observe(viewLifecycleOwner) { res ->
            when(res) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.apply {
                        errorLayout.visibility = View.GONE
                    }
                    bindData(res.data)
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    binding.apply {
                        errorLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun bindData(movie: Movie) {
        binding.apply {
            favoriteButton.visibility = View.VISIBLE
            favoriteButton.isChecked = movie.isFavorite
            artworkImageView.loadImage(movie.artwork)
            titleTextView.text = movie.title
            releaseAndGenreTextView.text = requireContext().getString(
                R.string.movie_adapter_release_year_and_genre, movie.releaseYear, movie.genre)
            priceTextView.text = movie.currency.convertToCurrency(movie.price)
            descriptionTextView.text = movie.description

            favoriteButton.setOnClickListener {
                homeViewModel.doUpdateFavorite(movie)
            }
        }
    }

}