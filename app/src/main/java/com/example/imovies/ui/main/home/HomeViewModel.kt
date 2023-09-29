package com.example.imovies.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.data.repository.favorite.FavoriteRepository
import com.example.imovies.data.repository.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private var _isForceUpdate = true

    private val _movie by lazy { MutableLiveData<Resource<Movie>>() }
    val movie: LiveData<Resource<Movie>>
        get() = _movie

    private val _movies by lazy { MutableLiveData<Resource<List<Movie>>>() }
    val movies: LiveData<Resource<List<Movie>>>
        get() = _movies

    private val _favorites by lazy { MutableLiveData<Resource<List<Movie>>>() }
    val favorites: LiveData<Resource<List<Movie>>>
        get() = _favorites

    fun setIsForceUpdate(value: Boolean) {
        _isForceUpdate = value
    }

    fun isForceUpdate() = _isForceUpdate

    fun doGetMovie(id: Int) {
        _movie.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _movie.postValue(movieRepository.doGetMovie(id))
        }
    }

    fun doGetMovies(term: String = "", isForceUpdate: Boolean) {
        _movies.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _movies.postValue(movieRepository.doGetMovies(term, isForceUpdate))
        }
    }

    fun doUpdateFavorite(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.doUpdateFavorite(movie)
        }
    }

    fun doGetFavorites() {
        _movies.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _favorites.postValue(favoriteRepository.doGetFavorites())
        }
    }

}