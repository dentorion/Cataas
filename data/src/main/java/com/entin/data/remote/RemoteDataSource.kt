package com.entin.data.remote

import com.entin.data.model.CatJson
import com.entin.domain.dto.SearchCatFullRequest
import com.entin.domain.dto.SearchCatShortRequest

interface RemoteDataSource {

    suspend fun getAllTags(): Result<List<String>>

    suspend fun getFullSearchCat(request: SearchCatFullRequest): Result<CatJson>

    suspend fun getShortSearchCat(request: SearchCatShortRequest): Result<CatJson>

    suspend fun getRandomCat(): Result<CatJson>
}