package com.dflch.desafioarquitecturas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.dflch.desafioarquitecturas.data.MoviesRepository
import com.dflch.desafioarquitecturas.data.local.LocalDataSource
import com.dflch.desafioarquitecturas.data.local.MoviesDatabase
import com.dflch.desafioarquitecturas.data.remote.RemoteDataSource
import com.dflch.desafioarquitecturas.ui.screens.home.Home


class MainActivity : ComponentActivity() {

    //Para XML
    //val viewModel: MainViewModel by viewModels()

    private lateinit var db: MoviesDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java, "movies-db"
        ).build()

        val repository = MoviesRepository(
            localDataSource = LocalDataSource(db.moviesDao()),
            remoteDataSource = RemoteDataSource()
        )

        setContent {
            Home(repository)
        }
    }
}

