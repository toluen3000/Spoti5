package com.example.spoti5.presentations.feature.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.spoti5.R
import com.example.spoti5.constants.Constants.REQUEST_CODE
import com.example.spoti5.databinding.ActivityMainBinding
import com.example.spoti5.domain.model.player.utils.PlaybackState
import com.example.spoti5.presentations.feature.play.PlayMusicViewModel
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import com.example.spoti5.utils.SharePrefUtils
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PlayMusicViewModel by viewModels()


    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    //nav
    private lateinit var navController: NavController

    // connect to spotify remote sdk
    private val TAG: String = MainActivity::class.java.simpleName

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Connecting to Spotify")
        viewModel.connectSpotify()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Disconnecting Spotify")
        viewModel.disconnectSpotify()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        installSplashScreen()

        setContentView(binding.root)
        

        // Define navHostFragment to navController to manage the screens
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)




        observeDestinationChange()

        // Observe Player State
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playbackState.collect { state ->
                    when (state) {
                        is PlayerUiState.Success -> bindMiniPlayer(state.data)
                        else -> hideMiniPlayer()
                    }
                }
            }
        }

        setupMiniPlayerControls()

    }

    private fun hideMiniPlayer() {
        binding.playMusicDialog.visibility = View.GONE
    }

    private fun setupMiniPlayerControls() {

        binding.playMusicDialog.visibility = View.GONE


        // open in PlayMusicFragment
        binding.playMusicDialog.setOnClickListener {

            val trackUri = (viewModel.playbackState.value as? PlayerUiState.Success)?.data?.trackUri
            val idTrack = trackUri?.split(":")?.lastOrNull() ?: ""
            val bundle = Bundle().apply {
                putString("uri", trackUri)
                putString("idTrack",idTrack)
            }

            binding.playMusicDialog.visibility = View.GONE

            navController.navigate(
                R.id.playMusicFragment,
                bundle,
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeScreenFragment, false)
                    .build()
            )


        }
    }

    private fun bindMiniPlayer(state: PlaybackState) {

        if (!state.isPlaying) {
            hideMiniPlayer()
            return
        }

        binding.playMusicDialog.apply {
            visibility = View.VISIBLE
            binding.txtTitle.text = state.trackName
            binding.txtArtist.text = state.artistName

            Glide.with(this@MainActivity)
                .load(state.albumImageUrl)
                .into(binding.imgTrack)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun observeDestinationChange() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    // Hide the action bar when on the login fragment
                    supportActionBar?.hide()
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.signUpFragment -> {
                    // Hide the action bar when on the sign-up fragment
                    supportActionBar?.hide()
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.searchScreenFragment -> {
                    // Show the action bar and bottom navigation when on the home fragment
                    supportActionBar?.show()
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.libraryScreenFragment -> {
                    // Show the action bar and bottom navigation when on the library screen fragment
                    supportActionBar?.show()
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                R.id.homeScreenFragment ->{
                    // Show the action bar and bottom navigation when on the home screen fragment
                    supportActionBar?.show()
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    // Show the action bar for other fragments
                    supportActionBar?.show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {

                    SharePrefUtils(this).saveAccessToken = response.accessToken
                    Log.e(TAG, "onActivityResult: Token: ${response.accessToken}")

                    navigateMainFragment()
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e(TAG, "onActivityResult: Error ${response.error}")
                }
                else -> {
                    Log.e(TAG, "onActivityResult: ${response.code} ${response.error}")
                }
            }
        }
    }

    private fun navigateMainFragment() {
        try {
            navController.navigate(R.id.action_loginFragment_to_homeScreenFragment
                ,null
                , NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}