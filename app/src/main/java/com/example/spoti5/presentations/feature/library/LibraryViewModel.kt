package com.example.spoti5.presentations.feature.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.domain.model.library.SavedAlbumItemModel
import com.example.spoti5.domain.repository.UserSavedAlbumRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.spoti5.data.result.Result
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val userSavedAlbumRepository: UserSavedAlbumRepository
): ViewModel() {

    private val _albumsSavedState = MutableStateFlow<MainUiState<List<SavedAlbumItemModel>>>(
        MainUiState.Loading)
    val albumsSavedState : StateFlow<MainUiState<List<SavedAlbumItemModel>>> = _albumsSavedState.asStateFlow()

    fun fetchUserSavedAlbums() {
        viewModelScope.launch {
            _albumsSavedState.value = MainUiState.Loading

            val result = userSavedAlbumRepository.getUserSavedAlbums()
            when (result) {


                Result.Empty -> {
                    _albumsSavedState.value = MainUiState.Error("No albums found")
                }
                is Result.Error -> {
                    _albumsSavedState.value = MainUiState.Error(result.message ?: "An error occurred")
                }
                is Result.Success -> {
                    _albumsSavedState.value = MainUiState.Success(result.data)
                }
            }
        }
    }
}