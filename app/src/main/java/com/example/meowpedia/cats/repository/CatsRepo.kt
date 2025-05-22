package com.example.meowpedia.cats.repository

import com.example.meowpedia.cats.api.CatsApi
import javax.inject.Inject

class CatsRepo @Inject constructor(private val api: CatsApi) {
    suspend fun fetchAllBreeds() = api.getAllBreeds()
    suspend fun fetchBreedByID(id: String) = api.getBreedByID(id)
    suspend fun fetchCatImage(id: String) = api.getCatImage(id)
}
