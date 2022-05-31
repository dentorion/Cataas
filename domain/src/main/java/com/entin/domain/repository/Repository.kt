package com.entin.domain.repository

import com.entin.domain.dto.SearchCatFullRequest
import com.entin.domain.dto.SearchCatShortRequest
import com.entin.domain.model.CatDomain
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface
 * Fetches data from RemoteDataSource
 */

interface Repository {

    fun getAllTags(): Flow<Result<List<String>>>

    fun getFullSearchCat(request: SearchCatFullRequest): Flow<Result<CatDomain>>

    fun getShortSearchCat(request: SearchCatShortRequest): Flow<Result<CatDomain>>

    fun getRandomCat(): Flow<Result<CatDomain>>
}