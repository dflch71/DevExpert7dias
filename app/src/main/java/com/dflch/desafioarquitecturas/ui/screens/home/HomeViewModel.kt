package com.dflch.desafioarquitecturas.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dflch.desafioarquitecturas.data.Movie
import com.dflch.desafioarquitecturas.data.local.MoviesDao
import com.dflch.desafioarquitecturas.data.local.toLocalMovie
import com.dflch.desafioarquitecturas.data.local.toMovie
import com.dflch.desafioarquitecturas.data.remote.MoviesService
import com.dflch.desafioarquitecturas.data.remote.ServerMovie
import com.dflch.desafioarquitecturas.data.remote.toLocalMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(val dao: MoviesDao): ViewModel() {

    //1. var state = UIState()
    //2. var state by mutableStateOf(UIState())
         //private set

    //3. LiveData
    //private val _state = MutableLiveData(UIState())
    //val state: LiveData<UIState> = _state

    //4. StateFlow
    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state

    init {

        viewModelScope.launch {
            //state = UIState( .. For 1. y 2.
            //_state.value = UIState( 3

            val isDbEmpty = dao.count() == 0
            if (isDbEmpty) {
                _state.value = UIState(loading = true)
                dao.insertAll(
                     Retrofit.Builder()
                         .baseUrl("https://api.themoviedb.org/3/")
                         .addConverterFactory(GsonConverterFactory.create())
                         .build()
                         .create(MoviesService::class.java)
                         .getMovies()
                         .results
                         .map { it.toLocalMovie() }
                )
            }

            dao.getMovies().collect{ movies ->
                _state.value = UIState(
                    loading = false,
                    movies = movies.map { it.toMovie() }
                )
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        //val movies = _state.value.movies.toMutableList()
        //movies.replaceAll{ if (it.id == movie.id) movie.copy(favorite = !movie.favorite) else it }

        //_state.value = _state.value.copy(movies = movies)

        viewModelScope.launch {
            dao.updateMovie(movie.copy(favorite = !movie.favorite).toLocalMovie())
        }

    }

    data class UIState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}