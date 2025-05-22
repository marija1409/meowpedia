package com.example.meowpedia.cats.repository

import com.example.meowpedia.cats.api.CatsApi
import com.example.meowpedia.cats.api.model.CatImage
import com.example.meowpedia.cats.api.model.CatsApiModel
import com.example.meowpedia.network.retrofit

object CatsRepo {
    private val catsApi: CatsApi = retrofit.create(CatsApi::class.java)

    suspend fun fetchAllBreeds(): List<CatsApiModel>{
        return catsApi.getAllBreeds()
    }

    suspend fun fetchBreedByID(id: String): CatsApiModel{
        return catsApi.getBreedByID(id)
    }

    suspend fun fetchCatImage(id: String): CatImage{
        return catsApi.getCatImage(id)
    }


}