package com.example.meowpedia.cats.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meowpedia.cats.api.model.CatsApiModel
import com.example.meowpedia.cats.details.model.DetailsUiModel
import com.example.meowpedia.cats.repository.CatsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.meowpedia.cats.details.Details.DetailsUiState
import com.example.meowpedia.cats.details.model.Weight
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: CatsRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val breedId = savedStateHandle.get<String>("Id") ?: ""

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

                val uiModel = detail.toDetailsUiModel(imageUrl)

                setState {
                    copy(
                        data = uiModel,
                        image = imageUrl,
                        error = null
                    )
                }

            } catch (error: Exception) {
                Log.e("DetailsViewModel", "Failed to fetch details", error)
                setState {
                    copy(error = DetailsUiState.DetailsError.DataUpdateFailed(error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

}

private fun CatsApiModel.toDetailsUiModel(imageUrl: String?): DetailsUiModel {
    return DetailsUiModel(
        id = id,
        name = name,
        image = imageUrl,
        description = description,
        origin = origin,
        temperament = temperament,
        life_span = life_span,
        weight = Weight(
            imperial = weight.imperial,
            metric = weight.metric
        ),
        adaptability = adaptability,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        energy_level = energy_level,
        rare = rare,
        wikipedia_url = wikipedia_url
    )
}

