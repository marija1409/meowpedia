package com.example.meowpedia.cats.details.model


data class DetailsUiModel(
    val id: String,
    val name: String,
    val image: String? = null,
    val description: String,
    val origin: String,
    val temperament: String,
    val life_span: String,
    val weight: Weight,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val rare: Int,
    val wikipedia_url: String?,

)


data class Weight(
    val imperial: String,
    val metric: String
)