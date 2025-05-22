package com.example.meowpedia.cats.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meowpedia.cats.api.model.CatImage
import com.example.meowpedia.cats.api.model.CatWeight
import com.example.meowpedia.cats.api.model.CatsApiModel
import com.example.meowpedia.cats.details.model.DetailsUiModel
import com.example.meowpedia.cats.list.CatList
import com.example.meowpedia.cats.list.CatList.CatListState
import com.example.meowpedia.cats.repository.CatsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.meowpedia.cats.details.Details
import com.example.meowpedia.cats.details.Details.DetailsUiState


class DetailsViewModel(
    private val repo: CatsRepo = CatsRepo,
    breedId: String
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState(breedId))

    val state = _state.asStateFlow()

    private fun setState(reducer: DetailsUiState.() -> DetailsUiState) = _state.update(reducer)

    init {
        fetchCatDetails(breedId)
    }

    fun fetchCatDetails(breedId: String) {
        viewModelScope.launch {
            setState { copy(fetching = true, error = null) }

            try {
                val detail = withContext(Dispatchers.IO) {
                    repo.fetchBreedByID(breedId)
                }

                val imageUrl = detail.image?.url ?: detail.reference_image_id?.let { imageId ->
                    try {
                        val image = withContext(Dispatchers.IO) {
                            repo.fetchCatImage(imageId)
                        }
                        image.url
                    } catch (e: Exception) {
                        Log.w("DetailsViewModel", "Image fetch failed", e)
                        null
                    }
                }

                val uiModel = DetailsUiModel(
                    id = detail.id,
                    name = detail.name,
                    image = imageUrl,
                    description = detail.description,
                    origin = detail.origin,
                    temperament = detail.temperament,
                    life_span = detail.life_span,
                    weight = CatWeight(
                        imperial = detail.weight.imperial,
                        metric = detail.weight.metric
                    ),
                    adaptability = detail.adaptability,
                    affection_level = detail.affection_level,
                    child_friendly = detail.child_friendly,
                    dog_friendly = detail.dog_friendly,
                    energy_level = detail.energy_level,
                    rare = detail.rare,
                    wikipedia_url = detail.wikipedia_url
                )


                setState {
                    copy(
                        data = uiModel,
                        image = imageUrl,
                        error = null
                    )
                }

            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Failed to fetch details", e)
                setState {
                    copy(error = Details.DetailsUiState.DetailsError.DataUpdateFailed(e))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

}
