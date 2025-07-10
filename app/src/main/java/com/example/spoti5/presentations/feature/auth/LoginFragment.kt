package com.example.spoti5.presentations.feature.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spoti5.BuildConfig
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentLoginBinding
import com.example.spoti5.utils.SharePrefUtils
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


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
            findNavController().popBackStack()
        }

    }

    private fun onRequestTokenClicked() {
        val builder = AuthorizationRequest.Builder(
            BuildConfig.SPOTIFY_CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            BuildConfig.SPOTIFY_REDIRECT_URI
        )
        builder.setScopes(arrayOf("streaming", "user-top-read",
            "user-library-read","user-library-modify",
            "playlist-read-private",
            "user-read-playback-state",
            "user-modify-playback-state",
            "user-read-recently-played",
            "playlist-modify-private"
            ))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(requireActivity(), authRequestCode, request)
    }

}