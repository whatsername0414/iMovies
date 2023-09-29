package com.example.imovies.ui.main.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.imovies.R
import com.example.imovies.common.Constant
import com.example.imovies.common.Constant.LAST_DATE_VISIT
import com.example.imovies.common.Constant.LAST_PAGE_VISIT
import com.example.imovies.common.Extensions.formatDate
import com.example.imovies.common.Extensions.hideSoftKeyboard
import com.example.imovies.common.Extensions.textDebounce
import com.example.imovies.common.dialog.CommonDialog
import com.example.imovies.common.setSingleClickListener
import com.example.imovies.data.repository.common.local.preferences.AppPreferences
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.databinding.FragmentHomeBinding
import com.example.imovies.ui.main.MainViewModel
import com.example.imovies.ui.main.home.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val movieAdapter by lazy { MovieAdapter() }
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        homeViewModel.doGetMovies("star", homeViewModel.isForceUpdate())
        homeViewModel.setIsForceUpdate(false)

        movieAdapter.apply {
            onFavoriteClicked = { movie ->
                homeViewModel.doUpdateFavorite(movie)
            }
            onMovieClicked = { movie ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movie.id))
            }
        }




        binding.apply {
            val lastDateVisit = appPreferences.getString(LAST_DATE_VISIT).orEmpty()
            if (mainViewModel.getIsNewOpen()) {
                navigate(appPreferences.getString(LAST_PAGE_VISIT).orEmpty())
                appPreferences.saveString(LAST_DATE_VISIT, Date().toString())
                mainViewModel.setIsNewOpen(false)
            } else {
                appPreferences.saveString(LAST_PAGE_VISIT, "")
            }
            binding.lastVisitTextView.text = if (lastDateVisit.isEmpty()) "No Data"
            else lastDateVisit.formatDate(
                    Constant.EEE_MMM_DD_HH_MM_SS_ZZZZ_YYY,
                    Constant.MMM_DD_YYY_HH_MM
                )
            movieRv.adapter = movieAdapter
            swipeRefreshLayout.setOnRefreshListener {
                searchEditText.setText("")
                requireActivity().hideSoftKeyboard()
                searchEditText.clearFocus()
                homeViewModel.doGetMovies("star", true)
            }
            searchEditText.apply {
                textDebounce(500, lifecycleScope) {
                    val term = it.toString()
                    homeViewModel.doGetMovies(term, false)
                }
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        p0: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            requireActivity().hideSoftKeyboard()
                            clearFocus()
                            return true
                        }
                        return false
                    }
                })
            }

            favoriteButton.setSingleClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoritesFragment())
            }
        }

    }

    private fun observers() {
        homeViewModel.movies.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Loading -> {
                    binding.apply {
                        swipeRefreshLayout.isRefreshing = true
                    }
                }
                is Resource.Success -> {
                    movieAdapter.submitList(res.data)
                    binding.apply {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
                is Resource.Error -> {
                    binding.apply {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    CommonDialog.with(this)
                        .setTitle(getString(R.string.common_error_title))
                        .setMessage(getString(R.string.common_error_message))
                        .setDialogDismissListener { isDismissedByButton ->
                            if (isDismissedByButton) {
                                homeViewModel.doGetMovies("star", true)
                            }
                        }
                        .show()
                }
            }
        }
    }

    private fun navigate(value: String) {
        val destination = value.split(":").firstOrNull()
        val args = value.split(":").lastOrNull()
        if (destination == R.id.favoritesFragment.toString()) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoritesFragment())
        } else if (destination == R.id.movieDetailsFragment.toString()) {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(args?.toInt() ?: -1))
        }
    }

}