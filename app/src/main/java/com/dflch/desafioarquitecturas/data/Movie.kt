package com.dflch.desafioarquitecturas.data

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val favorite:Boolean
)