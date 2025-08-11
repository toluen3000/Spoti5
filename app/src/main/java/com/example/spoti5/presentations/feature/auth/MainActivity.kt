package com.example.spoti5.presentations.feature.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
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
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import com.example.spoti5.presentations.feature.play.bottomDialog.BottomSheetDevice
import com.example.spoti5.utils.SharePrefUtils
import com.google.android.play.integrity.internal.v
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
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



    private var currentDestinationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        installSplashScreen()
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)

        setContentView(binding.root)

        hideNavigationBarOnly()

        // Define navHostFragment to navController to manage the screens
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestinationId = destination.id
            checkMiniPlayerState(viewModel.playbackState.value)
        }



        observeDestinationChange()

        // Observe Player State
        observePlayerState()
        observeSeekbar()
        viewModel.startSeekBarLoop()
        // Observe devices
        observeDevices()


        setupMiniPlayerControls()

    }

    private fun observeSeekbar() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerState.collect { state ->
                    binding.seekBar.progress = state.positionMs.toInt()
                    binding.seekBar.max = state.durationMs.toInt()
                }
            }
        }
    }


    private fun observeDevices() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.inforPlaybackState.collect { state ->
                    when (state) {
                        is ItemUiState.Success -> {
                            Log.d(TAG, "Devices: ${state.data}")
                            Log.d(TAG, "First Device: ${state.data.firstOrNull()?.name}")
                        }
                        is ItemUiState.Error -> {
                            Log.e(TAG, "Error fetching devices: ${state.message}")
                        }
                        is ItemUiState.Loading -> {
                            Log.d(TAG, "Loading devices...")
                        }

                        ItemUiState.Empty -> {
                            Log.d(TAG, "No devices found")
                        }
                    }
                }
            }
        }
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playbackState.collect { state ->
                    checkMiniPlayerState(state)
                }
            }
        }
    }

    private fun checkMiniPlayerState(state: PlayerUiState) {
        if (state is PlayerUiState.Success) {
            if (currentDestinationId == R.id.playMusicFragment || currentDestinationId == R.id.loginFragment) {
                hideMiniPlayer()
            } else {
                bindMiniPlayer(state.data)
            }
        } else {
            hideMiniPlayer() //  Idle/Error
        }
    }



    private fun hideMiniPlayer() {
        binding.playMusicDialog.visibility = View.GONE
    }

    private fun openBottomDialog() {
        val bottomSheet = BottomSheetDevice()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }


    private fun setupMiniPlayerControls() {

        binding.playMusicDialog.visibility = View.GONE

        binding.btnPlay.setOnClickListener {
            val currentState = viewModel.playbackState.value
            if (currentState is PlayerUiState.Success) {
                if (currentState.data.isPlaying ) {
                    viewModel.pause()
                    setPlayIcon()
                } else {
                    viewModel.resume()
                    binding.btnPlay.setImageResource(R.drawable.ic_pause)
                }
            } else {
                Log.e(TAG, "Playback state is not available or not playing")
            }
        }


        binding.btnDevice.setOnClickListener {
            openBottomDialog()
        }

        // open in PlayMusicFragment
        binding.playMusicDialog.setOnClickListener {


            val trackUri = (viewModel.playbackState.value as? PlayerUiState.Success)?.data?.trackUri
            val idTrack = trackUri?.split(":")?.lastOrNull() ?: ""
            val bundle = Bundle().apply {
                putString("uri", trackUri)
                putString("idTrack",idTrack)
                putBoolean("isPlaying",true)
            }


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


        binding.playMusicDialog.apply {
            visibility = View.VISIBLE
            binding.txtTitle.text = state.trackName
            binding.txtArtist.text = state.artistName
            binding.seekBar.progress = state.positionMs.toInt()
            binding.seekBar.max = state.durationMs.toInt().coerceAtLeast(1)

            if (state.isPlaying){
                binding.btnPlay.setImageResource(R.drawable.ic_pause)
            } else {
                setPlayIcon()
            }

            Glide.with(this@MainActivity)
                .load(state.albumImageUrl)
                .into(binding.imgTrack)
        }
    }

    private fun hideNavigationBarOnly() {
        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun setPlayIcon() {
        binding.btnPlay.setImageResource(R.drawable.ic_play_button)
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