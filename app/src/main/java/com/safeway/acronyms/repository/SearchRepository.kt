package com.safeway.acronyms.repository

import com.safeway.acronyms.data.model.AcronymResponse
import com.safeway.acronyms.data.remote.AcronymService
import com.safeway.acronyms.data.remote.Result
import com.safeway.acronyms.data.remote.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val acronymService: AcronymService
) {

    /*suspend fun dataFromNetwork(query: String) =
        safeApiCall { acronymService.getAbbreviationDefinitions() }*/

    suspend fun dataFromNetwork(query: String): Flow<Result<List<AcronymResponse>>>{

        return flow {
            emit(safeApiCall { acronymService.getAbbreviationDefinitions(query) })
        }
    }

}
