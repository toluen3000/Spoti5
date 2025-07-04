package com.example.spoti5.presentations.feature.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.spoti5.BuildConfig
import com.example.spoti5.presentations.feature.auth.MainActivity
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentLoginBinding
import com.example.spoti5.presentations.feature.auth.ViewModel.LoginViewModel
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.utils.SharePrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val authRequestCode = 1337

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!SharePrefUtils(requireContext()).saveAccessToken.isNullOrEmpty())
            onRequestTokenClicked()

        binding.btnFacebook.setOnClickListener {
            onRequestTokenClicked()
        }





        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

    }

    private fun onRequestTokenClicked() {
//        val builder = AuthorizationRequest.Builder(
//            BuildConfig.SPOTIFY_CLIENT_ID,
//            AuthorizationResponse.Type.TOKEN,
//            BuildConfig.SPOTIFY_REDIRECT_URI
//        )
//        builder.setScopes(arrayOf("streaming user-top-read"))
//        val request = builder.build()
//        AuthorizationClient.openLoginActivity(requireActivity(), authRequestCode, request)
        val builder = AuthorizationRequest.Builder(
            BuildConfig.SPOTIFY_CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            BuildConfig.SPOTIFY_REDIRECT_URI
        )
        builder.setScopes(arrayOf("streaming", "user-top-read"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(requireActivity(), authRequestCode, request)
    }

}