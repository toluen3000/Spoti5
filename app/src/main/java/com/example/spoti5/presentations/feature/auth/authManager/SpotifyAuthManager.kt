package com.example.spoti5.presentations.feature.auth.authManager
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import com.example.spoti5.data.database.entities.AuthToken
//import com.spotify.sdk.android.auth.AuthorizationClient
//import com.spotify.sdk.android.auth.AuthorizationRequest
//import com.spotify.sdk.android.auth.AuthorizationResponse
//import com.spotify.sdk.android.auth.BuildConfig
//
//public class SpotifyAuthManager {
//    companion object {
//        const val CLIENT_ID = "your_spotify_client_id" // Replace with your actual Spotify client ID
//        const val REDIRECT_URI = "com.yourpackage://callback"
//        const val REQUEST_CODE = 1337
//    }
//
//    private var authCallback: ((Result<AuthToken>) -> Unit)? = null
//
//    fun authenticate(context: Context, callback: (Result<AuthToken>) -> Unit) {
//        this.authCallback = callback
//
//        val builder = AuthorizationRequest.Builder(
//            CLIENT_ID,
//            AuthorizationResponse.Type.TOKEN,
//            REDIRECT_URI
//        )
//
//        builder.setScopes(arrayOf(
//            "user-read-private",
//            "user-read-email",
//            "playlist-read-private"
//        ))
//
//        val request = builder.build()
//        AuthorizationClient.openLoginActivity(context as Activity, REQUEST_CODE, request)
//    }
//
//    fun handleAuthResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//        if (requestCode == REQUEST_CODE) {
//            val response = AuthorizationClient.getResponse(resultCode, intent)
//            val callback = authCallback
//
//            when (response.type) {
//                AuthorizationResponse.Type.TOKEN -> {
//                    val token = AuthToken(
//                        accessToken = response.accessToken,
//                        expiresAt = System.currentTimeMillis() + (response.expiresIn * 1000)
//                    )
//                    callback?.invoke(Result.success(token))
//                }
//                AuthorizationResponse.Type.ERROR -> {
//                    callback?.invoke(Result.failure(Exception(response.error)))
//                }
//                else -> {
//                    callback?.invoke(Result.failure(Exception("Authentication cancelled")))
//                }
//            }
//
//            authCallback = null
//        }
//    }
//}