package com.example.meowpedia.cats.details

import com.example.meowpedia.cats.details.model.DetailsUiModel

interface Details {
    data class DetailsUiState(
        val breedId: String,
        val data: DetailsUiModel? = null,
        val image: String? = null,
        val fetching: Boolean = false,
        val error: DetailsError? = null,
    )
    {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
        }
    }
}