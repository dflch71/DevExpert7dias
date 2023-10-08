package com.dflch.desafioarquitecturas.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dflch.desafioarquitecturas.data.Movie
import com.dflch.desafioarquitecturas.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.requestMovies()

            repository.movies.collect {
                _state.value = UiState(movies = it)
            }
        }

    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(favorite = !movie.favorite))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}