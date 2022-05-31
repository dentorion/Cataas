package com.entin.data.api

import com.entin.data.model.CatJson
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCat {

    @GET("cat?json=true")
    suspend fun getRandomCat(): CatJson

    @GET("cat/says/{says}")
    suspend fun getFullSearchCat(
        @Path("says") says: String,
        @Query("tag") tag: String,
        @Query("filter") filter: String,
        @Query("color") colors: String,
        @Query("size") size: String,
        @Query("json") isJsonResponse: Boolean = true,
    ): CatJson

    @GET("cat/{tag}")
    suspend fun getShortSearchCat(
        @Path("tag") tag: String,
        @Query("filter") filter: String,
        @Query("json") isJsonResponse: Boolean = true,
    ): CatJson

    @GET("api/tags")
    suspend fun getAllTags(): List<String>
}