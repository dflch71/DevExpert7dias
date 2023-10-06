package com.dflch.desafioarquitecturas.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dflch.desafioarquitecturas.data.Movie
import com.dflch.desafioarquitecturas.data.local.MoviesDao
import com.dflch.desafioarquitecturas.data.remote.ServerMovie
import com.dflch.desafioarquitecturas.ui.theme.DesafioArquitecturasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(moviesDao: MoviesDao) {
    DesafioArquitecturasTheme {

        //Código se traslada -> MainViewModel
        //val movies = produceState<List<ServerMovie>>(initialValue = emptyList()){
        //    value = Retrofit.Builder()
        //        .baseUrl("https://api.themoviedb.org/3/")
        //        .addConverterFactory(GsonConverterFactory.create())
        //        .build()
        //        .create(MoviesService::class.java)
        //        .getMovies()
        //        .results
        //}

        //Poder ver el viewModel en compose
        val viewModel: HomeViewModel = viewModel{ HomeViewModel(moviesDao) }

        //LiveData
        //val state by viewModel.state.observeAsState(MainViewModel.UIState())

        //StateFlow
        val state by viewModel.state.collectAsState()

        //A surface container using the 'background' color from the theme

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Scaffold(
                topBar = { TopAppBar(title = { Text(text= "Movies") }) }
            ) { padding ->

                if (state.loading){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                if (state.movies.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        modifier = Modifier.padding(padding),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(state.movies) { movie ->
                            //items(viewModel.state.movies){movie ->
                            //items(movies.value){movie ->
                            MovieItem(
                                movie = movie,
                                onClick = { viewModel.onMovieClick(movie) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit ) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box {

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w185/${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 3f)
            )

            if (movie.favorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = Color.White
                )
            }
        }

        Text(
            text = movie.title,
            color = Color.Blue,
            modifier = Modifier
                .padding(8.dp)
                .height(42.dp),
            maxLines = 2,
            fontSize = 12.sp
        )
    }
}