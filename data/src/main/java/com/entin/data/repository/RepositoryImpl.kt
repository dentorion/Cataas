package com.entin.data.repository

import com.entin.data.remote.RemoteDataSource
import com.entin.data.utils.mapToCatDomain
import com.entin.domain.dto.SearchCatFullRequest
import com.entin.domain.dto.SearchCatShortRequest
import com.entin.domain.model.CatDomain
import com.entin.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * [Repository] interface implementation.
 */

class RepositoryImpl @Inject constructor(
    private val remoteSource: RemoteDataSource,
) : Repository {

    override fun getAllTags(): Flow<Result<List<String>>> = flow {
        remoteSource.getAllTags().onSuccess { listOfTags ->
            emit(Result.success(listOfTags))
        }.onFailure { e ->
            emit(Result.failure(e))
        }
    }

    override fun getFullSearchCat(request: SearchCatFullRequest): Flow<Result<CatDomain>> = flow {
        remoteSource.getFullSearchCat(request).onSuccess { catJson ->
            emit(Result.success(catJson.mapToCatDomain()))
        }.onFailure { e ->
            emit(Result.failure(e))
        }
    }

    override fun getShortSearchCat(request: SearchCatShortRequest): Flow<Result<CatDomain>> = flow {
        remoteSource.getShortSearchCat(request).onSuccess { catJson ->
            emit(Result.success(catJson.mapToCatDomain()))
        }.onFailure { e ->
            emit(Result.failure(e))
        }
    }

    override fun getRandomCat(): Flow<Result<CatDomain>> = flow {
        remoteSource.getRandomCat().onSuccess { catJson ->
            emit(Result.success(catJson.mapToCatDomain()))
        }.onFailure { e ->
            emit(Result.failure(e))
        }
    }
}