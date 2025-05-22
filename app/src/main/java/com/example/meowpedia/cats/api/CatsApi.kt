package com.example.meowpedia.cats.api

import com.example.meowpedia.cats.api.model.CatImage
import com.example.meowpedia.cats.api.model.CatsApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface CatsApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<CatsApiModel>

    @GET("breeds/{id}")
    suspend fun getBreedByID(@Path("id") breedId: String): CatsApiModel

    @GET("images/{id}")
    suspend fun getCatImage(@Path("id") imageId: String): CatImage


}