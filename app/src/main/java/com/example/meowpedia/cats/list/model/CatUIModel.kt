package com.example.meowpedia.cats.list.model

data class CatUIModel(
    val id: String,
    val name: String,
    val alternativeNames: String,
    val description: String,
    val temperaments: String,
    val image: String? = null
)
