package com.safeway.acronyms.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.safeway.acronyms.data.remote.AcronymService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSAService(
        okHttpClient: OkHttpClient
    ) = provideService(okHttpClient, AcronymService::class.java)

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        clazz: Class<T>
    ): T {
        return createRetrofit(okHttpClient).create(clazz)
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(AcronymService.ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(
                Json(JsonConfiguration(ignoreUnknownKeys = true))
                    .asConverterFactory(contentType)

            )
            .build()
    }
}
