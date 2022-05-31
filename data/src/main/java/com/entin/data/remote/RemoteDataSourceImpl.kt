package com.entin.data.remote

import com.entin.data.api.ApiCat
import com.entin.data.model.CatJson
import com.entin.data.utils.safeApiRequest
import com.entin.domain.dto.SearchCatFullRequest
import com.entin.domain.dto.SearchCatShortRequest
import javax.inject.Inject

/**
 * [RemoteDataSource] interface implementation.
 */

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiCat
) : RemoteDataSource {

    override suspend fun getAllTags(): Result<List<String>> =
        safeApiRequest {
            apiService.getAllTags()
        }

    override suspend fun getFullSearchCat(request: SearchCatFullRequest): Result<CatJson> =
        safeApiRequest {
            apiService.getFullSearchCat(
                says = request.text,
                tag = request.tag,
                filter = request.filter,
                colors = request.colors,
                size = request.size,
            )
        }

    override suspend fun getShortSearchCat(request: SearchCatShortRequest): Result<CatJson> =
        safeApiRequest {
            apiService.getShortSearchCat(
                tag = request.tag,
                filter = request.filter,
            )
        }

    override suspend fun getRandomCat(): Result<CatJson> =
        safeApiRequest {
            apiService.getRandomCat()
        }
}