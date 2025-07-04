package com.example.spoti5.presentations.feature.auth.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.domain.model.UserToken
import com.example.spoti5.domain.use_cases.AuthenticateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: AuthenticateUseCase,
) : ViewModel() {

    private val _tokenState = MutableStateFlow<MainUiState<UserToken>>(MainUiState.Loading)
    val tokenState: StateFlow<MainUiState<UserToken>> = _tokenState.asStateFlow()

    fun exchangeCodeForToken(code: String) {
        viewModelScope.launch {
            loginUseCase(code)
                .catch { exception ->
                    _tokenState.value = MainUiState.Error(exception.message ?: "Unknown error")
                }
                .collect { token ->
                    _tokenState.value = MainUiState.Success(token)
                    Log.d("LoginViewModel", "Token received: ${token.accessToken}")
                }
        }
    }

}
