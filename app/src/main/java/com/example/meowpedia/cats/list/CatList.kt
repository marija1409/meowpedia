package com.example.meowpedia.cats.list

import com.example.meowpedia.cats.list.model.CatUIModel


interface CatList {
    data class CatListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val breeds: List<CatUIModel> = emptyList(),
        val filteredBreeds: List<CatUIModel> = emptyList(),
        val error: ListError? = null

    )

    sealed class CatListUIEvent {
        data class SearchQueryChanged(val query: String) : CatListUIEvent()
        data object ClearSearch : CatListUIEvent()
    }

    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}