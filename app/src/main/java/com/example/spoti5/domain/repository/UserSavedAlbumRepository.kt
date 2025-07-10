package com.example.spoti5.domain.repository

import com.example.spoti5.domain.model.library.SavedAlbumItemModel
import com.example.spoti5.data.result.Result


interface UserSavedAlbumRepository {

    suspend fun getUserSavedAlbums(): Result<List<SavedAlbumItemModel>>

}