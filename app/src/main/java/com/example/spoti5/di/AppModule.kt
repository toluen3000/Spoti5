package com.example.spoti5.di

import com.example.spoti5.data.apis.SpotifyAuthApi
import com.example.spoti5.data.remote.SpotifyAuthRemoteDataSource
import com.example.spoti5.data.repositories.AuthRepositoryImpl
import com.example.spoti5.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

        @Provides
        fun provideSpotifyAuthApi(): SpotifyAuthApi = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyAuthApi::class.java)

        @Provides
        fun provideSpotifyAuthRepository(
            dataSource: SpotifyAuthRemoteDataSource
        ): AuthRepository = AuthRepositoryImpl(dataSource)
    }

