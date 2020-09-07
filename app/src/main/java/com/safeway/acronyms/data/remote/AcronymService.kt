package com.safeway.acronyms.data.remote

import com.safeway.acronyms.data.model.AcronymResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymService {
    companion object {
        const val ENDPOINT = "http://www.nactem.ac.uk/"
    }

    @GET("software/acromine/dictionary.py")
    suspend fun getAbbreviationDefinitions(
        @Query("sf") query: String
    ): Response<List<AcronymResponse>>

}