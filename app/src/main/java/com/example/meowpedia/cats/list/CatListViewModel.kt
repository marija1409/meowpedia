package com.example.meowpedia.cats.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meowpedia.cats.repository.CatsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.meowpedia.cats.list.CatList.CatListState
import com.example.meowpedia.cats.list.model.CatUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatListViewModel(private val repo: CatsRepo = CatsRepo): ViewModel() {

    private val _state = MutableStateFlow(CatListState())

    val state = _state.asStateFlow()

    private fun setState(reducer: CatListState.() -> CatListState) = _state.update(reducer)

    private val events = MutableSharedFlow<CatList.CatListUIEvent>()
    fun setEvent(event: CatList.CatListUIEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllBreeds()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is CatList.CatListUIEvent.ClearSearch -> setState { copy(query = "") }
                    is CatList.CatListUIEvent.SearchQueryChanged -> {
                        filterBreedList(query = it.query)
                    }

                }
            }
        }
    }


    private fun filterBreedList(query: String) {
        val filteredBreeds = _state.value.breeds.filter { breed ->
            breed.name.startsWith(query, ignoreCase = true)
        }
        setState { copy(filteredBreeds = filteredBreeds, query = query) }

    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repo.fetchAllBreeds().map { breed ->
                        CatUIModel(
                            id = breed.id,
                            name = breed.name,
                            alternativeNames = breed.alt_names,
                            description = breed.description,
                            temperaments = breed.temperament,
                            image = breed.image?.url
                        )
                    }
                }



                setState { copy(breeds = breeds) }
            } catch (error: Exception) {
                setState { copy(error = CatList.ListError.ListUpdateFailed(error))}
                Log.e("BreedListViewModel", "Error", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}

