package com.example.spoti5.di
//

import com.example.spoti5.data.repositories.AlbumsRepositoryImpl
import com.example.spoti5.data.repositories.SpotifyRepositoryImpl
import com.example.spoti5.domain.repository.AlbumsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.spoti5.domain.repository.SpotifyRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSpotifyRepository(
        impl: SpotifyRepositoryImpl
    ): SpotifyRepository


    @Binds
    @Singleton
    abstract fun bindAlbumsRepository(
        impl: AlbumsRepositoryImpl
    ): AlbumsRepository
}