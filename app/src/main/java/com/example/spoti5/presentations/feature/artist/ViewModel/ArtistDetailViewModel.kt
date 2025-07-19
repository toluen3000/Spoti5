package com.example.spoti5.presentations.feature.artist.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.data.repositories.ArtistDetailRepositoryImpl
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.ArtistModel
import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.example.spoti5.domain.model.artist.RelatedArtistModel
import com.example.spoti5.presentations.feature.artist.UiState.ArtistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val repositoryImpl: ArtistDetailRepositoryImpl
): ViewModel() {

    private val _artistState = MutableStateFlow<ArtistUiState<ArtistDetailModel>>(ArtistUiState.Loading)
    val artistState : StateFlow<ArtistUiState<ArtistDetailModel>> = _artistState.asStateFlow()

    fun fetchArtistInfor(artistId : String){
        viewModelScope.launch {
            _artistState.value = ArtistUiState.Loading
            when (val result = repositoryImpl.getArtistDetail(artistId)){
                Result.Empty -> {
                    _artistState.value = ArtistUiState.Empty
                }
                is Result.Error -> {
                    _artistState.value = ArtistUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _artistState.value = ArtistUiState.Success(result.data)
                }
            }

        }
    }


    private val _artistTopSong = MutableStateFlow<ArtistUiState<List<TrackItemModel>>>(ArtistUiState.Loading)
    val artistTopSong : StateFlow<ArtistUiState<List<TrackItemModel>>> = _artistTopSong.asStateFlow()


    fun fetchArtistTopSongs(artistId : String){
        viewModelScope.launch {
            _artistTopSong.value = ArtistUiState.Loading
            when (val result = repositoryImpl.getArtistTopTracks(artistId)){
                Result.Empty -> {
                    _artistTopSong.value = ArtistUiState.Empty
                }
                is Result.Error -> {
                    _artistTopSong.value = ArtistUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _artistTopSong.value = ArtistUiState.Success(result.data)

                }
            }
        }
    }

    private val _relatedArtists = MutableStateFlow<ArtistUiState<List<RelatedArtistModel>>>(ArtistUiState.Loading)
    val relatedArtist: StateFlow<ArtistUiState<List<RelatedArtistModel>>> = _relatedArtists.asStateFlow()

    fun fetchRelatedArtists(artistId : String){
        viewModelScope.launch {
            _relatedArtists.value = ArtistUiState.Loading

            when (val result = repositoryImpl.getArtistRelatedArtists(artistId)){
                Result.Empty -> {
                    _relatedArtists.value = ArtistUiState.Empty
                }
                is Result.Error -> {
                    if ((result.message ?: "").contains("404")) {
                        _relatedArtists.value = ArtistUiState.Empty
                    } else {
                        _relatedArtists.value = ArtistUiState.Error(result.message ?: "Unknown error")
                    }
                }
                is Result.Success -> {
                    _relatedArtists.value = ArtistUiState.Success(result.data)
                }
            }
        }
    }

    private val _artistAlbums = MutableStateFlow<ArtistUiState<List<AlbumModel>>>(ArtistUiState.Loading)
    val artistAlbums: StateFlow<ArtistUiState<List<AlbumModel>>> = _artistAlbums.asStateFlow()



    fun fetchArtistAlbums(artistId: String) {
        viewModelScope.launch {
            _artistAlbums.value = ArtistUiState.Loading
            when (val result = repositoryImpl.getArtistAlbums(artistId)) {
                Result.Empty -> {
                    _artistAlbums.value = ArtistUiState.Empty
                }
                is Result.Error -> {
                    _artistAlbums.value = ArtistUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _artistAlbums.value = ArtistUiState.Success(result.data)
                }
            }
        }
    }

    private val _severalArtists = MutableStateFlow<ArtistUiState<List<ArtistDetailModel>>>(ArtistUiState.Loading)
    val severalArtists: StateFlow<ArtistUiState<List<ArtistDetailModel>>> = _severalArtists.asStateFlow()

    fun fetchSeveralArtists(artistIds: String) {
        viewModelScope.launch {
            _severalArtists.value = ArtistUiState.Loading
            when (val result = repositoryImpl.getSeveralArtistsById(artistIds)) {
                Result.Empty -> {
                    _severalArtists.value = ArtistUiState.Empty
                }
                is Result.Error -> {
                    _severalArtists.value = ArtistUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _severalArtists.value = ArtistUiState.Success(result.data)
                }
            }
        }
    }


}