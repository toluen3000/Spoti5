package com.example.spoti5.di
//

import com.example.spoti5.data.repositories.AlbumsRepositoryImpl
import com.example.spoti5.data.repositories.ArtistDetailRepositoryImpl
import com.example.spoti5.data.repositories.SpotifyRepositoryImpl
import com.example.spoti5.data.repositories.UserSavedAlbumRepositoryImpl
import com.example.spoti5.domain.repository.AlbumsRepository
import com.example.spoti5.domain.repository.ArtistDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.spoti5.domain.repository.SpotifyRepository
import com.example.spoti5.domain.repository.UserSavedAlbumRepository

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

    @Binds
    @Singleton
    abstract fun bindUserSavedAlbumRepository(
        impl: UserSavedAlbumRepositoryImpl
    ): UserSavedAlbumRepository


    @Binds
    @Singleton
    abstract fun bindArtistDetailRepository(
        impl: ArtistDetailRepositoryImpl
    ): ArtistDetailRepository
}